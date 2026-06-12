# Yolo Design System — Catalog & Developer Guide

**Audience:** developers building any UI in MyHabitsHub.
**Current design language:** Design System v3 — *Yolo Design System v3* (`design-system-v3-spec.md` holds every value + provenance). The architecture comes from `theme-spec.md` (v2 "Nocturne") and `section-color-worlds.md` (v2.1 tab worlds). Values change via specs; **you never hardcode them**.
**Enforcement:** the `design-system` skill (`.claude/skills/design-system/SKILL.md`) auto-reviews UI code; `ContrastTest` in `core/designsystem` CI-locks every color pair.

---

## 1. The golden rules

1. **Never hardcode a color.** No `Color(0xFF...)`, no `Color.White`/`Color.Black`/`Color.Gray` in feature or shared UI code. Every color comes from `MaterialTheme.colorScheme`, `MaterialTheme.colorScheme.extended`, or `MaterialTheme.colorScheme.section`.
   *Only exceptions:* `Color.Transparent` (layout plumbing) and `Color.Unspecified` for genuinely multicolor image assets — always with a `// multicolor asset` comment.
2. **Never construct a `TextStyle` or set `fontSize` in feature code.** Use `MaterialTheme.typography.<slot>` (15 slots) or `YoloTypeExtras` (statHero/kicker/statUnit). Weight changes via `fontWeight =` on `Text` are allowed only for state emphasis (e.g. selected nav label Bold).
3. **Never use raw `dp` for screen-level spacing.** Use `YoloTokens.spacing.*` (semantic names). Raw dp inside a *designsystem component's internal* geometry is fine; screens use tokens.
4. **Never use raw `RoundedCornerShape(N.dp)` in features.** Use `MaterialTheme.shapes.*` (4/8/12/16/28).
5. **Never import** `com.yolo.core.designsystem.tokens.*` (the primitives watch-carry layer) **or** `com.yolo.core.designsystem.theme.Yolo*` color constants (`YoloBlue700`, `YoloInk800`, …) **from feature/shared modules.** Those are the design system's internals.
6. **Color precedence (memorize this):**
   - **Section hue = chrome & surfaces** — washes, sheets, nav pill, tab indicators, default M3 component accents inside a tab (automatic via `YoloSectionTheme`).
   - **Habit accent = the habit's own identity** — its icon chip, ring, chart line: `extended.habitAccents[n]`, passed explicitly.
   - **`extended.habitComplete` (teal) = done, always. `extended.streak` (amber) = streaks, always. `colorScheme.error` = destructive, always.** None of these ever re-hue per section.
   - A missed day is `extended.habitMissed` (muted gray outline) — **never red**.
7. **One theme root.** `YoloTheme` is applied once in `AppScreen`; `YoloSectionTheme` wraps the tab host in `MainScreen`. Never nest another `MaterialTheme`/`YoloTheme` in a screen.
8. **Dark mode is never an afterthought.** If you read a token pair from this catalog, both modes are already WCAG AA — adding your own color math breaks that guarantee.
9. **New colors enter through the spec, not through code.** If a design needs a color that has no token: add it to `design-system-v3-spec.md` style tables → `YoloPrimitives.kt`/`Color.kt` → semantic field → `ContrastTest` — then use it.
10. **Touch targets ≥ `YoloTokens.sizing.minTouchTarget` (48dp)** on every interactive element, every platform.

---

## 2. Token reference — "which token do I use?"

All tokens resolve through three composable accessors (no other entry points):

```kotlin
MaterialTheme.colorScheme.*            // M3 roles (re-skinned per tab by YoloSectionTheme)
MaterialTheme.colorScheme.extended.*   // import com.yolo.core.designsystem.theme.extended
MaterialTheme.colorScheme.section.*    // import com.yolo.core.designsystem.theme.section
YoloTokens.spacing / .sizing / .motion // import com.yolo.core.designsystem.theme.YoloTokens
MaterialTheme.typography.* / YoloTypeExtras.*
MaterialTheme.shapes.*
```

