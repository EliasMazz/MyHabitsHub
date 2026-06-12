# Design System v3 Spec — Design System v3 ("Copy the reference app/Health redesign exactly")

Status: APPROVED FOR IMPLEMENTATION · Date: 2026-06-12 · Branch target: refactor/modularize-shared
Supersedes: the **values** of `theme-spec.md` (Nocturne v2) and `section-color-worlds.md` (v2.1).
Does NOT supersede: the **architecture** of either spec — `YoloTheme` + M3 `ColorScheme` + `ExtendedColors` + `SectionColors`/`YoloSectionTheme` + `YoloTokens` stay exactly as structured (theme-spec §8, section-color-worlds §3). This document only swaps values, plus one narrowly-justified primitive addition (§9.1, Cyan block) required by a value-faithful copy of the reference app's GM-secondary family.

Ground truth: pixel-sampled press screenshots of the 2025–2026 reference "Gemini-coach era" redesign (files in `a local sample archive (not in repo)`, sampler scripts `sample*.py` hold every rect), cross-checked against Google's published token sources (Chromium GM3 ref palette, androidx Compose Material3 tokens, material-color-utilities, google/fonts).

**Provenance legend (used on every value in this document):**
- `[official-token]` — the pixel measurement matched a published Google token within JPEG noise (±2–3/channel); the OFFICIAL token value is adopted because it is the ground truth the screenshot approximates.
- `[measured]` — exists only as a measurement (no public token matches); the measured hex is adopted verbatim.
- `[derived]` — not directly copyable (no imagery, or the exact copy fails WCAG AA); derived by a stated rule, always staying inside the measured hue family. AA-fixes are marked `[derived: AA-fix of #xxxxxx]`.

Every text/fill pair in this spec was computed with python3 (WCAG 2.x relative-luminance formula); the full 205-pair run is embedded in §7. **Zero failures.** Fidelity never ships an AA failure: where the reference app's own JPEG-measured pair is sub-AA we keep the hue and shift tone minimally (each case flagged and listed in §10).

---

## 0. The three decisions that shape everything else

1. **the reference app's dark neutrals are GM3 Google-app GRAYS, not the public M3 baseline.** The measured dark ramp #131313 / #1E1F21 / #292A2C is the GM3 Google-apps family (#131314 / #1E1F20 / #282A2C), text #E3E3E3 / #C4C7C5 — NOT the purple-tinted public baseline (#141218 / #211F26 / …). An exact copy adopts the GM grays. (The working directive's gloss "warm-dark #141218-family" is the public-baseline approximation; the measurements override it — see §10.1.)
2. **The blue system is pure GM blue.** Light primary/links #0B57D0, dark primary #A8C7FA with #062E6F on it, dark nav pill #004A77 with #C2E7FF on it (both measured EXACT on the nav bar), data-viz blues #4285F4/#8AB4F8. the reference app ships **no dynamic color** — this is the fixed accent.
3. **Tab worlds are tints, not themes — and dark mode drops the tint.** Light canvases: Fitness #E5F4F7, Sleep #F0F0FA, Health #EAF1FB; everything else sits on #FFFFFF + #F0F4F9-family. The only dark-mode world imagery (Today) shows a **neutral** canvas (#131314/#1E1F20) with world identity carried entirely by colored chips/accents. We copy that: all three section worlds share neutral dark wash/sheet (§4.4).

World mapping (per directive): **Tracking ← Today-blue**, **Progress ← Fitness-teal**, **Settings ← Sleep-purple**. Note on Progress: the user previously chose a mint/emerald identity for the Progress world; the reference app's Fitness teal (#00958A bars, #93F4EA dark chip text, #CCEAEC light chips) is the closest world in the reference app's palette and is its direct replacement — the mint→teal mapping is 1:1 by role (§4.2).

---

## 1. Fidelity contract — every the reference app value → our token

### 1.1 Dark neutrals (image A, dark Today tab)

| the reference app element | Measured | Adopted | Our token(s) | Provenance |
|---|---|---|---|---|
| Hero/top wash, status bar, large insight card fill | #131313 | **#131314** | dark `surface`/`background`/`surfaceDim`; `heroSurface`; section dark `surfaceTintWash` (all worlds) | [official-token] GM3 N6 |
| Canvas around cards, nav bar, nested card, battery pill | #1E1F21 | **#1E1F20** | dark `surfaceContainer`; section dark `sheetSurface` (all worlds) | [official-token] GM3 dark surface-container |
| Ring track / hypnogram empty track | #292A2C | **#282A2C** | dark `surfaceContainerHigh`; dark `progressTrack`; dark heatmapLevel0; `surfaceHigher` dark | [official-token] GM3 dark surface-container-high |
| Primary text ("56%", titles, "premium") | #E3E3E3 | **#E3E3E3** | dark `onSurface`/`onBackground`; `textPrimary` dark; `onHeroSurface`; section dark `onSheetSurface` | [official-token] GM3 N90, measured exact |
| Secondary/body text, unselected nav items | #C5C6C6 | **#C4C7C5** | dark `onSurfaceVariant`; `textSecondary` dark; section dark `onSheetSurfaceVariant` | [official-token] GM3 NV80 |

the reference app's dark elevation quirk (cards #131314 sit DARKER than the #1E1F20 canvas) is a **composition pattern**, not a token change: screens that want the reference app look paint canvas = `surfaceContainer` and cards = `surface`. Roles keep standard M3 mapping.

### 1.2 The blue system (Today world / global accent)

| the reference app element | Measured | Adopted | Our token(s) | Provenance |
|---|---|---|---|---|
| Links & button text, light (5 convergent samples) | #0456CD–#1557BD | **#0B57D0** | light `primary`/`surfaceTint`; `info` light; Tracking light `accent`; habitAccent-blue | [official-token] GM P40, this IS the token |
| Ask Coach FAB fill (dark) | #A8C8FB | **#A8C7FA** | dark `primary`; `heroAccent`; `info` dark; Tracking dark `accent` | [official-token] GM P80 |
| FAB text/icon (dark) | #0B2B69 | **#062E6F** | dark `onPrimary`; `onInfo` dark; Tracking dark `onAccent` | [official-token] GM P20 |
| Selected nav pill (dark) | #004A77 | **#004A77** | dark `secondaryContainer`; Tracking dark `accentContainer` (= navIndicator) + `sheetIconChip` | [official-token] GM S30, measured EXACT |
| Icon inside nav pill (dark) | #C0E7FF | **#C2E7FF** | dark `onSecondaryContainer`; light `secondaryContainer`; Tracking dark `onAccentContainer`/`onSheetIconChip`; Tracking light `sheetIconChip` | [official-token] GM S90 |
| Header settings icon-chip (light) | #C4E9FF | **#C2E7FF** | Tracking light `sheetIconChip` | [official-token] GM S90 |
| Weekly-cardio ring arc (dark) | #8FBAFF | **#8AB4F8** | dark `progressIndicator` | [official-token] Google blue 300 |
| Cardio dashed data line (light) | #437FF4/#437DFB | **#4285F4** | light `progressIndicator` | [official-token] Google blue 500 |
| "+34" delta badge | #3371EA | not adopted | sits between blue 500/600; use `progressIndicator` for data badges | noted, §10.6 |
| "Today" label under pill | #8BCFF6 | not adopted | `navSelectedText` = accent per architecture (least-certain measurement, n=575) | noted, §10.6 |

### 1.3 Light neutrals (all light tabs)

| the reference app element | Measured | Adopted | Our token(s) | Provenance |
|---|---|---|---|---|
| Primary cards / sheets | #FFFFFF (100%) | **#FFFFFF** | light `surfaceContainerLowest`/`surfaceBright`; `surfaceHigher` light | [official-token] N100, measured exact |
| Secondary cards, list items, tonal buttons (4 convergent samples) | #F0F3F8 | **#F0F4F9** | light `surfaceContainer`; `surfaceLower`/`secondaryFill` light | [official-token] GM3 light surface-container family; Δ1/channel = JPEG noise |
| Chart empty track (light) | #EAEFF5 | **#EAEFF5** | light `surfaceContainerHigh`; light `progressTrack`; light heatmapLevel0 | [measured] |
| Inactive day circle | #DADDE0 | **#DADDE0** | light `surfaceDim` | [measured] |
| Primary text | #181B1E–#1C1D20 | **#1A1C1E** | light `onSurface`/`onBackground`; `textPrimary`/`habitPending` light; section light `onSheetSurface` | [official-token] GM3 light on-surface call; consensus of 3 samples |
| Secondary text | #414548 | **#444746** | light `onSurfaceVariant`; `textSecondary` light | [official-token] GM NV30 |
| Light canvas (base, non-section) | unmeasured | **#FAF9F8** | light `surface`/`background` | [official-token] GM N98 at M3 mapping; §10.4 |
| Outlined pill border | #B5BFC1 | not adopted as token | borders use `outline` #747775 / `outlineVariant` #C4C7C5 (GM NV50/NV80) | noted, §10.6 |

### 1.4 Fitness world (teal) → Progress section

