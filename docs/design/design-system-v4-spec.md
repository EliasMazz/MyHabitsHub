# Design System v4 Spec — "Pixel-true to the 12 reference PNGs"

Status: APPROVED FOR IMPLEMENTATION · Date: 2026-06-12 · Branch target: refactor/modularize-shared
Supersedes: the **values** of `design-system-v3-spec.md`. Does NOT supersede its architecture: `YoloTheme` + M3 `ColorScheme` + `ExtendedColors` + `SectionColors`/`YoloSectionTheme` + `YoloTokens` stay exactly as structured; every field list is unchanged. Typography stays Google Sans Flex per the user decision (v3 §5 stands verbatim — zero `Type.kt` changes in v4; the onboarding screen uses existing slots only).

Ground truth: **PNG-exact pixel measurements of the user's own 12 reference screenshots** in `docs/design/refs/` (1080×2424, no JPEG noise; 5×5-patch medians for fills, modal core-ink for glyphs). Light: `106287/106289/106292` (Today), `106290` (Health), `106291` (Fitness), `106299` (setup checklist). Dark: `106293` (Today), `106294` (Fitness), `106295` (Sleep). Onboarding: `106296/106298` (dark), `106300` (light). Measurement scripts: `/tmp/v4sample/measure.py`, `sample.py`, `crop.py`, `dark_sample.py`, `dark_probe.py`, `explore.py`, `refine.py`.

**Why v4 exists:** the user compared the shipped v3 palette against the live reference app on their phone and found it "really different." v3 was built from JPEG press shots and laundered measurements through published Google tokens; several of those token assumptions were wrong (light canvas, light text ink, the gray container, the data blues, the entire light vivid-chip system — none of which press shots showed). v4 adopts the PNG-exact measurement **verbatim** wherever one exists.

**Provenance legend (every value carries one):**
- `[measured]` — PNG-exact value adopted verbatim; element + image cited.
- `[derived]` — not directly measurable (no relevant imagery, or the measured pair fails WCAG AA); rule stated. AA-fixes are tagged `[derived: AA-fix of #xxxxxx]` and keep the measured hue, adjusting tone minimally.
- `[reuse v3]` — the refs show **nothing relevant** for this slot; the v3 value carries over unchanged (each case listed in §9.1).

WCAG: every text-on-fill pair defined in this document was computed with python3 (`/tmp/v4check.py`, WCAG 2.x relative luminance). **234 gated pairs, 0 failures** (§5). Where the reference app itself ships a sub-AA pair (it does, five times — PNG-exact, no noise excuse), we keep the hue and minimally shift tone, flagged inline and listed in §9.2.

---

## 0. The six findings that re-shape v3

1. **Light mode is white + `#F0F4F9` + `#DDE3EA` — not `#FAF9F8` + `#EAEFF5`.** The measured light surface system is exactly three neutrals: `#FFFFFF` (Health/Fitness canvas, sheets, cards — 106290/106291/106287), `#F0F4F9` (Today canvas, nav bar, tinted cards — 106287/106290/106299), `#DDE3EA` (gray chips, ring track, edit button, inactive day dots — 106287/106289/106291). Two more rungs were measured incidentally: `#F8FAFD` (Bpm icon capsule, 106289) and nothing between `#F0F4F9`/`#DDE3EA`. v3's `#FAF9F8`/`#F5F7FA`/`#EAEFF5`/`#E4E9EF`/`#DADDE0` ladder is replaced wholesale.
2. **Light ink is `#1F1F1F` (GM N10), not `#1A1C1E`.** Every light-mode title, value and headline measures `#1F1F1F` exactly (106290 section headlines, 106287 ring value, 106291 row titles, 106299 checklist text). Secondary `#444746` confirmed exact. Dark inks `#E3E3E3`/`#C4C7C5` confirmed exact.
3. **The nav system is GLOBAL, not per-world.** Active pill `#C2E7FF` light / `#004A77` dark, icon-on-pill `#004A77`/`#C2E7FF`, label `#00639B`/`#7FCFFF` — measured **identical on all three tabs in both modes** (106287/106290/106291; 106293/106294/106295). The label is the GM-cyan `secondary`, NOT the accent. This decouples `navIndicator`/`navSelectedText` from `accentContainer`/`accent` (fields unchanged; alias invariants replaced — §3.1, §7).
4. **The data blue is periwinkle `#437EF8`/`#90BAFF`/`#001944`, not Google blue 500/300.** Ring arc, heart icon, +83 badge border all measure `#437EF8` (106287/106289); dark heart `#90BAFF` (106293); badge fill `#90BAFF`, badge text `#001944`. v3's `#4285F4`/`#8AB4F8` appear nowhere.
5. **The vivid chip system is real and light-mode-saturated.** Light stat chips are full-color fills (`#41D5CA` teal, `#FFDBCD` peach, `#EDDCFF`/`#D4B2FF` lavender, `#BEEFBB` green, `#BDE9FF` cyan) with same-hue dark inks and pale icon capsules — not pastel containers. Dark chips invert (deep fill, pale ink, deeper icon circle). §2.1/§3.4 map every measured chip onto the habitAccent quads and section containers faithfully.
6. **Dark canvases are confirmed neutral, and the Today inversion is confirmed.** Fitness/Sleep dark: canvas `#131314`, cards `#1E1F20` (standard M3 mapping — measured at 24 spots, 106294/106295). Today dark inverts: canvas `#1E1F20`, coach panel `#131314` (106293). Light Today shows the same trick (canvas `#F0F4F9`, white sheet below). The inversion stays a composition pattern; section washes now encode it per world (§3).

World mapping (unchanged): **Tracking ← Today**, **Progress ← Fitness**, **Settings ← Sleep**.

---

## 1. M3 ColorScheme — complete tables

### 1.1 LightColorScheme

| Role | v4 | Was (v3) | Provenance | WCAG (§5) |
|---|---|---|---|---|
| primary | #0B57D0 | #0B57D0 | [measured: Ask-Coach FAB fill + coach sparkle 106287, 0/3 badge 106299, Customize/Show-more/See-all links 106290/106291, onboarding pill 106300 — confirmed EXACT] | onPrimary 6.39 |
| onPrimary | #FFFFFF | #FFFFFF | [measured: FAB icon/text 106287, 0/3 badge text 106299, onboarding pill text 106300] | — |
| primaryContainer | #D3E3FD | #D3E3FD | [reuse v3: no element resolves to primaryContainer; the refs' light blue container is the secondaryContainer pill below] | onPrimaryContainer 7.04 |
| onPrimaryContainer | #0842A0 | #0842A0 | [reuse v3] | — |
| inversePrimary | #A8C7FA | #A8C7FA | [measured: dark FAB 106293, onboarding dark pill 106296] | — |
| secondary | #00639B | #00639B | [measured: nav active label, all 3 light tabs 106287/106290/106291 — provenance upgraded from token assumption to pixel fact] | onSecondary 6.45 |
| onSecondary | #FFFFFF | #FFFFFF | [reuse v3 official] | — |
| secondaryContainer | #C2E7FF | #C2E7FF | [measured: nav active pill ×3 tabs, Log/Start pills 106287, Dismiss pill 106299 — confirmed EXACT] | onSecondaryContainer 7.20 |
| onSecondaryContainer | #004A77 | #004A77 | [measured: pill '+'/runner/text content 106287/106299, nav active icon ×3] | — |
| tertiary | #146C2E | #146C2E | [derived: AA-fix of measured chart green #009840 (3.77:1 w/ white, 106290 dots/line); GM T40 kept, same hue family] | onTertiary 6.53 |
| onTertiary | #FFFFFF | #FFFFFF | [reuse v3] | — |
| tertiaryContainer | #C4EED0 | #C4EED0 | [reuse v3: the measured green chip #BEEFBB is claimed by successContainer, §2] | onTertiaryContainer 7.32 |
| onTertiaryContainer | #0F5223 | #0F5223 | [reuse v3] | — |
| error | #B3261E | #B3261E | [reuse v3: refs contain no error UI; the measured reds #DC362E/#E46962 are chart markers — annexed §4.1] | onError 6.54 |
| onError / errorContainer / onErrorContainer | #FFFFFF / #F9DEDC / #8C1D18 | same | [reuse v3] | 7.17 |
| background / surface | #FFFFFF | #FAF9F8 | [measured: Health/Fitness content canvas 106290/106291, Today white sheet 106287, onboarding canvas 106300] | onSurface 16.48 |
| onBackground / onSurface | #1F1F1F | #1A1C1E | [measured: headlines/values 106290, row titles 106291, checklist text 106299, ring value + edit pencil 106287 — GM N10] | — |
| surfaceVariant | #DDE3EA | #E1E3E1 | [measured: gray container family — Bpm chip 106289, placeholder chips, edit button, F-day circle] | onSurfaceVariant 7.27 |
| onSurfaceVariant | #444746 | #444746 | [measured: all secondary text/labels, nav inactive, radio ring 106287/106289/106290/106299 — confirmed EXACT] | vs surface 9.39 |
| surfaceDim | #DDE3EA | #DADDE0 | [measured: inactive week dots 106291, F-day circle 106290] | — |
| surfaceBright | #FFFFFF | #FFFFFF | [measured] | — |
| surfaceContainerLowest | #FFFFFF | #FFFFFF | [measured: sheets/cards] | onSurface 16.48 |
| surfaceContainerLow | #F8FAFD | #F5F7FA | [measured: Bpm icon-capsule fill 106289 — also the GM3 Google-apps ladder step] | onSurface 15.76 |
| surfaceContainer | #F0F4F9 | #F0F4F9 | [measured: Today canvas, nav bar, tinted cards, checklist card, Log-activity pill 106287/106290/106291/106299 — confirmed EXACT] | onSurface 14.92 |
| surfaceContainerHigh | #E9EEF6 | #EAEFF5 | [derived: GM3 Google-apps ladder step between measured #F0F4F9 and #DDE3EA; no element lands between them in the refs] | onSurface 14.15 |
| surfaceContainerHighest | #DDE3EA | #E4E9EF | [measured: Bpm/empty chips, edit button, ring track 106287/106289] | onSurface 12.75 |
| outline | #747775 | #747775 | [reuse v3: zero stroked outlines exist in any ref; selection-control ring measures onSurfaceVariant #444746 (radio 106299), matching stock M3] | vs surface 4.53 |
| outlineVariant | #C4C7C5 | #C4C7C5 | [reuse v3] | — |
| inverseSurface / inverseOnSurface | #303030 / #F2F2F2 | same | [reuse v3] | 11.79 |
| scrim | #000000 | #000000 | [reuse v3] | — |
| surfaceTint | #0B57D0 | #0B57D0 | = primary | — |

