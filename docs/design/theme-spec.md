# Nocturne — Yolo Design Language v2 (FINAL)

**Status:** Final. Implementable without further decisions.
**Base:** Proposal 3 "Nocturne Editorial" (aggregate winner), with every judge-found flaw fixed and the strongest runner-up ideas grafted in (provenance log in §0).
**Stack contract (hard):** Compose Multiplatform Material3 only, everything in `commonMain`, **zero `expect/actual` in `core/designsystem`**. `MaterialTheme(colorScheme, typography, shapes)` + **one** extended-tokens CompositionLocal (`LocalExtendedColors`, evolved) + spacing/sizing/motion CompositionLocals. Component prefix stays `Yolo*`. Entry point stays `YoloTheme(darkTheme, content)`.
Every contrast ratio in this document was computed with the WCAG 2.2 relative-luminance formula against the exact hexes printed here (verification script run 2026-06-12; judges independently confirmed the shared pairs).

---

## 0. Decisions & provenance (what changed vs. the winning proposal, and why)

| # | Decision | Source / judge flaw fixed |
|---|---|---|
| 1 | **Space Grotesk dropped.** Single-family system: Manrope variable 400–800. Display/stat hierarchy carried by Manrope ExtraBold + tabular numerals. | Market judge ("drop or soften Space Grotesk"), a11y judge (2-font payload/QA risk, tight SG leading), typography research's definitive recommendation. Grafted from Stillwater/Momentum. Space Grotesk remains a documented, sanctioned later option for hero numerals only — non-goal for v2. |
| 2 | **All 20 legacy `accent*`/`cake*` ExtendedColors fields DELETED** (repo grep: zero consumers outside theme files — the "chat bubbles keep them" premise was false). Index migration map provided (§6.5). | Impl judge FLAW 1; market judge "muddies the system story". Deletion discipline grafted from Stillwater, index mapping from Momentum. |
| 3 | **Spacing & sizing are `@Immutable` data classes provided via CompositionLocals, profile-keyed by WindowSizeClass** — not a "plain object" and not a composable selector function. Responsive `screenEdge` now has a real mechanism. | Impl judge FLAWs 2 & 3. Grafted from Stillwater. |
| 4 | ~~`NativeAlertDialog` expect/actual explicitly deleted~~ **AMENDED (product decision 2026-06-12): `NativeAlertDialog` expect/actual is KEPT** (its Android actual migrates to Nocturne tokens; new dialogs may be added as needed). `SystemAppearance` expect/actual deleted (re-homed to app entry points). Consequence: a future desktop target needs a jvmMain actual for `NativeAlertDialog` (or it gains a default M3 implementation then). | User decision overriding impl judge. |
| 5 | **Bento grid re-specified as a non-lazy Column-of-Rows composition** (`YoloBentoGrid`). Dashboards cap at ~8 tiles; row spans expressed as fixed row heights, column spans as `Modifier.weight`. No `LazyVerticalGrid` row-span impossibility, no custom Layout math. | Impl judge ("LazyVerticalGrid supports column spans only"). |
| 6 | **Corrected numbers:** dark `surfaceBright` onSurface = **7.82:1** (was misquoted 8.23); light `onPrimaryContainer` = **10.37:1**; `#FFB870` on AMOLED black = **12.34:1** exact (was "≈12:1-class"). | A11y judge. |
| 7 | **`habitSkipped` light fixed to `#7A8A86` (3.38:1 on surface)** — Nocturne's `#94A3A0` computed 2.45:1, below the 3:1 non-text floor. | Extension of the a11y judge's affordance-visibility finding to every habit state. |
| 8 | **Complete ExtendedColors v1→v2 field table**, incl. `destructiveSecondaryOutline` (live consumer at `YoloButton.kt:85`) and `successOutline` dispositions. | Impl judge (P1 dropped fields silently). Grafted from Momentum's table completeness. |
| 9 | **Aura recolored: the everyday streak-tile aura is amber** (streak channel); mint aura is reserved for `heroSurface` brand moments (onboarding/celebration/paywall). Max one aura per screen, never behind body text. | Market judge ("mint glow = 2024–26 AI-app cliché"; warmth miscue). |
| 10 | **CVD claims made honest:** the 8 light accents are near-equiluminant by construction (pinned to ~4.7–5.6:1 on white), so hue pairs collapse under deuteranopia. Therefore color NEVER carries meaning alone: habit identity = color + icon; completion = fill + check vs. outline; heatmap adds fill-amount redundancy. No "CVD-checked" claim. | A11y judge (false-confidence claims in all three proposals). |
| 11 | **Dark-canonical rationale rewritten** (no self-contradiction, unsourced "82%" stat removed). Dark is the brand-canonical/review-first mode (evening/bedside usage cluster, OLED, store signature); light "paper" mode is a co-equal, fully specified citizen and the daylight default via `isSystemInDarkTheme()`. | Market judge. |
| 12 | **Kickers:** ALL-CAPS only on data/stat tiles (applied via `.uppercase()` in `YoloKicker` — Compose has no textTransform); coaching/greeting copy uses warm sentence-case headlines. | Market judge (warmth), impl judge (textTransform note). |
| 13 | **`reducedMotion` is a real token** in `YoloMotion`, plumbed as a plain `YoloTheme` parameter that platform entry points set from the OS setting. Pure commonMain. | A11y/impl judges (mechanism unspecified). Grafted from Stillwater. |
| 14 | **Token accessor object named `YoloTokens`** — avoids the `fun YoloTheme` / `object YoloTheme` same-name footgun. | Impl judge (flagged on Stillwater; pre-empted here). |
| 15 | **`material3-adaptive-navigation-suite` is NOT in the version catalog** — explicit catalog addition specified (§8.6). | Impl judge (P1 misclaimed it was cataloged). |
| 16 | **`progressIndicator` is a concrete token (= primary)**; habit rings receive their `HabitAccent` explicitly as a parameter. No conditional pseudo-token. | Impl judge. |
| 17 | **Bottom-nav labels stay 12sp (`labelMedium`)** — the a11y judge specifically praised this over the 11sp alternatives. | A11y judge. |
| 18 | **Light error stays `#BA1B32`** (5.73:1 even on the lightest card fill) — avoids Momentum's brand-red AA failure on cards. | A11y judge. |
| 19 | Duplicated spacing vocabulary removed: feature code sees ONLY semantic names; raw dp/ARGB primitives live in a single watch-carryable `tokens` file that feature modules are forbidden to import. | Impl judge ("two ways to say the same thing"). |
| 20 | The "primary-as-text-emphasis" call site (`AllComponentsGallery.kt:106`) is disambiguated from CTA-fill `primary` in the migration table. | Grafted from Stillwater (only proposal that caught it). |
| 21 | **Dark `habitMissed` lightened `#5E6D69` → `#7A8A86`** — the old value passed vs `surface` (3.24:1) but computed 2.74:1 on the §6.2 card `#1C2A27` / 2.92:1 on bento tiles `#182522`. CI gate (§2.7/§8.7) extended to assert habit states against card/tile fills, not just surface; dark `outline` scoped off elevated containers (hard rule 5); light `warning` restricted on tinted surfaces. | Post-final audit (2026-06-12) — same blind-spot class as #7, now structurally closed. |

---

## 1. Brand direction

**Design language name: Nocturne** (Yolo Design Language v2).