| the reference app element | Measured | Adopted | Our token(s) | Provenance |
|---|---|---|---|---|
| Fitness canvas wash (light) | #E5F4F7 (97%) | **#E5F4F7** | Progress light `surfaceTintWash` | [measured] |
| Step bars / exercise day markers | #00958A | **#009688** | light `habitComplete`; light heatmapLevel3 | [official-token] Material teal 500; AA-checked as graphics (3.49:1 ≥ 3) |
| Map route stroke | #016B5B | **#016B5B** | Progress light `accent`; light heatmapLevel4 | [measured] — also the AA-fix for #00958A-as-accent (6.45:1 w/ white) |
| Goal-met teal bars | #41DDD0/#2DCDC1 | **#41DDD0** | dark `habitComplete`; light heatmapLevel2; `auraMint` base | [measured] |
| Activity icon chip fill / glyph | #CCEAEC / #08655F | **#CCEAEC / #08655F** | Progress light `accentContainer`/`onAccentContainer`; light heatmapLevel1 | [measured] |
| "Fitness" tag chip fill / text | #BBFCF4 / #05514A | **#BBFCF4 / #05514A** | Progress light `sheetIconChip`/`onSheetIconChip` | [measured] |
| Steps chip (dark): leading / main / icon circle / text | #007B73 / #00514B / #003732 / #93F4EA | all four | dark heatmap L3 / Progress dark `accentContainer`+`sheetIconChip` / Progress dark `onAccent` + heatmap L1 / Progress dark `accent`+`onAccentContainer`+`onSheetIconChip` | [measured] |
| Goal dotted line | ~#578F77 | not adopted | low-confidence antialiased value | noted, §10.6 |

### 1.5 Sleep world (purple) → Settings section

| the reference app element | Measured | Adopted | Our token(s) | Provenance |
|---|---|---|---|---|
| Sleep canvas wash (light) | #F0F0FA (100%) | **#F0F0FA** | Settings light `surfaceTintWash` | [measured] |
| "Sleep" tag chip fill / text (light) | #EDDCFF / #5A2E8E | **#EADDFF / #5A2E8E** | Settings light `accentContainer`+`sheetIconChip` / `accent`+`onAccentContainer`+`onSheetIconChip` | fill [official-token] M3 baseline P90 (Δ within noise); text [measured] |
| "Sleep" tag pill dark (fill / text) | #5B2F90 / #EFDBFF | **#5B2F90 / #EFDBFF** | Settings dark `accentContainer`+`sheetIconChip` / `onAccentContainer`+`onSheetIconChip` | [measured] (cross-confirms #5A2E8E: same color both modes) |
| Sleep metric chip dark: main / band / icon circle / text | #8457BC / #5B2F90 / #421379 / #EEDCFF | band+circle+text adopted | habitAccent-purple dark (accent #EEDCFF, onAccent #421379, container #5B2F90); #8457BC NOT adopted (measures 4.01:1 vs its own text — sub-AA as shipped, §10.5) | [measured] |
| Duration bars / score dot-line (light) | #9A70D4 / #A77DDF | not adopted as tokens | data-viz only; both fail AA as text accents (3.72:1); charts in Settings world use accent family | noted, §10.5 |
| Settings dark `accent` | unmeasured | **#D0BCFF** (on #381E72) | Settings dark `accent`/`onAccent`; habitAccent-indigo dark | [official-token] M3 baseline P80/P20 — no dark purple accent exists in imagery |
| Hypnogram light: Awake / REM / Light / Deep | #D45988 / #BDEAFF / #417FF8 / #4A1982 | pink→habitAccent-pink (AA-fixed); REM→habitAccent-cyan dark accent; Light≈blue 500 family (progressIndicator); Deep→habitAccent-indigo light accent **#4A1982** | [measured]; pink fix [derived] |
| Hypnogram dark: Awake / REM | #FF9CBB / #BBE8FE | #FF9CBB → habitAccent-pink dark accent; #BBE8FE ≈ #BDEAFF family | [measured] |

### 1.6 Health world (green) + status channels

| the reference app element | Measured | Adopted | Our token(s) | Provenance |
|---|---|---|---|---|
| Health canvas wash (light) | #EAF1FB | **#EAF1FB** | Tracking light `surfaceTintWash` (borrowed — only measured light-blue canvas; no light Today exists, §10.3) | [measured] |
| Chart line/dots green | #02983F | not adopted directly | fails AA w/ white (3.77:1); success accent normalized to GM tertiary ramp (#146C2E/#6DD58C) which contains it | [derived: AA-fix of #02983F] |
| In-range chart band | #DEEEEB | not adopted | chart-specific; nearest token = light `successContainer` | noted, §10.6 |
| "Ideal"/"Typical"/"In range" chips (light) | #BFEFBB / #085226 (#035127) | **#BFEFBB / #085226** | light `successContainer`/`onSuccessContainer`; habitAccent-green light container pair | [measured] |
| Readiness chip dark: fill / icon circle / text | #00522C / #00381F / #BCEEBB | **all three** | dark `successContainer` / habitAccent-green dark onAccent / dark `onSuccessContainer` + green dark accent | [measured] |
| Amber status light: fill / text | #FFDEA9–#FFDFAC / #614206 | **#FFDEA9 / #614206** | light `warningContainer`+`streakContainer` / `onWarningContainer`+`warning`+`streak` | [measured] |
| Amber status dark ("Score: 72 · Fair"): fill / text | #5F4200 / #FDDEA4 | **#5F4200 / #FDDEA4** | dark `warningContainer`+`streakContainer` / `warning`+`streak`+`celebration` | [measured] |

### 1.7 GM token values adopted with no direct measurement (role completion)

All from the published GM3/M3 ref palettes at standard M3 role-to-tone mappings, `[official-token]`: GM secondary (cyan) #00639B/#003355/#7FCFFF/#DFF3FF; GM tertiary (green) #146C2E/#0F5223/#C4EED0/#6DD58C/#0A3818; M3 baseline error ramp #B3261E/#8C1D18/#F9DEDC/#F2B8B5/#601410; GM blue #0842A0 (P30, hover/onContainer), #7CACF8 (P70, dark hover), #D3E3FD (P90), #1B6EF3 (P50); GM neutrals #0E0E0F (N4), #343535 (N22), #303030 (N20), #F2F2F2 (N95), #E1E3E1 (NV90), #747775 (NV50), #C4C7C5 (NV80), #8E918F (NV60), #A9ACAA (NV70), #5C5F5E (NV40); M3 baseline purple #D0BCFF/#381E72/#E8DEF8. Marketing-only washes (#E4FDFA, #E6F8FC, #F8F2FE) are NOT adopted (blog assets, not app UI).

---

## 2. M3 ColorScheme — complete tables (GM neutrals + GM blue)

Replaces the teal-tinted Deep/Ink ramp values in `Theme.kt` LightColorScheme/DarkColorScheme. WCAG column = computed ratio from §7.

### 2.1 LightColorScheme

| Role | Value | Was (Nocturne) | Provenance | WCAG |
|---|---|---|---|---|
| primary | #0B57D0 | #047857 | [official-token] GM P40, measured 5× | onPrimary 6.39 |
| onPrimary | #FFFFFF | #FFFFFF | [official-token] | — |
| primaryContainer | #D3E3FD | #CEF5E4 | [official-token] GM P90 | onPrimaryContainer 7.04 |
| onPrimaryContainer | #0842A0 | #073D2F | [official-token] GM P30 (current androidx mapping) | — |
| inversePrimary | #A8C7FA | #4DDEAB | [official-token] GM P80 | — |
| secondary | #00639B | #52605D | [official-token] GM S40 | onSecondary 6.45 |
| onSecondary | #FFFFFF | #FFFFFF | [official-token] | — |
| secondaryContainer | #C2E7FF | #DCE7E3 | [official-token] GM S90, measured (#C4E9FF) | onSecondaryContainer 7.20 |
| onSecondaryContainer | #004A77 | #1C2724 | [official-token] GM S30, measured | — |
| tertiary | #146C2E | #B45309 | [official-token] GM T40 | onTertiary 6.53 |
| onTertiary | #FFFFFF | #FFFFFF | [official-token] | — |
| tertiaryContainer | #C4EED0 | #FFE9D1 | [official-token] GM T90 | onTertiaryContainer 7.32 |
| onTertiaryContainer | #0F5223 | #5F2D03 | [official-token] GM T30 | — |
| error | #B3261E | #BA1B32 | [official-token] M3 E40 | onError 6.54 |
| onError | #FFFFFF | #FFFFFF | [official-token] | — |
| errorContainer | #F9DEDC | #FFDADD | [official-token] M3 E90 | onErrorContainer 7.17 |
| onErrorContainer | #8C1D18 | #68101F | [official-token] M3 E30 | — |
| background / surface | #FAF9F8 | #F4F8F7 | [official-token] GM N98 | onSurface 16.25 |
| onBackground / onSurface | #1A1C1E | #14201E | [official-token]/[measured ×3] | — |
| surfaceVariant | #E1E3E1 | #E8EEEC | [official-token] GM NV90 | onSurfaceVariant 7.28 |
| onSurfaceVariant | #444746 | #52605D | [official-token] GM NV30, measured | vs surface 8.93 |
| surfaceDim | #DADDE0 | #D5DDDB | [measured] inactive-day circle | — |
| surfaceBright | #FFFFFF | #FFFFFF | [official-token] | — |
| surfaceContainerLowest | #FFFFFF | #FFFFFF | [official-token], measured sheets | onSurface 17.09 |
| surfaceContainerLow | #F5F7FA | #EEF4F2 | [derived: ladder step between sCLowest #FFFFFF and sC #F0F4F9, biased one notch toward sC — the exact midpoint #F8FAFC reads as white at card edges] | onSurface 15.92 |
| surfaceContainer | #F0F4F9 | #E8EEEC | [official-token], measured 4× (#F0F3F8) | onSurface 15.47 |
| surfaceContainerHigh | #EAEFF5 | #DFE7E5 | [measured] chart empty track | onSurface 14.78 |
| surfaceContainerHighest | #E4E9EF | #D5DDDB | [derived: ladder extension of measured steps] | onSurface 14.00 |
| outline | #747775 | #6E7E7B | [official-token] GM NV50 | vs surface 4.31 |
| outlineVariant | #C4C7C5 | #D5DDDB | [official-token] GM NV80 | — |
| inverseSurface | #303030 | #1C2724 | [official-token] GM N20 | inverseOnSurface 11.79 |
| inverseOnSurface | #F2F2F2 | #EEF4F2 | [official-token] GM N95 | — |
| scrim | #000000 | #000000 | [official-token] | — |
| surfaceTint | #0B57D0 | #047857 | = primary | — |

