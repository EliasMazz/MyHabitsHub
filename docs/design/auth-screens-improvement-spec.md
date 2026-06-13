# Auth Screens Improvement Spec — Raising Every Auth Screen to the Welcome Standard

**Status:** Approved design spec, implementation-ready. Zero TBDs; backend-dependent items are explicitly listed as NOT DONE in §9.
**Owner:** Product design — auth funnel.
**Date:** 2026-06-12.
**Branch context:** `refactor/modularize-shared`. All file:line references reflect this branch.
**Parent doc:** `docs/design/auth-conversion-spec.md` — §2 of that spec settled the Welcome screen (hero + thumb-zone stack), the SSO/email split, the collapsed-email decision, and the email-first stepper as a **flagged future item pending a backend email-exists endpoint (§2.4)**. Nothing here re-decides those. This spec covers the six screens behind the Welcome wall: **Login, Register, RegisterSuccess, EmailVerification, ForgotPassword, ResetPassword.**

**Hard constraint this iteration:** no backend changes. Every change below is client-only (Compose Multiplatform 1.10.0, navigation args, view-model logic, strings, design-system additive params).

Evidence tags: `[measured]`-class citations are inline (source + number). `[convention]` = documented industry pattern without hard numbers. `[security standard]` = OWASP/Twilio/NIST. `[no public data]` = honest gap, decision made on adjacent evidence. `[audit §N.x]` = forensic codebase audit finding.

---

## 1. Global policy — decided ONCE, referenced by every screen

These four policies replace today's four inconsistent per-screen behaviors (submit-only validation on Login/Register, keystroke gating on ForgotPassword/ResetPassword, fake `canRegister` on Register, none on Login) [audit §9.6].

### 1.1 Validation timing policy

| Rule | Behavior | Evidence |
|---|---|---|
| V1 | **Errors appear on blur**, never on keystroke. Email format is validated when the field loses focus *and* has content. No error is ever shown for a field the user hasn't finished. | Etre/LukeW inline-validation study: on-blur variant produced **+22% success, −22% errors, +31% satisfaction, −42% completion time, −47% fixations**; during-typing variants performed *worse*. Baymard: premature validation is the #1 inline-validation failure mode. UX Movement (n=77, n=90): more errors when messages interrupt completion mode. |
| V2 | **Errors clear at keystroke level** the instant input becomes valid. A stale red field after correction makes users believe the fix didn't take. | Baymard testing — persistent stale errors cause users to think corrected input is still wrong. |
| V3 | **Positive validation is allowed early**: a green check (trailing icon) appears on the email field once format-valid. Password creation gets a live rule checklist that ticks per keystroke (§4.2, §4.6) — positive feedback is exempt from the no-keystroke rule. | Baymard: green checks "confirm I didn't mistype", add "a sense of accomplishment and progression". |
| V4 | **Submit-press validates everything**: empty-field checks + format checks run on tap (and on IME Done); first invalid field receives focus and is scrolled into view. | GOV.UK research: sighted users scroll to errors; focus must move there. Baymard: a *second* occurrence of the same error is disproportionately damaging — first-pass recovery must be effortless. |
| V5 | **Login never runs the registration password policy.** Login validates: email non-empty + format, password non-empty. Nothing else — the server is the authority on whether credentials match. | [audit §2.b3]: today `LoginViewModel` runs `AuthValidatorUseCase(RegisterAuth)` (9+ chars/digit/uppercase), permanently locking out any legacy account whose password predates the policy, before any network call. Conversion-fatal and wrong by construction. |

**Blur detection prerequisite:** `YoloTextFieldLayout.kt:38–40` fires `onFocusChanged(false)` on initial composition. Internal fix (no API change): track whether the field has *ever* gained focus; emit blur callbacks only after first focus gain. Without this, V1 fires errors on screen entry. [audit §0]

### 1.2 Submit button policy