### 2.1 Color decision tree

| I need a color for… | Use |
|---|---|
| Screen background inside a tab | `colorScheme.section.surfaceTintWash` |
| Screen background outside tabs (auth, onboarding) | `colorScheme.background` |
| A card riding on the wash | `extended.surfaceHigher` (white / `#282A2C`) — cards stay neutral in every world |
| Card on a plain surface | `colorScheme.surfaceContainerLowest..Highest` ladder |
| Primary CTA / selected control / link | `colorScheme.primary` (inside a tab this IS the section accent) |
| Tonal chip / pill behind the CTA family | `colorScheme.primaryContainer` / `onPrimaryContainer` |
| Body text | `colorScheme.onSurface` (alias `extended.textPrimary`) |
| Secondary text | `colorScheme.onSurfaceVariant` (alias `extended.textSecondary`) |
| Hint/placeholder | `extended.textPlaceholder` |
| Disabled text/fill/border | `extended.textDisabled` / `disabledFill` / `disabledOutline` |
| Decorative divider | `colorScheme.outlineVariant` |
| Interactive border | `colorScheme.outline` |
| Error text/fill | `colorScheme.error` / `errorContainer` family |
| Success/“in range” | `extended.success*` quad |
| Warning | `extended.warning*` quad |
| Info | `extended.info*` quad |
| Streak anything | `extended.streak*` quad + `celebration` |
| Habit done / missed / skipped / pending | `extended.habitComplete` / `habitMissed` / `habitSkipped` / `habitPending` |
| Heatmap cells | `extended.heatmapLevel0..4` |
| Progress ring/bar | `extended.progressIndicator` on `extended.progressTrack` (habit rings pass `habitAccents[n].accent` explicitly) |
| Per-habit identity | `extended.habitAccents[n]` (`accent`/`onAccent`/`container`/`onContainer`) |
| Bottom sheet fill / content | `section.sheetSurface` / `onSheetSurface` / `onSheetSurfaceVariant` (automatic in `AppModalBottomSheet`) |
| Icon chip inside a sheet | `section.sheetIconChip` / `onSheetIconChip` (use `YoloSheetActionRow`) |
| Hero/marketing moment | `extended.heroSurface` / `onHeroSurface` / `heroAccent` (+ `auraAmber`/`auraMint`, max one aura per screen) |
| Modal scrim | `extended.overlay` |

### 2.2 Section worlds (set automatically — you rarely touch these directly)

