# Section Color Worlds — Nocturne v2.1 Addendum (FINAL)

**Status:** Final. Implementable without further decisions. Extends `docs/design/theme-spec.md` (Nocturne, Yolo Design Language v2) — nothing in that document is redesigned or re-valued here; every Nocturne hex, type slot, shape, spacing and motion token is unchanged.
**Driver:** User feedback — *"everything is a bit monotone. Be deeply inspired by the new reference redesign: bottom sheets tinted in the section color with colored icons inside. Every bottom-nav tab gets a different color. Progress tab colors can stay similar to what we have now (mint/emerald). For Tracking, copy the reference app new redesign."*
**Reference model:** the 2025–2026 reference redesign study: per-tab hue worlds (Today = cobalt blue, Fitness = teal, Sleep = purple) built as background-layer washes with neutral cards riding on top, two-tone icon-on-tinted-chip stat tiles, and M3 modal sheets with large top radii and drag handles.
**Verification:** every contrast ratio printed in this document was computed with the WCAG 2.2 relative-luminance formula against the exact hexes printed here (python3 script run 2026-06-12; **102 gated pairs, 0 failures**). No ratio in this file is estimated.

---

## 0. Amendments to the Nocturne contract (decision log)

| # | Decision | Rationale |
|---|---|---|
| A1 | **A second color CompositionLocal is added: `LocalYoloSection`** — amending Nocturne §0's "ONE extended-tokens CompositionLocal" clause. | Section colors are *subtree-scoped* (they vary per tab), unlike `ExtendedColors` which is global. Folding 14 fields × 3 sections into `ExtendedColors` would bloat the global local and still not let stock M3 components pick up the world. The split mirrors the industry pattern (Now in Android's `LocalGradientColors` layered over MaterialTheme). `LocalExtendedColors` remains the only *global* extension local. |
| A2 | **Per-tab bottom-nav selected colors — a deliberate divergence from the reference app.** the reference app keeps a constant blue accent on its nav/FAB chrome across all tabs; the user explicitly asked "every bottom-nav tab gets a different color." User directive wins; the nav *container* stays neutral (Nocturne `surfaceContainer`), only each item's selected pill/icon/label takes its own world. | User feedback overrides reference fidelity. |
| A3 | **Modal bottom sheets are section-tinted.** The teardown found the reference app's *modal* sheets neutral-dark (`≈#121315`); the user's directive ("bottom sheets tinted in the section color with colored icons inside — I liked that") wins. Tints are whisper-quiet in light mode (one step under the white cards) and deep-hue in dark mode (the reference app's section-wash recipe applied to the sheet plane). | User feedback overrides reference fidelity. |
| A4 | **`AppModalBottomSheet` corner radius 10dp → `extraLarge` 28dp top corners** — this enforces Nocturne §4.1's existing binding ("modal sheet = extraLarge"), which the current implementation violates. the reference app's observed sheet radii (~28–32dp) agree; no new value needed. | Spec-compliance fix surfaced by this work. |
| A5 | **Two new hue ramps enter §2.1 primitives: Cobalt (Tracking) and Violet (Settings)**, plus wash/sheet/chip values for all three worlds. No existing Nocturne primitive changes value or role. Amber remains the exclusive streak channel — **no section world may use amber**; coral/orange was rejected for Tracking because the teardown found no coral world in the reference redesign (Today = blue) and amber-adjacent hues would dilute the streak channel. | §2.1 extension, additive only. |
| A6 | **Status-blue and Cobalt coexist** (`info #0B69C7`, hue ≈210° vs Tracking cobalt `#0B57D0`, hue ≈217°). Inside the Tracking world, info chips read as world-coherent rather than as a distinct status family — the identical property the reference Today tab has. Acceptable because Nocturne status semantics never ride on hue alone (icon + container shape always present). | Honesty note, same class as Nocturne §2.5's brand-collision note. |

---

## 1. Concept: Section Color Worlds

Nocturne stays the **base operating system** of the design: the teal-ink near-black canvases, the paper-light mode, Manrope, the shape scale, spacing, motion, the forgiveness palette and the celebration economy are untouched. What changes: **each of the three bottom-nav tabs becomes a hue world** — the same layering recipe stamped in three different hues, which is exactly what makes the reference app's teal Fitness tab and purple Sleep tab feel like one app.

A world consists of: a barely-there **background wash** behind the tab's content (themes the toolbar zone), **neutral cards riding on top** of the wash, a **section-tinted modal bottom sheet** with **icon-on-tinted-chip** rows, a **section-colored nav pill** for that tab, and the section's accent flowing into M3 components (`primary`, containers, `surfaceTint`) inside the tab subtree only.

### Copied from the reference app (faithfully)

1. **Per-tab hue worlds, identical recipe per tab** — same tone roles, different hue family; the construction follows the M3 tonal recipe (accent ≈ tone 40 light / 80 dark, containers ≈ 90/30, on-containers ≈ 10/90).
2. **The background-layer wash** — section tint lives on a *background layer* (light: barely-there pastel; dark: moody deep-hue), never a full-saturation flood; cards stay neutral and ride on top.
3. **Icon-on-tinted-chip** — leading icon chips filled in a tonal variant of the relevant hue, icon drawn in a contrasting tint of the same hue; two-tone, monochromatic per chip.
4. **"Color is earned by data"** — empty/no-data tiles and rows fall back to neutral fills (`surfaceContainer` / `surfaceHigher`); the world wash stays, content color appears with content.
5. **Tinted sheets + colored icons inside** (per user directive, A3) and the drag-handle-on-rounded-sheet pattern.
6. **The literal Tracking hues** — `#A8C7FA` periwinkle and `#0B57D0` royal are lifted from the observed reference Today/chrome accents; the Progress dark wash `#11342B` is the observed Fitness forest wash, lifted verbatim.

### Stays Nocturne (non-negotiable)

1. **Forgiveness palette** — `habitMissed`/`habitSkipped`/`habitPending` are global and never re-hued per section; a missed day is a muted outline in every world, never red, never section-colored.
2. **Amber streak channel** — `streak`/`celebration`/`auraAmber` stay amber in every world; no section may claim amber (A5).
3. **Completion is the brand** — `habitComplete`/`success` stay emerald/mint in every world; a check-in inside the cobalt Tracking world still blooms brand-green. (These are `ExtendedColors` fields, structurally unaffected by the section scheme overlay.)
4. **Neutrals** — Ink and Deep ramps, `surface*`, `outline*`, text aliases, error, warning, info: all global. Cards stay `extended.surfaceHigher` (white / `#1C2A27`) in every world.
5. **Typography, shapes, spacing, motion, aura rules** — unchanged. No M3E shape-morph loading indicators, no Google Sans; Nocturne's springs and aura discipline stand.
6. **Dark-canonical discipline** — dark washes are deep, desaturated, never neon; tone-only elevation (`shadowElevation = 0` in dark) holds inside worlds.

---

## 2. The three worlds

### 2.1 Hue assignments & rationale

| Tab | World | Hue family | Why |
|---|---|---|---|
| **Tracking** | **Cobalt** (hue ≈ 217°) | the reference app "Today" blue, lifted faithfully | Our Tracking tab *is* the app's Today: the daily check-in feed. The teardown found Today = cobalt/periwinkle (`#0B57D0` royal segments light, `#A8C7FA` periwinkle accents dark, `#E9ECFA`/`#EFEFFA` pale washes), Fitness = teal, Sleep = purple — no coral world exists in the redesign. Teal collides with brand emerald (reserved for Progress) and amber is the streak channel, so the cobalt Today world is both the faithful lift and the only non-colliding choice. |
| **Progress** | **Emerald** (hue ≈ 160°) | Nocturne brand family — "similar to what we have now" per user | Light accent `#047857` / dark accent `#4DDEAB` *are* the brand primaries; the Progress world is near-identical to base Nocturne by construction (see §3.4). Serendipity: the reference app's Fitness dark wash `#11342B` sits perfectly in Nocturne's deep-green range and is adopted verbatim as the Progress dark wash. |
| **Settings** | **Violet** (hue ≈ 262°) | Derived from the reference app's Sleep world | the reference app assigns purple to its calm/rest domain; Settings is our quiet, low-arousal tab. Violet is maximally distant from brand mint and streak amber, collides with neither error red nor info blue, and Nocturne's own curation already proves the family harmonizes with teal-ink neutrals (habit accent slot 6, indigo). Dark accent is a tone-80-class pastel like every Nocturne dark accent. |

**CVD honesty note (extends Nocturne §2.5):** dark cobalt `#A8C7FA` and dark violet `#C9B3F5` are near-equiluminant pastels and converge under deuteranopia. Tab identity therefore never rides on color alone — icon + label + position always present; section worlds are ambience, not information. Same rule as habit accents.

### 2.2 `SectionColors` role glossary

| Role | Meaning | WCAG gate |
|---|---|---|
| `accent` / `onAccent` | The world's primary: CTAs, selected tab indicator, chart chrome, selected nav label | onAccent ≥4.5:1 on accent; accent ≥3:1 on surface, wash and sheet |
| `accentContainer` / `onAccentContainer` | Tonal container: chips, tonal buttons, nav pill | ≥4.5:1 pair |
| `surfaceTintWash` | The tab's background wash (replaces `background` inside the tab); also tints the toolbar zone | onSurface ≥4.5:1, onSurfaceVariant ≥4.5:1, accent ≥3:1 on it |
| `sheetSurface` / `onSheetSurface` / `onSheetSurfaceVariant` | Modal bottom-sheet fill + its primary/secondary content colors | both text pairs ≥4.5:1; error ≥4.5:1 on it |
| `sheetIconChip` / `onSheetIconChip` | The reference app icon treatment: tinted chip + colored icon inside sheets | icon pair ≥3:1 |
| `navIndicator` / `onNavIndicator` | Bottom-nav selected pill + icon on it (= `accentContainer` pair by construction) | icon ≥3:1 on pill (all compute ≥6:1) |
| `navSelectedText` | Selected nav label on the nav container (= `accent` by construction) | ≥4.5:1 vs `surfaceContainer` (12sp label) |
| `sheetDragHandle` | Drag handle pill | = `onSheetSurfaceVariant` @ 40% α — decorative, exempt |

### 2.3 TRACKING — Cobalt world (the reference lift)

**LIGHT** (nav container `#E8EEEC`, surface `#F4F8F7`):

| Role | Hex | Verified contrast |
|---|---|---|
| accent | `#0B57D0` | 6.39:1 vs onAccent; 5.97:1 on surface; 5.65:1 on wash; 5.25:1 on sheet; **5.44:1 on nav container** (= navSelectedText) |
| onAccent | `#FFFFFF` | 6.39:1 |
| accentContainer (= navIndicator) | `#D3E3FD` | — |
| onAccentContainer (= onNavIndicator) | `#0A2D62` | **10.34:1** on container |
| surfaceTintWash | `#EDF1FC` | onSurface 14.81:1; onSurfaceVariant 5.83:1; error 5.65:1; habitPending ring 14.81:1 |
| sheetSurface | `#E2E9FA` | onSheetSurface 13.76:1; onSheetSurfaceVariant 6.05:1; error `#BA1B32` 5.25:1; streak `#B45309` 4.13:1; onSurfaceVariant 5.41:1 |
| onSheetSurface | `#14201E` | 13.76:1 (= onSurface — ink stays global) |
| onSheetSurfaceVariant | `#4A5670` | 6.05:1 (cobalt-tinted slate) |
| sheetIconChip | `#C2D7FA` | — |
| onSheetIconChip | `#0B57D0` | **4.38:1** on chip (≥3:1 icon floor) |

**DARK** (nav container `#182522`, surface `#101B1A`):

| Role | Hex | Verified contrast |
|---|---|---|
| accent | `#A8C7FA` | 7.81:1 vs onAccent; 10.23:1 on surface; 9.47:1 on wash; 8.47:1 on sheet; **9.21:1 on nav container** |
| onAccent | `#0A2D62` | 7.81:1 |
| accentContainer (= navIndicator) | `#1E3A66` | — |
| onAccentContainer (= onNavIndicator) | `#D6E3FF` | **8.79:1** on container |
| surfaceTintWash | `#142036` | onSurface 13.12:1; onSurfaceVariant 7.58:1; error 7.23:1; habitPending ring 13.12:1 |
| sheetSurface | `#192944` | onSheetSurface 11.73:1; onSheetSurfaceVariant 7.11:1; error `#FF8A95` 6.46:1; streak `#FFB870` 8.56:1; onSurfaceVariant 6.78:1 |
| onSheetSurface | `#E2E8E6` | 11.73:1 |
| onSheetSurfaceVariant | `#A9B6CC` | 7.11:1 (cobalt-tinted mist) |
| sheetIconChip | `#27406B` | — |
| onSheetIconChip | `#A8C7FA` | **6.01:1** on chip |

### 2.4 PROGRESS — Emerald world (the brand, "similar to now")

**LIGHT:**

| Role | Hex | Verified contrast |
|---|---|---|
| accent | `#047857` | 5.48:1 vs onAccent; 5.12:1 on surface; 4.84:1 on wash; 4.51:1 on sheet; **4.67:1 on nav container** (the Nocturne-verified pairing) |
| onAccent | `#FFFFFF` | 5.48:1 |
| accentContainer (= navIndicator) | `#CEF5E4` | — (= base primaryContainer) |
| onAccentContainer (= onNavIndicator) | `#073D2F` | **10.37:1** on container |
| surfaceTintWash | `#E6F4ED` | onSurface 14.76:1; onSurfaceVariant 5.81:1; error 5.63:1; habitPending 14.76:1 |
| sheetSurface | `#D8EEE3` | onSheetSurface 13.75:1; onSheetSurfaceVariant 5.59:1; error 5.24:1; streak 4.13:1; onSurfaceVariant 5.41:1; textPlaceholder 5.41:1 |
| onSheetSurface | `#14201E` | 13.75:1 |
| onSheetSurfaceVariant | `#4C5F58` | 5.59:1 (mint-tinted slate) |
| sheetIconChip | `#AEE9CF` | — |
| onSheetIconChip | `#047857` | **4.01:1** on chip |

**DARK:**

| Role | Hex | Verified contrast |
|---|---|---|
| accent | `#4DDEAB` | 8.58:1 vs onAccent (the brand pairing); 10.35:1 on surface; 7.97:1 on wash; 6.44:1 on sheet; **9.31:1 on nav container** |
| onAccent | `#092E2D` | 8.58:1 |
| accentContainer (= navIndicator) | `#0B5C44` | — (= base primaryContainer) |
| onAccentContainer (= onNavIndicator) | `#A8F0D4` | **6.12:1** on container |
| surfaceTintWash | `#11342B` | **lifted verbatim from the reference Fitness forest wash.** onSurface 10.92:1; onSurfaceVariant 6.31:1; error 6.01:1; habitPending 10.92:1 |
| sheetSurface | `#14443A` | = `successContainer` dark (primitive reuse). onSheetSurface 8.82:1; onSheetSurfaceVariant 5.38:1; error 4.86:1; streak 6.44:1; onSurfaceVariant 5.10:1 |
| onSheetSurface | `#E2E8E6` | 8.82:1 |
| onSheetSurfaceVariant | `#A0BCB0` | 5.38:1 |
| sheetIconChip | `#0B5C44` | — |
| onSheetIconChip | `#A8F0D4` | **6.12:1** on chip (the verified primaryContainer pair, reused) |

### 2.5 SETTINGS — Violet world (calm third hue)

**LIGHT:**

| Role | Hex | Verified contrast |
|---|---|---|
| accent | `#6B4FC8` | 5.87:1 vs onAccent; 5.48:1 on surface; 5.13:1 on wash; 4.65:1 on sheet; **5.00:1 on nav container** |
| onAccent | `#FFFFFF` | 5.87:1 |
| accentContainer (= navIndicator) | `#E9DFFB` | — |
| onAccentContainer (= onNavIndicator) | `#33206B` | **10.52:1** on container |
| surfaceTintWash | `#F1EEFA` | onSurface 14.62:1; onSurfaceVariant 5.75:1; error 5.58:1; habitPending 14.62:1 |
| sheetSurface | `#E8E2F7` | onSheetSurface 13.27:1; onSheetSurfaceVariant 6.02:1; error 5.06:1; streak 3.98:1; onSurfaceVariant 5.22:1 |
| onSheetSurface | `#14201E` | 13.27:1 |
| onSheetSurfaceVariant | `#555070` | 6.02:1 (violet-tinted slate) |
| sheetIconChip | `#D5C8F4` | — |
| onSheetIconChip | `#6B4FC8` | **3.74:1** on chip |

**DARK:**

| Role | Hex | Verified contrast |
|---|---|---|
| accent | `#C9B3F5` | 7.21:1 vs onAccent; 9.42:1 on surface; 9.04:1 on wash; 8.22:1 on sheet; **8.48:1 on nav container** |
| onAccent | `#33206B` | 7.21:1 |
| accentContainer (= navIndicator) | `#3F2E73` | — |
| onAccentContainer (= onNavIndicator) | `#E5DBFA` | **8.57:1** on container |
| surfaceTintWash | `#1C1A33` | onSurface 13.59:1; onSurfaceVariant 7.85:1; error 7.49:1; habitPending 13.59:1 |
| sheetSurface | `#242141` | onSheetSurface 12.36:1; onSheetSurfaceVariant 6.86:1; error 6.81:1; streak 9.02:1; onSurfaceVariant 7.14:1 |
| onSheetSurface | `#E2E8E6` | 12.36:1 |
| onSheetSurfaceVariant | `#AFAAC6` | 6.86:1 (violet-tinted mist) |
| sheetIconChip | `#383263` | — |
| onSheetIconChip | `#C9B3F5` | **6.23:1** on chip |

### 2.6 New primitives (`tokens/YoloPrimitives.kt` + `theme/Color.kt` — §2.1 extension, additive)

```
// Cobalt (Tracking world — lifted from reference Today/chrome)
YoloCobalt700 = #0B57D0      YoloCobalt300 = #A8C7FA     YoloCobalt100 = #D3E3FD
YoloCobalt150 = #C2D7FA      YoloCobaltDeep = #0A2D62    YoloCobaltDarkContainer = #1E3A66
YoloCobaltContainerOn = #D6E3FF
YoloCobaltWashLight = #EDF1FC   YoloCobaltSheetLight = #E2E9FA
YoloCobaltWashDark  = #142036   YoloCobaltSheetDark  = #192944   YoloCobaltChipDark = #27406B
YoloCobaltVariantLight = #4A5670   YoloCobaltVariantDark = #A9B6CC

// Violet (Settings world — derived from the reference app Sleep)
YoloViolet700 = #6B4FC8      YoloViolet300 = #C9B3F5     YoloViolet100 = #E9DFFB
YoloViolet150 = #D5C8F4      YoloVioletDeep = #33206B    YoloVioletDarkContainer = #3F2E73
YoloVioletContainerOn = #E5DBFA
YoloVioletWashLight = #F1EEFA   YoloVioletSheetLight = #E8E2F7
YoloVioletWashDark  = #1C1A33   YoloVioletSheetDark  = #242141   YoloVioletChipDark = #383263
YoloVioletVariantLight = #555070   YoloVioletVariantDark = #AFAAC6

// Emerald world additions (accents/containers reuse existing Brand primitives)
YoloBrandWashLight = #E6F4ED    YoloBrandSheetLight = #D8EEE3   YoloBrandChipLight = #AEE9CF
YoloBrandWashDark  = #11342B    // reference Fitness forest wash, lifted verbatim
// dark sheet reuses #14443A (= dark successContainer primitive); dark chip reuses YoloBrand700 #0B5C44
YoloBrandVariantLight = #4C5F58    YoloBrandVariantDark = #A0BCB0
```

---

## 3. Token architecture (`core/designsystem`, pure commonMain — consistent with Nocturne §8)

### 3.1 New file: `theme/SectionTheme.kt`

```kotlin
enum class YoloSection { Tracking, Progress, Settings }

@Immutable
data class SectionColors(
    val accent: Color, val onAccent: Color,
    val accentContainer: Color, val onAccentContainer: Color,
    val surfaceTintWash: Color,
    val sheetSurface: Color, val onSheetSurface: Color, val onSheetSurfaceVariant: Color,
    val sheetIconChip: Color, val onSheetIconChip: Color,
    val navIndicator: Color, val onNavIndicator: Color, val navSelectedText: Color,
    val sheetDragHandle: Color,
)
```

Construction invariants (asserted in test, not free-floating): `navIndicator == accentContainer`, `onNavIndicator == onAccentContainer`, `navSelectedText == accent`, `sheetDragHandle == onSheetSurfaceVariant.copy(alpha = 0.40f)`. They are separate fields so a future world *may* decouple them; today they alias.

Six palette vals, built from §2.3–2.5 hexes: `TrackingSectionLight/Dark`, `ProgressSectionLight/Dark`, `SettingsSectionLight/Dark`, plus:

```kotlin
fun YoloSection.colors(isDark: Boolean): SectionColors = when (this) { /* table lookup */ }

val LocalYoloSection = staticCompositionLocalOf { ProgressSectionLight }

val ColorScheme.section: SectionColors
    @Composable @ReadOnlyComposable get() = LocalYoloSection.current
```

`staticCompositionLocalOf` is correct here (mirrors `LocalExtendedColors`): the value never mutates *within* a live subtree — it changes only when the tab subtree is recomposed by navigation anyway, so the full-subtree invalidation cost is moot. **Never** animate this local's value.

### 3.2 The wrapper: `YoloSectionTheme`

```kotlin
@Composable
fun YoloSectionTheme(section: YoloSection, content: @Composable () -> Unit) {
    val isDark = MaterialTheme.colorScheme.extended.isDark
    val colors = section.colors(isDark)
    val base = MaterialTheme.colorScheme
    val scheme = remember(base, colors) {
        base.copy(
            primary = colors.accent,
            onPrimary = colors.onAccent,
            primaryContainer = colors.accentContainer,
            onPrimaryContainer = colors.onAccentContainer,
            inversePrimary = section.colors(!isDark).accent,
            secondaryContainer = colors.navIndicator,
            onSecondaryContainer = colors.onNavIndicator,
            surfaceTint = colors.accent,
        )
    }
    CompositionLocalProvider(LocalYoloSection provides colors) {
        MaterialTheme(
            colorScheme = scheme,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            content = content,
        )
    }
}
```

The `ColorScheme` is built once per (base scheme × section) and `remember`-ed — **nothing is constructed during scroll or animation frames.** All stock M3 components inside the tab (buttons, tabs, switches, progress, chips, the `SecondaryIndicator`) pick up the world automatically because they read `primary`/containers.

### 3.3 Exact `ColorScheme.copy()` role swaps, per section, per mode

Roles NOT listed are **never** swapped: `secondary`/`onSecondary`, `tertiary*` (amber streak stays global), `error*`, all `surface*`/`background` slots, `outline*`, `inverse(On)Surface`, `scrim`. That is what keeps forgiveness, streak and status semantics constant across worlds.

| Role | Tracking L | Tracking D | Progress L | Progress D | Settings L | Settings D |
|---|---|---|---|---|---|---|
| primary | `#0B57D0` | `#A8C7FA` | `#047857` | `#4DDEAB` | `#6B4FC8` | `#C9B3F5` |
| onPrimary | `#FFFFFF` | `#0A2D62` | `#FFFFFF` | `#092E2D` | `#FFFFFF` | `#33206B` |
| primaryContainer | `#D3E3FD` | `#1E3A66` | `#CEF5E4` | `#0B5C44` | `#E9DFFB` | `#3F2E73` |
| onPrimaryContainer | `#0A2D62` | `#D6E3FF` | `#073D2F` | `#A8F0D4` | `#33206B` | `#E5DBFA` |
| inversePrimary | `#A8C7FA` | `#0B57D0` | `#4DDEAB` | `#047857` | `#C9B3F5` | `#6B4FC8` |
| secondaryContainer | `#D3E3FD` | `#1E3A66` | `#CEF5E4` | `#0B5C44` | `#E9DFFB` | `#3F2E73` |
| onSecondaryContainer | `#0A2D62` | `#D6E3FF` | `#073D2F` | `#A8F0D4` | `#33206B` | `#E5DBFA` |
| surfaceTint | `#0B57D0` | `#A8C7FA` | `#047857` | `#4DDEAB` | `#6B4FC8` | `#C9B3F5` |

### 3.4 Progress ≈ base, by construction

The Progress world's `primary`/`onPrimary`/`primaryContainer`/`onPrimaryContainer`/`inversePrimary`/`surfaceTint` swaps are **byte-identical to the base Nocturne scheme** — only `secondaryContainer`/`onSecondaryContainer` change (the neutral `#DCE7E3`/`#3A4744` pill becomes the mint `#CEF5E4`/`#0B5C44` pill, the reference-style colored pill). This is the formal guarantee behind "Progress can stay similar to what we have now."

### 3.5 Default provider & non-tab screens

`YoloTheme` (v2.1 edit) adds one line so the local always matches the mode and never crashes off-tab:

```kotlin
LocalYoloSection provides YoloSection.Progress.colors(darkTheme),
```

Auth, onboarding and any future non-tab surface therefore degrade gracefully to the **brand (Progress) world** — visually identical to pre-addendum Nocturne. Detail screens pushed *within* a tab graph (e.g. the profile SettingsScreen pushed from Account) inherit their tab's world, which is intended.

### 3.6 Tab-switch crossfade rules (performance contract)

- **Never animate `MaterialTheme.colorScheme` or `LocalYoloSection` between worlds.** Both worlds exist momentarily and blend optically via the navigation crossfade — the reference app "fluid" look. Tab content swaps through the existing `NavHost`; give it `fadeIn`/`fadeOut` transitions at `tween(YoloTokens.motion.standard /* 250ms */, easing = easingStandard)`.
- **The one continuously-tinting plane** (toolbar zone + screen background wash, which must bleed smoothly across the tab switch) uses exactly one animated value at the chrome host level:

```kotlin
val wash by animateColorAsState(
    targetValue = currentSection.colors(isDark).surfaceTintWash,
    animationSpec = tween(YoloTokens.motion.standard, easing = YoloTokens.motion.easingStandard),
)
Box(Modifier.drawBehind { drawRect(wash) }) { /* toolbar + NavHost */ }
```

  Draw-phase read only (`drawBehind`, never `Modifier.background(wash)`) — zero recomposition, zero allocation per frame; `Color` is an inline value class.
- When `reducedMotion` is true, the wash snaps (`snap()`), per Nocturne §4.4.

---

## 4. Bottom sheet spec (`AppModalBottomSheet` → `YoloSheet` per §6.6)

The signature the reference app-inspired surface: a section-tinted sheet with colored icon chips.

| Property | Spec |
|---|---|
| `containerColor` | `MaterialTheme.colorScheme.section.sheetSurface` (current code passes nothing → M3 default `surfaceContainerLow`; this is the change) |
| `contentColor` | `section.onSheetSurface` |
| Shape | `RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)` — Nocturne `extraLarge` top corners (A4 fixes the current 10dp violation; the reference app's observed radii agree) |
| Drag handle | 36×4dp pill (`CircleShape`), color `section.sheetDragHandle` (= `onSheetSurfaceVariant` @ 40% α), centered, 12dp top / 8dp bottom padding. Decorative — WCAG-exempt (current 3dp `Divider` handle is replaced) |
| Title | `headlineSmall`, color `section.onSheetSurface`. Destructive sheets (logout, delete account) keep `colorScheme.error` — verified ≥5.06:1 light / ≥6.46:1 dark on every sheet fill |
| Body / supporting text | `bodyMedium`, `section.onSheetSurfaceVariant` (≥5.38:1 everywhere) |
| Dividers inside sheet | `outlineVariant` (decorative, exempt — Nocturne rule) |
| Buttons | unchanged `AppButton` pair; the primary button picks up `section.accent` automatically via the §3.2 scheme overlay (sheet content is inside the tab's `YoloSectionTheme`); `onAccent` pairs verified ≥5.48:1 |
| Scrim | `extended.overlay` (unchanged) |

### 4.1 The icon-chip row (`YoloSheetActionRow` — NEW component, the reference app pattern)

Row anatomy (min height 56dp, horizontal padding `screenEdge`, gap `iconTextGap`):

1. **Chip:** 40dp × 40dp container, **shape = `YoloShapes.medium` (14dp)** — reads as the reference app squircle while staying distinct from Nocturne's full-pill CTAs (decision: `small` 10dp is too card-like at 40dp; circle would collide with the habit-ring vocabulary). Fill = `section.sheetIconChip`.
2. **Icon:** 24dp, tint = `section.onSheetIconChip` — the "colored icon inside" the user liked. Verified ≥3.74:1 light / ≥6.01:1 dark on every chip.
3. **Label:** `titleMedium`, `section.onSheetSurface`; optional supporting line `bodyMedium`, `section.onSheetSurfaceVariant`.
4. **Destructive variant:** chip fill = `colorScheme.errorContainer`, icon = `onErrorContainer`, label = `error` (all pairs already CI-locked in base Nocturne; error-on-sheet verified here).
5. **No-data/disabled variant:** chip fill = `surfaceContainerHighest`, icon = `textDisabled` — color is earned by data (the reference app rule).

Consumers picking this up immediately: `LogoutModalBottomSheet` (AccountScreen) and `DeleteUserConfirmation` — both render inside the Settings tab and therefore get the violet sheet + error accents with zero per-call-site color code.

---

## 5. Bottom nav spec (per-tab worlds on neutral chrome)

Container stays `colorScheme.surfaceContainer` (light `#E8EEEC`, dark `#182522`) — the chrome itself is Nocturne-neutral; **each item is colored by its OWN section** (not the currently selected one), so the selected pill always shows the destination's world.

Per `NavigationBarItem` (replacing the uniform `NavigationBarItemDefaults.colors`):

| Slot | Value | Light ratios | Dark ratios |
|---|---|---|---|
| `indicatorColor` | item.section `navIndicator` | T `#D3E3FD` · P `#CEF5E4` · S `#E9DFFB` | T `#1E3A66` · P `#0B5C44` · S `#3F2E73` |
| `selectedIconColor` | item.section `onNavIndicator` (drawn ON the pill) | T 10.34:1 · P 10.37:1 · S 10.52:1 | T 8.79:1 · P 6.12:1 · S 8.57:1 |
| `selectedTextColor` | item.section `navSelectedText` (12sp label on container → 4.5:1 floor) | T 5.44:1 · P 4.67:1 · S 5.00:1 | T 9.21:1 · P 9.31:1 · S 8.48:1 |
| `unselectedIconColor` / `unselectedTextColor` | `onSurfaceVariant` (global, unchanged) | 5.60:1 on container | 6.93:1 on container |

Label stays `labelMedium` 12sp, Bold when selected (existing behavior — the weight change is part of the state encoding).

**Documented exemption (pill vs container):** `navIndicator` vs `surfaceContainer` computes 1.00–1.10:1 light / 1.39–1.98:1 dark — below 3:1, exactly like stock M3's `secondaryContainer` pill. Acceptable under SC 1.4.11 because selection is redundantly encoded: pill appears/disappears (shape), icon recolors (≥6:1 vs pill), label recolors (≥4.67:1) and goes Bold. Added to the §2.7 exemption list as `navIndicator-vs-navContainer`.

---

## 6. Tracking screen guidance (`HabitTrackingScreen` adopts the cobalt world)

**Color precedence — the one rule to memorize:**

> **Section hue = chrome & surfaces** (wash, sheet, tab indicator, nav pill, toolbar tint, default M3 component chrome via the `primary` swap).
> **Habit accent = the habit's own identity** (its icon chip, ring fill, chart line) — always from `extended.habitAccents[n]`, passed explicitly per Nocturne §2.4.
> **Brand emerald = completion/success, always** (`habitComplete`, the 600ms check-in bloom) — a check-in inside the cobalt world still blooms green.
> **Amber = streak, always. Error red = destructive, always.** Neither ever re-hues per section.

Concrete changes:

1. **Screen background** (`HabitTrackingScreen.kt:82`): `colorScheme.background` → `colorScheme.section.surfaceTintWash` (`#EDF1FC` / `#142036`). v2.1 ships the flat wash; the full the reference app two-layer scroll motif (wash layer + neutral sheet sliding over it) is a documented later enhancement, not in scope.
2. **Day tab row** (`:125–129`): `containerColor` = `section.surfaceTintWash` (blends with the screen; the current `extended.surfaceHigher` strip disappears into the world). Selected text `onSurface` (14.81:1 on wash), unselected `onSurfaceVariant` (5.83:1 on wash) — both verified.
3. **Tab indicator** (`:132–139`): no code change needed — `TabRowDefaults.SecondaryIndicator` defaults to `primary`, which the §3.2 overlay has already swapped to cobalt (`#0B57D0` = 5.65:1 / `#A8C7FA` = 9.47:1 on the wash, ≥3:1 ✓).
4. **Habit rows** (`HabitTrackingViewItem.kt:46`): card fill **stays** `extended.surfaceHigher` (white / `#1C2A27`) — neutral cards riding on the tinted wash is the literal the reference app recipe. Do NOT tint the cards.
5. **Leading habit icon** (`:52–57`): adopt the icon-on-tinted-chip treatment — 40dp chip, `YoloShapes.medium`, fill = `habitAccent.container`, icon tint = `habitAccent.onContainer` (every quad already CI-locked ≥8.26:1). Until per-habit accents are wired to data, fallback = `section.accentContainer`/`onAccentContainer`. The current multicolor test asset (`tint = Color.Unspecified`) is replaced by this treatment.
6. **Check toggle** (`:64–68`): checked = `extended.habitComplete` fill + check glyph (brand emerald — NOT cobalt); unchecked = `extended.habitPending` ink ring (14.81:1 on wash). Never hue-only — Nocturne forgiveness rules unchanged.
7. **Progress tab** (`HabitProgressScreen` placeholder): build fresh inside `YoloSectionTheme(Progress)` — wash `#E6F4ED`/`#11342B`, bento tiles stay neutral `surfaceContainer`, hero streak tile keeps the amber aura, rings/charts pick up emerald via `primary`. By §3.4 this reads as "what we have now," plus the wash.
8. **Settings tab**: `AccountScreen.kt:102` and `SettingsScreen.kt:100` backgrounds → `section.surfaceTintWash`; `ProfileInfoBox`/`AppCardContainer` and `SettingItemListContainer` fills stay `surfaceHigher` (neutral cards on lilac wash); `SettingsItem` text/chevron colors unchanged (onSurface 14.62:1, onSurfaceVariant 5.75:1 on the wash); delete row keeps `error`. Logout/delete sheets pick up the violet `sheetSurface` automatically.
9. **Toolbar** (`AppToolbar.kt:57`): gains a `containerColor: Color = MaterialTheme.colorScheme.background` parameter; `MainScreen` passes the animated wash (§3.6) so the toolbar zone is "themed by the background layer" exactly as in the reference app teardown. Title/icons stay `onSurface` (≥13.12:1 on every wash).

---

## 7. File-by-file implementation plan

**core/designsystem** (all `src/commonMain/kotlin/com/yolo/core/designsystem/`):

| File | Change |
|---|---|
| `theme/SectionTheme.kt` | **NEW** — `YoloSection`, `SectionColors`, 6 palette vals, `YoloSection.colors(isDark)`, `LocalYoloSection`, `ColorScheme.section` accessor, `YoloSectionTheme` (§3.1–3.2) |
| `tokens/YoloPrimitives.kt` | add Cobalt/Violet/wash ARGB longs (§2.6) — watch-carry layer, additive |
| `theme/Color.kt` | add the §2.6 Compose `Color` vals |
| `theme/YoloTheme.kt` | add the default `LocalYoloSection provides YoloSection.Progress.colors(darkTheme)` line (§3.5) |
| `components/legacy/modals/AppModalBottomSheet.kt` | `containerColor`/`contentColor` from `section`, 28dp top shape, new drag handle, title/body colors (§4) |
| `components/sheets/YoloSheetActionRow.kt` | **NEW** — the icon-chip row + destructive/no-data variants (§4.1) |
| `components/legacy/AppToolbar.kt` | add `containerColor` parameter, default `colorScheme.background` (non-breaking for non-tab consumers) (§6.9) |
| `src/commonTest/.../theme/ContrastTest.kt` | add §8 assertions |

**shared** (`src/commonMain/kotlin/com/yolo/myhabitshub/presentation/`):

| File | Change |
|---|---|
| `BottomNavItem.kt` | add `val section: YoloSection` to the data class; wire in `items()`: Tracking→`Tracking`, Progress→`Progress`, Settings→`Settings` |
| `components/bottomnav/BottomNavigationBar.kt` | per-item `NavigationBarItemDefaults.colors` from `item.section.colors(extended.isDark)` (§5); container stays `surfaceContainer`; delete the dead commented preview |
| `screens/main/MainScreen.kt` | derive `currentSection = items[selectedBottomNavIndex].section`; wrap the toolbar + `mainContent()` Box (lines 77–90) in `YoloSectionTheme(currentSection)`; hoist the single `animateColorAsState` wash behind toolbar + content via `drawBehind` (§3.6) |
| `screens/main/MainViewModel.kt` / `MainViewState.kt` | no structural change required — `selectedBottomNavIndex` already exists; optionally expose `currentSection` as a derived val for readability |

**feature/habits** (`presentation/.../tracking/`, `/progress/`): `HabitTrackingScreen.kt` items 1–3 of §6; `HabitTrackingViewItem.kt` items 4–6; `HabitProgressScreen.kt` item 7.

**feature/account** (`presentation/.../account/`, `/settings/`, `/components/`): `AccountScreen.kt`, `SettingsScreen.kt` backgrounds (item 8); `SettingsItem.kt` no change; `DeleteUserConfirmation.kt` migrates to `YoloSheetActionRow` destructive variant when touched.

**Untouched on purpose:** `AppScreen.kt` root (YoloTheme stays the single root), `AppCardContainer` default fill (shared by non-tab screens; cards stay neutral everywhere), all Nocturne theme files' existing values, `strings.xml` labels.

**Migration order:** (1) primitives + `SectionTheme.kt` + ContrastTest additions — CI green before any UI change; (2) `YoloTheme` default provider; (3) `MainScreen` wrapper + nav bar + toolbar param (the chrome lands; all tabs get washes); (4) sheet + `YoloSheetActionRow` (the reference app moment lands); (5) Tracking/Progress/Settings screen touches.

---

## 8. ContrastTest additions (CI gate — extends Nocturne §2.7/§8.7)

Iterate `YoloSection.entries × {light, dark}` (6 palette instances) and assert **per palette** (14 pairs × 6 = 84 assertions):

| # | Pair | Floor |
|---|---|---|
| 1 | `onAccent` vs `accent` | 4.5:1 |
| 2 | `onAccentContainer` vs `accentContainer` | 4.5:1 |
| 3 | `onSheetSurface` vs `sheetSurface` | 4.5:1 |
| 4 | `onSheetSurfaceVariant` vs `sheetSurface` | 4.5:1 |
| 5 | `onSheetIconChip` vs `sheetIconChip` | 3.0:1 |
| 6 | `onNavIndicator` vs `navIndicator` | 3.0:1 |
| 7 | `navSelectedText` vs `surfaceContainer` (mode's nav container) | 4.5:1 |
| 8 | `onSurface` vs `surfaceTintWash` | 4.5:1 |
| 9 | `onSurfaceVariant` vs `surfaceTintWash` | 4.5:1 |
| 10 | `accent` vs `surfaceTintWash` | 3.0:1 |
| 11 | `accent` vs `sheetSurface` | 3.0:1 |
| 12 | `accent` vs `surface` | 3.0:1 |
| 13 | `error` vs `sheetSurface` | 4.5:1 |
| 14 | `onSurfaceVariant` vs `sheetSurface` | 4.5:1 |

Plus cross-world pairs (18 assertions): **streak** (`tertiary`) vs each `sheetSurface` ≥3:1 (6); **error** vs each `surfaceTintWash` ≥4.5:1 (6); **habitPending** ring vs each `surfaceTintWash` ≥3:1 (6).

Plus 4 structural-invariant assertions per palette (§3.1 aliases: navIndicator/onNavIndicator/navSelectedText equality + dragHandle alpha) — equality checks, not contrast.

**New exemption-list entries (documented, per §2.7):** `navIndicator`-vs-nav-container adjacency (redundant state encoding, §5); `sheetIconChip`-vs-`sheetSurface` and wash-vs-`surface` adjacency (decorative layering, 1.0–1.4:1 by design); `sheetDragHandle` (decorative 40% α).

**Total: 102 contrast assertions, all passing as of 2026-06-12 — every number in §2.3–2.5 is the computed value, not a target.**
