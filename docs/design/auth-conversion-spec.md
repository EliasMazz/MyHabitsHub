# Auth Conversion Spec — MyHabitsHub Sign-In Funnel

**Status:** Approved blueprint, design-only. No implementation in this change; a visual draft lands as a `core/catalog` demo (see §7).
**Owner:** Product design — auth funnel.
**Date:** 2026-06-12.
**Branch context:** `refactor/modularize-shared` — code references below reflect this branch.

Every recommendation in this document is tagged with its supporting data point `[source]`. Where evidence is vendor-sourced or contested, the tag says so.

---

## 1. Funnel architecture: where auth sits

### 1.1 Code reality today

- Logged-out users see the **AuthGraph and nothing else**: `AppScreen.kt:84-91` routes to `AuthGraphRoutes.Graph`, whose start destination is `Login` (`AuthGraph.kt:18-19`).
- **Onboarding runs AFTER auth**, inside the logged-in branch only (`AppScreen.kt:115` starts the logged-in NavHost at `OnBoardingRoutes.Graph`).
- Social sign-in (kmpauth Google/Apple via `AuthUIHelperButtons`) exists but is **only reachable from the logged-in Account tab** (`MainGraph.kt:21-25` → `AccountGraph.kt:40-45`). The logged-out funnel is email/password only.

So the current funnel is the documented worst-case pattern: credential form before any value, no social options at the wall, no guest mode.

### 1.2 Decision: two-phase plan

**Phase A (this spec, current wall position):** Replace the Login start destination with a new **Welcome screen** (§2) that puts platform-native SSO first and collapses email. The Welcome screen is designed to convert *at today's wall position* — it does not depend on moving the wall.

**Phase B (recommended later change, separately scoped):** Move the wall after the first value moment — onboarding/first-habit creation runs logged-out, auth is asked as "save your progress."

Evidence for Phase B (strongest cluster in the research — multiple independent sources):

- Duolingo moved sign-up a few steps later in onboarding → **~+20% DAU**; soft walls with a "Later" button before the hard wall added a further **+8.2% DAU** [First Round Review / Gina Gotthilf].
- "Continue as guest" instead of forced registration → **+45% conversion** ("the $300M button") [Jared Spool / UIE].
- NN/g "Login Walls Stop Users in Their Tracks": login-before-value defies reciprocity; recommends postponing credential creation until after the value moment [NN/g].
- Category leaders converge on it: Finch (4.9★, no auth gate at all, local-first), Atoms (account at step 9 of 10), Duolingo (quiz + first lesson before any wall) [teardown research]. Laggards (Habitica, Fabulous) gate early.
- **Apple App Store Guideline 5.1.1(v)** says apps "may not require users to enter personal information to function" except for account-specific features. A habit tracker that can store habits locally has weak grounds to hard-wall; deferred auth also de-risks review [App Store Review Guidelines 5.1.1(v)].

Phase B is a structural change (onboarding graph must move to the logged-out branch; local-first habit storage with post-auth merge) and is **explicitly out of scope here** — but every Phase A design decision below is chosen to survive the move unchanged: the Welcome screen works identically as a hard wall today and as a "save your progress" soft wall later (only the header copy line and the presence of a "Later" affordance change).

Benchmarks to beat once instrumented (§8): only **~26 of 100 users who start registration complete it**; **38% drop at the first screen** [Setgreet/UXCam]; health & fitness day-1 activation benchmark **26%** [Business of Apps].

---

## 2. The Welcome screen blueprint

### 2.1 Anatomy (top to bottom), with design-system tokens

The screen replaces `AuthGraphRoutes.Login` as the AuthGraph start destination (Login/Register remain as the email path, §2.5). All tokens below exist in `core/designsystem` (`Theme.kt`, `Dimens.kt`); rules per `docs/design/design-system-catalog.md` §1-2.