Nocturne is a dark-canonical, editorial take on calm-premium: a teal-ink near-black canvas (the brand's `#092E2D` DNA tinting every neutral) with magazine-grade hierarchy — kickers, oversized tabular numerals, bento-grid stats — and exactly two expressive hues: brand mint for identity and a warm amber streak channel for momentum. The resting UI is near-monochrome and quiet; full saturation, springs, and the single aura glow are budgeted exclusively to moments of progress — check-in, streak, milestone — with forgiveness designed in (a missed day is a muted outline, never a red zero). Light "paper" mode is the same identity printed on warm teal-tinted paper: white cards that pop, emerald instead of mint where math demands it, co-equal and CI-tested.

---

## 2. Color system

### 2.1 Primitives (`theme/Color.kt` — replaces the blue-slate `YoloBase` ramp)

```
// Brand (mint/emerald, hue ≈158–165) — heritage values kept
YoloBrand1000 = #092E2D   // deep teal — heroSurface ONLY, never everyday background
YoloBrand900  = #106461
YoloBrand800  = #047857   // NEW — light primary
YoloBrand700  = #0B5C44   // NEW — dark primaryContainer
YoloBrand600  = #19BA87   // hover, heatmap L3
YoloBrand500  = #4DDEAB   // dark primary, brand hero accent
YoloBrand300  = #7BE8C2   // NEW — heatmap L2 light
YoloBrand200  = #A8F0D4   // NEW — dark onPrimaryContainer
YoloBrand100  = #CEF5E4   // light primaryContainer (kept)
YoloBrandDeep = #073D2F   // NEW — light onPrimaryContainer

// Ink ramp (light-mode neutrals + dark-mode content; teal-tinted, hue ≈175°, sat 4–8%)
YoloInk0   = #FFFFFF      YoloInk50  = #F4F8F7     YoloInk100 = #EEF4F2
YoloInk150 = #E8EEEC      YoloInk200 = #DFE7E5     YoloInk250 = #D5DDDB
YoloInk300 = #B6C2BF      YoloInk350 = #A6B4B0     YoloInk400 = #94A3A0
YoloInk450 = #7A8A86      YoloInk500 = #6E7E7B     YoloInk550 = #5E6D69
YoloInk600 = #52605D      YoloInk700 = #3A4744     YoloInk800 = #28332F
YoloInkText = #E2E8E6     // dark-mode body text — softened white, no halation

// Deep ramp (dark-mode surfaces; teal-tinted near-black, never #000)
YoloDeep50  = #22312E     YoloDeep100 = #1C2A27    YoloDeep200 = #182522
YoloDeep300 = #14201E     YoloDeep400 = #101B1A    YoloDeep500 = #0C1514

// Amber (streak / tertiary channel — the signature warm hue)
YoloAmber800 = #5F2D03    YoloAmber700 = #B45309   YoloAmber300 = #FFB870
YoloAmber100 = #FFE9D1    YoloAmberDeepContainer = #4A2D0E
YoloAmberOnDark = #4A2800 YoloAmberContainerOn = #FFD9B0

// Red (error)
YoloRed700 = #BA1B32      // light error (replaces #DA233E primary-error duty)
YoloRed600 = #AA142A      // kept (hover)
YoloRed300 = #FF8A95      // dark error
YoloRed200 = #FF7987      // kept (destructive hover dark)
YoloRed100 = #FFDADD      YoloRedDeep900 = #55121C  YoloRedDeep800 = #68101F

// Info blue
YoloBlue700 = #0B69C7     YoloBlue300 = #8AC2FF    YoloBlue100 = #DCEBFB
YoloBlueDeep = #0A3D70    YoloBlueOnDark = #002E52 YoloBlueDarkContainer = #16344E
YoloBlueDarkContainerOn = #D6E8FF
```

Deleted after migration completes: `YoloBase*` (all), `YoloBrand500Alpha40`, the 10 alpha `Yolo<Accent>` colors, all 20 `YoloCake*` colors, `YoloRed500` (#DA233E survives only if marketing needs it; it carries no UI role).

### 2.2 Material3 ColorScheme — DARK (canonical mode; design and review dark-first)

| Role | Hex | Verified contrast |
|---|---|---|
| primary | `#4DDEAB` | 10.35:1 on surface; 7.99:1 on surfaceContainerHighest; 9.31:1 on surfaceContainer |
| onPrimary | `#092E2D` | 8.58:1 on primary (the brand pairing) |
| primaryContainer | `#0B5C44` | — |
| onPrimaryContainer | `#A8F0D4` | 6.12:1 on container |
| inversePrimary | `#047857` | — |
| secondary | `#B6C2BF` | — |
| onSecondary | `#1C2724` | 8.39:1 on secondary |
| secondaryContainer | `#3A4744` | — |
| onSecondaryContainer | `#DCE7E3` | 7.66:1 on container |
| tertiary (streak) | `#FFB870` | 10.34:1 on surface |
| onTertiary | `#4A2800` | 7.74:1 on tertiary |
| tertiaryContainer | `#4A2D0E` | — |
| onTertiaryContainer | `#FFD9B0` | 9.45:1 on container |
| error | `#FF8A95` | 7.81:1 on surface |
| onError | `#55121C` | 6.26:1 on error |
| errorContainer | `#55121C` | — |
| onErrorContainer | `#FFD3D7` | 10.44:1 on container |
| background | `#0C1514` | — |
| onBackground | `#E2E8E6` | 14.93:1 |
| surface | `#101B1A` | teal-tinted near-black; never #000 (tone elevation needs headroom) |
| onSurface | `#E2E8E6` | **14.17:1** on surface; 12.75:1 on surfaceContainer; 12.00:1 on High; 10.94:1 on Highest. Reserve `#F4F8F7` for display headlines only (halation control) |
| surfaceVariant | `#28332F` | — |
| onSurfaceVariant | `#A6B4B0` | 8.19:1 on surface; 6.32:1 on surfaceContainerHighest |
| surfaceDim | `#0C1514` | — |
| surfaceBright | `#3A4744` | onSurface on it = **7.82:1** (corrected — passes) |
| surfaceContainerLowest | `#0C1514` | — |
| surfaceContainerLow | `#14201E` | — |
| surfaceContainer | `#182522` | bento tile default |
| surfaceContainerHigh | `#1C2A27` | sheets, dialogs |
| surfaceContainerHighest | `#22312E` | input fills, ring tracks |
| outline | `#5E6D69` | **3.24:1** on surface (3.41 on Lowest/Dim, 3.08 on Low) — interactive borders pass SC 1.4.11 **on those fills only**; fails on elevated containers: 2.92 on surfaceContainer, 2.74 on High, 2.50 on Highest → hard rule 5 |
| outlineVariant | `#28332F` | decorative hairlines only |
| inverseSurface | `#E8EEEC` | inverseOnSurface on it = 14.24:1 |
| inverseOnSurface | `#14201E` | — |
| scrim | `#000000` | applied at 60% |
| surfaceTint | `#4DDEAB` | tonal elevation only — **shadowElevation = 0 everywhere in dark** |

### 2.3 Material3 ColorScheme — LIGHT ("paper" edition; co-equal, fully specified)

| Role | Hex | Verified contrast |
|---|---|---|
| primary | `#047857` | 5.48:1 on white; 5.12:1 on surface — passes as text |
| onPrimary | `#FFFFFF` | 5.48:1 |
| primaryContainer | `#CEF5E4` | mint lives here |
| onPrimaryContainer | `#073D2F` | **10.37:1** on container (corrected figure) |
| inversePrimary | `#4DDEAB` | — |
| secondary | `#52605D` | — |
| onSecondary | `#FFFFFF` | 6.58:1 |
| secondaryContainer | `#DCE7E3` | — |
| onSecondaryContainer | `#1C2724` | 12.15:1 |
| tertiary (streak) | `#B45309` | 5.02:1 on white; 4.69:1 on surface (≥14sp Medium or icon use on tinted surfaces; body-size streak text on cards uses onTertiaryContainer) |
| onTertiary | `#FFFFFF` | 5.02:1 |
| tertiaryContainer | `#FFE9D1` | — |
| onTertiaryContainer | `#5F2D03` | 9.57:1 |
| error | `#BA1B32` | 6.38:1 on white; 5.96:1 on surface; **5.73:1 on lightest card fill `#EEF4F2`** — headroom everywhere |
| onError | `#FFFFFF` | 6.38:1 |
| errorContainer | `#FFDADD` | — |
| onErrorContainer | `#68101F` | 9.70:1 |
| background | `#F4F8F7` | elevated neutral, never pure-white canvas |
| onBackground | `#14201E` | 15.63:1 |
| surface | `#F4F8F7` | — |
| onSurface | `#14201E` | **15.63:1** |
| surfaceVariant | `#E8EEEC` | — |
| onSurfaceVariant | `#52605D` | 6.15:1 on surface; 5.60:1 on surfaceContainer |
| surfaceDim | `#D5DDDB` | — |
| surfaceBright | `#FFFFFF` | — |
| surfaceContainerLowest | `#FFFFFF` | cards pop off the paper (+1dp soft shadow) |
| surfaceContainerLow | `#EEF4F2` | input fills (placeholder = 5.91:1 ✓) |
| surfaceContainer | `#E8EEEC` | nav bar/rail |
| surfaceContainerHigh | `#DFE7E5` | — |
| surfaceContainerHighest | `#D5DDDB` | — |
| outline | `#6E7E7B` | 4.26:1 on white; 3.98:1 on surface ✓ 3:1 |
| outlineVariant | `#D5DDDB` | decorative only |
| inverseSurface | `#1C2724` | inverseOnSurface on it = 13.81:1 |
| inverseOnSurface | `#EEF4F2` | — |
| scrim | `#000000` | 60% |
| surfaceTint | `#047857` | light cards: tone + 1dp shadow allowed |

**Hard rules.**
1. Mint-filled controls in light mode use deep-teal content `#092E2D` (8.58:1). **White-on-mint (1.70:1) is banned.**
2. `#092E2D` is never an everyday background — it is `extended.heroSurface` (onboarding hero, milestone celebration card, paywall).
3. Dark elevation = tone only (`shadowElevation = 0`); light = tone + max 1dp soft shadow on resting cards, 3dp raised, 6dp dialogs.
4. Light-mode root background is `surface #F4F8F7` — fixes the legacy bug where the M3 `background` slot held `#092E2D` in light mode (`Theme.kt:181`).
5. **Dark `outline` borders are banned on elevated dark containers** (`surfaceContainer` and above): `#5E6D69` computes 2.92:1 on `surfaceContainer #182522`, 2.74:1 on `surfaceContainerHigh #1C2A27` (sheets/dialogs) and 2.50:1 on `surfaceContainerHighest #22312E` (input fills) — all under the SC 1.4.11 3:1 floor. Interactive borders on those fills use `onSurfaceVariant #A6B4B0` instead (7.37 / 6.93 / 6.32:1 respectively); filled inputs keep the M3 indicator-line idiom rather than an outline.

### 2.4 ExtendedColors v2 (the ONE CompositionLocal — evolved in place, `theme/Theme.kt`)

`ExtendedColors` data class, `LocalExtendedColors`, and the `ColorScheme.extended` accessor are kept. Fields are re-pointed/extended per the table; `accent*`/`cake*` are removed (§6.5). New: `val isDark: Boolean` replaces legacy `LocalThemeIsDark`.

| Token | Light | Dark | Verified contrast / note |
|---|---|---|---|
| isDark | `false` | `true` | replaces `LocalThemeIsDark` (`AuthUIHelperButtons.kt:52`) |
| **Status (M3 has only error)** | | | |
| success / onSuccess | `#047857` / `#FFFFFF` | `#4DDEAB` / `#092E2D` | = primary family **by design** (completion IS the brand; documented). 5.48 / 10.35 & 8.58 |
| successContainer / onSuccessContainer | `#CEF5E4` / `#073D2F` | `#14443A` / `#A8F0D4` | 10.37 / 8.38 |
| warning / onWarning | `#9A6A00` / `#FFFFFF` | `#F2C66B` / `#3D2A00` | L 4.73 on white but **4.42 on surface `#F4F8F7`** → on tinted surfaces ≥14sp Medium or icon only (same restriction as tertiary); body-size warning text on cards uses onWarningContainer. D 10.95 on surface & 8.54 |
| warningContainer / onWarningContainer | `#FFF1D6` / `#4A3300` | `#3D2E0F` / `#F4DFAE` | 10.66 / 10.03 |
| info / onInfo | `#0B69C7` / `#FFFFFF` | `#8AC2FF` / `#002E52` | 5.44 / 9.42 & 7.43 |
| infoContainer / onInfoContainer | `#DCEBFB` / `#0A3D70` | `#16344E` / `#D6E8FF` | 9.05 / 10.30 |
| **Habit domain** | | | |
| streak / onStreak | = tertiary / onTertiary | = tertiary / onTertiary | semantic alias — feature code reads `extended.streak` |
| streakContainer / onStreakContainer | = tertiaryContainer pair | = tertiaryContainer pair | |
| celebration | `#FFB870` | `#FFB870` | decorative only (confetti/particles) — exempt list; 8.57:1 on heroSurface when used as accent there |
| progressTrack | `#DFE7E5` | `#22312E` | ring/bar track |
| progressIndicator | `#047857` | `#4DDEAB` | **concrete default = primary**; habit rings pass their `HabitAccent.accent` explicitly as a parameter |
| habitComplete | `#047857` + check glyph | `#4DDEAB` + check glyph | never hue-only |
| habitMissed | `#6E7E7B` outline | `#7A8A86` outline | L 3.98 vs surface, 4.26 vs white card; D **4.86 vs surface, 4.12 vs card `#1C2A27`, 4.38 vs tile `#182522`, 3.75 vs `#22312E`** ✓ 3:1 on every §6.2 placement (audit fix: prior `#5E6D69` fell to 2.74/2.92 on card/tile) — muted, **never red, never a red zero** |
| habitSkipped | `#7A8A86` dash | `#6E7E7B` dash | **fixed:** L 3.38 vs surface, 3.62 vs white card; D 4.13 vs surface, 3.50 vs card `#1C2A27`, 3.72 vs tile `#182522` ✓ 3:1 everywhere (Nocturne's #94A3A0 was 2.45 — failed) |
| habitPending | `#14201E` ring | `#E2E8E6` ring | ink ring, 15.63 / 14.17 — the daily tap affordance stays fully visible |
| heatmapLevel0..4 | `#E8EEEC` `#CEF5E4` `#7BE8C2` `#19BA87` `#047857` | `#182522` `#14443A` `#0B5C44` `#19BA87` `#4DDEAB` | adjacent levels are NOT 3:1 apart by nature → every cell gets a 1dp `outlineVariant` border + 2dp gap; levels redundantly encoded by fill amount in accessibility mode; tap shows count |
| **Brand moments** | | | |
| heroSurface / onHeroSurface | `#092E2D` / `#E2E8E6` | same | 11.75:1 |
| heroAccent | `#4DDEAB` | same | 8.58:1 on heroSurface |
| auraAmber | `#2EFFB870` (18% α) | `#1FFFB870` (12% α) | **the everyday aura** (streak hero tile) |
| auraMint | `#334DDEAB` (20% α) | `#294DDEAB` (16% α) | heroSurface moments ONLY (onboarding/celebration/paywall) |
| elevatedCardOutline | `Transparent` | `#1AF4F8F7` (10% α) | 1dp hairline on dark cards |
| **Text aliases (migration bridge)** | | | |
| textPrimary | `#14201E` | `#E2E8E6` | = onSurface |
| textSecondary | `#52605D` | `#A6B4B0` | = onSurfaceVariant; 6.15 / 8.19 |
| textTertiary | `#6E7E7B` | `#94A3A0` | light = 4.26:1 → **large text (≥14sp Medium+) or icons only**; dark 6.71 is body-safe |
| textPlaceholder | `#52605D` | `#94A3A0` | NOT WCAG-exempt — verified vs actual input fills: 5.91 on `#EEF4F2`, 5.18 on `#22312E` |
| textDisabled | onSurface @38% | onSurface @38% | exempt, perceivable (M3 convention) |
| **Interaction & disabled** | | | |
| primaryHover | `#065F46` | `#19BA87` | 7.68 on white / 7.05 on surface (desktop hover) |
| destructiveHover | `#AA142A` | `#FF7987` | |
| disabledFill | onSurface @12% | onSurface @12% | |
| disabledOutline | onSurface @12% | onSurface @12% | |
| overlay | `#CC14201E` (80% α) | `#CC0C1514` (80% α) | modal scrim |
| **Surfaces (bridge)** | | | |
| surfaceLower | `#EEF4F2` | `#0C1514` | recessed wells |
| surfaceHigher | `#FFFFFF` | `#1C2A27` | the card color |
| surfaceOutline | `#1414201E` (8% α) | `#1AF4F8F7` (10% α) | card hairline |
| secondaryFill | `#EEF4F2` | `#22312E` | kept v1 field (live consumer `YoloTextFieldLayout.kt:50`; the §6.2 `alternative` mapping) — matches the input-fill mapping; decorative fill, content on it = onSurface (15.03 / 10.94) |
| **Per-habit** | | | |
| habitAccents | `List<YoloHabitAccent>` — exactly 8, §2.5 | | |

```kotlin
@Immutable data class YoloHabitAccent(
    val accent: Color, val onAccent: Color, val container: Color, val onContainer: Color,
)
```

### 2.5 Per-habit categorical palette — 8 hues × 4 roles × 2 modes (`extended.habitAccents[0..7]`)

Curated 8 (Okabe-Ito-*inspired* hue spacing — not a guarantee; two pairs sit closer than true Okabe-Ito spacing, see honesty note). **Honesty note (replaces all "CVD-checked" claims):** pinning the light accents to ~4.7–5.6:1 on white makes them near-equiluminant, so several hue pairs (blue↔indigo, amber↔olive) collapse under deuteranopia — and emerald↔teal sit only ~12.4° apart in light mode (hue 162.9° vs 175.3°; 16.1° in dark), weakly distinguishable even for normal vision. Color therefore NEVER carries meaning alone — habit identity is always color **plus icon**; status is always color **plus shape** (fill+check / outline / dash / ring). The habit picker shows exactly these 8 — curation, no color wheel.

**LIGHT** (every accent ≥4.73:1 on white — passes even as text; `onAccent = #FFFFFF` for all):

| # | Name | accent | on white | container | onContainer | pair ratio |
|---|---|---|---|---|---|---|
| 0 | Coral | `#C2402A` | 5.17 | `#FFE0D5` | `#6E2113` | 8.93 |
| 1 | Amber | `#9A6A00` | 4.73 | `#FFEFC2` | `#4A3300` | 10.41 |
| 2 | Olive | `#5E7000` | 5.54 | `#E8F0C0` | `#2C3500` | 10.92 |
| 3 | Emerald | `#047857` | 5.48 | `#CEF5E4` | `#073D2F` | 10.37 |
| 4 | Teal | `#0F766E` | 5.47 | `#C7F1ED` | `#073F3B` | 9.66 |
| 5 | Blue | `#0B69C7` | 5.44 | `#DCEBFB` | `#0A3D70` | 9.05 |
| 6 | Indigo | `#5B5BD6` | 5.37 | `#E4E4FB` | `#2D2D80` | 9.33 |
| 7 | Magenta | `#C02670` | 5.60 | `#FBDCE9` | `#6B1240` | 9.25 |

**DARK** (lightened, ~20pts desaturated — no vibrating-on-dark; every accent ≥9.07:1 on `#101B1A`):

| # | accent | on #101B1A | onAccent | accent/onAccent | container | onContainer | pair ratio |
|---|---|---|---|---|---|---|---|
| 0 | `#FFB4A3` | 10.31 | `#4A150B` | 8.77 | `#4A2117` | `#FFD6CB` | 10.34 |
| 1 | `#F2C66B` | 10.95 | `#3D2A00` | 8.54 | `#3D2E0F` | `#F4DFAE` | 10.03 |
| 2 | `#C4D26A` | 10.70 | `#2B3100` | 8.28 | `#2E3510` | `#E2EBAE` | 10.24 |
| 3 | `#5BD6A9` | 9.75 | `#003826` | 7.30 | `#14443A` | `#A8F0D4` | 8.38 |
| 4 | `#67D6CB` | 10.10 | `#003833` | 7.47 | `#0F4540` | `#B8EBE6` | 8.26 |
| 5 | `#8AC2FF` | 9.42 | `#002E52` | 7.43 | `#16344E` | `#D6E8FF` | 10.30 |
| 6 | `#B4B6FF` | 9.28 | `#24246B` | 7.19 | `#2E2E66` | `#DEDEFF` | 9.42 |
| 7 | `#FF9CC6` | 9.07 | `#4D0F2C` | 7.62 | `#4A1A33` | `#FFD3E4` | 10.55 |

Usage: `accent` = rings, checks, chart lines, icons (carries meaning, ≥3:1 vs surface with huge margin). `container` = card tints, chips (decorative only). All eight dark accents are AOD-safe on pure black (worst case `#FF9CC6` = 10.82:1).

**Brand collision (slot 3 — documented, accepted):** Emerald IS the brand hue — light accent `#047857` is byte-identical to `primary`/`success`/`progressIndicator`, and dark `#5BD6A9` vs dark primary `#4DDEAB` is **1.06:1** — so a slot-3 habit's rings/checks are visually indistinguishable from default progress/success chrome. Accepted for v2 because habit identity always carries color **plus icon** (honesty note above) and slot 3 deliberately reads as "the brand one" in the picker; if product later needs all 8 slots distinct from chrome, swap slot 3 to a non-brand hue in a palette revision — revisit then, not silently.

### 2.6 Aura gradients (the screenshot signature — one per screen, ever)

Static two-stop `Brush.radialGradient` in `Modifier.drawBehind`. No shaders, no animation by default.

- **Streak hero tile (everyday):** `[auraAmber @ 0f, Transparent @ 0.62f]`, center at top-leading third, radius ≈ 1.2 × tile width. Amber, not mint — warmth, and distance from the AI-glow cliché.
- **heroSurface moments (onboarding / milestone / paywall):** `#092E2D` background + `auraMint` radial + `heroAccent` display type. Optional 6s ±8dp center drift, `LinearEasing`, disabled when `reducedMotion`.
- **Hard rules:** never behind body text (display/headline slots only); never on list rows; max ONE aura visible per screen at rest; never on conversational/coach surfaces.

### 2.7 Contrast compliance & CI gate

WCAG 2.2 AA floor shipped: 4.5:1 body text, 3:1 large text/icons/component boundaries/each state. A `commonTest` unit test iterates every `onX`-over-`X` pair in both ColorSchemes, every ExtendedColors pair, all 8 habit accent quads (both modes), and the component-vs-surface set (outline, habitMissed/Skipped/Pending, progress pairs) asserting ≥4.5:1 / ≥3:1 respectively. **Habit state tokens are additionally asserted ≥3:1 against every fill §6.2 actually places them on — the card (`surfaceHigher`: white light / `#1C2A27` dark) and the bento tile (`surfaceContainer` dark `#182522`) — not just `surface`** (closes the audit-found gap where dark habitMissed `#5E6D69` passed vs surface at 3.24:1 yet computed 2.74:1 on the card; token now `#7A8A86` = 4.12:1 there). Documented exemption list: `celebration`, `outlineVariant`, `auraMint/auraAmber`, `disabled*`, `heatmapLevel` adjacency (mitigated by borders + redundancy). APCA (Lc 60+ body / 75+ small) is the design-time secondary check for dark pairs; AA remains the shipping gate.

---

## 3. Typography — Manrope variable, single-family system

### 3.1 Font to bundle

**One file:** `Manrope[wght].ttf` (variable, ~161 KB, SIL OFL 1.1) from the google/fonts repo, path `ofl/manrope/Manrope[wght].ttf` → place as `core/designsystem/src/commonMain/composeResources/font/manrope_variable.ttf`. **Delete all 6 static Manrope TTFs** (−581 KB → −72% font payload; also kills the `manrope_light` → `FontWeight.Thin` registration bug at `theme/Type.kt:28-32`).

Register weight instances 400/500/600/700/800:

```kotlin
private val Manrope: FontFamily
    @Composable get() = FontFamily(
        listOf(400, 500, 600, 700, 800).map { w ->
            Font(
                Res.font.manrope_variable,
                weight = FontWeight(w),
                variationSettings = FontVariation.Settings(FontVariation.weight(w)),
            )
        }
    )
```

Supported: CMP 1.10.0 (VF support since 1.8.2) + Android minSdk 26. Fallback: rely on Compose's implicit per-platform fallback (Roboto/SF) — never build manual `FontFamily.Default` chains. Contingency (only if an iOS VF quirk appears in the one-time simulator check): bundle 5 statics — `manrope_regular/medium/semi_bold/bold` (keep) + `manrope_extrabold` (add from the same repo), drop ExtraLight/Light.

**Space Grotesk: deliberately NOT bundled** (decision log #1). It remains the documented escape hatch if hero numerals ever need more display personality — `SpaceGrotesk[wght].ttf`, OFL, 133 KB, weights 500/700, display-numerals-only role.

### 3.2 Full Material3 scale — all 15 slots (`theme/Type.kt`, val `YoloTypography`)

Tuned for Manrope's tall x-height / semi-condensed width: body tracking reduced vs stock M3; displays get negative tracking + ExtraBold (no optical sizes in Manrope — weight does the work).

| Slot | Weight | Size/LineHeight | LetterSpacing | tnum | Usage |
|---|---|---|---|---|---|
| displayLarge | ExtraBold 800 | 56/64 | −0.25sp | yes | giant streak count, timer hero |
| displayMedium | ExtraBold 800 | 44/52 | −0.25sp | yes | stats hero, completion % |
| displaySmall | Bold 700 | 36/44 | 0 | — | onboarding hero, big dates |
| headlineLarge | Bold 700 | 32/40 | 0 | — | screen heroes ("Good morning") |
| headlineMedium | Bold 700 | 28/36 | 0 | — | section heroes |
| headlineSmall | SemiBold 600 | 24/32 | 0 | — | dialog/sheet titles |
| titleLarge | SemiBold 600 | 22/28 | 0 | — | top-bar titles |
| titleMedium | SemiBold 600 | 16/24 | +0.1sp | — | habit card name, list item title |
| titleSmall | Medium 500 | 14/20 | +0.1sp | — | sub-section headers |
| bodyLarge | Regular 400 | 16/24 | +0.15sp | — | primary reading text, inputs |
| bodyMedium | Regular 400 | 14/20 | +0.25sp | — | default body, descriptions |
| bodySmall | Regular 400 | 12/16 | +0.4sp | — | captions, helper text |
| labelLarge | SemiBold 600 | 14/20 | +0.1sp | — | buttons, CTAs |
| labelMedium | Medium 500 | 12/16 | +0.4sp | — | chips, tabs, badges, **bottom-nav labels (12sp — a11y-praised, keep)** |
| labelSmall | Medium 500 | 11/16 | +0.5sp | — | tiny meta (units, date ticks) |

“tnum” = `fontFeatureSettings = "tnum"` baked into the slot's TextStyle so streak counters and timers never jiggle as digits tick. One-time visual check of tnum + VF weights on the iOS simulator (Skia path) is a scheduled migration step.

### 3.3 Editorial extension styles (`YoloTypeExtras` — plain `TextStyle` vals in Type.kt, not a CompositionLocal)

- `statHero`: ExtraBold 800, 72/80, −0.5sp, tnum — expanded/desktop hero-tile numeral. **Single-line only by contract:** components render it with `maxLines = 1` inside `YoloAutoSizeText` (auto-shrink), which is the documented wrap guard for the tight leading.
- `kicker`: Bold 700, 12/16, +1.5sp — editorial eyebrow on **data/stat tiles only** ("CURRENT STREAK"). ALL-CAPS applied via `text.uppercase()` inside `YoloKicker` (Compose has no textTransform). Color: `textSecondary`, or `heroAccent` on heroSurface. Coaching/greeting copy NEVER uses kicker styling — warm sentence-case headlines (`headlineLarge`).
- `statUnit`: SemiBold 600, 14/20, +0.1sp — "days"/"%" suffixes, baseline-aligned to the numeral.

Tile hierarchy recipe: kicker → display numeral (tnum) → `bodyMedium` support line, left-aligned, generous top whitespace.

Delete together with the legacy theme: `AppTypographyLegacy`, `appTypographyLegacy`, `MaterialThemAppTypography` (all in `theme/Type.kt`, consumed only by `theme/legacy/Theme.kt`).

---

## 4. Shapes, spacing, touch targets, motion

### 4.1 Shape scale (`theme/Shape.kt` — NEW; currently missing from YoloTheme)

```kotlin
val YoloShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),   // badges, tiny chips
    small      = RoundedCornerShape(10.dp),  // chips, snackbar, menus
    medium     = RoundedCornerShape(14.dp),  // text fields, small cards, dialog buttons
    large      = RoundedCornerShape(20.dp),  // standard cards, list containers
    extraLarge = RoundedCornerShape(28.dp),  // bento tiles, sheets (top corners), dialogs, hero cards
)
```

Component bindings: primary/secondary buttons = full pill (`CircleShape`); text fields = `medium`; bento tile & modal sheet = `extraLarge`; habit ring = circle, 6dp stroke; heatmap cell = 4dp radius, 2dp gap, 1dp border. No neumorphism; depth = tone (+1dp shadow in light only).

### 4.2 Spacing — `@Immutable data class YoloSpacing`, CompositionLocal, WindowSizeClass-keyed profiles

Feature code uses ONLY these semantic names (`YoloTokens.spacing.*`). Raw dp primitives exist solely in `tokens/YoloPrimitives.kt` (watch-carry layer) and are forbidden in feature modules.

| Semantic token | Compact (<600dp) | Medium/Expanded (600–1199) | Desktop (≥1200) | Legacy equivalent |
|---|---|---|---|---|
| elementGap | 8dp | 8dp | 8dp | defaultSpacing (8) |
| stackGapTight | 6dp | 6dp | 6dp | groupedVerticalElementSpacingSmall (6) |
| stackGap | 12dp | 12dp | 12dp | groupedVerticalElementSpacing (12) |
| iconTextGap | 12dp | 12dp | 12dp | inputIconTextSpacing (12) |
| itemGap | 16dp | 16dp | 16dp | horizontalItemSpacing (16) |
| cardPadding | 16dp | 16dp | 20dp | cardContentSpacing (16) |
| bentoGutter | 12dp | 12dp | 16dp | — (new) |
| listRowGap | 20dp | 20dp | 20dp | verticalListItemSpacing (20) |
| sectionHeaderGap | 20dp | 20dp | 20dp | sectionHeaderSpacing (20) |
| sectionGap | 24dp | 24dp | 32dp | sectionSpacing (24) |
| screenEdge | 24dp | 32dp | 40dp | outerSpacing (24) |
| dialogPadding | 32dp | 32dp | 32dp | dialogContentSpacing (32) |
| heroGap | 36dp | 36dp | 48dp | largeSpacing (36) |

(`verticalListItemSpacingSmall` 16dp: unused legacy — deleted.) Profile selection happens once inside `YoloTheme` from `currentWindowAdaptiveInfo().windowSizeClass` — this is the concrete mechanism for responsive `screenEdge` (decision log #3).

### 4.3 Sizing & touch targets — `@Immutable data class YoloSizing`, CompositionLocal, profile-keyed

| Token | Touch (phone/tablet) | Pointer (desktop) | Future watch profile |
|---|---|---|---|
| minTouchTarget | **48dp — hard floor, all form factors** | 48dp hit area | 48dp |
| controlHeight (buttons) | 52dp | 40dp visual (48dp hit via `Modifier.minimumInteractiveComponentSize()`) | 52dp |
| inputHeight | 56dp | 44dp | — |
| iconSmall / icon / iconLarge | 16 / 24 / 32dp | same | 24 / 32 |
| habitRing / ringStroke | 64dp / 6dp | 56dp / 5dp | — |
| navBarHeight / railWidth / drawerWidth | 80 / 80 / 280dp | — | — |
| maxFormWidth / maxReadingWidth / maxFeedWidth | 480 / 640 / 1080dp | same | — |
| hairline | 1dp | 1dp | 1dp |

**Pointer-profile mechanism (no expect/actual, no hand-waving):** `YoloTheme(inputProfile = YoloInputProfile.Auto)` — `Auto` selects Touch below 1200dp width and Pointer at ≥1200dp; platform entry points may override (the future desktop `main()` passes `Pointer`, Android/iOS pass nothing). A plain enum parameter, pure commonMain.

### 4.4 Motion tokens (`theme/Motion.kt`, `@Immutable data class YoloMotion`, CompositionLocal)

**Durations:** `instant=80ms` (state tints) · `quick=150ms` (hover, chip select) · `standard=250ms` (nav transitions, sheets) · `gentle=350ms` (pane reflow) · `emphasized=450ms` (hero entrances) · `celebration=600ms` (completion bloom — **hard cap; milestone set-pieces 1200ms absolute max**).

**Easings (`CubicBezierEasing`):** standard `(0.2, 0, 0, 1)` · decelerate `(0, 0, 0, 1)` · accelerate `(0.3, 0, 1, 1)` · emphasizedDecelerate `(0.05, 0.7, 0.1, 1)` · emphasizedAccelerate `(0.3, 0, 0.8, 0.15)`.

**Springs (reward register only — navigation never springs):**
- `pressSpring = spring(dampingRatio = 0.9f, stiffness = 1200f)` — pressable scale → 0.97 (buttons, tiles).
- `settleSpring = spring(dampingRatio = 0.65f, stiffness = 380f)` — ring fill / check settle.
- `celebrateSpring = spring(dampingRatio = 0.55f, stiffness = 220f)` — numeral tick-up, milestone scale.

**`reducedMotion: Boolean`** — a real token (decision log #13). When true: springs → 150ms fades, particles/aura-drift skipped, haptic kept. Plumbed as a `YoloTheme` parameter; `androidApp` reads `Settings.Global.ANIMATOR_DURATION_SCALE == 0f`, `iosApp` reads `UIAccessibilityIsReduceMotionEnabled()` — both in app layers, designsystem stays pure commonMain.

**The signature 600ms check-in (the ownable primitive):** press-and-hold → ring fills with the habit's `accent` (settleSpring) → haptic at 100% → 12–18 particle bloom in `accent` + `celebration` amber (450ms decelerate) → streak numeral ticks up (celebrateSpring). Milestones (7/30/100 days) additionally swap the tile to `heroSurface` + mint aura for one session view. Everyday check-ins never escalate — celebration economy with a noise cap.

---

## 5. Adaptive rules

**Source of truth:** `currentWindowAdaptiveInfo().windowSizeClass` (already wired in `core/presentation/.../DeviceConfiguration.kt`). androidx breakpoints verbatim — width **600 / 840**, opt-in **1200 / 1600** via `currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)` when the desktop target lands; height **480 / 900**. **Fix the documented bug first:** `DeviceConfiguration.kt:30-36` — a phone-landscape window (width 600–839, height <480) falls through to `DESKTOP`; add a branch for width-medium + height-compact → `MOBILE_LANDSCAPE`.

**Navigation adaptation** (`NavigationSuiteScaffold`; selected-destination state hoisted ABOVE the scaffold to survive desktop live-resize recomposition):

| Width class | Navigation | Layout |
|---|---|---|
| <600dp | bottom `NavigationBar` (80dp; labels `labelMedium` 12sp; active = `primary` icon on `secondaryContainer` pill indicator — drop the `Color.Transparent` hack) | single column; bento tiles full-width; forms full-bleed with `screenEdge` |
| 600–839 | `NavigationRail` (80dp, icons + labels) | 2-column bento; forms centered at `maxFormWidth` 480dp |
| 840–1199 | rail with labels; FAB docked to rail | `ListDetailPaneScaffold` (habit list 360–420dp pane ↔ detail ≥480dp) |
| ≥1200 | `PermanentNavigationDrawer` (280dp, `surfaceContainerLow`) | list-detail + `SupportingPaneScaffold` (detail + consistency grid); content column max 640dp |

**Max content widths:** auth/forms **480dp** (existing `YoloAdaptiveFormLayout` behavior — keep); settings/reading lists **640dp**; bento dashboard **1080dp** centered. Beyond max width: center, canvas shows `surface`. Height-compact (<480): collapse top bars; two-pane forms (existing behavior).

**Bento grid (implementable as specified — decision log #5):** `YoloBentoGrid` is a **non-lazy Column of Rows** (dashboards cap at ~8 tiles; the screen scrolls via its parent). Column spans = `Modifier.weight` within a Row; "row spans" = fixed tile heights in row units (1 unit = 156dp min; hero = 2 units + 2×`bentoGutter`). Compositions:
- Compact: hero streak tile full-width ×2 units (kicker + `displayLarge` + amber aura) → Row[today-progress, stat] ×1 unit → heatmap full-width ×1 unit.
- Medium: Row[hero (weight 1, 2 units), Column[stat, stat] (weight 1)] → heatmap full-width.
- Expanded+: Row[hero (weight 2, `statHero` numeral), weekly chart (weight 1)] → heatmap full-year row. The desktop money shot.
Tile surface: `surfaceContainer` + `elevatedCardOutline` hairline (dark) / `surfaceContainerLowest` + 1dp shadow (light); radius `extraLarge`; padding `cardPadding`; gutter `bentoGutter`.

**Desktop pointer polish (when jvm lands):** hover tint onSurface @8% + `pointerHoverIcon(PointerIcon.Hand)` on all interactives; persistent scrollbars; Esc = back; min window 800×600; hover-revealed secondary row actions allowed, primary actions never hover-only.

---

## 6. Legacy migration mapping (exhaustive — every observed token)

### 6.1 Theme roots & locals

| Legacy | New |
|---|---|
| `AppTheme { }` (`shared/.../app/AppScreen.kt:70`) + `YoloTheme { }` (`AppScreen.kt:86`) dual split | ONE `YoloTheme` at the AppScreen root for both auth states |
| `AppTheme(isDarkMode)` (`shared/.../AllComponentsGallery.kt:118`) | `YoloTheme(darkTheme = isDarkMode)` |
| `LocalThemeIsDark.current` (`feature/account/.../AuthUIHelperButtons.kt:52`) | `MaterialTheme.colorScheme.extended.isDark` |
| `SystemAppearance` expect/actual (`theme/legacy/Theme.kt:64` + android/ios actuals) | **DELETE.** Android: `enableEdgeToEdge()` in `AppActivity` (androidApp). iOS: status-bar style via iosApp config. Designsystem's only remaining expect/actual is `NativeAlertDialog` (kept by user decision) |
| `NativeAlertDialog` expect/actual (`components/legacy/modals/NativeAlertDialog.kt:29` + actuals) | **KEEP (user decision)** — Android actual re-pointed to `extended.surfaceHigher`; iOS actual is UIKit-only. Future desktop target requires a jvmMain actual |
| `LocalAppColors` / `LocalAppSpacing` / `LocalAppTypographyLegacy`, `lightModeAppColors` / `darkModeAppColors`, `asMaterialColorScheme`, all `ColorValues.kt` extensions | internal to legacy package — delete with it, no consumers |

### 6.2 `AppTheme.colors.*` → new tokens (usage counts from the audit)

| Legacy (sites) | New token |
|---|---|
| `text.primary` (27) | `colorScheme.onSurface` (bridge: `extended.textPrimary`) |
| `primary` as CTA/control fill (14 — Button.kt:76/83, Chip.kt:82/90/91/98, HorizontalPagerIndicator.kt:40, RadioButtonTextItem.kt:89, LoadingProgress.kt:60, UserInput.kt:77 (focus border), AppDialog.kt:64/173, OnBoardingScreenVariation2.kt:72, SignInScreen.kt:140) | `colorScheme.primary` (deliberate rebrand: near-black/white CTAs → emerald/mint) |
| `primary` as text/border emphasis (`AllComponentsGallery.kt:106`) | `colorScheme.onSurface` (disambiguated — decision log #20) |
| `text.secondary` (12) | `colorScheme.onSurfaceVariant` (bridge: `extended.textSecondary`) |
| `background` (8 + legacy root Surface) | `colorScheme.background` |
| `status.error` (7) | `colorScheme.error` |
| `onPrimary` (5) | `colorScheme.onPrimary` |
| `surfaceContainer` (4 — cards, dialogs, NativeAlertDialog.android:28) | `extended.surfaceHigher` (white card light / `#1C2A27` dark) |
| `outline` (4 — decorative dividers) | `colorScheme.outlineVariant`; interactive borders → `colorScheme.outline` |
| `alternative` (2) | `extended.secondaryFill` |
| `onAlternative` (1) | `colorScheme.onSurface` |
| `bottomNav.background` (1) | `colorScheme.surfaceContainer` |
| `bottomNav.selectedTextIcon` (2) | `colorScheme.primary` (verified: 4.67:1 light / 9.31:1 dark on surfaceContainer) |
| `bottomNav.unselectedTextIcon` (2) | `colorScheme.onSurfaceVariant` |
| `textInput.textIcon` (1) | `colorScheme.onSurface` |
| `textInput.background` (1) | `colorScheme.surfaceContainerLow` (light) / `surfaceContainerHighest` (dark) — set in `YoloTextField` defaults |
| `textInput.placeholder` (1) | `extended.textPlaceholder` |
| `textInput.readOnlyBackground` (1) | `colorScheme.surfaceContainerHigh` |
| `textInput.disabledTextIcon` (1) | `extended.textDisabled` |
| `textInput.disabledBackground` (1) | `extended.disabledFill` |
| `status.errorContainer` (1) | `colorScheme.errorContainer` |
| `status.info` / `status.infoContainer` (1 each, AppDialog) | `extended.info` / `extended.infoContainer` |
| `status.success*` / `status.warning*` (0 — unused) | now real tokens: `extended.success*` / `extended.warning*` quads |
| Hardcoded `Color.White` (`HabitTrackingScreen.kt:127`, `HabitTrackingViewItem.kt:44`) | `extended.surfaceHigher` — fixes dark mode |
| `indicatorColor = Color.Transparent` (`BottomNavigationBar.kt:48`) | `colorScheme.secondaryContainer` pill indicator (M3 idiom) |
| `tint = Color.Unspecified` (`HabitTrackingViewItem.kt:54,65`) | keep, with a `// multicolor asset` comment |

### 6.3 `AppTheme.spacing.*` → `YoloTokens.spacing.*` (values identical at compact — pixel-safe)

`defaultSpacing→elementGap` · `outerSpacing→screenEdge` · `sectionSpacing→sectionGap` · `sectionHeaderSpacing→sectionHeaderGap` · `groupedVerticalElementSpacing→stackGap` · `groupedVerticalElementSpacingSmall→stackGapTight` · `inputIconTextSpacing→iconTextGap` · `horizontalItemSpacing→itemGap` · `verticalListItemSpacing→listRowGap` · `verticalListItemSpacingSmall→DELETE (unused)` · `cardContentSpacing→cardPadding` · `dialogContentSpacing→dialogPadding` · `largeSpacing→heroGap`

### 6.4 `AppTheme.typography.*` → `MaterialTheme.typography.*`

| Legacy (sites) | New slot | Delta |
|---|---|---|
| h1 48/67 (1 — AutoResizableText default) | displayMedium 44/52 EB | tighter editorial leading, tnum |
| h2 40/56 (0) | — | delete |
| h3 32/45 (2) | headlineLarge 32/40 | |
| h4 24/34 (2) | headlineSmall 24/32 | |
| h5 20/28 Bold (7, incl. 2× `.copy(SemiBold)`) | titleLarge 22/28 SemiBold | SemiBold built-in — drop the `.copy()` hacks |
| h6 18/25 (3) | titleMedium 16/24 | intentional density |
| medium 16/24 Medium (1) | titleMedium 16/24 SemiBold | habit item title |
| bodyExtraLarge 18/29 (8) | bodyLarge 16/24 | intentional calm-down; true emphasis → titleMedium |
| bodyLarge 16/26 (6) | bodyLarge 16/24 | |
| bodyMedium 14/22 (6) | bodyMedium 14/20 | |
| bodySmall 12/19 (0) | bodySmall 12/16 | |
| bodyExtraSmall 10/16 (1 — nav labels) | labelMedium 12/16 | +2sp, a11y win |

### 6.5 ExtendedColors v1 → v2 (complete — all 38 fields dispositioned: 9 button-state + 5 text + 4 surface + 10 accent + 10 cake)

| v1 field | Disposition |
|---|---|
| primaryHover | kept — L `#065F46`, D `#19BA87` |
| destructiveHover | kept — L `#AA142A`, D `#FF7987` |
| destructiveSecondaryOutline | **DELETED as a field; live consumer `YoloButton.kt:85` re-pointed to `colorScheme.error`** (5.96:1 L / 7.81:1 D vs surface — passes as border) |
| disabledOutline | kept (consumers `YoloButton.kt:77,87`) — onSurface @12% both modes |
| disabledFill | kept (consumers `YoloButton.kt:46,52`) — onSurface @12% both modes |
| successOutline | DELETED — folded into `successContainer`; no consumers |
| success / onSuccess | kept (consumer `YoloSuccessIcon.kt:19`) — L `#047857`/`#FFFFFF`, D `#4DDEAB`/`#092E2D` |
| secondaryFill | kept (consumer `YoloTextFieldLayout.kt:50`) — L `#EEF4F2`, D `#22312E` (matches input-fill mapping) |
| textPrimary / textSecondary / textTertiary / textPlaceholder / textDisabled | kept, re-pointed to Ink ramp (§2.4); consumers across YoloButton/YoloIconButton/YoloTextField*/YoloAdaptiveFormLayout/YoloSimpleSuccessLayout/EmailVerificationScreen unchanged |
| surfaceLower / surfaceHigher / surfaceOutline / overlay | kept, re-pointed (§2.4) |
| accentBlue..accentGrey (10) | **DELETED** — zero consumers (repo-verified). Future need → `habitAccents[n].container`: Blue→5, Purple/Violet→6, Pink→7, Orange→0, Yellow→1, Green→2, Teal/LightBlue→4, Grey→`surfaceContainerHighest` |
| cakeViolet..cakeMint (10) | **DELETED** — zero consumers (repo-verified; the "chat bubbles" rationale was false). Future coach bubbles → `habitAccents[n].container/onContainer`: Violet/Purple→6, Green/Mint→3, Blue→5, Pink/Red→7, Orange→0, Yellow→1, Teal→4 |
| *(new fields)* | isDark; warning/info quads; successContainer pair; streak quad; celebration; progressTrack/Indicator; habitComplete/Missed/Skipped/Pending; heatmapLevel0..4; heroSurface/onHeroSurface/heroAccent; auraMint/auraAmber; elevatedCardOutline; habitAccents |

### 6.6 Component renames (components/legacy → components/, Yolo* convention)

`AppButton→YoloButton` (exists — merge variants) · `AppToolbar→YoloTopBar` · `AppCardContainer→YoloCard` · `AppModalBottomSheet→YoloSheet` · `AppDialog→YoloDialog` · `UserInput→YoloTextField` (exists — merge) · `LoadingProgress→YoloLoading` · `EmptyContentView→YoloEmptyState` · `Chip→YoloChip` · `Divider→YoloDivider` · `HorizontalPagerIndicator→YoloPagerDots` · `RadioButtonTextItem→YoloRadioRow` · `SectionContainer→YoloSection` · `Titles→` typography slots directly · `AutoResizableText→YoloAutoSizeText` · `NativeAlertDialog` kept as-is (user decision; token-only refresh) · `HorizontalScrollableList→YoloHorizontalList` · `AnimatedHorizontalPager/CircleButtonWithSteps/LogoImage/CustomDialog` → token-only refresh, keep names with Yolo prefix.
**New components:** `YoloBentoGrid` + `YoloBentoTile`, `YoloStreakRing` (press-and-hold fill), `YoloHeatmap`, `YoloKicker`, `YoloStreakBadge` (tnum), `YoloAuraBackdrop`.

### 6.7 Migration order

1. Land tokens: `Color.kt`, `Theme.kt` (schemes + ExtendedColors v2), `Type.kt` (VF + 15 slots + extras), NEW `Shape.kt` / `Dimens.kt` / `Motion.kt`, `YoloTheme.kt` v2 — plus the `commonTest` contrast gate.
2. Swap `AppScreen.kt:70` to the single `YoloTheme` root; replace `LocalThemeIsDark` consumer.
3. Migrate `components/legacy/*` per §6.2/§6.6 (mechanical); re-point `YoloButton.kt:85`.
4. Migrate feature screens: account, habits, onboarding, shared (incl. the two hardcoded `Color.White` fixes).
5. Delete `theme/legacy/` + both `SystemAppearance` platform actuals + legacy Type.kt symbols + static TTFs + `YoloBase*`/accent/cake constants. `NativeAlertDialog` expect/actual stays (user decision) — the only expect/actual left in designsystem; a desktop target later needs a jvmMain actual for it.
6. Fix `DeviceConfiguration.kt` landscape fall-through; add `material3-adaptive-navigation-suite` to the catalog; wire `NavigationSuiteScaffold`.
7. One-time iOS simulator check: VF weights 400–800 + tnum.

---

## 7. Wear-readiness checklist (encode now, build later — no watch screens designed)

1. **Primitives module is watch-consumable:** `tokens/YoloPrimitives.kt` holds ARGB longs + raw dp values with zero material3 dependencies; a future Android-only `wearApp` module consumes it to build Wear's own `ColorScheme`/typography. Feature modules never import it directly.
2. **Colors carry as values + semantic role names** (`streak`, `habitComplete`, `heatmapLevelN`, the 8 `HabitAccent` quads) — never hardcoded in components, so they survive Wear dynamic-color replacement.
3. **AOD/OLED-safe on pure black (verified):** `#4DDEAB` = 12.35:1, `#E2E8E6` = 16.92:1, `#FFB870` = **12.34:1**, worst habit accent `#FF9CC6` = 10.82:1.
4. **Typography split:** Manrope `FontFamily` (the file) carries; the phone `Typography` object does NOT. Watch profile rules pre-agreed: 12sp floor, body Regular→Medium, labels Medium→SemiBold (Wear M3 guidance).
5. **Touch targets as tokens:** `minTouchTarget = 48dp` everywhere; the future Watch `YoloSizing` profile sets `controlHeight = 52dp` (40dp only as a documented constrained exception).
6. **Check-in is a single-call domain use case** in a KMP module with no UI deps — the watch's entire 5-second app, and the data source for tiles/complications/widgets.
7. **Glance state exposed:** a tiny model (habit, done-today?, streak count) feeds dashboard hero tile today and tile/complication/watch later.
8. **Screens are chrome-free:** state + callbacks only; hosts own scaffolds (bar/rail/drawer today, `ScreenScaffold` on watch later). No layout assumes rectangular edges or bottom chrome.
9. **Does NOT carry:** M3 components, `YoloTypography`, `YoloShapes`, navigation patterns, `LocalExtendedColors` itself (watch rebuilds its scheme from primitives).

---

## 8. Kotlin architecture sketch (file-by-file)

All under `core/designsystem/src/commonMain/kotlin/com/yolo/core/designsystem/` unless noted. After migration step 5 the only expect/actual left in the module is `NativeAlertDialog` (kept by user decision).

### 8.1 `tokens/YoloPrimitives.kt` (NEW — layer 1, watch-carry; no material3, no compose-ui beyond Dp)

```kotlin
object YoloColorPrimitives {            // ARGB longs, not Compose Color
    const val Brand1000 = 0xFF092E2D; const val Brand500 = 0xFF4DDEAB
    const val Brand800 = 0xFF047857; const val Amber300 = 0xFFFFB870 // … full §2.1 set
}
object YoloSpacePrimitives { val s2 = 2.dp; val s4 = 4.dp; val s6 = 6.dp; val s8 = 8.dp
    val s12 = 12.dp; val s16 = 16.dp; val s20 = 20.dp; val s24 = 24.dp
    val s32 = 32.dp; val s36 = 36.dp; val s40 = 40.dp; val s48 = 48.dp }
```

### 8.2 `theme/Color.kt` (REWRITE) — `val YoloInk… = Color(0xFF…)` per §2.1; deletes `YoloBase*`, accents, cakes after step 5.

### 8.3 `theme/Theme.kt` (REWRITE) — schemes + the ONE extended local

```kotlin
val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }   // kept
val ColorScheme.extended: ExtendedColors @Composable @ReadOnlyComposable get() = LocalExtendedColors.current  // kept

@Immutable data class YoloHabitAccent(val accent: Color, val onAccent: Color, val container: Color, val onContainer: Color)
@Immutable data class ExtendedColors( val isDark: Boolean, /* §2.4 fields exactly */ val habitAccents: List<YoloHabitAccent> )
val LightExtendedColors = ExtendedColors(…); val DarkExtendedColors = ExtendedColors(…)
val LightColorScheme = lightColorScheme(/* §2.3, ALL roles incl. surfaceContainerLowest..Highest, inverse*, scrim, surfaceTint */)
val DarkColorScheme  = darkColorScheme(/* §2.2 */)
```

### 8.4 `theme/Type.kt` (REWRITE) — Manrope VF family (§3.1 snippet), `val YoloTypography = Typography(/* 15 slots §3.2 */)`, `object YoloTypeExtras { val statHero: TextStyle; val kicker: TextStyle; val statUnit: TextStyle }`. Removes `AppTypographyLegacy`/`appTypographyLegacy`/`MaterialThemAppTypography` at step 5.

### 8.5 NEW files

```kotlin
// theme/Shape.kt
val YoloShapes = Shapes(/* §4.1 */)

// theme/Dimens.kt
@Immutable data class YoloSpacing(/* §4.2 fields */)
@Immutable data class YoloSizing(/* §4.3 fields */)
enum class YoloInputProfile { Auto, Touch, Pointer }
internal val LocalYoloSpacing = staticCompositionLocalOf<YoloSpacing> { error("YoloTheme not applied") }
internal val LocalYoloSizing  = staticCompositionLocalOf<YoloSizing>  { error("YoloTheme not applied") }
internal fun spacingFor(widthClass: WindowSizeClass): YoloSpacing { /* §4.2 profile table */ }
internal fun sizingFor(widthClass: WindowSizeClass, profile: YoloInputProfile): YoloSizing { /* §4.3 */ }

// theme/Motion.kt
@Immutable data class YoloMotion(
    val instant: Int = 80, val quick: Int = 150, val standard: Int = 250,
    val gentle: Int = 350, val emphasized: Int = 450, val celebration: Int = 600,
    val easingStandard: Easing, val easingDecelerate: Easing, val easingAccelerate: Easing,
    val easingEmphasizedDecelerate: Easing, val easingEmphasizedAccelerate: Easing,
    val pressSpring: SpringSpec<Float>, val settleSpring: SpringSpec<Float>, val celebrateSpring: SpringSpec<Float>,
    val reducedMotion: Boolean = false,
)
internal val LocalYoloMotion = staticCompositionLocalOf<YoloMotion> { error("YoloTheme not applied") }
```

### 8.6 `theme/YoloTheme.kt` (REWRITE)

```kotlin
@Composable
fun YoloTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    inputProfile: YoloInputProfile = YoloInputProfile.Auto,
    reducedMotion: Boolean = false,           // app entry points pass the OS setting
    content: @Composable () -> Unit,
) {
    val widthClass = currentWindowAdaptiveInfo().windowSizeClass
    CompositionLocalProvider(
        LocalExtendedColors provides if (darkTheme) DarkExtendedColors else LightExtendedColors,
        LocalYoloSpacing provides spacingFor(widthClass),
        LocalYoloSizing provides sizingFor(widthClass, inputProfile),
        LocalYoloMotion provides YoloMotion.defaults(reducedMotion),
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            typography = YoloTypography,
            shapes = YoloShapes,
            content = content,
        )
    }
}

object YoloTokens {                                   // NOT named YoloTheme — no fun/object collision
    val spacing: YoloSpacing @Composable get() = LocalYoloSpacing.current
    val sizing: YoloSizing  @Composable get() = LocalYoloSizing.current
    val motion: YoloMotion  @Composable get() = LocalYoloMotion.current
}
```

Colors stay on the existing idiom: `MaterialTheme.colorScheme.*` and `MaterialTheme.colorScheme.extended.*`. Dimensions/motion: `YoloTokens.spacing.screenEdge`, `YoloTokens.motion.settleSpring`. Note: `core/designsystem` needs the `material3-adaptive` dependency for `currentWindowAdaptiveInfo` (already cataloged as `material3-adaptive`, currently used by `core/presentation` — add to designsystem's commonMain deps).

### 8.7 Tests — `core/designsystem/src/commonTest/kotlin/.../theme/ContrastTest.kt` (NEW)

~15-line WCAG luminance function; asserts every text pair ≥4.5:1, every component pair ≥3:1, both modes, incl. all 8 habit quads, placeholder-vs-actual-input-fill pairs, and every habit state token vs the card (`surfaceHigher`) and bento-tile (`surfaceContainer`) fills per §2.7; exemption list per §2.7. CI-locks every number in this spec.

### 8.8 Version catalog additions (`gradle/libs.versions.toml`)

```toml
# add — NOT currently in the catalog (decision log #15)
material3-adaptive-navigation-suite = { module = "org.jetbrains.compose.material3:material3-adaptive-navigation-suite", version.ref = "compose-material" }  # 1.10.0-alpha05, tracks material3
```
`material3-adaptive` / `-layout` / `-navigation` (adaptive 1.3.0-alpha02) are already cataloged — wire `-layout`/`-navigation` to `shared` when the pane scaffolds land.

### 8.9 Out of scope (documented non-goals for v2)

`MaterialExpressiveTheme` (experimental, pulled from a stable CMP line once — springs in §4.4 capture the useful part); Haze glass chrome (sanctioned later, bottom-bar scrim only — content stays matte forever); Android `dynamicColorScheme` (fragments the brand; static palette everywhere); mesh-gradient shaders; mascot layer; Space Grotesk display face; native SwiftUI navigation shell.