### 2.2 DarkColorScheme

| Role | Value | Was (Nocturne) | Provenance | WCAG |
|---|---|---|---|---|
| primary | #A8C7FA | #4DDEAB | [official-token] GM P80, measured FAB | onPrimary 7.50 |
| onPrimary | #062E6F | #092E2D | [official-token] GM P20, measured | — |
| primaryContainer | #0842A0 | #0B5C44 | [official-token] GM P30 | onPrimaryContainer 7.04 |
| onPrimaryContainer | #D3E3FD | #A8F0D4 | [official-token] GM P90 | — |
| inversePrimary | #0B57D0 | #047857 | [official-token] GM P40 | — |
| secondary | #7FCFFF | #B6C2BF | [official-token] GM S80 | onSecondary 7.65 |
| onSecondary | #003355 | #1C2724 | [official-token] GM S20 | — |
| secondaryContainer | #004A77 | #3A4744 | [official-token] GM S30, measured EXACT (nav pill) | onSecondaryContainer 7.20 |
| onSecondaryContainer | #C2E7FF | #DCE7E3 | [official-token] GM S90, measured | — |
| tertiary | #6DD58C | #FFB870 | [official-token] GM T80 | onTertiary 7.25 |
| onTertiary | #0A3818 | #4A2800 | [official-token] GM T20 | — |
| tertiaryContainer | #0F5223 | #4A2D0E | [official-token] GM T30 | onTertiaryContainer 7.32 |
| onTertiaryContainer | #C4EED0 | #FFD9B0 | [official-token] GM T90 | — |
| error | #F2B8B5 | #FF8A95 | [official-token] M3 E80 | onError 7.66 |
| onError | #601410 | #55121C | [official-token] M3 E20 | — |
| errorContainer | #8C1D18 | #55121C | [official-token] M3 E30 | onErrorContainer 7.17 |
| onErrorContainer | #F9DEDC | #FFD3D7 | [official-token] M3 E90 | — |
| background / surface / surfaceDim | #131314 | #0C1514 / #101B1A | [official-token] GM N6, measured hero+cards | onSurface 14.47 |
| onBackground / onSurface | #E3E3E3 | #E2E8E6 | [official-token] GM N90, measured EXACT | — |
| surfaceVariant | #444746 | #28332F | [official-token] GM NV30 | onSurfaceVariant 5.51 |
| onSurfaceVariant | #C4C7C5 | #A6B4B0 | [official-token] GM NV80, measured | vs surface 10.90 |
| surfaceBright | #343535 | #3A4744 | [official-token] GM N22 | — |
| surfaceContainerLowest | #0E0E0F | #0C1514 | [official-token] GM N4 | onSurface 15.03 |
| surfaceContainerLow | #1A1A1C | #14201E | [derived: ladder step between N6 #131314 and container #1E1F20, biased one notch toward the container (exact midpoint #18191A); GM N10 #1F1F1F would invert the ladder vs #1E1F20] | onSurface 13.54 |
| surfaceContainer | #1E1F20 | #182522 | [official-token], measured canvas/nav (#1E1F21) | onSurface 12.86 |
| surfaceContainerHigh | #282A2C | #1C2A27 | [official-token], measured tracks (#292A2C) | onSurface 11.22 |
| surfaceContainerHighest | #343535 | #22312E | [official-token] GM N22 | onSurface 9.59 |
| outline | #8E918F | #5E6D69 | [official-token] GM NV60 | vs surface 5.83 (was 3.24 — improved) |
| outlineVariant | #444746 | #28332F | [official-token] GM NV30 | — |
| inverseSurface | #E3E3E3 | #E8EEEC | [official-token] GM N90 | inverseOnSurface 10.28 |
| inverseOnSurface | #303030 | #14201E | [official-token] GM N20 | — |
| scrim | #000000 | #000000 | [official-token] | — |
| surfaceTint | #A8C7FA | #4DDEAB | = primary | — |

---

## 3. ExtendedColors v2 — field-by-field (every existing field kept)