| Rule | Behavior | Evidence |
|---|---|---|
| B1 | **Submit is always ENABLED** (never gated on field validity). Tapping with invalid/empty fields runs V4. | NN/g "Why Disabled Buttons Hurt UX"; Roselli 2024: on mobile, **autofill and paste often don't fire change listeners**, leaving a correctly-filled form with a dead button — directly applicable to Compose forms gated on `isValid` derived state. Disabled buttons also fail contrast and are skipped by screen readers. |
| B2 | **Disable only DURING the request**: on submit, primary button shows in-button spinner with label kept, `enabled = false`; **all sibling buttons also disable** (no navigating away mid-flight); re-enable everything on failure. | NN/g/Smashing/openreplay consensus: post-submission is the only good time to disable; spinner doubles as double-submit prevention. [audit §2.b12, §3.b2]: today Login's "Create account" and Register's "Log In" stay clickable mid-request, and Register's secondary even animates the spinner. |
| B3 | **VM double-submit guard** on every submit handler: `if (state.isLoading) return` (ForgotPassword/ResetPassword already do this; Login and Register don't — `RegisterViewModel.kt:29` fires twice on double-tap). | [audit §3.b1]; engineering convention (idempotency backstop is server-side, out of scope). |
| B4 | **IME Done submits** through the same code path as the button (intent dispatch, so B2/B3 apply). | Capture submit, not click — covers Enter/IME Done [openreplay/gomakethings]. |
| B5 | This retires ForgotPassword/ResetPassword's `canSubmit`-disables-button pattern and Register's fake `canRegister` (`RegisterViewState.kt:15`, never written [audit §3.b1]). `canSubmit` may survive internally for analytics, but never gates `enabled`. | Consistency; B1 evidence. |

### 1.3 Error recovery policy

| Rule | Behavior | Evidence |
|---|---|---|
| E1 | **Field-level errors live at the field** (`supportingText` + `isError`), with **adaptive copy** naming the actual problem ("Enter your email" vs "Enter a valid email address, like you@example.com"). | Baymard: only 2% of sites use adaptive messages; generic copy "converts small mistakes into hard stops". GOV.UK: message adjacent to the offending field. |
| E2 | **The top banner is for request-level (server) errors ONLY** — account exists, invalid credentials, network, token invalid. It is never used for field-format errors and **never for success** (the ForgotPassword bug, §4.5). Banner gets `liveRegion = Polite` semantics. | [audit §9.7] (inconsistent error geography), [audit §6.b1] (success rendered red). GOV.UK: top summary serves screen readers. |
| E3 | **Server-error banners carry a recovery action when one exists**: account-exists → "Log in instead"; email-not-verified → "Resend verification email"; token-invalid → "Request a new link". Mechanism: the action renders in `YoloAdaptiveFormLayout`'s new `errorAction` composable slot (§1.5) — the `errorText` slot itself stays a plain `String`. | Baymard: recovery speed drives abandonment; [audit §3.b9, §2, §7.b2] — today these are all dead ends. |
| E4 | **Inputs are NEVER cleared on error.** Email is preserved everywhere; password preserved on login failure (already true — keep it). | Baymard: 34% of sites clear fields after validation errors; preserved data → "substantially faster" recovery. |
| E5 | **Errors clear on keystroke** in the offending field (V2) **and the top banner clears when any field changes** ([audit §2.b7]: Login's banner currently persists while retyping). | Baymard stale-error finding. |
| E6 | **No misleading pre-submit copy**: client validation failures never use server-flavored copy. `error_invalid_credentials` is reserved for an actual 401. | [audit §2.b4]: Login shows "The email or password is incorrect" before any network call. |
| E7 | Reserve vertical space for one supporting-text line under each field so errors appearing/clearing don't shift layout (internal `YoloTextFieldLayout` fix). | [audit §0]; layout-shift convention. |

### 1.4 Keyboard, IME, and autofill policy

| Rule | Behavior | Evidence |
|---|---|---|
| K1 | Email fields: `KeyboardType.Email`, `KeyboardCapitalization.None`, autocorrect off. | Baymard touch-keyboard benchmark: 60% of top sites fail ≥2 of 5 keyboard optimizations; autocorrect mishandled by 79%, autocapitalization by 27%. No email starts with a capital. |
| K2 | IME chaining: email = `ImeAction.Next` → password = `ImeAction.Done` → Done dispatches submit intent (B4). Single-field screens (ForgotPassword, ResetPassword): `ImeAction.Done`/`ImeAction.Send` → submit. | Baymard qualitative (Next/Done breakage now comes from custom fields); flagged inference — no isolated A/B exists for ImeAction chaining. |
| K3 | Autofill semantics on every auth field: `ContentType.EmailAddress` (email), `ContentType.Password` (login password), `ContentType.NewPassword` (register/reset password). Available in CMP ≥1.8; project is on **1.10.0** (`gradle/libs.versions.toml`). | Google: correct autocomplete → up to **30% faster** completion; Chrome 2024 autofill cohort: **~75% lower abandonment, ~35% lower completion time** (correlational). Today no auth field is autofill-targetable [audit §0]. |
| K4 | **All password fields are single-line.** Today every password field in the app is multiline — Enter inserts a newline into the password (`YoloPasswordTextField.kt:58–68` passes no `singleLine`) [audit §0]. Fixed inside the component, not per-caller. | Bug; no evidence needed. |
| K5 | Password visibility: **shown by default during creation** (Register, ResetPassword) with a "Hide" toggle; **masked by default at Login** with the existing Show toggle. State default change only (`isPasswordVisible = true` in Register/ResetPassword initial state). | NN/g mobile checklist (show-by-default at creation); Zuko: ~50% of sessions re-enter the password field, avg 2×; unmasked sessions return far less, but few users find the toggle unaided. Parent spec §2.4 already keeps the toggle (27/36 users unmask [Quant-UX]). |
| K6 | Visibility-toggle touch target ≥48dp (currently 24dp clickable, `YoloPasswordTextField.kt:99–121`) via `Modifier.minimumInteractiveComponentSize()`. | Platform accessibility floor; [audit §0]. |

### 1.5 Exact design-system API additions (additive only — zero breaking changes)

All params appended with defaults preserving current behavior. Every existing caller compiles unchanged.

**`core/designsystem/.../textfields/YoloTextField.kt`** — current params end at `onFocusChanged` (lines 21–33). Add:

```kotlin
imeAction: ImeAction = ImeAction.Default,                            // K2
onImeAction: (() -> Unit)? = null,                                   // B4/K2 — wired to KeyboardActions for Next/Done/Send/Go
capitalization: KeyboardCapitalization = KeyboardCapitalization.Unspecified, // K1
autoCorrectEnabled: Boolean? = null,                                 // K1 — maps to KeyboardOptions.autoCorrectEnabled
contentType: ContentType? = null,                                    // K3 — applied via Modifier.semantics { contentType?.let { this.contentType = it } }
trailingIcon: (@Composable () -> Unit)? = null,                      // V3 — green check on valid email
```

Internal change: `KeyboardOptions` at lines 54–56 built from all four keyboard params; `keyboardActions` constructed when `onImeAction != null`.

**`core/designsystem/.../textfields/YoloPasswordTextField.kt`** — current params end at `onFocusChanged` (lines 37–49). Add:

```kotlin
imeAction: ImeAction = ImeAction.Default,        // K2
onImeAction: (() -> Unit)? = null,               // B4
contentType: ContentType? = null,                // K3 — callers pass ContentType.Password or ContentType.NewPassword
```

Internal fixes (no API): `singleLine = true` hardcoded on the `BasicTextField` (K4); toggle icon wrapped in `minimumInteractiveComponentSize()` (K6).

**`core/designsystem/.../textfields/YoloTextFieldLayout.kt`** — internal only: blur-detection fix (§1.1 prerequisite); reserved one-line supporting-text space (E7); `liveRegion = LiveRegionMode.Polite` + error semantics on supporting text when `isError`.

**`core/designsystem/.../layout/YoloAdaptiveFormLayout.kt`** — add:

```kotlin
navigationIcon: (@Composable () -> Unit)? = null,   // back-affordance slot, rendered top-start above the header
errorAction: (@Composable () -> Unit)? = null,      // E3 — recovery-action slot, rendered directly below errorText inside the banner; ignored when errorText == null
```

Internal: `errorText` slot (lines 158–171) keeps its error styling but gains `liveRegion = Polite` semantics and moves from `labelSmall` to `bodySmall` for legibility. The slot remains **error-only by contract** — the ForgotPassword success-through-error-slot usage is removed in §4.5, so no success/info variant is added to this layout. [audit §0, §6.b1]

**`core/designsystem/.../buttons/YoloButton.kt`** — internal only: (a) `onClick` guarded by `if (!isLoading)` so `isLoading=true` is never clickable regardless of `enabled` ([audit §0]: today it stays clickable); (b) `defaultMinSize(minHeight = 48.dp)` to meet the touch floor (Material default 40dp).

---

## 2. Brand cohesion — the shared auth scaffold decision

### 2.1 The decision

**Welcome keeps the full hero (settled, parent spec §2.1). No other auth screen gets a hero. Form screens get a compact brand header. Result screens (no keyboard, no fields) get a single restrained aura accent behind the status icon.**

Rationale, per the research:

- There is **no published evidence that brand imagery on form screens helps completion** — a genuine, flagged gap. The adjacent evidence runs the other way: MoneYou removed extra page elements from a sign-up flow → **9.69% → 11.13% (+14.86%, 95%+ sig)**; Yuppiechef removed navigation from its sign-up page → **conversions doubled**; distraction-removal tests commonly show ~10% lifts.
- Mobile mechanics: a hero pushes fields under the keyboard, forcing scroll — directly contrary to field-visibility findings. The Welcome screen can afford a hero precisely because it has **no fields**; Login/Register/ForgotPassword/ResetPassword cannot.
- Result screens (RegisterSuccess, EmailVerification, ForgotPassword sent-state, ResetPassword success) have no keyboard and no fields, so the distraction-cost evidence doesn't bind; these are the funnel's emotional peaks and currently have zero brand treatment [audit §9.8]. One `auraMint` radial wash behind the status icon (catalog rule: **max one aura per screen**, `design-system-catalog.md` §2.1) carries the Welcome brand language through at zero completion risk. Tagged honestly: [no public data; brand decision constrained by catalog rules].

### 2.2 What every form screen gets (the "compact brand band")

Small `YoloBrandLogo` (as today) + **screen title** (`titleLarge`) + **one supporting line** (`bodyMedium`, `textTertiary`) + **48dp back affordance** in the new `navigationIcon` slot. No imagery, no aura, nothing pushing fields below the keyboard. Today Login has *no header at all* (the `welcome_back` string exists unused [audit §2.b10]) and ForgotPassword reuses a link label ("Forgot password?") as a page title [audit §6.b6].

Back affordances are currently **zero across all auth form screens**; ForgotPassword's back intent is dead code, and on iOS (no system back button) the screen is close to a trap [audit §9.9, §6.b4].

### 2.3 New shared components (all in `feature/auth/presentation/.../components/`)

| Component | Purpose | Used by |
|---|---|---|
| **`AuthCompactHeader`** | The §2.2 brand band: logo + title + support line. Single source of header truth for the four form screens. | Login, Register, ForgotPassword, ResetPassword |
| **`AuthResultHero`** | Status icon (success/failure) centered on one `auraMint` radial wash + title + body. Brand treatment for keyboard-free result moments (§2.1). | RegisterSuccess, EmailVerification (all 3 states), ForgotPassword sent-state, ResetPassword success |
| **`CheckEmailContent`** | The "check your inbox" interstitial: `AuthResultHero` (email icon) + **the exact address entered, verbatim, prominent** + spam hint + primary CTA slot + resend button with countdown + wrong-email/back escape link. | RegisterSuccess, ForgotPassword sent-state |
| **`ResendCountdownButton`** | Secondary `YoloButton` that renders a caller-supplied ready-state label — `resend_email` ("Resend email", NEW §6) for the password-reset resend (ForgotPassword sent-state), the existing `resend_verification_email` for verification resends (RegisterSuccess, EmailVerification failure, Login's not-verified banner) — and "Resend in %1$ds" (disabled, live countdown) during cooldown (§5.1). | Inside `CheckEmailContent`; EmailVerification failure state |
| **`PasswordRuleChecklist`** | Live requirement checklist; each rule renders gray-with-dot → green-with-check per keystroke. Rules sourced from `PasswordValidatorUseCase` so copy can never drift from the validator again (§4.2). | Register, ResetPassword |

Why a checklist and not a strength meter: requirement disclosure is mandated by Baymard/NN/g ("nothing is more annoying than having to guess the requirements"); the Muzli/Leslie Mu live-feedback redesign measured **1.42% → 1.89% conversion (+33% relative)** with six rules displayed. Strength meters help mainly with *specific, data-driven* feedback (CMU 2017, n=4,509) — a zxcvbn-class meter means a new dependency and the current policy is composition-bound anyway (§9), so the meter is deferred, never a submit blocker (Egelman CHI'13).

---

## 3. Navigation & route changes (`AuthGraphRoutes.kt`, `AuthGraph.kt`)

Email hand-off is currently impossible: only `RegisterSuccess(val email: String)` carries an email; `Login`, `Register`, `ForgotPassword` are `data object`s [audit §8]. A user who registers must **retype the email they typed seconds ago** to log in [audit §4.b4] — a pure client-side fix.

| # | Change | File | Evidence |
|---|---|---|---|
| N1 | `Login` → `data class Login(val email: String = "")`. Callers pass what they know: Register (typed email), RegisterSuccess (registered email), ResetPassword success (`""` — email unknown, token-only), EmailVerification success (`""`), Welcome (`""`). `LoginViewModel` seeds state from `SavedStateHandle.toRoute()`. | `AuthGraphRoutes.kt:12–13`; callers at `AuthGraph.kt:31–36, 60–69, 92–99, 105–111, 126–131` | Authgear/SitePoint: prefill last-known identifier; never make a user retype the email [convention]. |
| N2 | `ForgotPassword` → `data class ForgotPassword(val email: String = "")`. Login passes the typed email (pass-through only — never persisted). | `AuthGraphRoutes.kt:21–22`; `AuthGraph.kt:44–46` | Uxcel/uxpatterns.dev/SitePoint/Authgear: prefill the reset email from the login attempt [convention]; removes a full field from a frustrated-state flow. |
| N3 | `Register` → `data class Register(val email: String = "")`. Used by RegisterSuccess's wrong-email escape (§5.2) and available to Welcome (`""`). | `AuthGraphRoutes.kt:15–16` | Wrong-email recovery must not force retyping [convention]. |
| N4 | **Fix the `TODO()` crash**: `LoginViewModel.kt:34` → `sendEvent(LoginEvent.NavigateToForgotPasswordEvent(state.value.email))`; event gains an `email` field; `AuthGraph.kt:44–46` forwards it into N2's route. | `LoginViewModel.kt:34`, `LoginEvent.kt:7` | [audit §2.b1] — tapping "Forgot password?" throws `NotImplementedError` and kills the app. **55% have abandoned a login over a forgotten password; up to 19% reset-flow abandonment** [LoginRadius/CXL; Baymard, parent spec §2.4]. Highest-severity fix in this spec. |
| N5 | Register → RegisterSuccess pops Register: `popUpTo(Register) { inclusive = true }`. Back from RegisterSuccess currently lands on the stale filled form; resubmit yields `error_account_exists`. | `AuthGraph.kt:57–59` | [audit §4.b3]; Baymard repeat-error damage. |
| N6 | RegisterSuccess → Login adds `launchSingleTop = true` (parity with sibling blocks). | `AuthGraph.kt:105–111` | [audit §8.7]. |
| N7 | Remove form-state save/restore across auth nav: the `restoreState = true` flags on Welcome→Register/Login (`AuthGraph.kt:27–35`) and Login→Register (`:49`), **and** the `saveState = true` + `restoreState = true` pair in Register→Login's `popUpTo` block (`:62–67`). These are NOT all no-ops: `:64` saves Register's state when the user leaves via "Log In", so the `restoreState` flags on the two Register entries can silently resurrect a stale filled Register form. Removing them is a **deliberate behavior change**: auth forms always start fresh, seeded only by explicit route args (N1/N2/N3) — restored stale state would fight prefill and recreate the N5 stale-form problem. | `AuthGraph.kt:27–35, 47–52, 62–68` | [audit §8.6, corrected: `saveState = true` at `AuthGraph.kt:64` makes restoration real, not a no-op]. |
| N8 | Deep-link base paths (`/api/auth/reset-password`, `/api/auth/verify`) pointing at raw API endpoints: **NOT changed** — a dedicated web redirect path requires server work (§9). | `AuthGraph.kt:83–88, 116–122` | [audit §8.5]; AppsFlyer/AASA matrix [platform constraint]. |

---

## 4. Per-screen specs

Every change list is **ordered by conversion impact**. Global policies §1.1–1.4 are referenced as V/B/E/K rules, not restated.

---

### 4.1 LOGIN — `login/LoginScreen.kt`, `LoginViewModel.kt`

**Verdict: KEEP, restructure internals.**
**Merge analysis:** Login + Register **cannot** merge into the email-first stepper this iteration — it requires the email-exists endpoint, which does not exist (parent spec §2.4 flag stands; no backend changes available). What **can** combine today: shared `AuthCompactHeader` scaffold, the §1 global policies, cross-prefill nav args (N1/N2/N3), and bidirectional recovery links ("Log in instead" on Register's account-exists error; "Create account" stays here) — a soft funnel that makes the fork cheap without faking a merge.

| # | Change | Why (evidence) | Where | How |
|---|---|---|---|---|
| 1 | **Fix the Forgot-password crash** | N4 — app-killing `TODO()` at the account-recovery entry; 55% abandon over forgotten passwords [LoginRadius/CXL] | `LoginViewModel.kt:34`, `LoginEvent.kt:7`, `AuthGraph.kt:44–46` | Send `NavigateToForgotPasswordEvent(email = state.value.email)`; route per N2. |
| 2 | **Drop the register-policy validator** (V5) | [audit §2.b3] — legacy passwords client-blocked before any network call | `LoginViewModel.kt:51–52, 88–128` | Replace `authValidatorUseCase(RegisterAuth(...))` with: email non-empty + format check, password non-empty. Delete `InvalidEmailAndPasswordError → error_invalid_credentials` pre-submit mapping (`:111–113`, E6). |
| 3 | **Per-field errors + submit-press validation** (V1/V4/E1) | Etre/LukeW +22% success/−42% time; Baymard adaptive copy (2% adoption) | `LoginScreen.kt:76–99`, `LoginState.kt` | Add `emailError: UiText?`, `passwordError: UiText?` to state; wire `isError`/`supportingText`; empty-field copy `error_email_required`/`error_password_required`; focus first error on failed submit (V4). Top banner reserved for server errors (E2). |
| 4 | **Clear stale errors on keystroke** (V2/E5) | Baymard stale-error finding; [audit §2.b7] | `LoginViewModel.kt:38–39` | `OnEmailChange`/`OnPasswordChange` clear that field's error + `errorText`. |
| 5 | **IME chain + autofill** (K1–K4) | Baymard 60% keyboard failures; Chrome autofill −75% abandonment | `LoginScreen.kt:78–99` | Email: `imeAction = Next`, `capitalization = None`, `autoCorrectEnabled = false`, `contentType = ContentType.EmailAddress`, green-check `trailingIcon` when valid (V3). Password: `imeAction = Done`, `onImeAction = { dispatch(OnLoginClick) }`, `contentType = ContentType.Password`. |
| 6 | **Button policy** (B1–B3) | NN/g/Roselli; [audit §2.b5, §2.b12] | `LoginScreen.kt:113–128`, `LoginViewModel` | Primary: `enabled = !isLoggingIn` (no validity gating). Secondary "Create account": `enabled = !isLoggingIn`. Add VM re-entry guard. |
| 7 | **Email-not-verified recovery** (E3) | [audit §5.b1] — broken loop today; verify-wall drop-off 20–30% [benchmark] | `LoginViewModel.kt` error mapping | When the server returns the not-verified error, banner shows `error_email_not_verified` + `ResendCountdownButton` ("Resend verification email", existing `resend_verification_email` key) in the banner's `errorAction` slot (§1.5/E3), calling the existing `ResendVerificationEmailUseCase` with the typed email. Cooldown per §5.1. |
| 8 | **Compact brand header + back affordance** (§2.2) | [audit §2.b10, §2.b11] — no header (`welcome_back` unused), no back affordance | `LoginScreen.kt:73–76` | `AuthCompactHeader(title = welcome_back, support = login_support_line)`; `navigationIcon` back arrow → navigateUp (to Welcome). |
| 9 | **Forgot-password link touch target** | 48dp floor; [audit §2.b9] — raw `Text.clickable`, no hit-slop | `LoginScreen.kt:101–109` | Replace with `YoloButton(style = TEXT)` (min-height fix in §1.5 gives 48dp). |
| 10 | **Email prefill in** (N1) | Authgear/SitePoint [convention]; [audit §2.b13] | `LoginViewModel` init | Seed `email` from `SavedStateHandle.toRoute<AuthGraphRoutes.Login>().email`. |
| 11 | Remove dead import | [audit §2.b14] | `LoginIntent.kt:3` | Delete `RegisterViewIntent` import. |

---

### 4.2 REGISTER — `register/RegisterScreen.kt`, `RegisterViewModel.kt`

**Verdict: KEEP, restructure internals.**
**Merge analysis:** same as Login — the stepper merge is backend-blocked (§2.4 parent). Register already complies with the two-field rule (email + password only, no confirm-password — parent spec §2.4; Expedia $12M field-count evidence; NN/g "email and password should be enough"). Combines today via shared scaffold + policies + N1/N3 prefill.

| # | Change | Why (evidence) | Where | How |
|---|---|---|---|---|
| 1 | **Fix the password-copy-vs-validator contradiction** | [audit §3.b6] — hint says "6+ characters, one digit"; `PasswordValidatorUseCase` (`core/domain/.../validator/PasswordValidatorUseCase.kt:5–13`) requires **9+ chars + digit + uppercase**. A user following the hint exactly is rejected with an error repeating the wrong rule. Baymard: users go to extreme lengths to avoid a second validation failure; 82% of sites have overly complex requirements | `RegisterScreen.kt:103–104`, `string.xml:9, 32` | Replace `password_hint` supporting text with **`PasswordRuleChecklist`** (§2.3) rendering the three actual rules, ticking live per keystroke (V3). Rules read from the validator, not duplicated in strings. Fix `error_invalid_password` copy (§6). Policy relaxation to NIST length-only is backend-dependent — NOT DONE (§9). |
| 2 | **Show password by default** (K5) + single-line (K4) | NN/g creation-time default; Zuko ~50% re-entry; Muzli live-feedback case +33% relative | `RegisterViewState.kt` | `isPasswordVisible = true` initial; toggle becomes "Hide". |
| 3 | **Kill fake `canRegister`; apply button policy** (B1–B3) | [audit §3.b1, §3.b2] — `canRegister=true` never written; double-tap double-fires; secondary shows spinner AND stays clickable | `RegisterViewState.kt:15`, `RegisterViewModel.kt:29`, `RegisterScreen.kt:114–129` | Primary `enabled = !isLoading`; secondary `enabled = !isLoading`, **remove** its `isLoading` pass-through; VM re-entry guard. Delete dead `canRegister`, `isEmailValid`, `isPasswordValid` (`RegisterViewState.kt:8, 11, 15`). |
| 4 | **Validation timing** (V1–V4) | Etre/LukeW; Baymard; [audit §3.b3, §3.b4] — submit-only today, and focus-gain clears ALL errors including the other field's (wrong trigger, fires on blur/composition too) | `RegisterViewModel.kt:44–52, 54–57`, `RegisterScreen.kt:90–92, 106–108` | Email format error on blur; per-field clearing on keystroke only (V2); delete `OnInputTextFocusGain` clear-all intent; email green check (V3); focus first error on submit (V4). |
| 5 | **Account-exists recovery action** (E3) | Baymard adaptive recovery; [audit §3.b9] — banner with no directed recovery | `RegisterViewModel.kt:76`, `RegisterScreen.kt` | Banner `error_account_exists` (recopy, §6) + TEXT button `login_instead` in the banner's `errorAction` slot (§1.5/E3) → `Login(email = typed email)` (N1). |
| 6 | **IME chain + autofill** (K1–K4) | Baymard/Chrome (as 4.1#5) | `RegisterScreen.kt:81–108` | Email: Next/no-caps/no-autocorrect/`ContentType.EmailAddress`. Password: Done → submit, `ContentType.NewPassword`. |
| 7 | **Compact brand header + back** (§2.2) | [audit §3.b8] — generic "Welcome", no back affordance | `RegisterScreen.kt:78` | `AuthCompactHeader(title = register_title, support = register_support_line)`; back arrow → navigateUp (Welcome). |
| 8 | **Email prefill in** (N3) | wrong-email recovery path (§5.2) | `RegisterViewModel` init | Seed from `SavedStateHandle.toRoute<Register>().email`. |
| 9 | Remove dead snackbar plumbing | [audit §3.b7] | `RegisterScreen.kt:66, 74–76` | Delete unused `snackbarHostState` param + scaffold wrapper. |

---

### 4.3 REGISTER SUCCESS — `register_success/RegisterSuccessScreen.kt`, `RegisterSuccessViewModel.kt`

**Verdict: KEEP as a separate screen, restructure content onto `CheckEmailContent`.**
**Merge analysis:** this is the verification interstitial — it cannot merge into Register (it must survive Register being popped, N5). It **shares its entire body** with ForgotPassword's new sent-state via `CheckEmailContent` (§2.3) — a component-level merge, which is the correct granularity given both screens show "we emailed you" with resend + escape.

| # | Change | Why (evidence) | Where | How |
|---|---|---|---|---|
| 1 | **Show the actual email** | [audit §4.b1] — headline renders "We've sent a verification email to " + **blank**: the route arg is delivered but never written to state (`registeredEmail = ""` forever). Wrong-email detection is impossible without seeing the address [convention: verbatim address display is mandatory on verification interstitials] | `RegisterSuccessViewModel.kt:18–19`, `RegisterSuccessViewState.kt:7` | In init: `updateState { copy(registeredEmail = savedStateHandle.toRoute<AuthGraphRoutes.RegisterSuccess>().email) }`. Replace the string-key read + `IllegalStateException` throw with `toRoute` ([audit §4.b5]). |
| 2 | **Resend cooldown** (§5.1) | Twilio retry guidance [security standard]; [audit §4.b2] — user can hammer resend indefinitely, server 429 the only backstop | `RegisterSuccessViewModel.kt:29–50`, screen `:78–87` | `ResendCountdownButton`: 30s after first send, 60s subsequent; countdown label `resend_in_x`. |
| 3 | **Pop Register from the stack** (N5) | [audit §4.b3] — back lands on the stale filled form → `error_account_exists` on resubmit | `AuthGraph.kt:57–59` | `popUpTo(Register) { inclusive = true }`. |
| 4 | **Log In prefills the email** (N1) | user retypes the email they registered seconds ago [audit §4.b4]; Authgear [convention] | `RegisterSuccessScreen` primary CTA, `AuthGraph.kt:105–111` | Navigate `Login(email = registeredEmail)` + `launchSingleTop` (N6). |
| 5 | **Wrong-email escape** (§5.2) | Discourse-class stranding failure [convention] | screen body via `CheckEmailContent` | TEXT link `edit_email` ("Not your email? Edit it") → `Register(email = registeredEmail)` (N3) so the user corrects the typo, not retypes. In-place unverified-email update is backend-dependent — NOT DONE (§9). |
| 6 | **Brand treatment** (§2.1) | result-screen aura decision | screen layout | Body becomes `CheckEmailContent` (which embeds `AuthResultHero` email icon + spam hint `check_spam_hint`). |
| 7 | **"Open email app" button: DEFERRED** (§5.3) | no published lift [no public data]; unreliable on both platforms [platform constraint] | — | Decision recorded in §5.3; not in this iteration. |

---

### 4.4 EMAIL VERIFICATION — `email_verification/EmailVerificationScreen.kt`, `EmailVerificationViewModel.kt`

**Verdict: KEEP (standalone deep-link entry — nothing to merge with), restructure the failure state.**
**Merge analysis:** deep-link target; must exist as its own destination. Shares `AuthResultHero` for its three states; nothing else combines.

| # | Change | Why (evidence) | Where | How |
|---|---|---|---|---|
| 1 | **Failure state gets recovery actions** | [audit §5.b1] — failure shows only "Close"; the expired-link user has no resend path anywhere (Login doesn't offer one either — fixed by 4.1#7). Postmark convention: expired-link page offers one-tap re-request, never a dead end | `EmailVerificationScreen.kt:93–114`, VM | Failure state renders: an email field (the screen only holds a token, so the user supplies the address) + `ResendCountdownButton` ("Resend verification email") calling the existing `ResendVerificationEmailUseCase`, + TEXT `back_to_login` → `Login("")`. |
| 2 | **Differentiate network failure from token failure** | [audit §5.b4] — expired link vs no-internet show identical copy; a network blip must not read as "your link is dead" | `feature/auth/domain/.../EmailVerificationUseCase.kt:15–24`, `EmailVerificationViewModel.kt:51–58` | Prerequisite (client-only): `EmailVerificationResult.Failure` is a bare `object` carrying no error — change it to `data class Failure(val error: DataError.Remote)`. `AuthRepository.verifyEmail` already returns `EmptyResult<DataError.Remote>`, so the use case's `fold` `onFailure` receives the error today and merely drops it; pass it through. Then in the VM: `Failure(NO_INTERNET)`/`Failure(REQUEST_TIMEOUT)` → distinct state: copy `verification_failed_network` + primary `retry` re-running `verifyEmail()` with the same token. All other errors → token-failure state (#1). |
| 3 | **Guard re-verification on recreation** | [audit §5.b3] — `init`-fired call re-runs on process restore; single-use tokens then show failure after a successful verification | `EmailVerificationViewModel.kt:19–21` | SavedStateHandle boolean `hasAttemptedVerification`; skip auto-fire when true and current state is success. |
| 4 | **Blank-token guard at entry** | [audit §5.b2] — token defaults `""`, the null-check is dead, blank token silently POSTed | `EmailVerificationViewModel.kt:16–21` | `if (token.isBlank())` in init → go straight to token-failure state (#1), no network call. |
| 5 | **Success → Login prefill: pass `""`** (N1) | email unknown here (token-only); API returning it is backend-dependent (§9) | `AuthGraph.kt:126–131` | `Login(email = "")` — route ready for the day the API returns the address. |
| 6 | **Brand treatment + tokens** (§2.1) | [audit §5.b6] — flat success moment, hard-coded 32dp/80dp | `EmailVerificationScreen.kt:98–103` | All three states render via `AuthResultHero`; spacers/sizes from `YoloTokens`. |

---

### 4.5 FORGOT PASSWORD — `forgot_password/ForgotPasswordScreen.kt`, `ForgotPasswordViewModel.kt`

**Verdict: RESTRUCTURE — one screen, two explicit states (request form → sent interstitial).** This is the spec item 7 fix.
**Merge analysis:** stays a separate destination from Login (different task, deep prefill link via N2); its success interstitial is **absorbed as a state of this screen**, not a new route; that state's body is `CheckEmailContent`, shared with RegisterSuccess (component-level merge).

**The bug being designed away:** on success, the VM writes the success string into `errorText` (`ForgotPasswordViewModel.kt:73–81`), which lands in `YoloAdaptiveFormLayout`'s hard-coded red error slot (`YoloAdaptiveFormLayout.kt:161–169`) — "We've successfully sent you an email…" rendered in **error red, labelSmall, at the top of the form**, with the form still fully active underneath and the submit button still tappable forever. Users pattern-match red = failure → retry or abandon. `isEmailSentSuccessfully` is set and read by nothing [audit §6.b1, §6.b2, §6.b3].

**The proper state design:**

```
State A — request form                       State B — sent (isEmailSent = true)
┌────────────────────────────┐               ┌────────────────────────────┐
│ ←  [logo]                  │               │ ←  [logo]                  │
│ Reset your password        │               │   AuthResultHero:          │
│ Enter your email and we'll │               │   ✉ icon on auraMint wash  │
│ send you a link to set a   │   Success     │   "Check your email"       │
│ new one.                   │  ─────────►   │ If an account exists for   │
│ ┌────────────────────────┐ │               │ elias@… we've sent a       │
│ │ Email     [✓]          │ │               │ password reset link.       │
│ └────────────────────────┘ │               │ Can't find it? Check spam. │
│ [ Send reset link        ] │               │ [ Resend in 27s          ] │
│                            │               │ [ Back to log in         ] │
└────────────────────────────┘               └────────────────────────────┘
```

State B uses `YoloSimpleResultLayout` (already in the codebase, used by ResetPassword/RegisterSuccess [audit §6.b2]) hosting `CheckEmailContent`.

| # | Change | Why (evidence) | Where | How |
|---|---|---|---|---|
| 1 | **Dedicated sent-state replaces success-in-error-slot** | [audit §6.b1/b2]; success styled as failure is a direct conversion killer at the highest-frustration moment of the funnel (19% reset abandonment [Baymard]) | `ForgotPasswordViewModel.kt:73–81`, `ForgotPasswordViewState.kt:11`, screen | On Success: `updateState { copy(isEmailSent = true) }` (rename of the dead `isEmailSentSuccessfully`, now actually read); screen switches to State B. `errorText` never receives success copy again. |
| 2 | **Resend cooldown in sent-state** (§5.1) | [audit §6.b3] — infinite resubmits today; Twilio [security standard] | sent-state via `CheckEmailContent` | 30s → 60s countdown; resend re-fires the same use case with the same email. |
| 3 | **Anti-enumeration copy (client-side share)** | OWASP: identical success message whether or not the account exists; [audit §6.b6] — NOT_FOUND currently leaks straight through `toUiText` ("error_not_found") | `ForgotPasswordViewModel.kt:64–70` | Map `DataError.NOT_FOUND` → the **same sent-state** as success, copy `reset_link_sent_body` ("If an account exists for %1$s…"). Response-timing parity is server-side — NOT DONE (§9); copy-level masking is what the client can do. |
| 4 | **Email prefill from Login** (N2) | Authgear/SitePoint [convention]; removes a field from a frustrated flow | VM init | Seed email from `SavedStateHandle.toRoute<ForgotPassword>().email`. |
| 5 | **Button policy** (B1/B5) | NN/g/Roselli — retire `canSubmit` gating (autofill risk) | `ForgotPasswordScreen.kt:77–79`, VM:29–43 | `enabled = !isLoading`; empty-tap → field error `error_email_required` (V4). Keep VM re-entry guard (already correct). |
| 6 | **Back affordance (live, in-UI)** | [audit §6.b4] — `OnBackClick` is dead code; no back UI; iOS trap | screen header | `AuthCompactHeader` + `navigationIcon` back arrow dispatching the existing `OnBackClick` → already-wired `navigateUp` (`AuthGraph.kt:73–78`). State B's "Back to log in" → `Login(email)`. |
| 7 | **Copy upgrade** | [audit §6.b6] — "Submit" CTA, link-label-as-title, no reassurance line | strings (§6) | Title `forgot_password_title`, support `forgot_password_support`, CTA `send_reset_link`. |
| 8 | **IME + autofill** (K1–K3) | as 4.1#5 | email field | `ImeAction.Send` → submit; `ContentType.EmailAddress`; no-caps/no-autocorrect; green check (V3). |

---

### 4.6 RESET PASSWORD — `reset_password/ResetPasswordScreen.kt`, `ResetPasswordViewModel.kt`

**Verdict: KEEP (best screen in the audit), fix the dead-ends.**
**Merge analysis:** standalone deep-link target; success state already uses the shared result layout. Nothing merges; it adopts §1 policies + `PasswordRuleChecklist` + `AuthCompactHeader`.

| # | Change | Why (evidence) | Where | How |
|---|---|---|---|---|
| 1 | **Token-invalid dead-end gets an exit** (E3) | [audit §7.b2] — copy says "request a new email" but offers no route; the user is stranded on a form that can never succeed. Postmark: expired-link landing must offer one-tap re-request | `ResetPasswordViewModel.kt:79–87`, screen, `AuthGraph` | Token-error state shows primary `request_new_link` → `ForgotPassword(email = "")` (new event + nav wiring), plus TEXT `back_to_login`. |
| 2 | **Blank-token check at entry, not at submit** | [audit §7.b1] — user types a full password before learning the link is dead | `ResetPasswordViewModel.kt:57–62` → init | Run the blank check in init; blank → immediate token-error state (#1), field never shown. |
| 3 | **`PasswordRuleChecklist` replaces the wrong hint** | same contradiction as Register ([audit §7.b3] — hint says 6+/digit, validator says 9+/digit/uppercase, the file's own preview says 9) | `ResetPasswordScreen.kt:94–95` | Checklist per §2.3; ticks live. |
| 4 | **Show password by default** (K5) + single-line (K4) | creation context — NN/g | state default | `isPasswordVisible = true`. |
| 5 | **Button policy** (B1/B5) | retire `canSubmit` gating | `ResetPasswordScreen.kt:107–108` | `enabled = !isLoading`; empty-tap → field error. |
| 6 | **Unify error geography** (E1/E2) | [audit §7.b5] — token error at top, password error at field, on a one-field form | screen | Password errors at field; token errors are request-level → state #1 (full-screen recovery state, not a banner over a usable form). |
| 7 | **Success → Login** | OWASP: do NOT auto-login after reset; route to login. Email unknown (token-only) → `Login("")` (N1-ready) | `AuthGraph.kt:92–99` | Keep current route-to-login; session invalidation + changed-password email are backend — NOT DONE (§9). |
| 8 | **IME + autofill** | as above | password field | `ImeAction.Done` → submit; `ContentType.NewPassword`. |
| 9 | `error_same_password` mapping: **NOT DONE** | no error-code contract exists for "same as old password"; guessing a `DataError` mapping risks mislabeling other conflicts | `string.xml:36` | String stays; mapping deferred to §9 (needs backend error-code contract). |
| 10 | **Compact brand header + CTA copy** (§2.2) | consistency with the §7 matrix (ResetPwd = `AuthCompactHeader`); today the screen renders a bare `headerText` and a generic "Submit" CTA — descriptive CTA labels [convention — GOV.UK] | `ResetPasswordScreen.kt:85, 105`, strings (§6) | `AuthCompactHeader(title = set_new_password, support = reset_password_support)`; **no back arrow** (`navigationIcon = null` — deep-link entry, explicit exits per §7). CTA: `save_new_password` ("Save new password") replaces `submit`. |

---

## 5. Verification & recovery mechanics (cross-screen)

### 5.1 Resend cooldown — exact spec

- **30 seconds** after the first send (including the automatic send that created the screen), **60 seconds** for every subsequent resend on the same screen instance. Source: Twilio 2FA retry guidance — ~30s starting cooldown with visible countdown, backoff on successive resends [security standard]; 30–60s is the observed industry range [convention].
- **UI:** the resend `YoloButton` (SECONDARY/TEXT per context) renders disabled with a **live countdown label** `resend_in_x` ("Resend in %1$ds"), ticking every second — never a dead unlabeled button [convention: countdown variants dominate production examples]. At 0 it re-enables with its normal label.
- **Mechanics:** VM-owned countdown (`viewModelScope` ticker), survives recomposition; not persisted across process death (acceptable — the server 429 remains the backstop). In-flight guard unchanged (RegisterSuccess already has it; ForgotPassword sent-state reuses the same pattern).
- **Applies to:** RegisterSuccess resend, ForgotPassword sent-state resend, EmailVerification failure-state resend, Login's email-not-verified banner resend.
- After **3 resends** in one screen instance the cooldown stays at 60s and the spam hint (`check_spam_hint`, always visible in `CheckEmailContent`) is the standing answer; no hard cap (server rate limit governs).

### 5.2 Wrong-email recovery (RegisterSuccess)

Best practice is (a) show the exact address (fixed, 4.3#1) and (b) inline "edit address" that updates the unverified account and resends [convention]. **(b) requires an update-unverified-email endpoint — backend, NOT available.** This iteration's path: `edit_email` TEXT link → `Register(email = registeredEmail)` (N3) — the form returns prefilled with the wrong address for one-tap correction; submitting a *corrected* address registers cleanly (different email = new account). Limitations flagged in §9: the typo'd account stays orphaned until backend cleanup of unverified accounts exists, and resubmitting the *same* address correctly yields `error_account_exists` → its banner now carries "Log in instead" (4.2#5), so even that path has an exit.

### 5.3 "Open email app" button — decision: DEFER (v1.1, behind instrumentation)

Feasibility in CMP without new dependencies: **yes, technically** — `expect/actual` with `Intent(ACTION_MAIN, CATEGORY_APP_EMAIL)` on Android and `UIApplication.openURL("message://")` on iOS. But: Android's intent is unreliably implemented (in practice mostly Gmail responds); iOS `message://` opens Apple Mail only, **fails silently** when Mail isn't configured, and can't be tested on Simulator; in-email-client webviews swallow links [platform constraint]. There is **no published A/B lift for the button** [no public data], and the robust version (provider detection from the email domain, MX lookup for custom domains) needs server support. Verdict: not worth the device-matrix cost this iteration; revisit in v1.1 alongside provider detection, instrumented per the parent spec's §8 metrics plan.

### 5.4 What verify-later would buy (recorded, not actionable)

Hard verification walls lose 20–30% of users who reach them [benchmark: double-opt-in datasets]; Duolingo/GitLab/Slack all ship soft verification [convention]. Moving MyHabitsHub to verify-later (session at registration, dismissible banner, gate only recovery/social) requires backend session issuance at registration — **NOT DONE** (§9), recorded here so the RegisterSuccess investment is understood as interim.

---

## 6. Copy table — every changed or new user-facing string

File: `feature/auth/presentation/src/commonMain/composeResources/values/string.xml` (note: the file is `string.xml`, not `strings.xml` [audit §10]). Tone: warm, momentum-framed, consistent with "Build habits that stick." All `NEW`/changed; unchanged strings omitted.

| Key | Current | New |
|---|---|---|
| `welcome_back` | "Welcome back!" (UNUSED) | unchanged text — **now used** as Login title (4.1#8) |
| `login_support_line` | — NEW | "Pick up where you left off." |
| `register_title` | (screen used `welcome` = "Welcome") | "Create your account" |
| `register_support_line` | — NEW | "Start building habits that stick." |
| `password_hint` | "Use 6+ characters, at least one digit" (WRONG vs validator) | **deleted** — replaced by checklist rows below |
| `password_rule_length` | — NEW | "At least 9 characters" |
| `password_rule_digit` | — NEW | "At least one number" |
| `password_rule_uppercase` | — NEW | "At least one uppercase letter" |
| `error_invalid_password` | "…at least 6 characters and include one digit" (WRONG) | "Your password needs at least 9 characters, one number, and one uppercase letter." |
| `error_invalid_email` | (generic invalid) | "Enter a valid email address, like you@example.com." |
| `error_email_required` | — NEW | "Enter your email." |
| `error_password_required` | — NEW | "Enter your password." |
| `error_account_exists` | (bare statement) | "Looks like you already have an account." (paired with `login_instead` action) |
| `login_instead` | — NEW | "Log in instead" |
| `forgot_password_title` | (screen reused `forgot_password` = "Forgot password?") | "Reset your password" |
| `forgot_password_support` | — NEW | "Enter your email and we'll send you a link to set a new one." |
| `send_reset_link` | (screen used `submit` = "Submit") | "Send reset link" |
| `forgot_password_email_sent_successfully` | "We've successfully sent you an email…" (rendered red) | **deleted** — replaced by sent-state strings below |
| `check_email_title` | — NEW | "Check your email" |
| `reset_link_sent_body` | — NEW | "If an account exists for %1$s, we've sent a password reset link." (OWASP anti-enumeration phrasing) |
| `check_spam_hint` | — NEW | "Can't find it? Check your spam folder." |
| `resend_in_x` | — NEW | "Resend in %1$ds" |
| `resend_email` | — NEW | "Resend email" — `ResendCountdownButton` ready label for the password-reset resend (ForgotPassword sent-state, §2.3); verification resends keep the existing `resend_verification_email` |
| `back_to_login` | — NEW | "Back to log in" |
| `edit_email` | — NEW | "Not your email? Edit it" |
| `request_new_link` | — NEW | "Request a new link" |
| `set_new_password` | "Set a new password" (bare `headerText`) | unchanged text — now the ResetPassword `AuthCompactHeader` title (4.6#10) |
| `reset_password_support` | — NEW | "Almost done — choose a strong new password." |
| `save_new_password` | (screen used `submit` = "Submit") | "Save new password" |
| `retry` | — NEW | "Try again" |
| `verification_failed_network` | — NEW | "We couldn't reach the server. Check your connection and try again." |
| `hide` accessibility handled by existing designsystem `hide_password`/`show_password` | — | unchanged |

Notes: reset-link **expiry is deliberately not stated** in `reset_link_sent_body` — stating expiry is best practice [Postmark], but the client doesn't know the token TTL and inventing one is worse than omitting it; add the clause once backend confirms TTL (§9). `error_same_password` stays in the file, unmapped (4.6#9). `submit` is **deleted**: it lives in this auth-feature `string.xml` (it has no non-auth callers), and its last two callers are both replaced — ForgotPassword's CTA by `send_reset_link` (4.5#7) and ResetPassword's CTA by `save_new_password` (4.6#10). Removal listed in §8.5.

---

## 7. What is explicitly shared vs. per-screen (summary matrix)

| Concern | Decided in | Login | Register | RegisterSuccess | EmailVerif | ForgotPwd | ResetPwd |
|---|---|---|---|---|---|---|---|
| Validation timing | §1.1 | V1–V5 | V1–V4 | — | — | V1/V2/V4 | V1/V2/V4 |
| Button policy | §1.2 | B1–B4 | B1–B4 | B2/B3 + cooldown | B2/B3 + cooldown | B1–B5 | B1–B5 |
| Error recovery | §1.3 | E1–E6 | E1–E5 | — | E3 | E1–E5 + §4.5 | E1–E3 |
| Keyboard/autofill | §1.4 | K1–K4 | K1–K5 | — | K1 (failure field) | K1–K3 | K2–K5 |
| Header | §2.2 | AuthCompactHeader | AuthCompactHeader | AuthResultHero | AuthResultHero | both (per state) | AuthCompactHeader |
| Back affordance | §2.2 | yes → Welcome | yes → Welcome | no (stack popped; forward-only) | no (deep link; explicit exits) | yes → Login | no (deep link; explicit exits) |

---

## 8. Implementation plan — ordered, file by file

Build order (dependencies first); within screens, land 8.3 step 1 before anything else if cherry-picking.

### 8.1 Design system (additive, unblocks everything)
1. `core/designsystem/.../textfields/YoloTextFieldLayout.kt` — blur fix, reserved supporting space, error semantics (§1.5).
2. `core/designsystem/.../textfields/YoloTextField.kt` — six new params (§1.5).
3. `core/designsystem/.../textfields/YoloPasswordTextField.kt` — three new params + `singleLine=true` + 48dp toggle (§1.5).
4. `core/designsystem/.../layout/YoloAdaptiveFormLayout.kt` — `navigationIcon` + `errorAction` slots, banner semantics/restyle (§1.5).
5. `core/designsystem/.../buttons/YoloButton.kt` — isLoading click guard, 48dp min height (§1.5).

### 8.2 Shared auth components + strings
6. `feature/auth/presentation/.../components/AuthCompactHeader.kt` — NEW (§2.3).
7. `feature/auth/presentation/.../components/AuthResultHero.kt` — NEW (§2.3).
8. `feature/auth/presentation/.../components/ResendCountdownButton.kt` — NEW (§5.1).
9. `feature/auth/presentation/.../components/CheckEmailContent.kt` — NEW (§2.3).
10. `feature/auth/presentation/.../components/PasswordRuleChecklist.kt` — NEW (§2.3); reads rules from `core/domain/.../validator/PasswordValidatorUseCase.kt` (expose the rule set as data — additive function/property on the use case, no behavior change).
11. `feature/auth/presentation/src/commonMain/composeResources/values/string.xml` — all §6 additions/changes/deletions.

### 8.3 Navigation
12. `feature/auth/presentation/.../navigation/AuthGraphRoutes.kt` — N1/N2/N3 (Login/ForgotPassword/Register → data classes with `email: String = ""`).
13. `feature/auth/presentation/.../navigation/AuthGraph.kt` — N4 forward email to ForgotPassword; N5 popUpTo Register; N6 launchSingleTop; N7 dead flags; new ResetPassword→ForgotPassword wiring (4.6#1); EmailVerification failure→Login wiring (4.4#1).

### 8.4 Screens (conversion-impact order)
14. `login/LoginViewModel.kt` + `LoginEvent.kt` — **TODO() crash fix first** (4.1#1), then 4.1#2/4/6/7/10; `LoginIntent.kt` dead import; `LoginState.kt` per-field errors.
15. `login/LoginScreen.kt` — 4.1#3/5/6/8/9.
16. `forgot_password/ForgotPasswordViewModel.kt` + `ForgotPasswordViewState.kt` + `ForgotPasswordViewIntent.kt` (new sent-state `OnResendClick`) + `ForgotPasswordViewEvent.kt` (new `NavigateToLogin(email)` for State B's "Back to log in" — today only `NavigateBack` exists) — sent-state (4.5#1), NOT_FOUND masking (4.5#3), prefill (4.5#4), cooldown (4.5#2).
17. `forgot_password/ForgotPasswordScreen.kt` — two-state layout (§4.5 diagram), header/back (4.5#6), CTA/copy (4.5#7), IME (4.5#8).
18. `register_success/RegisterSuccessViewModel.kt` + `RegisterSuccessViewState.kt` + `RegisterSuccessViewIntent.kt` (new `OnEditEmailClick` for the wrong-email escape, 4.3#5) + `RegisterSuccessViewEvent.kt` (`NavigateToLoginEvent` gains `email` for 4.3#4; new `NavigateToRegisterEvent(email)`) — email surfacing via `toRoute` (4.3#1), cooldown (4.3#2).
19. `register_success/RegisterSuccessScreen.kt` — `CheckEmailContent` body, Log In prefill, wrong-email escape (4.3#4/5/6).
20. `register/RegisterViewModel.kt` + `RegisterViewState.kt` + `RegisterViewIntent.kt` (delete `OnInputTextFocusGain`, 4.2#4) — dead state removal, guards, validation timing, account-exists action, prefill (4.2#3/4/5/8).
21. `register/RegisterScreen.kt` — checklist, show-by-default, IME/autofill, header, snackbar removal (4.2#1/2/6/7/9).
22. `reset_password/ResetPasswordViewModel.kt` + `ResetPasswordViewEvent.kt` (new `NavigateToForgotPassword` event, 4.6#1) — init token check (4.6#2), token-error exit event (4.6#1).
23. `reset_password/ResetPasswordScreen.kt` — checklist, show-by-default, error geography, button policy, IME, `AuthCompactHeader` + `save_new_password` CTA (4.6#3–6/8/10).
24. `feature/auth/domain/.../EmailVerificationUseCase.kt` (`Failure` → `data class Failure(val error: DataError.Remote)`, 4.4#2 prerequisite, client-only) + `email_verification/EmailVerificationViewModel.kt` + `EmailVerificationViewState.kt` (failure-kind discrimination, failure-state email field, resend cooldown) + `EmailVerificationViewIntent.kt` (new `OnEmailChange`/`OnResendClick`/`OnRetryClick`) — blank-token init guard, recreation guard, network-vs-token states (4.4#1–4).
25. `email_verification/EmailVerificationScreen.kt` — failure recovery UI, `AuthResultHero`, tokens (4.4#1/6).

### 8.5 Cleanup
26. Delete dead code: `ForgotPasswordViewIntent.OnBackClick` becomes live (kept); `RegisterViewState.isEmailValid/isPasswordValid/canRegister`; `RegisterViewIntent.OnInputTextFocusGain`; `LoginIntent` import; `welcome_back` becomes live (kept); `password_hint`, `forgot_password_email_sent_successfully`, `submit` removed [audit §9.10, §6 note].

## 9. NOT DONE this iteration (backend-dependent — every item flagged, none silently dropped)

| Item | Blocked on |
|---|---|
| Email-first stepper (Login/Register merge) | email-exists endpoint — parent spec §2.4 flag stands |
| NIST 800-63B length-only password policy (8+ chars, no composition rules) + breached-password blocklist | server-side policy + blocklist service; this iteration aligns copy/checklist to the *existing* 9+/digit/uppercase validator instead |
| In-place edit of unverified account email + orphaned-account cleanup | update-email endpoint, cleanup job (§5.2 ships the client-side workaround) |
| Verify-later soft wall (session at registration, banner, gated actions) | session issuance at registration (§5.4) |
| Anti-enumeration response-timing parity on forgot-password | server; client ships copy-level masking only (4.5#3) |
| Reset-link expiry stated in copy + expired-link web landing with re-request | token TTL contract + web page |
| Dedicated web redirect paths for deep links (today they hit raw API endpoints, `AuthGraph.kt:83–88, 116–122`) | server routes |
| `error_same_password` mapping | error-code contract for same-as-old rejection (4.6#9) |
| Email returned by verify/reset APIs (would enable Login prefill from token-only screens) | API response change |
| 6-digit code alongside the deep link in verification/reset emails (cross-device + scanner-proof path [Okta pattern]) | email templates + verification endpoint |
| "Open email app" / provider-detection buttons | deferred by decision §5.3 (client-feasible but unreliable; no measured lift) |
| Server-side resend rate limiting beyond existing 429 | backend |
| SSO wiring, Credential Manager, passkeys, post-auth skeleton handoff | separate scope — parent spec §3–4; Leg 5 research recorded for the app-shell team |

---

## Source key

Etre/LukeW inline-validation study (A List Apart); Baymard Institute (inline validation, touch keyboards, adaptive errors, field clearing, password requirements, reset abandonment); UX Movement (premature-validation studies n=77/n=90); NN/g (disabled buttons, placeholders, mobile registration checklist, login walls); Adrian Roselli 2024; Smashing Magazine; NIST SP 800-63B Rev 4; Tan et al. CCS 2020; Egelman et al. CHI 2013; CMU 2017 data-driven meter (n=4,509); Muzli/Leslie Mu redesign; Zuko form analytics; Google autocomplete/Chrome autofill 2024; GOV.UK Design System; Expedia (Joshua Porter); Penzo 2006; MoneYou (VWO); Yuppiechef; Twilio 2FA retry guidance; OWASP Forgot Password Cheat Sheet; Postmark; Okta cross-device fallback design; Prospeo/HubSpot double-opt-in benchmarks; SuperTokens/Verify550/signupdrop (verify-later); Duolingo/GitLab/Slack/Notion patterns; Discourse meta (wrong-email stranding); AppsFlyer deep-link matrix; LoginRadius/CXL; Authgear/SitePoint/Uxcel (prefill conventions); forensic codebase audit 2026-06-12 (this branch). Vendor-sourced and no-data items are flagged inline.