### 1.2 DarkColorScheme

| Role | v4 | Was (v3) | Provenance | WCAG |
|---|---|---|---|---|
| primary | #A8C7FA | #A8C7FA | [measured: FAB fill, coach sparkle, See-all links 106293/106294/106295, onboarding pill+dot+link 106296 — confirmed EXACT] | onPrimary 7.50 |
| onPrimary | #062E6F | #062E6F | [measured: FAB icon + Ask-Coach text 106293, onboarding pill text 106296] | — |
| primaryContainer / onPrimaryContainer | #0842A0 / #D3E3FD | same | [reuse v3: no dark blue container beyond the #004A77 pill, which is secondaryContainer] | 7.04 |
| inversePrimary | #0B57D0 | #0B57D0 | [measured: light counterpart] | — |
| secondary | #7FCFFF | #7FCFFF | [measured: nav active label, all 3 dark screens 106293/106294/106295] | onSecondary 7.65 |
| onSecondary | #003355 | #003355 | [reuse v3 official] | — |
| secondaryContainer | #004A77 | #004A77 | [measured: nav active pill ×3 + Log/Start pills 106293 — confirmed EXACT] | onSecondaryContainer 7.20 |
| onSecondaryContainer | #C2E7FF | #C2E7FF | [measured: pill content + nav active icon 106293/106294/106295] | — |
| tertiary / onTertiary / tertiaryContainer / onTertiaryContainer | #6DD58C / #0A3818 / #0F5223 / #C4EED0 | same | [reuse v3: M3 tertiary stays the GM green ramp for stock components; the measured dark chart green #54D071 lives in `success` (§2)] | 7.25 / 7.32 |
| error | #F2B8B5 | #F2B8B5 | [measured: streak-chip heart icon 106293 — M3 E80 EXACT, incidental confirmation] | onError 7.66 |
| onError / errorContainer / onErrorContainer | #601410 / #8C1D18 / #F9DEDC | same | [reuse v3] | 7.17 |
| background / surface / surfaceDim | #131314 | #131314 | [measured: Fitness/Sleep canvas 106294/106295, Today coach panel 106293, onboarding canvas 106296 — confirmed EXACT] | onSurface 14.47 |
| onBackground / onSurface | #E3E3E3 | #E3E3E3 | [measured: all dark titles/numerals 106293/106294/106295 — confirmed EXACT] | — |
| surfaceVariant | #444746 | #444746 | [reuse v3] | onSurfaceVariant 5.51 |
| onSurfaceVariant | #C4C7C5 | #C4C7C5 | [measured: dark secondary gray — captions, day labels, coach body, nav inactive 106293/106294/106295 — confirmed EXACT] | vs surface 10.90 |
| surfaceBright | #333537 | #343535 | [measured: dark gray container family, see sCHighest] | — |
| surfaceContainerLowest | #0E0E0F | #0E0E0F | [reuse v3: nothing below #131314 appears in refs] | onSurface 15.03 |
| surfaceContainerLow | #1B1B1B | #1A1A1C | [measured: Bpm icon circle/oval fill 106293] | onSurface 13.42 |
| surfaceContainer | #1E1F20 | #1E1F20 | [measured: Today canvas + nav bar ×3 + Fitness/Sleep metric cards (24 spots) 106293/106294/106295 — confirmed EXACT] | onSurface 12.86 |
| surfaceContainerHigh | #282A2C | #282A2C | [reuse v3: no v4 element lands between #1E1F20 and #333537] | onSurface 11.22 |
| surfaceContainerHighest | #333537 | #343535 | [measured: Bpm/empty chips, edit circle, F-day highlight circle 106293/106294/106295] | onSurface 9.60 |
| outline / outlineVariant | #8E918F / #444746 | same | [reuse v3: no outlines in refs — "separation is purely tonal" per measurement] | 5.83 |
| inverseSurface / inverseOnSurface | #E3E3E3 / #303030 | same | [reuse v3] | 10.28 |
| scrim | #000000 | #000000 | [reuse v3] | — |
| surfaceTint | #A8C7FA | #A8C7FA | = primary | — |