| Zone | Spec | Token mapping |
|---|---|---|
| 1. Hero (upper ~45%) | Brand moment: `YoloBrandLogo` + app name + one-line value promise ("Build habits that stick."). One aura blob max. | Background `extended.heroSurface` (YoloDeep400 light / dark variant), text `extended.onHeroSurface`, accent `extended.heroAccent`, aura `extended.auraMint` — catalog rule: **max one aura per screen** [design-system-catalog.md §2.1:69] |
| 2. Transition gap | Hero fades into screen background. | `colorScheme.background` (sanctioned auth background, catalog §2.1:47); vertical gap `YoloTokens.spacing.heroGap` (36dp → 48dp ≥1200dp) |
| 3. Button stack (lower half — thumb zone) | 2 SSO-class options + collapsed email, full-width, stacked, `stackGap` apart, width capped. | Heights: SSO buttons 52dp (`YoloTokens.sizing.controlHeight`); width `fillMaxWidth().widthIn(max = YoloTokens.sizing.maxFormWidth)` (480dp); spacing `YoloTokens.spacing.stackGap` (12dp); horizontal padding `screenEdge` (24/32/40dp) |
| 4. Legal line | Implicit-consent fine print directly under the stack. | Reuse `AgreePrivacyPolicyTermsConditionsText` (§5); gap above = `spacing.itemGap` (16dp) |
| 5. Returning-user escape | "Log in" ghost/text action at the very bottom. | `YoloButton(style = YoloButtonStyle.TEXT)` — leaders pattern: "login for returning users is a ghost/text secondary, never competing" [teardown consensus] |

**Why the stack sits in the lower half:** bottom-anchored CTAs show **~10–20% conversion improvement** in aggregated tests [Heyflow]; **75% of users operate with thumb only** [Steven Hoober]. **Why order inside the stack matters:** **78% of users expect the top/first button to be the primary action** [Quant-UX, n=36+follow-ups] — order conveys priority, position conveys reachability; we use both.

**Why exactly 2 visible buttons + 1 collapsed email entry:** Baymard documents **3–6 equally-loud SSO buttons competing with the primary CTA as a recurring conversion failure** ("NASCAR problem") [Baymard; Daniel Burka]; practical consensus is **2–3 options max per platform, one visually primary** [Baymard "Simplifying Sign-In" + NN/g mobile registration checklist]. Choice overload is real precisely "when options are similar and the user lacks a prior preference — which describes auth buttons exactly" [Scheibehenne meta-analysis caveat, applied].

### 2.2 Android wireframe

```
┌──────────────────────────────────────┐
│ ███ heroSurface ████████████████████ │  ← extended.heroSurface
│ ███   ( auraMint blob, 1 max )  ████ │
│ ███      [ YoloBrandLogo ]      ████ │
│ ███      MyHabitsHub            ████ │  ← onHeroSurface, headline
│ ███  Build habits that stick.   ████ │  ← onHeroSurface, value line
│ ██████████████████████▼ fade ███████ │
│                                      │  ← colorScheme.background
│            (heroGap 36dp)            │
│                                      │
│ ┌──────────────────────────────────┐ │
│ │ [G]  Continue with Google        │ │  ← OFFICIAL Google button (§3)
│ └──────────────────────────────────┘ │     52dp, full width ≤480dp
│            (stackGap 12dp)           │
│ ┌──────────────────────────────────┐ │
│ │  ✉   Continue with email         │ │  ← YoloButton SECONDARY, 52dp
│ └──────────────────────────────────┘ │
│            (itemGap 16dp)            │
│   By continuing you agree to our     │  ← AgreePrivacyPolicy…Text
│   Terms & Privacy Policy             │     onSurfaceVariant, links
│                                      │
│   Already have an account? Log in    │  ← YoloButtonStyle.TEXT
│         (thumb zone, bottom)         │
└──────────────────────────────────────┘
   + Credential Manager bottom sheet auto-surfaces over this (§4.1)
```