Judgments called out by the directive: **(a)** the reference app imagery clearly supports a green success channel (In-range/Ideal/Typical chips, Readiness chip, health chart) → `success*` goes the reference app-green (GM tertiary accents + measured chip containers), NOT emerald. **(b)** `habitComplete` maps to the Fitness goal-met teal (the reference app's "goal achieved" color), which is also the Progress world — replacing the old "habitComplete == primary" identity. **(c)** the reference app's palette has exactly ONE warm channel (the amber status family) → `streak` and `warning` intentionally converge on the same measured values; `celebration` uses the brightest measured warm tone.

Format: light | dark. "Was" column shows Nocturne light|dark.

| Field | New light | New dark | Was | Provenance / notes |
|---|---|---|---|---|
| isDark | false | true | — | unchanged |
| success | #146C2E | #6DD58C | #047857\|#4DDEAB | [official-token] GM T40/T80; [derived: AA-fix of measured #02983F, which is 3.77:1 w/ white] |
| onSuccess | #FFFFFF | #0A3818 | #FFFFFF\|#092E2D | [official-token] (6.53 / 7.25) |
| successContainer | #BFEFBB | #00522C | #CEF5E4\|#14443A | [measured] status chips / Readiness chip |
| onSuccessContainer | #085226 | #BCEEBB | #073D2F\|#A8F0D4 | [measured] (7.25 / 7.17) |
| warning | #614206 | #FDDEA4 | #9A6A00\|#F2C66B | [measured] amber family (9.17 / 7.13) |
| onWarning | #FFFFFF | #5F4200 | #FFFFFF\|#3D2A00 | light [official]; dark [measured] |
| warningContainer | #FFDEA9 | #5F4200 | #FFF1D6\|#3D2E0F | [measured] (#FFDEA9 chosen from measured range #FFDEA9–#FFDFAC) |
| onWarningContainer | #614206 | #FDDEA4 | #4A3300\|#F4DFAE | [measured] (7.11 / 7.13) |
| info | #0B57D0 | #A8C7FA | #0B69C7\|#8AC2FF | [official-token] = primary (the reference app has one blue); redundancy accepted, field kept |
| onInfo | #FFFFFF | #062E6F | #FFFFFF\|#002E52 | [official-token] (6.39 / 7.50) |
| infoContainer | #D3E3FD | #0842A0 | #DCEBFB\|#16344E | [official-token] GM P90/P30 |
| onInfoContainer | #0842A0 | #D3E3FD | #0A3D70\|#D6E8FF | [official-token] (7.04 / 7.04) |
| streak | #614206 | #FDDEA4 | #B45309\|#FFB870 | [measured] = warning; single warm channel, §3(c) |
| onStreak | #FFFFFF | #5F4200 | #FFFFFF\|#4A2800 | as onWarning (9.17 / 7.13) |
| streakContainer | #FFDEA9 | #5F4200 | #FFE9D1\|#4A2D0E | [measured] |
| onStreakContainer | #614206 | #FDDEA4 | #5F2D03\|#FFD9B0 | [measured] (7.11 / 7.13) |
| celebration | #FDDEA4 | #FDDEA4 | #FFB870 both | [measured] brightest measured warm tone; decorative |
| progressTrack | #EAEFF5 | #282A2C | #DFE7E5\|#22312E | [measured] both (chart track / ring track) |
| progressIndicator | #4285F4 | #8AB4F8 | #047857\|#4DDEAB | [official-token] Google blue 500/300; measured #437FF4 / #8FBAFF (ring arc) (3.08 / 6.84 vs track) |
| habitComplete | #009688 | #41DDD0 | #047857\|#4DDEAB | [official-token] Material teal 500 (measured #00958A) / [measured] goal-met teal. Graphics role, 3:1 floors: 3.49/3.67/3.33 light, 11.03+ dark |
| habitMissed | #5C5F5E | #A9ACAA | #6E7E7B\|#7A8A86 | [official-token] GM NV40/NV70 (≥5.84 / ≥6.29 vs surfaces) |
| habitSkipped | #747775 | #8E918F | #7A8A86\|#6E7E7B | [official-token] GM NV50/NV60 (≥4.10 / ≥4.53 vs surfaces — was 3.38 edge, improved) |
| habitPending | #1A1C1E | #E3E3E3 | #14201E\|#E2E8E6 | = textPrimary |
| heatmapLevel0 | #EAEFF5 | #282A2C | #E8EEEC\|#182522 | [measured] empty tracks |
| heatmapLevel1 | #CCEAEC | #003732 | #CEF5E4\|#14443A | [measured] activity chip / steps icon circle |
| heatmapLevel2 | #41DDD0 | #00514B | #7BE8C2\|#0B5C44 | [measured] goal-met bars / steps chip main |
| heatmapLevel3 | #009688 | #007B73 | #19BA87\|#19BA87 | [official≈measured #00958A] / [measured] steps chip leading |
| heatmapLevel4 | #016B5B | #41DDD0 | #047857\|#4DDEAB | [measured] map stroke / goal-met teal — entire ramp is measured Fitness teal |
| heroSurface | #131314 | #131314 | #092E2D both | [measured] dark Today hero wash |
| onHeroSurface | #E3E3E3 | #E3E3E3 | #E2E8E6 both | [measured] (14.47) |
| heroAccent | #A8C7FA | #A8C7FA | #4DDEAB both | [official-token] the FAB blue (10.80 on hero) |
| auraAmber | 0x2EFDDEA4 | 0x1FFDDEA4 | 0x2EFFB870\|0x1FFFB870 | [derived: same alphas over measured amber] |
| auraMint | 0x3341DDD0 | 0x2941DDD0 | 0x334DDEAB\|0x294DDEAB | [derived: same alphas over measured goal-met teal] |
| elevatedCardOutline | Transparent | 0x1AFAF9F8 | Transparent\|0x1AF4F8F7 | [derived: alpha base re-pointed to new N98] |
| textPrimary | #1A1C1E | #E3E3E3 | #14201E\|#E2E8E6 | [official/measured] (16.25 / 14.47) |
| textSecondary | #444746 | #C4C7C5 | #52605D\|#A6B4B0 | [official/measured] (8.93 / 10.90) |
| textTertiary | #5C5F5E | #A9ACAA | #6E7E7B\|#94A3A0 | [official-token] GM NV40/NV70 (6.14 / 8.11 vs surface — NV50 light fails 4.5 at 4.31, hence NV40; NV60 dark #8E918F fails the 4.5 placeholder/input-fill floor vs #343535 at 3.87, hence NV70) |
| textPlaceholder | #5C5F5E | #A9ACAA | #52605D\|#94A3A0 | = textTertiary values; vs input fills (light sCLow #F5F7FA / dark sCHighest #343535): 6.01 / 5.37 ≥ 4.5 — ContrastTest-enforced (:92-93) |
| textDisabled | 0x611A1C1E | 0x61E3E3E3 | 0x6114201E\|0x61E2E8E6 | [derived: alpha base re-pointed] |
| primaryHover | #0842A0 | #7CACF8 | #065F46\|#19BA87 | [official-token] GM P30/P70 (one tone toward mid) |
| destructiveHover | #8C1D18 | #EC928E | #AA142A\|#FF7987 | [official-token] M3 E30/E70 |
| disabledFill / disabledOutline | 0x1F1A1C1E | 0x1FE3E3E3 | 0x1F14201E\|0x1FE2E8E6 | [derived: alpha base re-pointed] |
| overlay | 0xCC1A1C1E | 0xCC0E0E0F | 0xCC14201E\|0xCC0C1514 | [derived: alpha base re-pointed to new onSurface/N4] |
| surfaceLower | #F0F4F9 | #0E0E0F | #EEF4F2\|#0C1514 | [official-token] |
| surfaceHigher | #FFFFFF | #282A2C | #FFFFFF\|#1C2A27 | [official/measured] |
| surfaceOutline | 0x141A1C1E | 0x1AFAF9F8 | 0x1414201E\|0x1AF4F8F7 | [derived: alpha base re-pointed] |
| secondaryFill | #F0F4F9 | #343535 | #EEF4F2\|#22312E | [official-token] |

### 3.1 habitAccents — 8 quads × 2 modes (accent / onAccent / container / onContainer)

The 8 slots re-point to the 8 hue families actually present in the reference app imagery: blue, teal, green, purple, pink (hypnogram Awake), amber, cyan (REM/secondary), indigo (hypnogram Deep). All 32 pairs ≥ 4.5:1 (§7).

Light:
| # | Family | accent | onAccent | container | onContainer | Provenance |
|---|---|---|---|---|---|---|
| 1 | blue | #0B57D0 | #FFFFFF | #D3E3FD | #0842A0 | [official-token] |
| 2 | teal | #016B5B | #FFFFFF | #CCEAEC | #08655F | [measured ×3] |
| 3 | green | #146C2E | #FFFFFF | #BFEFBB | #085226 | accent [official]; container pair [measured] |
| 4 | purple | #5A2E8E | #FFFFFF | #EADDFF | #5A2E8E | [measured] / container [official-token] |
| 5 | pink | #C04373 | #FFFFFF | #FFD8E4 | #8E2452 | accent [derived: AA-fix of measured #D45988 (3.76→4.89)]; container [official-token] M3 baseline T90; onContainer [derived] |
| 6 | amber | #96660A | #FFFFFF | #FFDEA9 | #614206 | accent [derived: AA tone-40 of measured amber family (5.00)]; container pair [measured] |
| 7 | cyan | #00639B | #FFFFFF | #C2E7FF | #004A77 | [official-token], container measured |
| 8 | indigo | #4A1982 | #FFFFFF | #E8DEF8 | #4A1982 | accent [measured: hypnogram Deep]; container [official-token] M3 baseline S90 |

Dark:
| # | Family | accent | onAccent | container | onContainer | Provenance |
|---|---|---|---|---|---|---|
| 1 | blue | #A8C7FA | #062E6F | #0842A0 | #D3E3FD | [official-token] |
| 2 | teal | #93F4EA | #003732 | #00514B | #93F4EA | [measured] steps chip |
| 3 | green | #BCEEBB | #00381F | #00522C | #BCEEBB | [measured] readiness chip |
| 4 | purple | #EEDCFF | #421379 | #5B2F90 | #EFDBFF | [measured] sleep chip (band, not the sub-AA #8457BC main fill) |
| 5 | pink | #FF9CBB | #5D1135 | #702342 | #FFD8E4 | accent [measured: hypnogram Awake dark]; rest [derived] (6.72/8.06) |
| 6 | amber | #FDDEA4 | #5F4200 | #5F4200 | #FDDEA4 | [measured] score badge |
| 7 | cyan | #BDEAFF | #003355 | #004A77 | #C2E7FF | accent [measured: REM]; rest [official-token] |
| 8 | indigo | #D0BCFF | #381E72 | #4A1982 | #EADDFF | [official-token] baseline P80/P20; container [measured: Deep] |

---

## 4. SectionColors — 3 worlds × 2 modes

Architecture unchanged: 14 fields, last 4 derived (`navIndicator`=accentContainer, `onNavIndicator`=onAccentContainer, `navSelectedText`=accent, `sheetDragHandle`=onSheetSurfaceVariant@40%). Field names below are the code names from `SectionTheme.kt` (`surfaceTintWash`, `sheetIconChip`, `onSheetIconChip` — not wash/chip shorthand). All ratios in §7.

### 4.1 Tracking ← the reference app "Today" (GM blue)

| Field | Light | Provenance | Dark | Provenance |
|---|---|---|---|---|
| accent | #0B57D0 | [official-token] | #A8C7FA | [official-token], measured FAB |
| onAccent | #FFFFFF | [official-token] | #062E6F | [official-token], measured |
| accentContainer | #D3E3FD | [official-token] | #004A77 | [official-token], measured EXACT — the nav pill becomes pixel-identical to the reference app's |
| onAccentContainer | #0842A0 | [official-token] | #C2E7FF | [official-token], measured |
| surfaceTintWash | #EAF1FB | [measured: Health-tab canvas, borrowed §10.3] | #131314 | [measured: hero wash] |
| sheetSurface | #E0E9F8 | [derived: wash #EAF1FB ↔ P90 #D3E3FD blend ≈ midpoint, hand-tuned ≤4/channel less saturated than the exact midpoint #DFEAFC] | #1E1F20 | [measured: nested card] |
| onSheetSurface | #1A1C1E | [official/measured] | #E3E3E3 | [measured] |
| onSheetSurfaceVariant | #44474F | [derived: blue-cast NV30] | #C4C7C5 | [official-token], measured |
| sheetIconChip | #C2E7FF | [official-token], measured header chip #C4E9FF | #004A77 | [measured] = accentContainer (the reference app provides one blue container tone per mode) |
| onSheetIconChip | #004A77 | [official-token] | #C2E7FF | [measured] |

### 4.2 Progress ← the reference app "Fitness" (teal) — the mint→teal mapping

User's prior choice was "mint-similar"; the reference app's Fitness teal is the closest world. Role-by-role: emerald accent→measured map-stroke teal; mint wash→measured Fitness canvas; mint chips→measured Fitness tag chip; dark mint text→measured steps-chip mint.

| Field | Light | Provenance | Dark | Provenance |
|---|---|---|---|---|
| accent | #016B5B | [measured: map route] — doubles as AA-fix of #00958A (3.71→6.45 w/ white) | #93F4EA | [measured: steps chip text] |
| onAccent | #FFFFFF | [official-token] | #003732 | [measured: steps icon circle] |
| accentContainer | #CCEAEC | [measured: activity icon chip] | #00514B | [measured: steps chip main] |
| onAccentContainer | #08655F | [measured: activity icon glyph] | #93F4EA | [measured] |
| surfaceTintWash | #E5F4F7 | [measured: Fitness canvas] | #131314 | [measured: neutral dark canvas, §4.4] |
| sheetSurface | #D9EDF0 | [derived: wash↔container midpoint] | #1E1F20 | [measured] |
| onSheetSurface | #1A1C1E | [official/measured] | #E3E3E3 | [measured] |
| onSheetSurfaceVariant | #3F4946 | [derived: teal-cast NV30] | #C4C7C5 | [official-token] |
| sheetIconChip | #BBFCF4 | [measured: "Fitness" tag chip] | #00514B | [measured] |
| onSheetIconChip | #05514A | [measured: tag chip text] | #93F4EA | [measured] |

### 4.3 Settings ← the reference app "Sleep" (purple)

| Field | Light | Provenance | Dark | Provenance |
|---|---|---|---|---|
| accent | #5A2E8E | [measured: Sleep tag text; cross-confirmed by dark #5B2F90] | #D0BCFF | [official-token] baseline P80 (no dark purple accent in imagery) |
| onAccent | #FFFFFF | [official-token] | #381E72 | [official-token] baseline P20 |
| accentContainer | #EADDFF | [official-token] baseline P90 (measured #EDDCFF within noise) | #5B2F90 | [measured: dark Sleep tag pill] |
| onAccentContainer | #5A2E8E | [measured] | #EFDBFF | [measured] |
| surfaceTintWash | #F0F0FA | [measured: Sleep canvas] | #131314 | [measured, §4.4] |
| sheetSurface | #E7E3F8 | [derived: one step deeper than wash] | #1E1F20 | [measured] |
| onSheetSurface | #1A1C1E | [official/measured] | #E3E3E3 | [measured] |
| onSheetSurfaceVariant | #49454F | [official-token] baseline NV30 | #C4C7C5 | [official-token] |
| sheetIconChip | #EADDFF | [official-token] = accentContainer | #5B2F90 | [measured] |
| onSheetIconChip | #5A2E8E | [measured] | #EFDBFF | [measured] |

### 4.4 Dark-canvas policy (deliberate, faithful, and a visible change)

All three worlds share `surfaceTintWash` #131314 / `sheetSurface` #1E1F20 in dark mode. This copies the only dark evidence that exists (image A: neutral canvas, world identity from colored chips/accents) and the research's takeaway "tab worlds are tints, not themes." Consequence: our previous hue-tinted dark washes (#142036/#11342B/#1C1A33) disappear; dark world identity now comes from accent/chip/navIndicator only — exactly like the reference app. The SectionColors fields stay per-world (no structural change); their dark values converge. If real dark Fitness/Sleep imagery later surfaces with tinted canvases, only these 12 values change.

---

## 5. Typography — Google Sans Flex + exact M3/M3E scale

### 5.1 Typeface: the genuine article, legally

reference uses Google Sans (Flex) for display/headline and Google Sans Text for body. License forensics (verified against Google's FAQ, the Fonts download API, and binary name-table ID 13):

- **Google Sans Flex: SIL OFL 1.1 since Nov 18, 2025** — bundling in a commercial non-Google app is explicitly permitted. **This is what we bundle.** It is metrically identical to classic Google Sans (x/cap 0.712) — no substitute needed.
- Google Sans Text: NOT released (API returns "Unable to find family") → body text also uses Google Sans Flex (acceptable: GSF's opsz axis covers text sizes; classic Google Sans and GSF share the skeleton).
- Product Sans: still proprietary — not used.
- Nocturne's documented non-goal "no Google Sans" (theme-spec §8.9) is **explicitly overridden** by this spec: the license landscape changed in Nov–Dec 2025.

**Source of the file** (GSF is NOT yet at `google/fonts/ofl/googlesansflex/` — tracking issue google/fonts#10006, milestone 2026 Q2):
1. Official zip: `https://fonts.google.com/download?family=Google%20Sans%20Flex` → `GoogleSansFlex-VariableFont_GRAD,ROND,opsz,slnt,wdth,wght.ttf` + `OFL.txt` (direct VF: `https://fonts.gstatic.com/s/googlesansflex/v21/t5t7IQcYNIWbFgDgAAzZ34auoVyXipusfhcat2c.ttf`, 4.15 MB).
2. Mirrors: Fontsource `google-sans-flex`; `github.com/expo/google-fonts` → `font-packages/google-sans-flex`.
3. Hard-gate fallback if a google/fonts repo path is required TODAY: **Roboto Flex** (`ofl/robotoflex/`, OFL 1.1, x/cap 0.723 — Google's own documented 3P mapping for the M3E axis system). GSF preferred.

Bundle: rename to `google_sans_flex_variable.ttf` (composeResources naming), ship `OFL.txt` in the repo (e.g. `docs/licenses/google-sans-flex-OFL.txt`) and surface it in the app's licenses screen (OFL requires the license to accompany distribution). Optional size mitigation: subset/instance with `fonttools varLib.instancer` (axes we use: wght only) — the 4.15 MB VF instanced to wght 100–1000 with other axes pinned drops to well under 1 MB. For iOS/desktop targets where Compose variable-axis instancing is unreliable, use the static `_24pt` cuts from the official zip (`GoogleSansFlex_24pt-{Regular,Medium,Bold}.ttf`).

**Weights registered: 400 (Regular), 500 (Medium), 700 (Bold)** via `FontVariation.weight()` — the new scale uses only these three (Manrope's 600/800 registrations are dropped). Tabular numerals: GSF carries `tnum` (verified in the shipping binary's GSUB) — keep `fontFeatureSettings = "tnum"` on the stat slots exactly as today.

### 5.2 The 15-slot scale — exact M3/M3E token values

Policy (matches documented the reference app/M3E usage): **display, headline and titleLarge use the M3E *Emphasized* styles** (the "emphasized typography" Google names as an M3E pillar — weight 500, tracking 0); **titleMedium/titleSmall, body and label slots use the M3 baseline** (current androidx values incl. the post-M3E tracking nudges) — press screenshots show regular-weight body and medium labels. Sizes/line heights are identical between baseline and emphasized, so every size below is the exact public token value.

| Slot | New (GSF) | Source token | Current (Manrope) | Delta |
|---|---|---|---|---|
| displayLarge | **500 · 57/64 · 0** + tnum | displayLargeEmphasized | 800 · 56/64 · −0.25 + tnum | weight 800→500, size 56→57, tracking −0.25→0 |
| displayMedium | **500 · 45/52 · 0** + tnum | displayMediumEmphasized | 800 · 44/52 · −0.25 + tnum | weight 800→500, size 44→45, tracking →0 |
| displaySmall | **500 · 36/44 · 0** | displaySmallEmphasized | 700 · 36/44 · 0 | weight 700→500 |
| headlineLarge | **500 · 32/40 · 0** | headlineLargeEmphasized | 700 · 32/40 · 0 | weight 700→500 |
| headlineMedium | **500 · 28/36 · 0** | headlineMediumEmphasized | 700 · 28/36 · 0 | weight 700→500 |
| headlineSmall | **500 · 24/32 · 0** | headlineSmallEmphasized | 600 · 24/32 · 0 | weight 600→500 |
| titleLarge | **500 · 22/28 · 0** | titleLargeEmphasized | 600 · 22/28 · 0 | weight 600→500 |
| titleMedium | **500 · 16/24 · +0.2** | titleMedium (androidx current) | 600 · 16/24 · +0.1 | weight 600→500, tracking +0.1→+0.2 |
| titleSmall | **500 · 14/20 · +0.1** | titleSmall | 500 · 14/20 · +0.1 | unchanged |
| bodyLarge | **400 · 16/24 · +0.5** | bodyLarge | 400 · 16/24 · +0.15 | tracking +0.15→+0.5 |
| bodyMedium | **400 · 14/20 · +0.2** | bodyMedium (androidx current) | 400 · 14/20 · +0.25 | tracking +0.25→+0.2 |
| bodySmall | **400 · 12/16 · +0.4** | bodySmall | 400 · 12/16 · +0.4 | unchanged |
| labelLarge | **500 · 14/20 · +0.1** | labelLarge | 600 · 14/20 · +0.1 | weight 600→500 |
| labelMedium | **500 · 12/16 · +0.5** | labelMedium | 500 · 12/16 · +0.4 | tracking +0.4→+0.5 |
| labelSmall | **500 · 11/16 · +0.5** | labelSmall | 500 · 11/16 · +0.5 | unchanged |

Net effect, answering "check font and sizes of everything": every size/line-height was already M3-shaped except the displays (56/44 → exact 57/45); the real correction is **weight** — Nocturne's extra-bold (800/700/600) ramp flattens to Google's 500/400 system, and tracking returns to exact token values.

### 5.3 YoloTypeExtras

| Style | New (GSF) | Source token | Current | Delta |
|---|---|---|---|---|
| statHero | **500 · 57/64 · 0 + tnum** | = displayLargeEmphasized (the reference app's hero "56%" class) | 800 · 72/80 · −0.5 + tnum | 72→57, 800→500 — the reference app hero numbers are display-large class, not poster-size |
| kicker | **700 · 12/16 · +0.5** (caps optional) | labelMediumEmphasized | 700 · 12/16 · +1.5 | tracking +1.5→+0.5 (Google apps don't letter-space kickers that wide) |
| statUnit | **500 · 14/20 · +0.1 + tnum** | titleSmall class | 600 · 14/20 · +0.1 + tnum | weight 600→500 |

Emphasized variants beyond these slots (e.g. `titleMediumEmphasized` 700 for selected toolbar segments) may be added later as extras — NOT as new Typography slots (architecture frozen).

---

## 6. Shapes & component metrics (M3E-faithful)

### 6.1 Shapes (theme/Shape.kt)

Exact M3E `ShapeTokens` corner scale replaces the bespoke 6/10/14/20/28:

| Slot | New | Current | Delta |
|---|---|---|---|
| extraSmall | **4dp** | 6dp | −2 |
| small | **8dp** | 10dp | −2 |
| medium | **12dp** | 14dp | −2 |
| large | **16dp** | 20dp | −4 |
| extraLarge | **28dp** | 28dp | unchanged |

M3E extension radii (largeIncreased 20, extraLargeIncreased 32, extraExtraLarge 48, full) are documented here for component use; Compose `Shapes` has 5 slots, so these stay component-level constants in `YoloTokens` ONLY if a component needs them (no structural change required now). Buttons remain full-pill by component convention (matches M3E round default; pressed-state morph to small/medium radius is optional M3E polish).

### 6.2 Component metrics annex (from androidx M3E tokens — "sizes of everything")

- Bottom navigation bar: **64dp** container (the M3E short bar the reference app ships; classic tall = 80dp). Active indicator = full-corner pill in `secondaryContainer` (ours: section `navIndicator`), vertical-item indicator 56×32dp, icon 24dp.
- Buttons: Small/default **40dp** high (20dp icon, 16dp h-padding), Medium 56dp (24/24), XSmall 32dp. Shape full → pressed morph small(8)/medium(12).
- FAB: baseline **56dp / corner 16**; Medium 80dp / corner 20; Small 40dp / corner 12.
- Top app bar (small) and docked/floating toolbars: **64dp**.
- These are guidance for `Dimens.kt`/components; no Dimens token renames — only values that disagree with the above should move to them when touched.

---

## 7. WCAG verification annex — computed, not eyeballed

Method: python3, WCAG 2.x relative luminance (sRGB linearization, threshold 0.03928, exponent 2.4), ratio = (L_hi+0.05)/(L_lo+0.05). Floors: 4.5 for text-on-fill pairs, 3.0 for large-text/UI-graphics pairs (outline, habit-state glyphs, progress graphics, accent-on-wash). **205 pairs, 0 failures.** Full run output:

```
== scheme-L ==
PASS   6.39 (4.5)  onPrimary/primary            #FFFFFF on #0B57D0
PASS   7.04 (4.5)  onPrimaryContainer/primaryContainer  #0842A0 on #D3E3FD
PASS   6.45 (4.5)  onSecondary/secondary        #FFFFFF on #00639B
PASS   7.20 (4.5)  onSecondaryContainer/secondaryContainer  #004A77 on #C2E7FF
PASS   6.53 (4.5)  onTertiary/tertiary          #FFFFFF on #146C2E
PASS   7.32 (4.5)  onTertiaryContainer/tertiaryContainer  #0F5223 on #C4EED0
PASS   6.54 (4.5)  onError/error                #FFFFFF on #B3261E
PASS   7.17 (4.5)  onErrorContainer/errorContainer  #8C1D18 on #F9DEDC
PASS  16.25 (4.5)  onBackground/background      #1A1C1E on #FAF9F8
PASS  16.25 (4.5)  onSurface/surface            #1A1C1E on #FAF9F8
PASS   8.93 (4.5)  onSurfaceVariant/surface     #444746 on #FAF9F8
PASS   7.28 (4.5)  onSurfaceVariant/surfaceVariant  #444746 on #E1E3E1
PASS  11.79 (4.5)  inverseOnSurface/inverseSurface  #F2F2F2 on #303030
PASS  17.09 (4.5)  onSurface/sCLowest #FFFFFF · 15.92 sCLow #F5F7FA · 15.47 sC #F0F4F9 · 14.78 sCHigh #EAEFF5 · 14.00 sCHighest #E4E9EF
PASS   9.39 (3.0)  onSurfaceVariant vs same five containers: 9.39 / 8.75 / 8.51 / 8.12 / 7.69
PASS   4.31 (3.0)  outline/surface              #747775 on #FAF9F8
PASS   6.07 (3.0)  primary/surface              #0B57D0 on #FAF9F8

== scheme-D ==
PASS   7.50 (4.5)  onPrimary/primary            #062E6F on #A8C7FA
PASS   7.04 (4.5)  onPrimaryContainer/primaryContainer  #D3E3FD on #0842A0
PASS   7.65 (4.5)  onSecondary/secondary        #003355 on #7FCFFF
PASS   7.20 (4.5)  onSecondaryContainer/secondaryContainer  #C2E7FF on #004A77
PASS   7.25 (4.5)  onTertiary/tertiary          #0A3818 on #6DD58C
PASS   7.32 (4.5)  onTertiaryContainer/tertiaryContainer  #C4EED0 on #0F5223
PASS   7.66 (4.5)  onError/error                #601410 on #F2B8B5
PASS   7.17 (4.5)  onErrorContainer/errorContainer  #F9DEDC on #8C1D18
PASS  14.47 (4.5)  onSurface/surface            #E3E3E3 on #131314
PASS  10.90 (4.5)  onSurfaceVariant/surface     #C4C7C5 on #131314
PASS   5.51 (4.5)  onSurfaceVariant/surfaceVariant  #C4C7C5 on #444746
PASS  10.28 (4.5)  inverseOnSurface/inverseSurface  #303030 on #E3E3E3
PASS  15.03 (4.5)  onSurface/sCLowest #0E0E0F · 13.54 sCLow #1A1A1C · 12.86 sC #1E1F20 · 11.22 sCHigh #282A2C · 9.59 sCHighest #343535
PASS  11.32 (3.0)  onSurfaceVariant vs same five containers: 11.32 / 10.20 / 9.69 / 8.45 / 7.22
PASS   5.83 (3.0)  outline/surface              #8E918F on #131314   (Nocturne edge was 3.24 — improved)
PASS  10.80 (3.0)  primary/surface              #A8C7FA on #131314
PASS   7.22 (3.0)  onSurfaceVariant/sCHighest (darkOutlineBanned gate)  #C4C7C5 on #343535

== ext-L ==
PASS   6.53 onSuccess/success · 7.25 onSuccessContainer/successContainer · 9.17 onWarning/warning
PASS   7.11 onWarningContainer/warningContainer · 6.39 onInfo/info · 7.04 onInfoContainer/infoContainer
PASS   9.17 onStreak/streak · 7.11 onStreakContainer/streakContainer
PASS  16.25 textPrimary · 8.93 textSecondary · 6.14 textTertiary · 6.14 textPlaceholder (vs surface, 4.5)
PASS   6.01 (4.5)  textPlaceholder/inputFill    #5C5F5E on #F5F7FA (light input fill = surfaceContainerLow; ContrastTest :92-93)
PASS  14.47 onHeroSurface/heroSurface
PASS  habitComplete #009688: 3.49 surface / 3.67 card / 3.33 tile (3.0)
PASS  habitMissed #5C5F5E: 6.14 / 6.45 / 5.84 · habitSkipped #747775: 4.31 / 4.53 / 4.10 · habitPending: 16.25 / 17.09 / 15.47
PASS   3.08 (3.0) progressIndicator/progressTrack  #4285F4 on #EAEFF5
PASS  10.80 (3.0) heroAccent/heroSurface  #A8C7FA on #131314

== ext-D ==
PASS   7.25 onSuccess/success · 7.17 onSuccessContainer/successContainer · 7.13 onWarning/warning
PASS   7.13 onWarningContainer · 7.50 onInfo/info · 7.04 onInfoContainer · 7.13 onStreak · 7.13 onStreakContainer
PASS  14.47 textPrimary · 10.90 textSecondary · 8.11 textTertiary · 8.11 textPlaceholder (vs surface, 4.5)
PASS   5.37 (4.5)  textPlaceholder/inputFill    #A9ACAA on #343535 (dark input fill = surfaceContainerHighest; NV60 #8E918F fails this floor at 3.87 — hence NV70)
PASS  14.47 onHeroSurface/heroSurface
PASS  habitComplete #41DDD0: 11.03 / 8.56 / 9.81 · habitMissed #A9ACAA: 8.11 / 6.29 / 7.21
PASS  habitSkipped #8E918F: 5.83 / 4.53 / 5.19 · habitPending #E3E3E3: 14.47 / 11.22 / 12.86
PASS   6.84 (3.0) progressIndicator/progressTrack  #8AB4F8 on #282A2C
PASS  10.80 (3.0) heroAccent/heroSurface

== sections (floors: text pairs 4.5; accent-on-surface 3.0; cross-world streak/sheet 3.0, error/wash 4.5, habitPending/wash 3.0) ==
Tracking-L : onAccent 6.39 · onAccentContainer 7.04 · onSheetIconChip 7.20 · onSheet/sheet 13.98 · variant/sheet 7.60 · onSheet/wash 15.03 · accent/wash 5.62 · accent/sheet 5.22 · streak/sheet 7.50 · error/wash 5.75 · habitPending/wash 15.03
Tracking-D : onAccent 7.50 · onAccentContainer 7.20 · onSheetIconChip 7.20 · onSheet/sheet 12.86 · variant/sheet 9.69 · onSheet/wash 14.47 · accent/wash 10.80 · accent/sheet 9.60 · streak/sheet 12.70 · error/wash 10.87 · habitPending/wash 14.47
Progress-L : onAccent 6.45 · onAccentContainer 5.45 · onSheetIconChip 8.04 · onSheet/sheet 14.10 · variant/sheet 7.69 · onSheet/wash 15.15 · accent/wash 5.72 · accent/sheet 5.32 · streak/sheet 7.56 · error/wash 5.79 · habitPending/wash 15.15
Progress-D : onAccent 10.28 · onAccentContainer 7.18 · onSheetIconChip 7.18 · onSheet/sheet 12.86 · variant/sheet 9.69 · onSheet/wash 14.47 · accent/wash 14.47 · accent/sheet 12.87 · streak/sheet 12.70 · error/wash 10.87 · habitPending/wash 14.47
Settings-L : onAccent 9.48 · onAccentContainer 7.35 · onSheetIconChip 7.35 · onSheet/sheet 13.62 · variant/sheet 7.45 · onSheet/wash 15.10 · accent/wash 8.37 · accent/sheet 7.55 · streak/sheet 7.31 · error/wash 5.77 · habitPending/wash 15.10
Settings-D : onAccent 7.71 · onAccentContainer 7.21 · onSheetIconChip 7.21 · onSheet/sheet 12.86 · variant/sheet 9.69 · onSheet/wash 14.47 · accent/wash 10.89 · accent/sheet 9.68 · streak/sheet 12.70 · error/wash 10.87 · habitPending/wash 14.47

== habitAccents (all 4.5 floors) ==
Light  accent/onAccent : blue 6.39 · teal 6.45 · green 6.53 · purple 9.48 · pink 4.89 · amber 5.00 · cyan 6.45 · indigo 11.98
Light  container pairs : blue 7.04 · teal 5.45 · green 7.25 · purple 7.35 · pink 6.42 · amber 7.11 · cyan 7.20 · indigo 9.26
Dark   accent/onAccent : blue 7.50 · teal 10.28 · green 10.15 · purple 10.19 · pink 6.72 · amber 7.13 · cyan 10.21 · indigo 7.71
Dark   container pairs : blue 7.04 · teal 7.18 · green 7.17 · purple 7.21 · pink 8.06 · amber 7.13 · cyan 7.20 · indigo 9.29

== informational: raw the reference app measurements that fail AA (why the [derived] fixes exist) ==
4.01  the reference app's own dark sleep-chip main fill vs its text   #8457BC on #EEDCFF
3.71  Fitness step-bar teal w/ white                       #00958A on #FFFFFF
3.76  hypnogram Awake pink w/ white                        #D45988 on #FFFFFF
3.72  sleep duration-bar purple w/ white                   #9A70D4 on #FFFFFF
3.77  health chart green w/ white                          #02983F on #FFFFFF

TOTAL PAIRS: 205   FAILURES: 0
```

---

## 8. ContrastTest impact (core/designsystem/src/commonTest/.../theme/ContrastTest.kt)

| Assertion block | Impact |
|---|---|
| 22 scheme pairs ×2 modes (:40-66) | No code change. All pairs pass on new values (§7); floors keep holding because the test reads token objects. |
| Extended pairs incl. habit states vs surface/card/tile (:68-111) | No code change; all pass. Light `habitSkipped` edge improves 3.38→4.10; dark `outline` edge 3.24→5.83. The placeholder/input-fill floor (:92-93, light `surfaceContainerLow` / dark `surfaceContainerHighest`) passes 6.01 light / 5.37 dark — dark `textPlaceholder`/`textTertiary` are NV70 #A9ACAA precisely because NV60 #8E918F fails this floor at 3.87 vs #343535 (§7 ext rows). |
| `habitAccents.size == 8` (:110) | Unchanged — still 8 quads. |
| Section 14-pair + alias-invariant block (:127-161) | No code change; all pairs pass; alias invariants (navIndicator/onNavIndicator/navSelectedText/dragHandle) hold automatically (derived fields untouched). |
| **`progressWorldStaysBrandIdentical` (:172-178)** | **MUST be replaced.** Progress accent is now Fitness teal (#016B5B/#93F4EA) while base primary is GM blue. Replace with `trackingWorldStaysBrandIdentical`: TrackingLight.accent == LightColorScheme.primary (#0B57D0), TrackingLight.accentContainer == primaryContainer (#D3E3FD), TrackingDark.accent == DarkColorScheme.primary (#A8C7FA) — true by construction (the reference app's global accent IS the Today blue). This is the only test edit. |
| `darkOutlineBannedOnElevatedContainers` (:181-188) | No code change; passes at 7.22 (was pinned near 3.0). |
| New fields | None added — no new assertions required. Note for reviewers: dark wash/sheet now identical across worlds and streak==warning values; no existing assertion checks distinctness, so no edits needed. |

---

## 9. Implementation delta plan (exact file → val list)

### 9.1 `core/designsystem/src/commonMain/kotlin/com/yolo/core/designsystem/tokens/YoloPrimitives.kt`

- **Ink ramp (16):** Ink0 #FFFFFF · Ink50 #FAF9F8 · Ink100 #F5F7FA · Ink150 #F0F4F9 · Ink200 #EAEFF5 · Ink250 #E4E9EF · Ink300 #DADDE0 · Ink350 #C4C7C5 · Ink400 #A9ACAA · Ink450 #8E918F · Ink500 #747775 · Ink550 #5C5F5E · Ink600 #444746 · Ink700 #303030 · Ink800 #1A1C1E · InkText #E3E3E3. (Const names keep their ladder semantics; light-role tone positions shift slightly — see Theme.kt re-points below.)
- **Deep ramp (6):** Deep50 #343535 · Deep100 #282A2C · Deep200 #1E1F20 · Deep300 #1A1A1C · Deep400 #131314 · Deep500 #0E0E0F.
- **Brand block (10) → Fitness teal family:** Brand1000 #003732 · Brand900 #00514B · Brand800 #016B5B · Brand700 #08655F · Brand600 #009688 · Brand500 #41DDD0 · Brand300 #93F4EA · Brand200 #BBFCF4 · Brand100 #CCEAEC · BrandDeep #05514A. (Brand = the app's green-teal channel: success containers stay separate, see Color.kt.)
- **Amber block (7):** Amber800 #5F4200 · Amber700 #614206 · Amber300 #FDDEA4 · Amber100 #FFDEA9 · AmberDeepContainer #5F4200 · AmberOnDark #5F4200 · AmberContainerOn #FDDEA4.
- **Red block (7) → M3 baseline error ramp:** Red700 #B3261E · Red600 #8C1D18 · Red300 #F2B8B5 · Red200 #EC928E · Red100 #F9DEDC · RedDeep900 #601410 · RedDeep800 #8C1D18.
- **Blue block (7) → GM blue (info):** Blue700 #0B57D0 · Blue300 #A8C7FA · Blue100 #D3E3FD · BlueDeep #0842A0 · BlueOnDark #062E6F · BlueDarkContainer #0842A0 · BlueDarkContainerOn #D3E3FD.
- **Cobalt block (14, Tracking):** Cobalt700 #0B57D0 (unchanged) · Cobalt300 #A8C7FA (unchanged) · Cobalt100 #D3E3FD (unchanged) · Cobalt150 #C2E7FF · CobaltDeep #0842A0 · CobaltDarkContainer #004A77 · CobaltContainerOn #C2E7FF · CobaltWashLight #EAF1FB · CobaltSheetLight #E0E9F8 · CobaltWashDark #131314 · CobaltSheetDark #1E1F20 · CobaltChipDark #004A77 · CobaltVariantLight #44474F · CobaltVariantDark #C4C7C5.
- **Violet block (14, Settings):** Violet700 #5A2E8E · Violet300 #D0BCFF · Violet100 #EADDFF · Violet150 #EADDFF · VioletDeep #381E72 · VioletDarkContainer #5B2F90 · VioletContainerOn #EFDBFF · VioletWashLight #F0F0FA · VioletSheetLight #E7E3F8 · VioletWashDark #131314 · VioletSheetDark #1E1F20 · VioletChipDark #5B2F90 · VioletVariantLight #49454F · VioletVariantDark #C4C7C5.
- **Emerald-world consts (6) → teal world:** BrandWashLight #E5F4F7 · BrandSheetLight #D9EDF0 · BrandChipLight #BBFCF4 · BrandWashDark #131314 · BrandVariantLight #3F4946 · BrandVariantDark #C4C7C5.
- **NEW: Cyan block (5)** — the one structural addition, required because the reference app's M3 `secondary` is the GM cyan family, which no existing primitive covers: `Cyan700 #00639B · Cyan300 #7FCFFF · Cyan100 #C2E7FF · Cyan800 #004A77 · Cyan900 #003355`. Mirror in `theme/Color.kt` like every other block.
- Watch-carry caveat (spec drift D3) unchanged: warning/success extras still live in Color.kt only.

### 9.2 `theme/Color.kt`

- Status-extras block (:115-127): YoloGreenContainerDark #00522C · YoloWarningLight #614206 · YoloWarningContainerLight #FFDEA9 · YoloOnWarningContainerLight #614206 · YoloWarningDark #FDDEA4 · YoloOnWarningDark #5F4200 · YoloWarningContainerDark #5F4200 · YoloOnWarningContainerDark #FDDEA4 · YoloOnErrorContainerDark #F9DEDC · YoloPrimaryHoverLight #0842A0.
- Stray neutrals (drift D1/D2) — retire by re-pointing roles to the new Cyan consts: `YoloOnSecondaryDark` → #003355 (true dark onSecondary, = Cyan900), `YoloOnSecondaryContainerDark` → #C2E7FF (= Cyan100). Light secondaryContainer/onSecondaryContainer and light inverseSurface no longer borrow these vals (see 9.3).
- Add success-light extras if missing as named vals: YoloSuccessContainerLight #BFEFBB, YoloOnSuccessContainerLight #085226 (currently primary-container reuse).

### 9.3 `theme/Theme.kt`

- LightColorScheme/DarkColorScheme: re-point all roles per §2 (notably: light secondary roles → Cyan consts, not Ink; light inverseSurface → Ink700 #303030; dark background → Deep400 #131314, same as surface; dark surfaceContainerLowest → Deep500; dark surfaceBright/Highest → Deep50; dark surfaceVariant/outlineVariant → Ink600; dark outline → Ink450; tertiary roles → GM green values, no longer the amber block).
- ExtendedColors light/dark blocks: re-point per §3 (success*, warning*, info*, streak*, celebration, progress*, habit*, heatmap*, hero*, aura*, text*, hovers, surfaces).
- **11 alpha-baked literals (the silent-drift trap):** light textDisabled `0x611A1C1E` (:170) · disabledFill/disabledOutline `0x1F1A1C1E` (:174-175) · overlay `0xCC1A1C1E` (:176) · surfaceOutline `0x141A1C1E` (:180) · dark elevatedCardOutline `0x1AFAF9F8` (:224) · textDisabled `0x61E3E3E3` (:230) · disabledFill/disabledOutline `0x1FE3E3E3` (:234-235) · overlay `0xCC0E0E0F` (:236) · surfaceOutline `0x1AFAF9F8` (:240).
- LightHabitAccents/DarkHabitAccents (:104-124): the 8 quads per §3.1.

### 9.4 `theme/SectionTheme.kt`

- Re-point the 6 palette vals per §4 tables. **Decouple `ProgressSectionDark.sheetSurface` from `YoloGreenContainerDark`** (it becomes neutral #1E1F20 = Deep200, while YoloGreenContainerDark becomes the dark successContainer #00522C). No field changes; derived fields untouched.

### 9.5 `theme/Type.kt` + font asset

1. Delete `core/designsystem/src/commonMain/composeResources/font/manrope_variable.ttf`; add `google_sans_flex_variable.ttf` (source §5.1; optionally wght-instanced to cut size). Res accessors regenerate at build.
2. Update import (:12) to the generated `google_sans_flex_variable` resource; rename `private val Manrope` → `GoogleSansFlex`; registration list becomes weights **400/500/700** with `FontVariation.Settings(FontVariation.weight(w))` (drop 600/800). If switching to static `_24pt` cuts for iOS/desktop: one `Font()` per file, no variationSettings.
3. Re-point the 3 direct family references in YoloTypeExtras (:155, :165, :174).
4. Apply the 15-slot values per §5.2 and extras per §5.3. Keep tnum fontFeatureSettings on displayLarge/displayMedium/statHero/statUnit. Structure stays a `@Composable get()` (drift D4 — required by composeResources, unchanged).
5. Add `docs/licenses/google-sans-flex-OFL.txt` and wire into the licenses screen.

### 9.6 `theme/Shape.kt`

- 6/10/14/20/28 → **4/8/12/16/28** per §6.1.

### 9.7 `ContrastTest.kt`

- Replace `progressWorldStaysBrandIdentical` with `trackingWorldStaysBrandIdentical` per §8. Run full suite; §7 predicts green (incl. the :92-93 placeholder/input-fill floor, now listed in the ext-L/ext-D blocks).

### 9.8 `components/buttons/YoloButton.kt:115`

- `CircularProgressIndicator(color = Color.Black)` → use the button's content color (`LocalContentColor.current`). The lone component-level hardcode; wrong in dark mode under any palette.

### 9.9 Not touched

`Dimens.kt`, `Motion.kt`, `YoloTheme.kt` (still defaults to Progress world per addendum §3.5), all section-world consumers (MainScreen, BottomNavigationBar, tracking/progress/settings/account screens, AppModalBottomSheet, YoloSheetActionRow) — they read tokens and re-skin for free.

---

## 10. Honesty section — what could NOT be copied exactly

1. **"#141218-family" gloss vs measurement.** The implementation directive's parenthetical pointed at the public M3 baseline dark neutrals (#141218/#211F26 — purple-tinted). The pixel record proves the reference app ships GM3 Google-app GRAYS (#131314/#1E1F20/#282A2C). "Copy EXACTLY" wins: GM grays adopted; the public-baseline family appears nowhere in this spec.
2. **Typeface.** Google Sans Text (the reference app's body face) is unreleased — body text uses Google Sans Flex instead (same skeleton, OFL since Nov 2025). GSF itself is genuine, not a substitute; but it is not yet in the google/fonts repo (issue #10006), so the bundled file comes from the official fonts.google.com zip/gstatic/Fontsource rather than a canonical `ofl/` path. Fallback if that gate matters: Roboto Flex. This overrides Nocturne's "no Google Sans" non-goal — the licensing fact it rested on expired.
3. **No light-mode Today imagery exists.** Tracking light wash #EAF1FB is borrowed from the measured Health-tab canvas (the only measured light blue canvas); Tracking light sheet, and all light sheets, are [derived] (no bottom-sheet press shots at all).
4. **No dark-mode Fitness/Sleep/Health imagery exists.** Dark worlds use the measured neutral canvas policy (§4.4). Dark Progress chip/container colors come from the Today tab's steps chip (real dark imagery, different tab). Settings dark `accent` is the M3 baseline P80 — purely token-derived. Base light `surface` #FAF9F8 is likewise unverifiable (light canvases are always world-washed in press shots).
5. **the reference app ships sub-AA pairs in JPEG; we don't.** Measured failures and our fixes: sleep-chip main #8457BC vs #EEDCFF (4.01) → container uses the chip's darker band #5B2F90 (7.21); step-bar teal #00958A as a white-text accent (3.71) → accent uses measured #016B5B (6.45), #009688 kept for 3:1 graphics roles; hypnogram pink #D45988 (3.76) → #C04373 [derived: AA-fix]; duration-bar purple #9A70D4 (3.72) → data-viz only, accent uses measured #5A2E8E; chart green #02983F (3.77) → GM T40 #146C2E. All retain the measured hue.
6. **Measured but deliberately not adopted:** "+34" badge #3371EA (between blue 500/600, single low-n sample); "Today" nav label #8BCFF6 (least-certain measurement; architecture uses accent for navSelectedText); outlined-pill border #B5BFC1 (between NV70/NV80; outline roles stay on official NV tones); goal dotted line ~#578F77 (low confidence); in-range chart band #DEEEEB (chart-specific); marketing washes #E4FDFA/#E6F8FC/#F8F2FE (blog assets, not app UI). Dark hypnogram Light/Deep stage colors are occluded by the FAB in every dark shot — light-mode equivalents used.
7. **JPEG provenance.** No PNG press assets exist; every [measured] value carries ±2–3/channel uncertainty (flat fills with n>5,000 effectively exact). Where measurement matched a published token, the token value was adopted precisely to launder out JPEG noise. One unresolved token ambiguity: GM3 dark surface-container is #1E1F20 in Google-app shipping themes but Chromium's ref N12 is #1F2020; the measurement (#1E1F21) sides with #1E1F20, which we adopt.
8. **Structural divergences from the reference app, owned by our architecture:** the reference app uses blue links even inside the Sleep/Fitness worlds; our per-world accent architecture keeps purple/teal accents in Settings/Progress (architecture is frozen by directive). `streak` and `warning` converge on the reference app's single amber channel (fields kept, values equal). `info` duplicates `primary` (one blue). Dark wash/sheet values converge across worlds (§4.4). the reference app's dark cards-darker-than-canvas inversion is a composition pattern, not a token (§1.1).



