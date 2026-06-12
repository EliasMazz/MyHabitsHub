---
name: design-system
description: Enforce and auto-correct the Yolo design system (v3) in MyHabitsHub. Use whenever writing, editing, or reviewing Compose UI code in this repo (screens, components, themes, previews), when the user asks to check/fix design-system compliance, or before finishing any task that touched *.kt files under feature/*/presentation, shared/src, or core/designsystem. Detects hardcoded colors/text styles/spacing/shapes, wrong token usage, and theme-nesting violations; fixes them via the mapping tables; verifies with ContrastTest.
---

# Design System Enforcement — Yolo Design System v3

You are enforcing the design system documented in `docs/design/design-system-catalog.md`
(values: `docs/design/design-system-v3-spec.md`; architecture: `docs/design/theme-spec.md` §8 +
`docs/design/section-color-worlds.md` §3). Read the catalog if you need context beyond this file.

## When writing NEW UI code

Apply these defaults without being asked:

1. Screen background inside a tab → `MaterialTheme.colorScheme.section.surfaceTintWash`; outside tabs → `colorScheme.background`. Never wrap screens in `MaterialTheme`/`YoloTheme` (the shell provides it).
2. Cards → `extended.surfaceHigher` riding on the wash; text → `typography` slots; spacing → `YoloTokens.spacing.*`; shapes → `MaterialTheme.shapes.*`; CTAs → `YoloButton`; sheets → `AppModalBottomSheet` + `YoloSheetActionRow`; inputs → `YoloTextField*`.
3. Color precedence: section hue = chrome/surfaces (automatic), `extended.habitAccents[n]` = habit identity (explicit param), `extended.habitComplete` = done (always teal), `extended.streak`/`celebration` = streaks (always amber), `colorScheme.error` = destructive, `extended.habitMissed` = missed (muted gray — NEVER red).
4. Previews use `PreviewHelper { }` and `PreviewHelper(darkTheme = true) { }` — check both modes.
5. Touch targets ≥ `YoloTokens.sizing.minTouchTarget` (48dp).
6. Any NEW component added to `core/designsystem` must ship in the same change with a
   `CatalogEntry` demo in `core/catalog` (demos/ComponentDemos.kt or a new demos/ file) — the
   runnable catalog is the design system's living reference.
7. Never name the external app this design was studied from in any repo artifact (code,
   comments, docs, commit messages) — refer to "the reference redesign study" only.

## Violation scan (run on changed files; for a full audit run on all UI source)

Scope: `feature/*/presentation/src/**/*.kt`, `shared/src/commonMain/**/*.kt` (UI code). The design
system's own files (`core/designsystem/**/theme/*`, `**/tokens/*`) are EXEMPT from color-literal
rules but components under `core/designsystem/**/components/**` must use tokens too.

Run from the repo root (`cd "$(git rev-parse --show-toplevel)"` first). Paths are passed
explicitly to each grep — do NOT collapse them into one unquoted variable (zsh does not
word-split, which silently turns the scan into a no-op).

```bash
cd "$(git rev-parse --show-toplevel)"

# V1 — hardcoded color literals (no exceptions)
grep -rnE "Color\(0x[0-9A-Fa-f]{6,8}\)" feature shared/src --include="*.kt" | grep -v build/

# V2 — named color constants (Transparent/Unspecified reviewed separately in V3)
grep -rnE "Color\.(White|Black|Red|Green|Blue|Gray|LightGray|DarkGray|Yellow|Cyan|Magenta)\b" feature shared/src --include="*.kt" | grep -v build/

# V3 — Color.Unspecified must carry the multicolor-asset comment; Color.Transparent is OK
grep -rn "Color.Unspecified" feature shared/src --include="*.kt" | grep -v build/ | grep -v "multicolor asset"

# V4 — TextStyle construction / raw fontSize in feature code
grep -rnE "TextStyle\(|fontSize\s*=|TextUnit\s*=\s*[0-9.]+\.sp" feature shared/src --include="*.kt" | grep -v build/

# V5 — raw corner shapes in feature code
grep -rnE "RoundedCornerShape\([0-9]" feature shared/src --include="*.kt" | grep -v build/

# V6 — forbidden imports (primitives layer & raw color constants) in features/shared
grep -rnE "import com\.yolo\.core\.designsystem\.tokens\.|import com\.yolo\.core\.designsystem\.theme\.Yolo(Ink|Deep|Brand|Amber|Red|Blue|Cyan|Green|Cobalt|Violet|Warning|Success|Neutral|SurfaceVariant|OnSecondary|OnError|PrimaryHover|OnWarning|OnSuccess|TealHeatmap)" feature shared/src --include="*.kt" | grep -v build/

# V7 — theme nesting / mode sniffing in screens (regex requires "(" so member reads never match)
grep -rnE "isSystemInDarkTheme\(\)|MaterialTheme\s*\(" feature shared/src --include="*.kt" | grep -v build/

# V8 — legacy theme ghosts (must stay extinct, whole repo)
grep -rnE "\bAppTheme\b|theme\.legacy|LocalThemeIsDark|appTypographyLegacy" . --include="*.kt" | grep -v build/

# V9 — raw dp screen-edge/section padding where a token exists (ADVISORY — judge each hit)
grep -rnE "padding\((horizontal\s*=\s*)?(16|20|24|32|36)\.dp" feature shared/src --include="*.kt" | grep -v build/
```

V1–V8: every hit is a violation — fix it, with two reviewed carve-outs:
- **V3:** add the `// multicolor asset` justification comment if it truly is a multicolor image.
- **V4 third-party interop:** forwarding a `TextUnit` parameter that a third-party API requires
  (e.g. kmpauth's `GoogleSignInButton(fontSize = …)`) is allowed — but its raw `.sp` default is
  still a violation; source the value from a typography slot (`MaterialTheme.typography.labelLarge.fontSize`).

V9 is advisory: replace with the matching `YoloTokens.spacing.*` when the padding plays a
semantic role (screen edge, section gap, card padding); leave true component-internal geometry alone.

## Auto-fix mapping

| Found | Replace with |
|---|---|
| `Color.White` as card/sheet fill | `MaterialTheme.colorScheme.extended.surfaceHigher` |
| `Color.White` as text on dark/colored fill | `MaterialTheme.colorScheme.onPrimary` (on primary) or the matching `on*` role |
| `Color.Black` text | `MaterialTheme.colorScheme.onSurface` |
| `Color.Black` loading/indicator tint | `LocalContentColor.current` |
| `Color.Gray`/`LightGray` text | `colorScheme.onSurfaceVariant` (or `extended.textPlaceholder` for hints) |
| `Color.Red` anything | `colorScheme.error` — unless it marks a missed habit: `extended.habitMissed` |
| `Color(0x...)` literal | nearest semantic token by ROLE (use §2.1 decision tree in the catalog); if no token fits, STOP and tell the user a spec addition is needed — do not invent hexes |
| `TextStyle(fontSize = X)` | nearest typography slot by size: 57→displayLarge · 45→displayMedium · 36→displaySmall · 32→headlineLarge · 28→headlineMedium · 24→headlineSmall · 22→titleLarge · 16→titleMedium/bodyLarge (Medium weight? title : body) · 14→titleSmall/bodyMedium/labelLarge · 12→bodySmall/labelMedium · 11→labelSmall |
| `RoundedCornerShape(4-6.dp)` | `MaterialTheme.shapes.extraSmall` |
| `RoundedCornerShape(8-10.dp)` | `MaterialTheme.shapes.small` |
| `RoundedCornerShape(12-14.dp)` | `MaterialTheme.shapes.medium` |
| `RoundedCornerShape(16-20.dp)` | `MaterialTheme.shapes.large` |
| `RoundedCornerShape(24-28.dp)` | `MaterialTheme.shapes.extraLarge` |
| `padding(24.dp)` screen edge | `padding(YoloTokens.spacing.screenEdge)` |
| edge/gap dp 8/6/12/16/20/24/32/36 | `elementGap/stackGapTight/stackGap·iconTextGap/itemGap·cardPadding/listRowGap·sectionHeaderGap/sectionGap/dialogPadding/heroGap` (pick by ROLE, not just value) |
| `isSystemInDarkTheme()` in a screen | `MaterialTheme.colorScheme.extended.isDark` |
| nested `MaterialTheme(...)` in a screen | delete — rely on the shell's `YoloTheme`/`YoloSectionTheme` |
| `colorScheme.background` as a tab screen's background | `colorScheme.section.surfaceTintWash` |
| ad-hoc sheet `Row` with icon | `YoloSheetActionRow` |
| new `AppButton` call in NEW code | `YoloButton` |

Imports needed for fixes: `com.yolo.core.designsystem.theme.extended`,
`com.yolo.core.designsystem.theme.section`, `com.yolo.core.designsystem.theme.YoloTokens`.
Remove imports the fix orphaned.

## Hard rules you must never "fix" the wrong way

- NEVER add a hex/Color literal as a fix. If no token expresses the design intent, report it as
  a spec gap (`docs/design/design-system-v3-spec.md` owns values) and leave a TODO referencing it.
- NEVER recolor `habitMissed` toward red, `habitComplete` away from teal, or `streak` away from amber.
- NEVER edit values in `theme/`, `tokens/`, or `ContrastTest.kt` to make a violation "pass" —
  token changes go through the spec + ContrastTest, both modes, and are a user-level decision.
- `NativeAlertDialog` expect/actual is kept by explicit user decision — never delete it.
- The font is Google Sans Flex under OFL — `docs/licenses/google-sans-flex-OFL.txt` must ship;
  never swap or remove the font file without a user decision.

## Verify after fixing

```bash
./gradlew :core:designsystem:compileAndroidMain :shared:compileAndroidMain   # + touched feature modules
./gradlew :core:designsystem:iosSimulatorArm64Test                           # WCAG ContrastTest gate
```

Then re-run the violation scan on the touched files — it must come back clean (V9 judged, not zero).
Report: violations found by class (V1–V9), fixes applied, anything escalated as a spec gap.