`YoloSection { Tracking, Progress, Settings }` — Tracking = reference Today blue (also the app's global accent), Progress = Fitness teal, Settings = Sleep purple. `MainScreen` wraps the tab host in `YoloSectionTheme(currentSection)`, which also overlays `colorScheme.primary*`, `inversePrimary`, `secondaryContainer*` and `surfaceTint` — so stock M3 components adopt the world with zero code. Dark mode: all worlds share the neutral dark canvas; identity comes from accents/chips (v3 dark policy).

### 2.3 Typography slots

| Slot | Style | Use for |
|---|---|---|
| displayLarge / displayMedium | Medium 57/64 · 45/52, tnum | hero numerals (streak count, big %) |
| displaySmall | Medium 36/44 | onboarding hero, big dates |
| headlineLarge / headlineMedium | Medium 32/40 · 28/36 | screen greetings, section heroes |
| headlineSmall | Medium 24/32 | dialog/sheet titles |
| titleLarge | Medium 22/28 | top-bar titles |
| titleMedium | Medium 16/24 | card/list-item titles |
| titleSmall | Medium 14/20 | sub-section headers |
| bodyLarge / bodyMedium / bodySmall | Regular 16/24 · 14/20 · 12/16 | reading text, descriptions, captions |
| labelLarge | Medium 14/20 | buttons, CTAs |
| labelMedium | Medium 12/16 | chips, tabs, badges, bottom-nav labels |
| labelSmall | Medium 11/16 | tiny meta (units, ticks) |
| `YoloTypeExtras.statHero` | Medium 57/64, tnum | stat-tile hero numeral, single line + auto-shrink |
| `YoloTypeExtras.kicker` | Bold 12/16 | ALL-CAPS eyebrow on data tiles only (`text.uppercase()`) |
| `YoloTypeExtras.statUnit` | Medium 14/20, tnum | "days"/"%" suffix next to numerals |

Font: Google Sans Flex (variable, bundled; OFL license at `docs/licenses/google-sans-flex-OFL.txt` — keep it shipped and listed in any licenses screen).

### 2.4 Spacing (`YoloTokens.spacing`) — adaptive (compact / ≥600dp / ≥1200dp)

`elementGap` 8 · `stackGapTight` 6 · `stackGap` 12 · `iconTextGap` 12 · `itemGap` 16 · `cardPadding` 16→20 · `bentoGutter` 12→16 · `listRowGap` 20 · `sectionHeaderGap` 20 · `sectionGap` 24→32 · `screenEdge` 24→32→40 · `dialogPadding` 32 · `heroGap` 36→48.

### 2.5 Sizing (`YoloTokens.sizing`) & shapes & motion

Sizing: `minTouchTarget` 48 · `controlHeight` 52 (40 pointer) · `inputHeight` 56 (44 pointer) · `iconSmall/icon/iconLarge` 16/24/32 · `habitRing/ringStroke` 64/6 (56/5 pointer) · `navBarHeight/railWidth/drawerWidth` 80/80/280 · `maxFormWidth/maxReadingWidth/maxFeedWidth` 480/640/1080 · `hairline` 1.
Shapes: `extraSmall` 4 (badges) · `small` 8 (chips, snackbar) · `medium` 12 (text fields, icon chips) · `large` 16 (cards) · `extraLarge` 28 (sheets top corners, dialogs). Buttons are full pill by convention.
Motion (`YoloTokens.motion`): durations `instant` 80 / `quick` 150 / `standard` 250 / `gentle` 350 / `emphasized` 450 / `celebration` 600 (hard cap); easings `easingStandard/Decelerate/Accelerate/EmphasizedDecelerate/EmphasizedAccelerate`; springs `pressSpring/settleSpring/celebrateSpring` (reward moments only — navigation never springs); honor `motion.reducedMotion` (springs/particles → 150ms fades).

---

## 3. Component catalog

### Current (`components/…`) — prefer these

| Component | Use for | Notes |
|---|---|---|
| `YoloButton(text, onClick, …, style, enabled, isLoading, leadingIcon)` | All buttons | `YoloButtonStyle`: PRIMARY, DESTRUCTIVE_PRIMARY, SECONDARY, DESTRUCTIVE_SECONDARY, TEXT |
| `YoloIconButton` | Icon-only tap targets | sized via `YoloTokens.sizing.minTouchTarget` (48dp) |
| `YoloTextField` / `YoloPasswordTextField` / `YoloTextFieldLayout` | All text input | colors prewired (placeholder/error/disabled) |
| `YoloAdaptiveFormLayout(headerText, errorText, logo, …, formContent)` | Auth/forms | handles portrait/landscape/desktop (480dp max) |
| `YoloAdaptiveResultLayout` / `YoloSimpleResultLayout` | Success/result screens | |
| `YoloSnackbarScaffold` | Screens needing snackbars | |
| `YoloSurface` | Themed root surface with optional header | |
| `YoloSheetActionRow(icon, label, …, supportingText, style, onClick)` | Rows inside bottom sheets | the reference app icon-on-tinted-chip pattern; `Default`/`Destructive`/`Disabled` |
| `YoloBrandLogo` / `YoloSuccessIcon` / `YoloFailureIcon` | Brand moments | |

### Legacy (`components/legacy/…`) — fine to use, migrated to tokens, renames pending

`AppToolbar(title, onNavigationIconClick, …, containerColor)` (pass `Color.Transparent` under the tab wash) · `AppModalBottomSheet(title, …)` (section-tinted, 28dp top corners — put `YoloSheetActionRow`s inside) · `AppDialog`/`AppDialogImage`/`DialogType` · `AppButton`/`ButtonStyle`/`CircularActionButton` (prefer `YoloButton` in new code) · `Chip`/`ChipStyle`/`ChipSize` · `LoadingProgress`/`LoadingProgressMode` · `Divider` · `ScreenTitle`/`SmallTitle`/`SectionTitle`/`DialogOrBottomSheetTitle` (prefer typography slots in new code) · `SectionContainer` · `AppCardContainer` · `UserInput` (prefer `YoloTextField`) · `AppRadioButtonWithText`/`AppRadioButton` · `AnimatedHorizontalPager` · `HorizontalPagerIndicator` · `HorizontalScrollableList` · `CustomDialog` · `LogoImage` · `PreviewHelper(darkTheme) {}` (wraps previews in `YoloTheme` + `Surface`) · `NativeAlertDialog` (expect/actual — kept deliberately; needs a jvmMain actual before a desktop target ships).

App shell: `BottomNavigationBar(items, selectedIndex, onClickItem)` + `BottomNavItem(label, icon, route, destination, section)` — each item carries its `YoloSection`; per-tab pill colors are automatic.

---

## 4. Step-by-step recipes

### 4.1 Build a new screen inside a tab

```kotlin
@Composable
fun MyNewScreenContent(viewState: MyViewState, onIntent: (MyIntent) -> Unit) {
    // 1. Background = the tab's wash (NOT colorScheme.background)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.section.surfaceTintWash)
            .padding(horizontal = YoloTokens.spacing.screenEdge),      // 2. semantic spacing
        verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.sectionGap),
    ) {
        // 3. Typography slots, never TextStyle()
        Text("Good morning", style = MaterialTheme.typography.headlineLarge)

        // 4. Cards are neutral and ride on the wash
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.extended.surfaceHigher,
            ),
        ) { /* content with cardPadding */ }

        // 5. CTA picks up the section accent automatically (primary is overlaid per tab)
        YoloButton(text = "Add habit", onClick = { onIntent(MyIntent.Add) })
    }
}
```

Don't wrap the screen in any theme — `MainScreen` already provides `YoloSectionTheme`. Previews: `PreviewHelper(darkTheme = true) { MyNewScreenContent(...) }` and check BOTH modes.

### 4.2 Add a bottom sheet

```kotlin
AppModalBottomSheet(title = "Habit options", onDismiss = onDismiss, hideButtons = true) {
    Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.elementGap)) {
        YoloSheetActionRow(
            icon = painterResource(Res.drawable.ic_back), // placeholder — use your feature's icon
            label = "Edit habit",
            supportingText = "Name, schedule, reminders",
            onClick = onEdit,
        )
        YoloSheetActionRow(
            icon = painterResource(Res.drawable.ic_delete),
            label = "Delete habit",
            style = YoloSheetActionRowStyle.Destructive,
            onClick = onDelete,
        )
    }
}
```

The sheet tint, drag handle, title color and chip colors all come from the current tab's section — zero color code at the call site.

### 4.3 Show a habit with its identity color

```kotlin
val accent = MaterialTheme.colorScheme.extended.habitAccents[habit.colorIndex % 8]
Box(Modifier.size(40.dp).clip(MaterialTheme.shapes.medium).background(accent.container)) {
    Icon(habitIcon, null, tint = accent.onContainer, modifier = Modifier.size(YoloTokens.sizing.icon))
}
// Ring fill = accent.accent; completed check = extended.habitComplete (teal, NOT the accent)
```

### 4.4 Add a new tab/section

1. Add the enum case to `YoloSection` (`SectionTheme.kt`).
2. Define `<Name>SectionLight/Dark` palettes (hexes enter via the spec first — rule 9); dark wash/sheet stay neutral (`Deep400`/`Deep200`) per the v3 dark policy.
3. Extend `YoloSection.colors()`; add the `BottomNavItem` with `section =`.
4. `ContrastTest.sectionWorldsMeetWcag` covers it automatically via `YoloSection.entries` — run it.

### 4.5 Add a new component to the design system

Location: `core/designsystem/.../components/<category>/Yolo<Name>.kt`. Rules: `Yolo` prefix; commonMain only (no expect/actual without explicit approval — see `NativeAlertDialog` exception); every color from the three accessors; sizes from `YoloTokens.sizing`; shapes from `MaterialTheme.shapes`; a `PreviewHelper` preview in both modes; if it introduces color *pairs*, add assertions to `ContrastTest`; **and a catalog entry (§4b) in the same PR**.

---

## 4b. The runnable catalog — `core/catalog` module

The visual counterpart of this document: a browsable, on-device gallery of every token and
component, with a **dark-mode toggle** and a **section-world switcher** so you see each demo in
all 6 palette contexts live.

- **Module:** `:core:catalog` (depends only on `:core:designsystem`). The catalog is also the
  design system's reference implementation — its demo code follows every rule in this guide.
- **Open it (debug only):** in `shared/.../app/AppScreen.kt`, flip
  `val showDesignSystemCatalog = false` to `true` and run the app. The flag is a compile-time
  `false` in commits, so R8 strips the catalog from release builds; never wire it to release
  navigation.
- **Add a demo (the catalog must keep growing):**
  1. Write a small `@Composable` demo in `core/catalog/.../demos/FoundationDemos.kt` or
     `ComponentDemos.kt` (new file in `demos/` for a new area) — tokens only, like any screen.
  2. Append a `CatalogEntry("Title", "one-line description") { MyDemo() }` to that file's
     entry list (`foundationEntries` / `componentEntries`).
  3. That's it — `DesignSystemCatalogScreen` renders it automatically, grouped by category.
- **Definition of done for any new design-system component:** ships in the same PR with
  (a) a catalog entry, (b) `PreviewHelper` previews in both modes, (c) `ContrastTest`
  assertions if it introduces new color pairs.

---

## 5. Verification

```bash
# WCAG gate — must stay green (runs all scheme/extended/section/habit-accent pairs, both modes)
./gradlew :core:designsystem:iosSimulatorArm64Test

# Compile gates
./gradlew :core:designsystem:compileAndroidMain :shared:compileAndroidMain
```

The `design-system` skill (`.claude/skills/design-system/SKILL.md`) encodes the violation greps and the auto-fix mapping — run it over any UI diff (it is also what Claude applies automatically when writing UI code here).

## 6. Anti-pattern quick table

| ❌ Wrong | ✅ Right |
|---|---|
| `Color.White` card | `extended.surfaceHigher` |
| `Color(0xFF0B57D0)` | `colorScheme.primary` (or the semantic token that means it) |
| `TextStyle(fontSize = 22.sp, …)` | `MaterialTheme.typography.titleLarge` |
| `padding(24.dp)` at screen edge | `padding(YoloTokens.spacing.screenEdge)` |
| `RoundedCornerShape(12.dp)` in a screen | `MaterialTheme.shapes.medium` |
| `import …designsystem.theme.YoloBlue700` in a feature | use a semantic role |
| Red “missed” indicator | `extended.habitMissed` (muted, never red) |
| Section-colored completion check | `extended.habitComplete` (teal in every world) |
| Second `MaterialTheme {}` inside a screen | rely on `YoloTheme`/`YoloSectionTheme` from the shell |
| `isSystemInDarkTheme()` in a screen | `MaterialTheme.colorScheme.extended.isDark` |