Composition note (both modes): the **Today inversion** — light Today paints canvas = `surfaceContainer` (#F0F4F9) with a `surface`-white sheet sliding over it; dark Today paints canvas = `surfaceContainer` (#1E1F20) with the coach panel in `surface` (#131314). Fitness/Sleep use the standard mapping (canvas = `surface`, cards = `surfaceContainer`). Encoded per world in §3 washes; roles keep standard M3 meaning.

---

## 2. ExtendedColors — field-by-field (every existing field, light | dark)

| Field | Light | Dark | Was (v3 L\|D) | Provenance |
|---|---|---|---|---|
| isDark | false | true | — | unchanged |
| success | #146C2E | #54D071 | #146C2E\|#6DD58C | light [derived: AA-fix of measured #009840 (3.77 w/ white), GM T40 kept]; dark [measured: RHR/HRV/REM/Deep/Efficiency chart lines+dots 106294/106295] |
| onSuccess | #FFFFFF | #0A3818 | same\|#0A3818 | light [reuse v3]; dark [derived: v3 tone kept — 6.68 on new accent] |
| successContainer | #BEEFBB | #25352A | #BFEFBB\|#00522C | [measured: 'In range' chips 106290 ×3] \| [measured: in-range bands, 6 spots 106294 + 5 spots 106295; the 106295 efficiency band #24342A (1-unit off) unifies here] |
| onSuccessContainer | #00522C | #54D071 | #085226\|#BCEEBB | [measured: 'In range' chip text 106290 ×3] \| [measured: lines/dots drawn on the band] |
| warning | #614206 | #FFBA2B | #614206\|#FDDEA4 | light [reuse v3: no light warning text in refs — measured light amber #E09F00 is a chart dot, annexed]; dark [measured: amber low dots + out-of-range line segment 106295] |
| onWarning | #FFFFFF | #5F4200 | same\|#5F4200 | light [reuse v3]; dark [derived: v3 container tone — 5.43 on #FFBA2B] |
| warningContainer | #FFDEA9 | #5F4200 | same | [reuse v3: no warning containers in refs] |
| onWarningContainer | #614206 | #FFBA2B | #614206\|#FDDEA4 | light [reuse v3]; dark [measured = warning, 5.43] |
| info | #0B57D0 | #A8C7FA | same | = primary [measured: coach sparkle 106287 light #0B57D0 / 106293 dark #A8C7FA] |
| onInfo | #FFFFFF | #062E6F | same | [measured: FAB content] |
| infoContainer | #C2E7FF | #004A77 | #D3E3FD\|#0842A0 | [measured: the refs' only blue containers — Log/Start/Dismiss pills 106287/106299 light, 106293 dark] |
| onInfoContainer | #004A77 | #C2E7FF | #0842A0\|#D3E3FD | [measured: pill contents] |
| streak | #614206 | #FFBA2B | #614206\|#FDDEA4 | = warning (v3 single-warm-channel rule kept; refs show no amber chip to split them) |
| onStreak | #FFFFFF | #5F4200 | same\|#5F4200 | as onWarning |
| streakContainer | #FFDEA9 | #5F4200 | same | [reuse v3] |
| onStreakContainer | #614206 | #FFBA2B | #614206\|#FDDEA4 | as onWarningContainer |
| celebration | #E09F00 | #FFBA2B | #FDDEA4 both | [measured: amber highlight block/dots 106290] \| [measured: 106295] — decorative, no text floor |
| progressTrack | #E9EEF6 | #333537 | #EAEFF5\|#282A2C | light [derived: AA-fix — measured track #DDE3EA vs measured arc #437EF8 is 2.91 < 3.0; one GM-ladder step lighter, neutral family kept (= sCHigh)]; dark [derived: the measured dark gray-container tone #333537 (no dark ring exists in refs)] |
| progressIndicator | #437EF8 | #90BAFF | #4285F4\|#8AB4F8 | [measured: ring arc + 559-of-628 + heart icon + badge border 106287/106289] \| [measured: heart icon 106293 — the dark twin of the same glyph] |
| habitComplete | #00948A | #43DCD0 | #009688\|#41DDD0 | [measured: active week dots + weekly-cardio chart line 106291/106290] \| [measured: cardio trend line/dots + goal-met energy bars 106294] |
| habitMissed | #5C5F5E | #A9ACAA | same | [reuse v3: forgiveness states have no ref imagery] |
| habitSkipped | #747775 | #8E918F | same | [reuse v3] |
| habitPending | #1F1F1F | #E3E3E3 | #1A1C1E\|#E3E3E3 | = textPrimary [measured] |
| heatmapLevel0 | #DDE3EA | #333537 | #EAEFF5\|#282A2C | light [measured: empty placeholder chips/F-circle 106289/106290]; dark [derived: measured gray-container role] |
| heatmapLevel1 | #CBE7EA | #003733 | #CCEAEC\|#003732 | [measured: activity icon circle 106291] \| [measured: pin glyph 106293] |
| heatmapLevel2 | #41D5CA | #007B73 | #41DDD0\|#00514B | [measured: Steps/Distance chip fill 106287/106289] \| [measured: dark Distance chip fill 106293] |
| heatmapLevel3 | #00948A | #00867D | #009688\|#007B73 | [measured: fitness chart teal 106290/106291] \| [measured: completed-day energy bars 106294] |
| heatmapLevel4 | #006A64 | #43DCD0 | #016B5B\|#41DDD0 | [measured: icon flower/glyph dark teal 106287/106291] \| [measured: trend line 106294] — entire ramp measured except dark L0 |
| heroSurface | #131314 | #131314 | same | [measured: dark coach panel 106293; light-mode hero composition unchanged from v3] |
| onHeroSurface | #E3E3E3 | #E3E3E3 | same | [measured] (14.47) |
| heroAccent | #A8C7FA | #A8C7FA | same | [measured: FAB 106293] (10.80 on hero) |
| auraAmber | 0x2EE09F00 | 0x1FFFBA2B | 0x2EFDDEA4\|0x1FFDDEA4 | [derived: v3 alphas over measured ambers] |
| auraMint | 0x3341D5CA | 0x2943DCD0 | 0x3341DDD0\|0x2941DDD0 | [derived: v3 alphas over measured teals] |
| elevatedCardOutline | Transparent | 0x1AFFFFFF | Transparent\|0x1AFAF9F8 | [derived: alpha base re-pointed — light surface is now #FFFFFF] |
| textPrimary | #1F1F1F | #E3E3E3 | #1A1C1E\|#E3E3E3 | [measured] (16.48 / 14.47) |
| textSecondary | #444746 | #C4C7C5 | same | [measured — incl. the coach card rule: timestamp, HEADLINE and body all measure textSecondary, 106287/106293] (9.39 / 10.90) |
| textTertiary | #5C5F5E | #A9ACAA | same | [reuse v3: no tertiary text in refs; the measured #ABAFB1 is the decorative inactive pager dot, annexed §4.3] |
| textPlaceholder | #5C5F5E | #A9ACAA | same | = textTertiary; vs new input fills (light sCLow #F8FAFD / dark sCHighest #333537): 6.17 / 5.38 ≥ 4.5 ✓ |
| textDisabled | 0x611F1F1F | 0x61E3E3E3 | 0x611A1C1E\|same | [derived: alpha base re-pointed to new light ink] |
| primaryHover | #0842A0 | #7CACF8 | same | [reuse v3] |
| destructiveHover | #8C1D18 | #EC928E | same | [reuse v3: the measured #E46962 is the HRV chart marker, annexed §4.1 — not a hover state] |
| disabledFill / disabledOutline | 0x1F1F1F1F | 0x1FE3E3E3 | 0x1F1A1C1E\|same | [derived: alpha re-point] |
| overlay | 0xCC1F1F1F | 0xCC0E0E0F | 0xCC1A1C1E\|same | [derived: alpha re-point] |
| surfaceLower | #F0F4F9 | #0E0E0F | #F0F4F9\|#0E0E0F | light [measured]; dark [reuse v3] |
| surfaceHigher | #FFFFFF | #1E1F20 | #FFFFFF\|#282A2C | [measured: white sheet/cards 106287/106290] \| [measured: metric cards on #131314 canvas, 24 spots 106294/106295] |
| surfaceOutline | 0x141F1F1F | 0x1AFFFFFF | 0x141A1C1E\|0x1AFAF9F8 | [derived: alpha re-points] |
| secondaryFill | #F0F4F9 | #333537 | #F0F4F9\|#343535 | [measured: tinted cards/Log-activity pill 106290/106291] \| [measured: Bpm/empty chips + edit circle 106293] |

### 2.1 habitAccents — 8 quads × 2 modes (accent / onAccent / container / onContainer)

The 8 slots re-point to the 8 hue families the PNGs actually contain: **blue, teal, green, purple, peach, amber, cyan, periwinkle**. Two slot identities change vs v3: slot 5 **pink → peach** (the Readiness chip family; zero pink exists in the v4 refs) and slot 8 **indigo → periwinkle** (the measured data-blue family; zero indigo exists). `habitAccents.size == 8` unchanged. All 32 pairs ≥ 4.5 (§5).

Light:
| # | Family | accent | onAccent | container | onContainer | Provenance |
|---|---|---|---|---|---|---|
| 1 | blue | #0B57D0 | #FFFFFF | #C2E7FF | #004A77 | all [measured: FAB/links + pills 106287/106299] |
| 2 | teal | #006A64 | #FFFFFF | #41D5CA | #00504B | all [measured: Steps/Distance chip — flower icon, white glyph, fill, label text 106287/106289] |
| 3 | green | #146C2E | #FFFFFF | #BEEFBB | #00522C | accent [derived: AA-fix of measured #009840]; container pair [measured: 'In range' chip 106290] |
| 4 | purple | #5A2F90 | #FFFFFF | #D4B2FF | #5A2F90 | accent/onContainer [measured: sleep moon/value text 106287]; container [measured: Sleep chip filled portion 106287, 5.17 with its own measured ink] |
| 5 | peach | #812800 | #FFFFFF | #FFDBCD | #812800 | all [measured: Readiness chip — figure/label ink + fill 106287]; capsule extras #FF9B72/#FFEDE6 annexed §4.2 |
| 6 | amber | #96660A | #FFFFFF | #FFDEA9 | #614206 | accent [derived: AA-fix of measured chart amber #E09F00 (2.30 w/ white); v3 tone-40 kept]; container pair [reuse v3 — no amber chip in refs] |
| 7 | cyan | #00639B | #FFFFFF | #BDE9FF | #004D68 | accent [measured: nav active label]; container pair [measured: 'Light' sleep-stage chip 106290] |
| 8 | periwinkle | #437EF8 | #001944 | #90BAFF | #001944 | all [measured: ring arc / +83 badge border, fill and text 106287] — onAccent is the badge navy (4.56), white fails on #437EF8 (3.76) |

Dark:
| # | Family | accent | onAccent | container | onContainer | Provenance |
|---|---|---|---|---|---|---|
| 1 | blue | #A8C7FA | #062E6F | #004A77 | #C2E7FF | all [measured: FAB + pills 106293] |
| 2 | teal | #90F4EA | #003733 | #00716A | #90F4EA | accent/onAccent [measured: Distance chip text + pin glyph 106293]; container [derived: AA-fix of measured chip fill #007B73 — its own measured text pair is 4.00 < 4.5; hue held, tone −8%, 4.57] |
| 3 | green | #54D071 | #0A3818 | #25352A | #54D071 | accent + container pair [measured: chart lines/dots + in-range bands 106294/106295]; onAccent [derived: v3 tone] |
| 4 | purple | #CEA8FF | #421378 | #5A2F90 | #EDDCFF | accent [measured: hypnogram end dots 106295]; onAccent [measured: icon circle purple 106293]; container pair [measured: Resilience/Mindfulness chip fill + content 106293] |
| 5 | peach | #FF9B72 | #812800 | #812800 | #FFDBCD | accent [measured: capsule ring 106287 — the only vivid peach measured; no dark peach exists]; rest [derived: light quad inverted — 4.55 / 7.27] |
| 6 | amber | #FFBA2B | #5F4200 | #5F4200 | #FFBA2B | accent/onContainer [measured: 106295 low dots]; rest [derived: v3 deep-amber primitive, 5.43] |
| 7 | cyan | #7FCFFF | #003355 | #004D68 | #BDE9FF | accent [measured: nav active label ×3 dark]; onAccent [reuse v3]; container [derived: mirror of measured light onContainer]; onContainer [measured family] |
| 8 | periwinkle | #90BAFF | #001944 | #0842A0 | #D3E3FD | accent [measured: heart icon 106293]; onAccent [measured family, 8.71]; container pair [reuse v3 GM P30/P90 — no dark periwinkle container in refs] |

---

## 3. SectionColors — 3 worlds × 2 modes

Architecture unchanged: 14 fields. **Two construction invariants change** (§3.1): `navIndicator`/`onNavIndicator`/`navSelectedText` are no longer aliases of the accent family — they are global, measured constants. `sheetDragHandle` = `onSheetSurfaceVariant @ 40%` stays.

### 3.1 Decision v4-D1 — the nav chrome goes global (reverses v2.1 amendment A2)

v2.1 A2 gave every tab its own selected-pill color on user request. The user's v4 directive — **exact color fidelity to the reference screenshots** — wins over that older preference: the refs show one constant nav system on every tab in both modes (pill `#C2E7FF`/`#004A77`, icon `#004A77`/`#C2E7FF`, label `#00639B`/`#7FCFFF` — §0.3). The three nav fields keep existing per-world (no structural change); their values converge across worlds and equal the M3 `secondary` family exactly: `navIndicator == secondaryContainer`, `onNavIndicator == onSecondaryContainer`, `navSelectedText == secondary`. ContrastTest invariants updated accordingly (§7). The nav bar container = `surfaceContainer` (#F0F4F9 / #1E1F20) — both measured on all six screens.

### 3.2 Tracking ← Today

| Field | Light | Provenance | Dark | Provenance |
|---|---|---|---|---|
| accent | #0B57D0 | [measured: FAB/links/badge 106287/106299] | #A8C7FA | [measured: FAB/links 106293] |
| onAccent | #FFFFFF | [measured: FAB content] | #062E6F | [measured: FAB content] |
| accentContainer | #C2E7FF | [measured: Log/Start/Dismiss pills] | #004A77 | [measured: Log/Start pills 106293] |
| onAccentContainer | #004A77 | [measured: pill content] | #C2E7FF | [measured] |
| surfaceTintWash | #F0F4F9 | [measured: Today canvas 106287 — v3's borrowed Health-blue #EAF1FB is retired; the real Today canvas is neutral] | #1E1F20 | [measured: Today dark canvas 106293] |
| sheetSurface | #FFFFFF | [measured: Today white sheet 106287] | #131314 | [measured: coach panel 106293 — the Today inversion, panel darker than canvas] |
| onSheetSurface | #1F1F1F | [measured] | #E3E3E3 | [measured] |
| onSheetSurfaceVariant | #444746 | [measured — the blue-cast #44474F is retired; secondary text measures plain #444746 everywhere] | #C4C7C5 | [measured] |
| sheetIconChip | #C2E7FF | [measured: = pill family] | #004A77 | [measured] |
| onSheetIconChip | #004A77 | [measured] | #C2E7FF | [measured] |
| navIndicator / onNavIndicator | #C2E7FF / #004A77 | [measured: global, §3.1] | #004A77 / #C2E7FF | [measured: global] |
| navSelectedText | #00639B | [measured: global] | #7FCFFF | [measured: global] |
| sheetDragHandle | #444746 @40% | [derived: rule] | #C4C7C5 @40% | [derived: rule] |

### 3.3 Progress ← Fitness

| Field | Light | Provenance | Dark | Provenance |
|---|---|---|---|---|
| accent | #006A64 | [measured: activity icon glyph 106291, steps flower 106287] | #90F4EA | [measured: Distance chip label/value 106293] |
| onAccent | #FFFFFF | [measured: shoe/pin glyphs in flowers] | #003733 | [measured: pin glyph 106293] |
| accentContainer | #41D5CA | [measured: Steps/Distance stat-chip fill] | #00716A | [derived: AA-fix of measured #007B73 (4.00 vs own text); tone −8%, 4.57] |
| onAccentContainer | #00504B | [measured: on-teal chip text] | #90F4EA | [measured] |
| surfaceTintWash | #FFFFFF | [measured: Fitness content canvas 106291] | #131314 | [measured: Fitness canvas 106294] |
| sheetSurface | #F0F4F9 | [derived: the measured tinted-card-on-white relationship 106291 applied to the sheet plane] | #1E1F20 | [measured: card-on-canvas relationship 106294] |
| onSheetSurface | #1F1F1F | [measured] | #E3E3E3 | [measured] |
| onSheetSurfaceVariant | #444746 | [measured — teal-cast #3F4946 retired] | #C4C7C5 | [measured] |
| sheetIconChip | #CBE7EA | [measured: activity icon circle 106291 — the literal icon-chip pattern] | #5DF0E3 | [measured: check-badge/Distance flower 106293/106294] |
| onSheetIconChip | #006A64 | [measured: icon glyph] | #003733 | [measured: glyph in flower] |
| navIndicator / onNavIndicator / navSelectedText | #C2E7FF / #004A77 / #00639B | [measured: global] | #004A77 / #C2E7FF / #7FCFFF | [measured: global] |
| sheetDragHandle | #444746 @40% | [derived] | #C4C7C5 @40% | [derived] |

### 3.4 Settings ← Sleep

| Field | Light | Provenance | Dark | Provenance |
|---|---|---|---|---|
| accent | #5A2F90 | [measured: sleep moon/value/sub-label ink 106287, Resilience text 106289 — replaces v3 #5A2E8E] | #CEA8FF | [measured: hypnogram end dots 106295 — replaces v3 token-only #D0BCFF] |
| onAccent | #FFFFFF | [derived: tone pairing, 9.36] | #421378 | [measured: dark icon-circle purple 106293] |
| accentContainer | #EDDCFF | [measured: Resilience/Mindfulness chip fill + Sleep chip tail 106287/106289 — PNG-exact; v3's baseline #EADDFF retired] | #5A2F90 | [measured: dark Resilience/Mindfulness chip fill 106293] |
| onAccentContainer | #5A2F90 | [measured] | #EDDCFF | [measured: dark chip glyph/label/value 106293] |
| surfaceTintWash | #FFFFFF | [derived: neutral-canvas policy — every measured light canvas is neutral (#FFFFFF or #F0F4F9); v3's lilac #F0F0FA retired; dark Sleep confirms the Sleep layout follows Fitness] | #131314 | [measured: Sleep canvas 106295] |
| sheetSurface | #F0F4F9 | [derived: tinted-card-on-white, as Progress] | #1E1F20 | [measured: Sleep metric cards 106295] |
| onSheetSurface | #1F1F1F | [measured] | #E3E3E3 | [measured] |
| onSheetSurfaceVariant | #444746 | [measured — baseline-purple #49454F retired] | #C4C7C5 | [measured] |
| sheetIconChip | #F8EDFF | [measured: Sleep icon-capsule fill 106287] | #421378 | [measured: Resilience/Mindfulness icon circle 106293] |
| onSheetIconChip | #5A2F90 | [measured: moon glyph] | #EDDCFF | [measured: head glyph] |
| navIndicator / onNavIndicator / navSelectedText | #C2E7FF / #004A77 / #00639B | [measured: global] | #004A77 / #C2E7FF / #7FCFFF | [measured: global] |
| sheetDragHandle | #444746 @40% | [derived] | #C4C7C5 @40% | [derived] |

### 3.5 Light-canvas policy (v4's analog of v3 §4.4)

The v4 refs eliminate v3's tinted light washes: **no tinted canvas exists anywhere** — light worlds are `#F0F4F9` (Today) or `#FFFFFF` (Fitness/Health), dark worlds are `#1E1F20` (Today) or `#131314` (Fitness/Sleep). World identity is carried entirely by the vivid chips, icon circles and accents — in BOTH modes. All v3 hue-tinted washes/sheets (#EAF1FB/#E0E9F8, #E5F4F7/#D9EDF0, #F0F0FA/#E7E3F8 and the cast variants) are retired. If tinted-canvas imagery ever surfaces, only the 12 wash/sheet values change.

### 3.6 The vivid stat-chip anatomy (the pattern the quads encode)

Measured chip anatomy, light: **fill** = quad `container` (saturated), **label/value text** = quad `onContainer` (same-hue deep ink), **icon capsule** = section `sheetIconChip`-class pale tone, **icon glyph** = quad `accent` (or `onSheetIconChip`). Dark inverts: fill = deep container, text = pale `onContainer`, icon circle = deeper tone, glyph = pale. The neutral/no-data chip ("color is earned by data"): fill `secondaryFill` (#DDE3EA/#333537), capsule `surfaceContainerLow` (#F8FAFD/#1B1B1B), text `textSecondary`, data glyph `progressIndicator` (the measured Bpm chip, 106289/106293). Empty placeholder chips: `secondaryFill`, no content.

---

## 4. Component & data-viz annex (measured values that are deliberately NOT tokens)

### 4.1 Chart semantic colors (component-level constants, `YoloChartColors` candidate)

| Semantic | Light | Dark | Source |
|---|---|---|---|
| Negative/red marker | #DC362E | #E46962 | [measured: HR red dots 106290 / HRV low marker 106294] |
| Caution/amber marker | #E09F00 | #FFBA2B | [measured: 106290 dots+block / 106295 dots+line] |
| Positive/green line+dots | #009840 | #54D071 | [measured: 106290 / 106294-106295] (tokens: success channel) |
| Sleep/blue line+dots | #008FB7 | — (none measured; [derived: use secondary #7FCFFF]) | [measured: 106290 HR bottom row] |
| Cardio teal line+dots | #00948A | #43DCD0 | [measured: 106290/106294] (= habitComplete) |
| In-range band | #D3E8E3–#D3E9E3 (green), #D3E7F1 (blue), #EEE9DB (beige), #EEDDE1 (pink strip) | #25352A | [measured: 106290 / 106294-106295] |
| Goal/dotted line | #43DCD0 dots, hairline renders ~#38A39C over card | — | [measured: 106294] |
| Dot treatment | white ring #FFFFFF around light-mode dots | #131314 gap-ring around selected dot | [measured: 106290/106294] |
| Hypnogram | — | bar #413B4D (translucent purple), end dots #CEA8FF | [measured: 106295] |

### 4.2 Delta badge ("+83") and chip capsule extras

- Progress delta badge: fill `#90BAFF`, 2dp border `#437EF8`, text `#001944`, full-pill — exactly the periwinkle quad (container/accent/onAccent). [measured: 106287]
- Readiness capsule: outer ring `#FF9B72`, inner `#FFEDE6` [measured: 106287] — peach-quad component constants (dark accent reuses #FF9B72).

### 4.3 Small chrome

| Element | Light | Dark | Disposition |
|---|---|---|---|
| Pager dot active / inactive (in-content pagers) | #444746 / #ABAFB1 | #C4C7C5 / #606262 | [measured: 106287/106289 / 106293] — component constants; decorative, exempt |
| Onboarding pager active / inactive | #0B57D0 / @40% over canvas | #A8C7FA / @40% | [measured: 106300/106296 — inactive equals active at 40% alpha, verified] |
| Gesture bar | #606264 (#666666 onboarding) | #EDEDED (#ECECED onboarding) | system chrome — not themed by us |
| Checklist illustration tiles | #EDD7C5 / #76C7EC / #D6DDEF | — | [measured: 106299] illustration asset colors, not tokens |
| "25%" streak-style chip (dark) | — | fill #363738, icon #F2B8B5 | [measured: 106293] neutral chip + E80 icon; renders as secondaryFill-class chip — #363738 unified to #333537 (1–2 units, same tone) |
| Coach/insight text rule | timestamp, headline AND body = textSecondary; sparkle = primary | same (#C4C7C5 / #A8C7FA) | [measured: 106287/106293] |
| Stat-progress text ("559 of 628") | ref ships #437EF8 on #F0F4F9 = 3.41 (sub-AA) | — | our rule: periwinkle stat text only at large-text sizes (≥18sp / 14sp-bold, 3:1); body-size stat text uses primary #0B57D0 (5.78) |
| habitComplete on gray chips | #00948A vs #DDE3EA = 2.90 | — | guidance: never place habitComplete glyphs on secondaryFill/sCHighest; use white or #F0F4F9 backing (3.75 / 3.39) |

---

## 5. WCAG verification annex — computed, not eyeballed

Method: python3 (`/tmp/v4check.py`), WCAG 2.x relative luminance (sRGB linearization, 0.03928 threshold, 2.4 exponent). Floors: 4.5 text-on-fill, 3.0 large-text/UI-graphics. **234 pairs, 0 failures.** Full run:

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
PASS  16.48 (4.5)  onSurface/surface            #1F1F1F on #FFFFFF
PASS   9.39 (4.5)  onSurfaceVariant/surface     #444746 on #FFFFFF
PASS   7.27 (4.5)  onSurfaceVariant/surfaceVariant  #444746 on #DDE3EA
PASS  11.79 (4.5)  inverseOnSurface/inverseSurface  #F2F2F2 on #303030
PASS  onSurface vs containers: 16.48 #FFFFFF · 15.76 #F8FAFD · 14.92 #F0F4F9 · 14.15 #E9EEF6 · 12.75 #DDE3EA
PASS  onSurfaceVariant vs containers: 9.39 · 8.98 · 8.51 · 8.06 · 7.27
PASS   4.53 (3.0)  outline/surface              #747775 on #FFFFFF
PASS   6.39 (3.0)  primary/surface              #0B57D0 on #FFFFFF

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
PASS  onSurface vs containers: 15.03 #0E0E0F · 13.42 #1B1B1B · 12.86 #1E1F20 · 11.22 #282A2C · 9.60 #333537
PASS  onSurfaceVariant vs containers: 11.32 · 10.11 · 9.69 · 8.45 · 7.23
PASS   5.83 (3.0)  outline/surface              #8E918F on #131314
PASS  10.80 (3.0)  primary/surface              #A8C7FA on #131314

== ext-L ==
PASS   6.53 onSuccess/success · 7.25 onSuccessContainer/successContainer (#00522C on #BEEFBB)
PASS   9.17 onWarning/warning · 7.11 onWarningContainer/warningContainer
PASS   6.39 onInfo/info · 7.20 onInfoContainer/infoContainer (#004A77 on #C2E7FF)
PASS   9.17 onStreak/streak · 7.11 onStreakContainer/streakContainer
PASS  16.48 textPrimary · 9.39 textSecondary · 6.45 textTertiary (vs surface, 4.5)
PASS   6.17 (4.5)  textPlaceholder/inputFill    #5C5F5E on #F8FAFD
PASS  habitComplete #00948A: 3.75 surface / 3.39 surfaceLower (3.0)
PASS  habitMissed #5C5F5E: 6.45 / 5.84 · habitSkipped #747775: 4.53 / 4.10 · habitPending 16.48
PASS   3.23 (3.0)  progressIndicator/progressTrack  #437EF8 on #E9EEF6
PASS   3.41 (3.0)  progressIndicator/ringInnerDisc  #437EF8 on #F0F4F9
PASS  10.80 heroAccent/heroSurface · 14.47 onHeroSurface/heroSurface

== ext-D ==
PASS   6.68 onSuccess/success (#0A3818 on #54D071) · 6.56 onSuccessContainer/successContainer (#54D071 on #25352A)
PASS   5.43 onWarning/warning (#5F4200 on #FFBA2B) · 5.43 onWarningContainer/warningContainer
PASS   7.50 onInfo/info · 7.20 onInfoContainer/infoContainer (#C2E7FF on #004A77)
PASS   5.43 onStreak/streak · 5.43 onStreakContainer/streakContainer
PASS  14.47 textPrimary · 10.90 textSecondary · 8.11 textTertiary (vs surface, 4.5)
PASS   5.38 (4.5)  textPlaceholder/inputFill    #A9ACAA on #333537
PASS  habitComplete #43DCD0: 10.95 surface / 9.74 card (3.0)
PASS  habitMissed #A9ACAA: 8.11 / 7.21 · habitSkipped #8E918F: 5.83 / 5.19 · habitPending 14.47
PASS   6.25 (3.0)  progressIndicator/progressTrack  #90BAFF on #333537
PASS  10.80 heroAccent/heroSurface · 14.47 onHeroSurface/heroSurface

== sections (floors: text 4.5; chip-icon/accent-on-surface/streak-on-sheet/pending-on-wash 3.0; error 4.5) ==
Tracking-L : onAccent 6.39 · onAccentContainer 7.20 · onSheetIconChip 7.20 · onSheet/sheet 16.48 · variant/sheet 9.39 · onSheet/wash 14.92 · accent/wash 5.78 · accent/sheet 6.39 · accent/surface 6.39 · onNavIndicator 7.20 · navSelectedText/navBar 5.84 · error/sheet 6.54 · error/wash 5.92 · streak/sheet 9.17 · pending/wash 14.92
Tracking-D : onAccent 7.50 · onAccentContainer 7.20 · onSheetIconChip 7.20 · onSheet/sheet 14.47 · variant/sheet 10.90 · onSheet/wash 12.86 · accent/wash 9.60 · accent/sheet 10.80 · accent/surface 10.80 · onNavIndicator 7.20 · navSelectedText/navBar 9.65 · error/sheet 10.87 · error/wash 9.67 · streak/sheet 10.89 · pending/wash 12.86
Progress-L : onAccent 6.47 · onAccentContainer 5.16 · onSheetIconChip 4.98 · onSheet/sheet 14.92 · variant/sheet 8.51 · onSheet/wash 16.48 · accent/wash 6.47 · accent/sheet 5.86 · accent/surface 6.47 · onNavIndicator 7.20 · navSelectedText/navBar 5.84 · error/sheet 5.92 · error/wash 6.54 · streak/sheet 8.30 · pending/wash 16.48
Progress-D : onAccent 10.23 · onAccentContainer 4.57 · onSheetIconChip 9.43 · onSheet/sheet 12.86 · variant/sheet 9.69 · onSheet/wash 14.47 · accent/wash 14.43 · accent/sheet 12.83 · accent/surface 14.43 · onNavIndicator 7.20 · navSelectedText/navBar 9.65 · error/sheet 9.67 · error/wash 10.87 · streak/sheet 9.68 · pending/wash 14.47
Settings-L : onAccent 9.36 · onAccentContainer 7.26 · onSheetIconChip 8.27 · onSheet/sheet 14.92 · variant/sheet 8.51 · onSheet/wash 16.48 · accent/wash 9.36 · accent/sheet 8.47 · accent/surface 9.36 · onNavIndicator 7.20 · navSelectedText/navBar 5.84 · error/sheet 5.92 · error/wash 6.54 · streak/sheet 8.30 · pending/wash 16.48
Settings-D : onAccent 6.69 · onAccentContainer 7.26 · onSheetIconChip 10.20 · onSheet/sheet 12.86 · variant/sheet 9.69 · onSheet/wash 14.47 · accent/wash 9.43 · accent/sheet 8.39 · accent/surface 9.43 · onNavIndicator 7.20 · navSelectedText/navBar 9.65 · error/sheet 9.67 · error/wash 10.87 · streak/sheet 9.68 · pending/wash 14.47

== habitAccents (all 4.5 floors) ==
Light  accent/onAccent : blue 6.39 · teal 6.47 · green 6.53 · purple 9.36 · peach 9.39 · amber 5.00 · cyan 6.45 · periwinkle 4.56
Light  container pairs : blue 7.20 · teal 5.16 · green 7.25 · purple 5.17 · peach 7.27 · amber 7.11 · cyan 7.19 · periwinkle 8.71
Dark   accent/onAccent : blue 7.50 · teal 10.23 · green 6.68 · purple 6.69 · peach 4.55 · amber 5.43 · cyan 7.65 · periwinkle 8.71
Dark   container pairs : blue 7.20 · teal 4.57 · green 6.56 · purple 7.26 · peach 7.27 · amber 5.43 · cyan 7.19 · periwinkle 7.04

== onboarding ==
L: title 16.48 · subtitle 9.39 · pillText/pill 6.39 · link 6.39 · dotActive 6.39
D: title 14.47 · subtitle 10.90 · pillText/pill 7.50 · link 10.80 · dotActive 10.80

== component (measured pairs we also gate) ==
PASS 5.17 sleepValueText/filledChip #5A2F90 on #D4B2FF · 7.19 'Light' chip #004D68 on #BDE9FF
PASS 8.71 badgeText/badgeFill #001944 on #90BAFF · 4.56 badgeText/badgeBorder #001944 on #437EF8
PASS 12.75 editPencil #1F1F1F on #DDE3EA · 7.27 BpmLabel-L #444746 on #DDE3EA · 7.23 BpmLabel-D #C4C7C5 on #333537
PASS 7.20 navActiveIcon/pill (both modes)

TOTAL PAIRS: 234   FAILURES: 0

== informational: PNG-exact reference pairs that FAIL AA -> why each [derived: AA-fix] exists ==
 4.00 (4.5)  dark Distance-chip text on its own fill   #90F4EA on #007B73  -> container #00716A
 2.91 (3.0)  light ring arc vs gray ring track         #437EF8 on #DDE3EA  -> progressTrack #E9EEF6
 3.41 (4.5)  559-of-628 stat text on canvas            #437EF8 on #F0F4F9  -> large-text-only rule (S4.3)
 3.77 (4.5)  chart green w/ white                      #009840 on #FFFFFF  -> accent #146C2E
 2.30 (4.5)  chart amber w/ white                      #E09F00 on #FFFFFF  -> accent #96660A
 3.74 (4.5)  chart blue w/ white                       #008FB7 on #FFFFFF  -> data-viz only
 2.90 (3.0)  fitness teal vs gray container            #00948A on #DDE3EA  -> placement guidance (S4.3)
```

---

## 6. Onboarding screen spec (replaces the current Welcome layout)

Source: pixel-identical dark frames 106296/106298 + light 106300; layout identical across modes; dp = px / 2.625 (reference 412×923.4dp). Typeface: Google Sans Flex, existing slots only — no `Type.kt` change.

### 6.1 Structure, top → bottom

| # | Element | Spec | Provenance |
|---|---|---|---|
| 1 | **Hero photo** | Full-bleed, edge-to-edge behind status bar, `ContentScale.Crop` aligned `TopCenter`, drawn region height 505dp. Fully opaque y 0→229dp. | [measured: opaque to y600px, fade ends y1325px] |
| 2 | **Gradient fade** | Vertical scrim of `colorScheme.surface` (light #FFFFFF, dark #131314), **linear** alpha 0.00 at 230dp → 1.00 at 505dp (24.8% → 54.7% of screen height; slope ≈ 0.357 alpha/100dp). Below 505dp the canvas is solid surface. Implementation: `Brush.verticalGradient(230.dp→505.dp, surface.copy(alpha=0f) → surface)` over the image; no other stops — the measured ramp is clean linear. | [measured: per-pixel dark/light differencing — alpha 0.000@600px, 0.345@850, 0.754@1150, 1.000@~1327px] |
| 3 | **Icon disc** | 88dp circle, fill **#FFFFFF in BOTH themes** [measured: 106296/106300]; horizontally centered; center-y 512dp (55.4% height — straddles the fade terminus exactly as the ref). Content: our loop-h brand mark, 44dp, color #0B57D0 [derived: mark size unmeasured, ~50% of disc; constant brand-blue on the constant white disc]. | [measured: bbox 425-654px, d=230px=87.6dp, center-y 1344px] |
| 4 | **App name** "MyHabitsHub" | `displaySmall` (GSF 500 · 36/44 · 0) — measured cap height 26.3dp → ~37sp ≈ M3 displaySmall. Color `onSurface` (#1F1F1F / #E3E3E3). Centered. 40dp below disc bottom [measured 41.5dp]. | [measured: cap 1567-1636px; colors exact] |
| 5 | **Subtitle** (per-page value-prop line) | `bodyLarge` 16sp, `textSecondary` (#444746 / #C4C7C5), centered, 24dp horizontal padding, 16dp below title [measured visual gap 16.4dp]. | [measured: cap ~30px → ~16sp] |
| 6 | **Pager** | The ref shows a 4-dot pager; we ship a **3-page value-prop pager** (existing `HorizontalPager` + dot-row components). Paged content = title #4 stays fixed; subtitle #5 and photo crop-focus swap per page (photo asset constant, copy varies). Dots: 8dp circles, active = 16×8dp pill, 4dp gaps, group centered; row center-y 64dp below subtitle baseline [measured 62.9dp], 32dp above the button [measured 32.4dp]. Colors: active `primary` (#0B57D0 / #A8C7FA), inactive `primary @ 40% alpha` [measured: #9DBCEC light / #4F5B70 dark = exactly active@40% over canvas]. | [measured: d=21px=8dp, pill 42×21px, gap 12px] |
| 7 | **Primary pill — "Continue with Google"** (our Google SSO draft button) | Height 56dp, width = screen − 2×16dp margins, full-pill r=28dp (ref chord-verified perfect circle). Fill `primary`, label `onPrimary`: light **#0B57D0 / #FFFFFF**, dark **#A8C7FA / #062E6F** [measured]. Label `titleMedium` 16sp GSF 500 [measured cap ~31px ≈ 16sp]. Action: existing SSO draft wiring (Google auth stub) — unchanged behavior, new skin. | [measured: y1999-2145px, h=147px=56dp, margins 42px=16dp, r=73.5px=28dp] |
| 8 | **Secondary text-link — "Continue with email"** | Plain text link (the ref's "Sign in with Fitbit" treatment): 16sp GSF 500, color `primary` (#0B57D0 / #A8C7FA), no container, centered, ≥48dp touch target. 32dp below the pill [measured 32dp]. Routes to the existing email sign-in/sign-up flow (incl. forgot-password). | [measured: cap 2229-2259px, centered x540] |
| 9 | **Bottom anchor** | Link baseline 63dp above screen bottom [measured 62.9dp]: implement as 48dp bottom padding on the link box + `navigationBarsPadding()`. | [measured] |

Vertical chain (dp, top→bottom): photo 0–229 opaque → fade 230–505 → disc center 512 → 40 → title → 16 → subtitle → 64 → dots → 32 → pill(56) → 32 → link → 48+insets.

Theming: rendered under base `YoloTheme` (default Progress world is irrelevant here — every color is scheme-level `primary`/`onSurface`/`textSecondary`). All five text pairs verified (§5 onboarding block).

### 6.2 Photo asset — [asset: to fetch]

Requirements: **free commercial license** (Unsplash License or Pexels License; keep the license URL + photographer credit in `docs/licenses/`); subject = habit/lifestyle ritual (morning run at dawn, journaling with coffee, lacing running shoes, sunrise stretch); portrait, **≥1080×1600px**; key subject in the TOP 60% of frame (everything below 55% height disappears under the fade); tones that survive BOTH scrims — avoid pure-white skies at the fade zone (light mode) and avoid near-black bottom thirds reading as banding (dark mode; the ref photo darkens warmly into #131314); no visible brand logos; faces acceptable only if license explicitly covers commercial use. Ship as `feature/onboarding/presentation/src/commonMain/composeResources/drawable/onboarding_hero.jpg`, quality ~80, target ≤350KB.

---

## 7. ContrastTest impact (core/designsystem/src/commonTest/kotlin/com/yolo/core/designsystem/theme/ContrastTest.kt)

| Assertion block | Impact |
|---|---|
| 22 scheme pairs ×2 modes | No code change — all pass on v4 values (§5). |
| Extended pairs incl. habit states + placeholder/input-fill floors | No code change — all pass; light input fill is now #F8FAFD (6.17), dark #333537 (5.38). |
| `habitAccents.size == 8` | Unchanged — still 8 quads (slot identities pink→peach, indigo→periwinkle are value swaps, not structure). |
| **Section alias invariants** (`navIndicator == accentContainer`, `onNavIndicator == onAccentContainer`, `navSelectedText == accent`) | **MUST be replaced** (§3.1): new invariants — for every world×mode, `navIndicator == scheme.secondaryContainer`, `onNavIndicator == scheme.onSecondaryContainer`, `navSelectedText == scheme.secondary`, and all three are equal ACROSS worlds per mode. `sheetDragHandle == onSheetSurfaceVariant.copy(alpha=.40f)` unchanged. |
| `trackingWorldStaysBrandIdentical` | **Edit**: `TrackingLight.accent == LightColorScheme.primary` still holds; the `accentContainer == primaryContainer` clause becomes `accentContainer == secondaryContainer` (#C2E7FF / #004A77) — the measured pills are the secondary-container family. |
| Section 14-pair numeric block | No structural change; all 90 section pairs pass (§5). Note: section dark washes now DIVERGE (Tracking #1E1F20 vs others #131314) — no assertion checks wash equality, so no edit. |
| `progressIndicator/progressTrack ≥ 3.0` | Passes at 3.23 light (#437EF8 on #E9EEF6 — the AA-fixed track) / 6.25 dark. If anyone "fixes" progressTrack back to the measured #DDE3EA this gate fails at 2.91 — that is intentional. |
| `darkOutlineBannedOnElevatedContainers` | Passes — onSurfaceVariant vs new #333537 = 7.23. |
| New exemption-list entries | `habitComplete`-vs-`secondaryFill` adjacency (2.90, placement guidance §4.3); periwinkle stat-text large-only rule (§4.3); pager dots decorative. |

---

## 8. Implementation delta plan (exact file → values)

### 8.1 `core/designsystem/src/commonMain/kotlin/com/yolo/core/designsystem/tokens/YoloPrimitives.kt`

**Value swaps on existing v3 primitives** (same const, new ARGB):

| Const | v3 | v4 |
|---|---|---|
| Ink50 | #FAF9F8 | #FFFFFF (light surface is white; alias of Ink0) |
| Ink100 | #F5F7FA | #F8FAFD |
| Ink200 | #EAEFF5 | #E9EEF6 |
| Ink250 | #E4E9EF | #DDE3EA |
| Ink300 | #DADDE0 | #DDE3EA (alias of Ink250; retire at next cleanup) |
| Ink800 | #1A1C1E | #1F1F1F |
| Deep50 | #343535 | #333537 |
| Deep300 | #1A1A1C | #1B1B1B |
| Brand1000 | #003732 | #003733 |
| Brand900 | #00514B | #00504B |
| Brand800 | #016B5B | #006A64 |
| Brand700 | #08655F | #00716A (the AA-fixed dark container) |
| Brand600 | #009688 | #00948A |
| Brand500 | #41DDD0 | #41D5CA |
| Brand300 | #93F4EA | #90F4EA |
| Brand200 | #BBFCF4 | #5DF0E3 (tag chip is gone; the dark flower takes the slot) |
| Brand100 | #CCEAEC | #CBE7EA |
| BrandDeep | #05514A | retire (no consumer) |
| Violet700 | #5A2E8E | #5A2F90 |
| Violet300 | #D0BCFF | #CEA8FF |
| Violet100 | #EADDFF | #EDDCFF |
| Violet150 | #EADDFF | #F8EDFF |
| VioletDeep | #381E72 | #421378 |
| VioletDarkContainer | #5B2F90 | #5A2F90 |
| VioletContainerOn | #EFDBFF | #EDDCFF |
| Amber300 | #FDDEA4 | #FFBA2B |
| Amber100 | #FFDEA9 | unchanged |
| AmberContainerOn | #FDDEA4 | #FFBA2B |
| CobaltWashLight / CobaltSheetLight | #EAF1FB / #E0E9F8 | #F0F4F9 (=Ink150) / #FFFFFF — tinted-wash retirement §3.5 |
| CobaltWashDark | #131314 | #1E1F20 (the measured Today dark canvas) |
| CobaltVariantLight / VioletVariantLight / BrandVariantLight | #44474F / #49454F / #3F4946 | #444746 (cast variants retired — measured plain) |
| BrandWashLight / BrandSheetLight / BrandChipLight | #E5F4F7 / #D9EDF0 / #BBFCF4 | #FFFFFF / #F0F4F9 / #CBE7EA |
| VioletWashLight / VioletSheetLight | #F0F0FA / #E7E3F8 | #FFFFFF / #F0F4F9 |

Unchanged (confirmed by measurement): Ink0/Ink150/Ink350–Ink700/InkText, Deep100/200/400/500, Cobalt700/300/100/150/Deep/DarkContainer/ContainerOn (#0B57D0/#A8C7FA/#D3E3FD/#C2E7FF/#0842A0/#004A77/#C2E7FF), Cyan block (#00639B/#7FCFFF/#C2E7FF/#004A77/#003355), Red block, Amber800/700.

**New primitives (additions):**

```
// Periwinkle (measured data-blue family — replaces Google blue 500/300 in progress roles)
YoloPeriwinkle500 = #437EF8   YoloPeriwinkle300 = #90BAFF   YoloPeriwinkleInk = #001944
// Peach (Readiness chip family — habitAccent slot 5)
YoloPeach900 = #812800   YoloPeach100 = #FFDBCD   YoloPeach400 = #FF9B72   YoloPeach50 = #FFEDE6
// Green chart family (success channel dark + chip)
YoloGreen400 = #54D071   YoloGreenBandDark = #25352A   YoloGreenChipLight = #BEEFBB (replaces #BFEFBB)
// Cyan additions
YoloCyan150 = #BDE9FF   YoloCyan850 = #004D68
// Purple additions
YoloViolet200 = #D4B2FF
// Amber addition
YoloAmber500 = #E09F00 (light celebration / chart amber)
// Component-layer (annex S4 — optional consts, not theme tokens):
//   chart: #DC362E #E46962 #008FB7 #D3E8E3 #D3E7F1 #EEE9DB #EEDDE1 #413B4D #00867D
//   pager dots: #ABAFB1 #606262
```

### 8.2 `theme/Color.kt`
Mirror every §8.1 swap/addition as Compose `Color` vals. Re-point `YoloGreenContainerDark` #00522C → it remains the LIGHT on-success ink only (dark successContainer is now `YoloGreenBandDark` #25352A); rename when touched. `YoloSuccessContainerLight` #BFEFBB → #BEEFBB, `YoloOnSuccessContainerLight` #085226 → #00522C.

### 8.3 `theme/Theme.kt`
- LightColorScheme/DarkColorScheme: re-point per §1 — notably light background/surface → Ink0 #FFFFFF, light onSurface → Ink800 #1F1F1F, light surfaceVariant/surfaceDim/sCHighest → #DDE3EA, sCLow → #F8FAFD, sCHigh → #E9EEF6; dark sCLow → #1B1B1B, dark sCHighest/surfaceBright → #333537.
- ExtendedColors blocks: re-point per §2 (success*, celebration, infoContainer pair, warning/streak dark, progress*, habitComplete, heatmap*, surfaceHigher dark #1E1F20, secondaryFill dark #333537, aura*).
- **11 alpha-baked literals (silent-drift trap):** light textDisabled `0x611F1F1F` · disabledFill/disabledOutline `0x1F1F1F1F` · overlay `0xCC1F1F1F` · surfaceOutline `0x141F1F1F` · dark elevatedCardOutline `0x1AFFFFFF` · dark surfaceOutline `0x1AFFFFFF` (dark textDisabled/disabledFill/overlay keep their #E3E3E3/#0E0E0F bases — unchanged).
- LightHabitAccents/DarkHabitAccents: the 16 quads per §2.1.

### 8.4 `theme/SectionTheme.kt`
Re-point the 6 palette vals per §3. **Set the nav trio explicitly per §3.1** (no longer constructed from accentContainer): light `#C2E7FF`/`#004A77`/`#00639B`, dark `#004A77`/`#C2E7FF`/`#7FCFFF` in all three worlds. Tracking dark wash `#1E1F20` + sheet `#131314` (the inversion); other worlds wash `#131314` + sheet `#1E1F20`.

### 8.5 `ContrastTest.kt`
Per §7: replace the three nav alias invariants with the global-nav invariants; edit `trackingWorldStaysBrandIdentical`'s container clause; add the two new exemption-list comments. Run full suite — §5 predicts green (234/234).

### 8.6 NOT touched
`Type.kt` + font assets (typography frozen — user decision), `Shape.kt`, `Dimens.kt`, `Motion.kt`, `YoloTheme.kt` (default-world line stays), `YoloButton.kt` (v3 §9.8 fix stands). All section-world consumers re-skin via tokens for free.

### 8.7 Consumers with visible composition changes
- `shared .../components/bottomnav/BottomNavigationBar.kt`: per-item world coloring (v2.1 §5) is **removed** — all items use the scheme `secondary` trio (or equivalently any world's nav fields, now identical). Container stays `surfaceContainer` (#F0F4F9/#1E1F20 — both measured).
- `shared .../screens/main/MainScreen.kt`: the animated wash now interpolates neutrals only (#F0F4F9↔#FFFFFF light, #1E1F20↔#131314 dark); mechanism unchanged.
- `feature/habits .../tracking/HabitTrackingScreen.kt`: adopt the Today composition — canvas = wash #F0F4F9/#1E1F20, content sheet/cards per §3.2; chip rows adopt §3.6 anatomy via habitAccent quads.
- `feature/onboarding/presentation .../OnBoardingScreen.kt` (+ retire `OnBoardingScreenVariation1/2.kt`): rebuild per §6; add `onboarding_hero.jpg` [asset: to fetch] + license file.

Migration order: (1) primitives + Theme + SectionTheme + ContrastTest in one commit — CI green; (2) BottomNavigationBar/MainScreen chrome; (3) tracking/progress/settings screen compositions; (4) onboarding rebuild (independent — can land parallel after 1).

---

## 9. Honesty section

### 9.1 `[reuse v3]` inventory (refs show nothing relevant — the complete list)
Light: primaryContainer pair, onSecondary, tertiary containers, error ramp, outline/outlineVariant, inverse pair, scrim, warning/streak light family, habitMissed/Skipped, textTertiary/Placeholder, hovers, onSuccess. Dark: primaryContainer pair, onSecondary #003355, tertiary ramp, error ramp (except #F2B8B5 — now incidentally measured), sCLowest #0E0E0F, sCHigh #282A2C, outline pair, inverse pair, surfaceLower #0E0E0F, habitMissed/Skipped, textTertiary/Placeholder, hovers, onWarning/container tones #5F4200/#FFDEA9, cyan onAccent, periwinkle dark container pair, peach dark (no dark peach imagery). Settings light wash/sheet and Progress light sheet are [derived] from the measured neutral-canvas policy, not reuse.

### 9.2 The reference app ships sub-AA pairs in PNG (no noise excuse); we don't
Five PNG-exact failures and our fixes (all hue-preserving, §5 informational block): `#90F4EA` on `#007B73` (4.00) → container `#00716A`; ring arc `#437EF8` vs track `#DDE3EA` (2.91) → track `#E9EEF6`; `559 of 628` text `#437EF8` on `#F0F4F9` (3.41) → large-text-only rule; chart green `#009840` (3.77 w/ white) → `#146C2E`; chart amber `#E09F00` (2.30 w/ white) → `#96660A` as text accents — measured values stay verbatim in chart-graphics roles where 3:1 applies or no floor exists.

### 9.3 Measured but deliberately not tokens
Chart bands/lines (§4.1), +83 badge composition (§4.2), pager dots, gesture bars, checklist illustration tiles, hypnogram bar `#413B4D`, dark `#363738` chip (unified to #333537), `#24342A` band (unified to #25352A), `#008FB7` (light-only chart blue), `#E46962` (dark chart red). Onboarding inactive dots are alpha-derived, not stored colors.

### 9.4 Structural divergences, owned
Our per-world accents still diverge from the reference's global-blue links inside Fitness/Sleep screens (architecture is frozen; the reference itself does this on nav, which we now copy exactly). `streak`==`warning` convergence kept. M3 `tertiary` keeps the GM green ramp while `success` carries the measured chart green — stock components vs measured semantics. The 4-dot onboarding pager ships as 3 dots (we have 3 value-prop pages). Dark `sCHigh` #282A2C is the one dark neutral with no v4 evidence (nothing renders between #1E1F20 and #333537 in the refs).