- **Google FIRST, sole primary prominence.** Platform-native first: every Android device has a Google account logged in, which is what makes the One Tap/Credential Manager fast-path numbers possible — Reddit **+50–60% sign-ups from the button alone**, Pinterest **+126% on Android** [Google identity case studies; vendor-published but the only at-scale numbers].
- **Apple on Android: OMIT in v1.** Apple explicitly permits SiwA on Android via web OAuth (Sign in with Apple JS) and requires the iOS app to be live first — both fine for us — but it adds web-flow plumbing for a small cohort. Spotify ships Android without it; Airbnb adds it only at large scale [cross-platform reality research]. **Mitigation (mandatory backend requirement):** account linking by verified email + a "set password" path so Apple-created accounts (incl. Hide My Email relays) can log in on Android [compliance checklist MUST #8]. Revisit if cross-platform support tickets appear.

### 2.3 iOS wireframe

```
┌──────────────────────────────────────┐
│ ███ heroSurface ████████████████████ │
│ ███   ( auraMint blob, 1 max )  ████ │
│ ███      [ YoloBrandLogo ]      ████ │
│ ███      MyHabitsHub            ████ │
│ ███  Build habits that stick.   ████ │
│ ██████████████████████▼ fade ███████ │
│                                      │
│            (heroGap 36dp)            │
│                                      │
│ ┌──────────────────────────────────┐ │
│ │ [] Continue with Apple          │ │  ← OFFICIAL Apple button (§3)
│ └──────────────────────────────────┘ │     52dp — FIRST, no scrolling
│            (stackGap 12dp)           │
│ ┌──────────────────────────────────┐ │
│ │ [G]  Continue with Google        │ │  ← SAME size as Apple (§3)
│ └──────────────────────────────────┘ │     52dp
│            (stackGap 12dp)           │
│ ┌──────────────────────────────────┐ │
│ │  ✉   Continue with email         │ │  ← YoloButton SECONDARY, 52dp
│ └──────────────────────────────────┘ │
│            (itemGap 16dp)            │
│   By continuing you agree to our     │
│   Terms & Privacy Policy             │
│                                      │
│   Already have an account? Log in    │
└──────────────────────────────────────┘
   Entire stack visible without scrolling on the smallest supported device.
```

- **Apple FIRST.** HIG (verbatim): *"Prominently display a Sign in with Apple button. Make a Sign in with Apple button no smaller than other sign-in buttons, and avoid making people scroll to see the button."* [Apple HIG, Sign in with Apple]. Apple does not formally require top placement, but first position is the conventional safe path through review, and platform-native-first is the rational default absent any published Apple-vs-Google-order A/B (verified gap) [platform research].
> **AMENDED (product decision, 2026-06-12): iOS ships Apple + email ONLY in v1 — Google on iOS
> is deferred.** Consequence: with no third-party login on iOS beyond SiwA itself, the 4.8
> obligation analysis below becomes moot until Google is added there; the moment Google ships
> on iOS, the original analysis applies unchanged (SiwA already present, so still compliant).
> Cross-platform account continuity (Google-created accounts logging in on iPhone) is handled
> by the mandatory email-linking + set-password backend requirement (§2.2). The implemented
> Welcome screen (feature/auth/presentation/welcome/) follows the amended rule via
> `isAndroidPlatform`. Original recommendation preserved below for that future decision:

- **Google on iOS: INCLUDE.** Rationale: (a) cross-platform continuity — Android users who signed up with Google must be able to log in on iPhone; (b) Google explicitly sanctions it (iOS-specific button padding in its branding spec). **Consequence — App Store Guideline 4.8:** offering Google Sign-In for the primary account obliges us to also offer a login service that limits data to name+email, lets users hide their email, and doesn't ad-track — in practice **Sign in with Apple**. Email/password does NOT exempt us (the own-login exemption applies only when it is the *exclusive* method). We ship SiwA on iOS anyway, so 4.8 is satisfied by construction; shipping Google-only would be a guaranteed rejection [App Store Review Guideline 4.8; dev-forum rejection threads 687293, 750911].
- **Both vendors' prominence rules bind simultaneously:** Apple demands its button be no smaller than others; Google demands its button be "at least as prominently displayed as other third-party sign-in options." The only joint solution is **equal-size, equal-weight, stacked buttons** — which this layout uses [Apple HIG + Google branding guidelines].

### 2.4 Email path: collapsed behind "Continue with email"

- Email is a **secondary `YoloButton`, not a form on first paint**. The password field is the worst-converting field in form analytics — **10.5% mean abandonment**, vs 6.4% for email [Zuko form analytics] — so it must not appear on the Welcome screen. Multi-step/progressive forms show up to **+300% completion vs long single forms** [involve.me/Omnisend — vendor claims; directional]. Spotify/Notion/Duolingo all converged on the collapsed pattern [teardown research].

- **Unified vs split — decision: unified entry, split screens behind it (interim), unified email-first stepper (target).**
  - **Interim (no backend change):** "Continue with email" navigates to the existing `RegisterScreen` (`feature/auth/presentation/.../register/RegisterScreen.kt`) — at a pre-value wall, new users dominate the population (38% of drop happens at the first screen because it's full of strangers, not returning users [Setgreet/UXCam]). The bottom "Log in" text action on the Welcome screen routes returning users to the existing `LoginScreen`. Both screens already share `YoloAdaptiveFormLayout` + `YoloBrandLogo` and cross-link to each other (`AuthGraph.kt:21-53`), so no dead ends.
  - **Target (v1.1):** one email-first stepper — single email field → backend lookup → existing account shows password (login), new account shows new-password (register). This removes the login/register fork entirely (a choice the user shouldn't have to make — same choice-architecture evidence as §2.1) and keeps the password field off the first email step.
  - **Email form rules (apply to existing screens now):** single email field → single password field; keep the existing `YoloPasswordTextField` visibility toggle (masking measurably increases login failure [LukeW]; **27 of 36 users unmasked when given the option** [Quant-UX]); **NO confirm-password field ever** — it caused **>25% of ALL signup-form abandonments** in the cited dataset [Zuko/UX Movement]; length-only password rule, no composition requirements [Baymard]. Current `RegisterScreen` already complies (email + password only). Known pre-existing bug to fix during implementation: `RegisterScreen.kt:105` sets the password field's `isError` from `state.emailError`.

- Pre-existing gaps the implementation must close (noted, not designed here): `AuthGraphRoutes.ResetPassword` is declared but has no registered composable (`AuthGraphRoutes.kt:22`); `ForgotPassword`'s `onBack` is an empty lambda (`AuthGraph.kt:57-59`). Forgotten-password recovery is conversion-critical: **55% have abandoned a login over a forgotten password; 92% would leave rather than do a reset** [LoginRadius/CXL]; **up to 19% abandonment among returning users stuck in reset** [Baymard].

### 2.5 Copy

- All three buttons use the "Continue with …" form (sanctioned by both vendors' approved title lists; also auth-framing best practice — "Continue", never "Register") [Apple HIG approved titles; Google branding guidelines approved text; NN/g].
- Phase B variant: hero value line becomes "Save your progress" framing — the only copy change the deferred wall needs [Duolingo soft-wall pattern, First Round Review].

---

## 3. Button specs mapped to our design system

### 3.1 Where official branding OVERRIDES our system — they do, exactly here

Our system buttons: `YoloButton`, `shape = CircleShape` (full pill, `YoloButton.kt:102`), height `controlHeight` 52dp, brand colors, brand typography. For the two SSO buttons, the following official rules **override** any Yolo styling:

| Property | Our system | Google button (binding) | Apple button (binding) |
|---|---|---|---|
| Fill/text colors | Brand palette | Light: #FFFFFF fill, #747775 1px inside stroke, #1F1F1F text; Dark: #131314 / #8E918F / #E3E3E3; Neutral: #F2F2F2, no stroke. **No other colors.** | Black, White, or White-with-outline only. Title/logo **black or white only**, never brand-tinted. |
| Logo | n/a | Multicolor "G" **on white**, never recolored/monochromed/resized/redrawn, never icon-only without button chrome | Apple logo **only from Apple Design Resources**, never redrawn, never logo-alone-as-button, no cropping, no added vertical padding |
| Typography | Brand font | **Roboto Medium (TrueType), 14px/20px** for custom-rendered buttons | **System font (SF)**, title size = **43% of button height** |
| Padding | System | Android/Web: 12px left of G, 10px right of G, 12px right of text. iOS: 16/12/16px | Margin ≥ 1/10 button height |
| Min size | 48dp touch floor | Scale only with aspect ratio preserved | ≥ **140×30pt** |
| Corner radius | Pill (CircleShape) | Rectangular **or pill** — pill allowed ✓ | Square-to-capsule, "adjust to match other buttons in your app" — pill allowed ✓ |
| Label text | free | "Sign in/Sign up/Continue with Google" only; never bare "Google" | "Sign in/Sign up/Continue with Apple" only |

**Net result:** our **pill shape and 52dp height survive** (52dp > 30pt minimum; pill is sanctioned by both specs — this is why the catalog's pill convention and the SSO buttons can coexist on one screen). Our **colors and fonts do not** — the SSO buttons render in official chrome only. The email button and TEXT login action remain pure `YoloButton`s, which creates the visual hierarchy: official-chrome SSO primary at top, brand-secondary email below [Google branding guidelines; Apple HIG].

### 3.2 Variant selection per theme

The Welcome screen's button stack sits on `colorScheme.background` (not the hero wash), so variant follows app theme via `extended.isDark`:

| App theme | Google button | Apple button |
|---|---|---|
| Light | **Light** theme (#FFF fill + #747775 stroke — the stroke is required because background is light) | **Black** ("use on white or light backgrounds"; never on dark) |
| Dark | **Dark** theme (#131314 fill, #8E918F stroke) | **White** ("use on dark backgrounds that provide sufficient contrast") |

If a future iteration places buttons directly on `heroSurface` (a dark wash in both themes): Google **Light**, Apple **White**, regardless of app theme. [Google branding guidelines themes; Apple HIG style rules]

### 3.3 kmpauth-uihelper mapping (for later implementation)

Existing wrapper: `feature/account/presentation/.../components/AuthUIHelperButtons.kt` — already selects `GoogleButtonMode.Dark/Light` and `AppleButtonMode.White/Black` from `extended.isDark` (:64, :81) and wires `GoogleButtonUiContainerFirebase` / `AppleButtonUiContainer`. Catalog version pinned at **kmpauth 2.2.0** (`gradle/libs.versions.toml:36`).

- Available composables: `GoogleSignInButton`, `AppleSignInButton` (+ `…IconOnly` variants), containers `GoogleButtonUiContainerFirebase`, `AppleButtonUiContainer` — any fully custom button may be placed inside a container and call its `onClick()` [KMPAuth docs].
- **Caveats:** uihelper buttons approximate but don't pixel-match the 2025+ Google spec (no bundled Roboto Medium, padding drift). If exact compliance is wanted, render our own brand-compliant button inside the containers [KMPAuth research]. **Upgrade required:** Android must use the Credential Manager (non-legacy) path — kmpauth **2.4.0+**; the legacy Google Sign-In SDK is deprecated/unsupported [Android Identity docs; KMPAuth 2.4.0 release notes]. Hardcoded strings in `AuthUIHelperButtons.kt` (:64) must move to resources, and the height parameter set to `YoloTokens.sizing.controlHeight`.
- Module wiring note: `feature/auth/presentation` has **no kmpauth dependency today** — the Welcome screen implementation either adds `kmpauth.uihelper`/`kmpauth.firebase` there or moves `AuthUIHelperButtons` into a shared auth-ui module [auth UX map §3-4].

---

## 4. Friction reducers roadmap (ordered by measured impact)

1. **Android: Credential Manager bottom sheet + auto sign-in for returning users** (alongside the persistent Google button — Google: "You should always include the Button flow as a user might dismiss the bottomsheet"). Largest documented wins available to us: Reddit **+90% sign-ups from One Tap alone**, Pinterest **+126% Android**, eBay **+100% sign-ins**, Vikatan +270% [Google identity case studies]; Credential Manager era: X 2x login success, KAYAK −50% sign-in time, Zoho 6x faster [Android Developers Blog 2025]. Requires kmpauth ≥2.4.0 non-legacy path (§3.3).
2. **iOS: ASAuthorization existing-credential fast path at launch** — system sheet + Face ID, zero typing for returning users. No Apple-published conversion numbers (verified gap), but it is the same mechanism producing One Tap's measured lifts [platform research]; returning-user auto-surfacing addresses the 55%-abandon-at-forgotten-password problem [LoginRadius].
3. **Autofill plumbing, both platforms** — iOS `textContentType` = `.username`/`.password`/`.newPassword`/`.oneTimeCode` + Associated Domains (`webcredentials:`); Android `autofillHints` + Credential Manager password storage; CMP 1.7+/1.8 exposes `ContentType.Username/Password/NewPassword`. Delete stored credentials on account deletion [Apple/Android autofill docs; Play policy].
4. **Email-form hygiene (ship with the redesign):** keep show-password toggle (27/36 users unmask [Quant-UX]; masking increases failures [LukeW]); never add confirm-password (>25% of signup abandonments [Zuko/UX Movement]); length-only password rules [Baymard].
5. **Passkeys — v1.1, not a v1.0 blocker.** FIDO Passkey Index (Oct 2025): **93% sign-in success vs 63%** legacy, **73% faster (8.5s vs 31.2s)**, headline **30% conversion lift over passwords**; Microsoft 98% vs 32% [FIDO Alliance; Microsoft]. Our Apple/Google buttons already ride platform biometrics, so passkeys are an upgrade path for email users (surface in the same Credential Manager sheet).
6. **Skip magic links on mobile.** Only vendor-grade evidence (HubSpot +25% login success [vendor]); the app-switch to an email client mid-onboarding contradicts the speed mechanism behind every other win here [research ranking #9].

---

## 5. Legal text

- **Pattern:** implicit-consent line under the button stack — "By continuing, you agree to our **Terms & Conditions** and **Privacy Policy**" — rendered by the existing `AgreePrivacyPolicyTermsConditionsText` (`feature/account/presentation/.../components/AgreePrivacyPolicyTermsConditionsText.kt`: bodyMedium, `onSurfaceVariant`, underlined `LinkAnnotation.Url` to `Constants.URL_PRIVACY_POLICY` / `URL_TERMS_CONDITIONS`). Placement: `spacing.itemGap` below the email button, above the returning-user TEXT action (§2.2/2.3 wireframes). Same component must be reused on the email Register screen.
- **No ToS checkbox.** No credible direct A/B exists (verified gap), but the inference chain is solid: a checkbox is an extra required field, and field-count data is unambiguous (4→3 fields ≈ **+50% conversion** in cited case studies; T&C checkboxes are a tracked abandonment point in Zuko's analytics). Legally: US "sign-in-wrap" ("By continuing, you agree…") is broadly enforceable for ToS; **GDPR forbids pre-ticked/implicit consent only for data-processing consent such as marketing — not for contract acceptance** [Termly/TermsFeed/SecurePrivacy].
- **Single exception:** a separate, **unchecked** checkbox solely for marketing opt-in, shown only where law requires (EU), never on the Welcome screen — on the post-auth profile/notification step.
- Every leader app in the teardown uses exactly this fine-print pattern (only regulated-health Flo uses explicit consent checkboxes) [teardown consensus #3].

---

## 6. Compliance checklist (binding, June 2026)

### MUST
1. **iOS ships Sign in with Apple because Google Sign-In is on the iOS auth screen** — Guideline 4.8 requires an equivalent privacy-preserving login (name+email only, email hiding, no ad-tracking); SiwA is the only mainstream service qualifying. Email/password does not exempt us — that exemption requires our own login to be the *exclusive* method [App Store 4.8].
2. **SiwA button no smaller than the Google button and visible without scrolling** — equal width/height, stacked; Apple first by convention (HIG quote in §2.3) [Apple HIG].
3. **SiwA button spec:** black/white/white-outline only; ≥140×30pt; SF font, title 43% of height; only the three approved titles; logo only from Apple Design Resources [Apple HIG].
4. **Google button spec (both platforms):** light/dark/neutral only; multicolor G on white, unmodified; Roboto Medium if custom-rendered; exact paddings (Android 12/10/12px; iOS 16/12/16px); approved text only; **at least as prominent as other third-party options** — parity with Apple's button is the only joint solution [Google branding guidelines].
5. **Android uses Credential Manager** (bottom sheet + persistent button; legacy SDK unsupported) — verify kmpauth ≥2.4.0 non-legacy path [Android Identity docs].
6. **Account deletion:** in-app path on both stores (iOS 5.1.1(v); Play policy) **plus** a public web deletion link declared in Play's Data safety form [store policies].
7. **Autofill + credential hygiene** per §4.3, including deleting stored credentials on account deletion [Play policy; Apple docs].
8. **Backend account linking by verified email + "set password" path** so Apple-created accounts (incl. Hide My Email relays) can sign in on Android without Apple-on-Android [cross-platform research].

### MUST-NOT
1. No Google-only social login on iOS — guaranteed 4.8 rejection [App Store 4.8; forum threads 687293, 750911].
2. No shrinking, recoloring, below-the-fold placement, custom titles, or redrawn logos on the SiwA button [Apple HIG].
3. No Google-button customization beyond spec: no monochrome/recolored G, no G on non-white tile, no non-Roboto custom text, no bare "Google" label, no stretching [Google branding].
4. No webviews for OAuth on Android (Google blocks embedded-webview auth generally) [Android docs].
5. No Sign in with Apple on Android/web before the iOS app with SiwA is live ("An app on the App Store is required" for SiwA JS) — moot for v1 since we omit Apple-on-Android (§2.2) [Apple usage guidelines].
6. No forced login where account-based features don't require it (relevant to Phase B; 5.1.1(v)) and no data collection beyond need at sign-up [App Store 5.1.1(v)].
7. No assuming kmpauth-uihelper buttons are pixel-perfect against the 2025+ Google spec — render custom brand-compliant buttons inside the containers if exactness matters [KMPAuth research].

---

## 7. Draft scope — the `core/catalog` demo

**What the visual draft IS:** a `CatalogEntry` demo in `:core:catalog` (append to `componentEntries`, pattern per `CatalogEntry.kt:24-39` and catalog doc §4b), viewable by flipping the debug flag at `AppScreen.kt:79` (`showDesignSystemCatalog = true`).

The demo shows:
- The full Welcome screen anatomy of §2 — hero zone (`heroSurface` + one `auraMint` blob + `YoloBrandLogo` + value line), button stack in the lower half, legal line, TEXT login action.
- **A platform toggle (Android / iOS)** switching between the §2.2 ordering (Google → email) and the §2.3 ordering (Apple → Google → email).
- **Visual-only, disabled buttons.** Because `:core:catalog` depends only on `:core:designsystem`, kmpauth composables cannot be used; the Google and Apple buttons are **lookalike renderings approximating official chrome** (official fill/stroke/text colors from §3.1 as literal values inside the demo only — sanctioned, since the catalog is the reference implementation; G glyph and Apple glyph approximated, clearly labeled as branding placeholders to be replaced by kmpauth-uihelper/official assets at implementation).
- A persistent **"DRAFT — no auth wired"** banner inside the demo surface.
- The legal line is **recreated as plain text per §5's rendering spec** inside the demo
  (`AgreePrivacyPolicyTermsConditionsText` lives in `feature/account/presentation`, unreachable
  from `:core:catalog`); the real screen reuses the existing component.
- Light/dark rendering via the catalog's standard theme handling (Google Light/Apple Black vs Google Dark/Apple White per §3.2).

**Explicitly NOT in the draft:**
- No kmpauth/Firebase SDK calls; no new module dependencies.
- No navigation changes — `AuthGraph.kt`, `AuthGraphRoutes.kt`, `AppScreen.kt` untouched (debug flag flips locally only, committed as `false`).
- No changes to production `LoginScreen` / `RegisterScreen` / `SignInScreen` / `AuthUIHelperButtons`.
- No backend work (account linking, email-first lookup), no Credential Manager, no analytics.

---

## 8. Metrics plan — instrument at implementation time

Funnel events (screen-view → authenticated), all carrying `platform`, `theme`, and `wall_position` (`pre_value` today / `post_value` after Phase B) so the Phase B A/B is measurable from day one [research ranking #10 — vertical-specific data doesn't exist publicly; we own it]:

| # | Event | Properties | Answers |
|---|---|---|---|
| 1 | `auth_welcome_viewed` | entry_source (cold_start, session_expired, deeplink) | Top-of-funnel volume; first-screen drop benchmark to beat: **38%** [Setgreet/UXCam] |
| 2 | `auth_method_selected` | method (apple / google / email_register / email_login) | Method mix (expect ~20–50% social share [industry range]); validates button order & collapse decisions |
| 3 | `auth_system_sheet_shown` | sheet (credential_manager / asauthorization), action (completed / dismissed) | Measures the One Tap-class lift we're buying in §4.1-4.2 vs Reddit +90% / eBay +100% baselines |
| 4 | `auth_email_step_completed` | step (email_entered / password_entered / submitted), error_type | Localizes email-path drop; password-field abandonment baseline: **10.5%** [Zuko] |
| 5 | `auth_completed` | method, is_new_user, elapsed_ms from event 1 | Headline conversion; registration-completion benchmark: **26/100** [Setgreet]; passkey-era speed target ~8.5s [FIDO] |
| 6 | `auth_abandoned` | last_step, elapsed_ms | Complement of 5; feeds win-back analysis and the soft-wall vs hard-wall test (Duolingo +8.2% DAU from wall tuning [First Round Review]) |

Derived KPIs: welcome→authenticated rate (target: beat 26% [Setgreet]), day-1 activation (vertical benchmark 26% [Business of Apps]), method-level completion rate, returning-user re-auth success (passkey/Credential Manager target 93% vs 63% legacy [FIDO]).

---

## Source key

Google identity case studies (Reddit/Pinterest/eBay/Vikatan — vendor-published, only at-scale numbers); First Round Review (Duolingo, Gotthilf); UIE/Spool; NN/g; Baymard; Zuko form analytics; UX Movement; Quant-UX; LukeW; LoginRadius/CXL; Setgreet/UXCam; Business of Apps; Heyflow; Hoober; Scheibehenne meta-analysis; FIDO Passkey Index Oct 2025; Microsoft; Android Developers Blog 2025; Apple App Store Review Guidelines 4.8 & 5.1.1(v); Apple HIG (Sign in with Apple); Google Sign-In branding guidelines; Google Play Data safety policy; Termly/TermsFeed/SecurePrivacy; KMPAuth (mirzemehdi) release notes/docs; app teardowns (PageFlows, ScreensDesign, App Fuel, Appcues, UserGuiding, Mobbin). Vendor-sourced or contested figures are flagged inline.
