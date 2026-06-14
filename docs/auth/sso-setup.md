# Social Sign-In Setup — Google (Android) & Apple (iOS)

Step-by-step setup for the **no-Firebase, custom-backend** social sign-in in MyHabitsHub.
Google One-Tap is shown on **Android only**; Sign in with Apple on **iOS only**.

> TL;DR of the architecture: the app gets a raw provider credential natively, POSTs it to **your**
> backend, and your backend returns the app's own JWT session (`accessToken`/`refreshToken`) — the
> exact same session shape as email/password login. No Firebase is involved in auth. (Firebase
> stays only for push notifications.)

---

## 1. How it works

```
Android  Credential Manager ─ Google idToken ─┐
                                              ├─▶ POST your backend ─▶ { accessToken, refreshToken, user }
iOS      ASAuthorization ─ Apple identityToken ┘        (verifies token)         │
         + authorizationCode + raw nonce                                         ▼
                                                          SessionStorage.set(AuthInfo) ─▶ app unlocks
```

Client pieces (already implemented):

| Concern | Where |
|---|---|
| Google native sign-in (Credential Manager) | `feature/auth/presentation/src/androidMain/.../sso/SocialSignIn.android.kt` |
| Apple native sign-in (ASAuthorization, pure Kotlin/Native) | `feature/auth/presentation/src/iosMain/.../sso/SocialSignIn.ios.kt` |
| Backend exchange | `AuthRepository.loginWithGoogle/loginWithApple` → `core/data/.../AuthRepositoryImpl.kt` |
| Use case | `feature/auth/domain/.../SsoLoginUseCase.kt` |
| Screen wiring | `feature/auth/presentation/.../welcome/WelcomeScreen.kt` + `WelcomeViewModel.kt` |
| Web client ID config | `SsoConfig` (DI in `InitKoin.kt`, value from `BuildConfig.GOOGLE_WEB_CLIENT_ID`) |

---

## 2. Backend contract (implement this on your server)

The client calls two endpoints. **Both must return the same JSON the email-login endpoint returns**
(`AuthInfoResponse`), so the existing Ktor bearer/refresh machinery works unchanged.

### `POST /api/auth/google`
```jsonc
// request
{ "idToken": "<Google ID token (JWT)>" }
```

### `POST /api/auth/apple`
```jsonc
// request
{
  "identityToken": "<Apple identity token (JWT)>",
  "authorizationCode": "<short-lived single-use code>",
  "nonce": "<RAW nonce — backend must SHA-256 it and compare to the token's nonce claim>"
}
```

### Response (both endpoints) — `200 OK`
```jsonc
{
  "accessToken": "<your app JWT>",
  "refreshToken": "<your app refresh token>",
  "user": { "id": "...", "email": "...", "hasVerifiedEmail": true }
}
```
**All fields are required — there are no defaults** (`AuthInfoResponse`/`UserResponse` are strict). A
missing field deserializes to a `SERIALIZATION` error → generic SSO failure. For SSO users set
`hasVerifiedEmail = true` (the provider already verified the email).

When the email already belongs to an existing account, **auto-link only if both the provider's and
the existing account's email are verified; otherwise return `409 Conflict`** (the app shows "you
already have an account"). Other non-2xx codes surface a generic error. The full, hand-to-the-backend
spec — token verification, the exact linking decision, and all status codes — is in
[`backend-sso-contract.md`](./backend-sso-contract.md). **That file is the authoritative backend contract.**

> The routes/field names live in one place each — `AuthRepositoryImpl.kt` (routes) and
> `GoogleLoginRequest`/`AppleLoginRequest` (fields). Change them there if your API differs
> (e.g. `api/v1/auth/...`).

### What the backend must verify
**Google** — fetch Google's JWKS (`https://www.googleapis.com/oauth2/v3/certs`), verify the
`idToken` signature, then check:
- `iss` ∈ {`accounts.google.com`, `https://accounts.google.com`}
- `aud` == your **Web** client ID (the same `GOOGLE_WEB_CLIENT_ID`)
- `exp` not expired
- use `sub` as the stable user id, `email`/`email_verified` for the profile.

**Apple** — fetch Apple's JWKS (`https://appleid.apple.com/auth/keys`), verify the `identityToken`
signature, then check:
- `iss` == `https://appleid.apple.com`
- `aud` == your app bundle id (`com.yolo.myhabitshub`)
- `exp` not expired (Apple tokens live ~10 min; allow ±60–300 s clock skew)
- `nonce`: the client sends `request.nonce = lowercaseHex(SHA-256(rawNonce))` to Apple, and Apple
  copies it **verbatim** into the token's `nonce` claim. So the backend must compute
  `lowercaseHex(SHA-256(received_nonce))` from the `nonce` field in the request and **constant-time
  compare** it to the token's `nonce` claim. (Hex is fine as long as both sides use it — the famous
  "hex vs base64url" mismatch is the only failure mode here.)
- verify the signature against Apple's JWKS, matching by the JWT header `kid` (Apple rotates keys).
- use `sub` as the stable user id. **`email`/`name` arrive only on the very first sign-in** — persist
  them then (see §6).
- The `authorizationCode` is optional for login; you only need it if the server will call Apple's
  token endpoint to obtain a refresh token / revoke (which requires the Apple private key — see §5).

---

## 3. Google setup (Android) — keys & console, in order

You need three things to line up: the **Web client ID**, the app **package name**, and the signing
**SHA-1/SHA-256** fingerprints — all registered in the same Google Cloud / Firebase project.

1. **Package name** — the Android OAuth client must match `applicationId` = `com.yolo.myhabitshub`.

2. **Compute signing fingerprints** for every key that will run the app:
   ```bash
   # all variants at once (debug + release if configured)
   ./gradlew :androidApp:signingReport

   # debug keystore explicitly (default password: android)
   keytool -list -v -alias androiddebugkey \
     -keystore ~/.android/debug.keystore -storepass android -keypass android
   ```
   For Play Store builds, also take the **SHA-256** from
   **Play Console → Setup → App integrity → App signing key certificate** (Google's managed key, not
   your upload key).

3. **Register the fingerprints** in Firebase (this project already uses Firebase for FCM, so use the
   same project): **Project settings → Your apps → Android app → Add fingerprint** — add the
   **SHA-1** and the **SHA-256** for debug, release/upload, and Play App Signing. This creates the
   Android OAuth client (`client_type: 1`) behind the scenes. Fingerprint changes take effect
   immediately, no republish.

4. **Get the Web client ID** (`client_type: 3`) — this is the `serverClientId` Credential Manager
   needs. Find it in either:
   - **Google Cloud Console → APIs & Services → Credentials → OAuth 2.0 Client IDs →
     "Web client (auto created by Google Service)"**, or
   - the `oauth_client` entry with `"client_type": 3` in `google-services.json`.
   > ⚠️ Use the **Web** client ID, never the Android one. Wrong ID ⇒ `DEVELOPER_ERROR (10)`.

5. **Put the Web client ID in `local.properties`** (already set in this repo):
   ```properties
   GOOGLE_WEB_CLIENT_ID=220287993884-xxxxxxxx.apps.googleusercontent.com
   ```
   It flows: `local.properties` → `BuildConfig.GOOGLE_WEB_CLIENT_ID` → `SsoConfig` (DI) →
   `WelcomeViewState.googleServerClientId` → `GetSignInWithGoogleOption.Builder(serverClientId)`.

6. **Re-download `google-services.json`** after adding fingerprints / enabling the Google provider,
   and keep it at `androidApp/google-services.json`.

7. **Test** on an emulator/device that has a Google account added (Settings → Accounts).

> Note: the legacy `GoogleSignInClient` / One-Tap `BeginSignInRequest` (`play-services-auth`) are
> deprecated. This app uses **AndroidX Credential Manager** (`GetSignInWithGoogleOption`), the
> current API.

---

## 4. Apple setup (iOS) — keys & Xcode, in order

Native Sign in with Apple needs an **Apple Developer Program** membership (the free tier can't enable
the capability).

1. **Add the capability in Xcode** — open `iosApp/iosApp.xcodeproj`, select the app target →
   **Signing & Capabilities → + Capability → Sign in with Apple**. This:
   - adds the `com.apple.developer.applesignin` entitlement (a starter `iosApp/iosApp/iosApp.entitlements`
     is already in the repo; Xcode will reference it via `CODE_SIGN_ENTITLEMENTS`), and
   - enables **Sign in with Apple** on your App ID (`com.yolo.myhabitshub`) in the Apple Developer
     portal automatically (with automatic signing).

2. **Confirm the App ID** in
   [developer.apple.com → Certificates, Identifiers & Profiles → Identifiers → `com.yolo.myhabitshub`]
   has **Sign in with Apple** checked (Primary App ID — no grouping needed).

3. **Bundle ID parity** — the Xcode target bundle id, the `GoogleService-Info.plist` `BUNDLE_ID`, and
   the `aud` your backend expects must all be `com.yolo.myhabitshub`.

4. **Test on a physical device.** Sign in with Apple is unreliable in the iOS Simulator.

### NOT needed for native iOS Apple sign-in
- ❌ Services ID, ❌ Apple private key (`.p8`), ❌ Key ID, ❌ Apple Team ID in any client config,
- ❌ return/redirect URL (`https://<project>.firebaseapp.com/__/auth/handler`),
- ❌ `REVERSED_CLIENT_ID` URL scheme (that's Google-on-iOS, which we don't do),
- ❌ hand-written nonce code (the client generates the raw nonce, sends `SHA-256(nonce)` to Apple, and
  forwards the raw nonce to your backend).

> The `.p8` private key + Key ID + Team ID + a Services ID are **only** required if your *backend*
> wants to exchange the `authorizationCode` with Apple's token endpoint (to get a server-side refresh
> token or to revoke on account deletion — required by App Store Guideline 5.1.1(v) for account
> deletion). Identity-token login itself does not need them.

---

## 5. Configuration checklist

| Item | Where | Status in repo |
|---|---|---|
| `GOOGLE_WEB_CLIENT_ID` (Web client ID) | `local.properties` | ✅ set |
| `google-services.json` (with SHA fingerprints) | `androidApp/` | ✅ present — re-download after adding SHAs |
| SHA-1 + SHA-256 registered | Firebase console | ⬜ verify all keystores |
| `GoogleService-Info.plist` | `iosApp/iosApp/` | ✅ present |
| Sign in with Apple capability + entitlement | Xcode target | ⬜ add in Xcode |
| App ID has Sign in with Apple | Apple Developer portal | ⬜ verify |
| `/api/auth/google` + `/api/auth/apple` endpoints | your backend | ⬜ implement (§2) |

---

## 6. Production edge cases (backend-side)

1. **Clock skew** — allow 2–5 min leeway on `iat`/`exp` when verifying Google/Apple tokens, or
   valid sign-ins on slightly-off devices fail as "not yet valid".
2. **Apple Hide-My-Email + first-login-only profile** — Apple returns `email`/`name` **only on the
   first authorization** and may return a `@privaterelay.appleid.com` address. Persist them on first
   login; treat the relay address as a valid unique handle; default a missing name to the email's
   local part.
3. **Account collision** — if `email` already exists under another provider, link the provider to the
   existing user (or return `409 Conflict`, which the app shows as "you already have an account").
4. **App Store Guideline 4.8 / 5.1.1(v)** — offering Google means you must also offer Apple on iOS
   (the app does), and you must support **account deletion** server-side (revoking the Apple grant
   needs the `authorizationCode` + Apple private key).

---

## 7. Troubleshooting

| Symptom | Cause → Fix |
|---|---|
| Google: `DEVELOPER_ERROR` / status `10` | Wrong `serverClientId` (Android instead of Web ID) → use the Web client ID. Or SHA-1/SHA-256 not registered / package mismatch / stale `google-services.json`. |
| Google: `NoCredentialException` / "No credentials available" | No Google account on the device, or stale Play Services → add an account, update Play Services. |
| Google: sheet dismissed | `GetCredentialCancellationException` → handled as `Cancelled` (no error shown). |
| Apple: button does nothing / "not entitled" | Capability missing, running on Simulator, or no Developer Program membership → add capability, test on device. |
| Apple: `MissingOrInvalidNonce` / nonce mismatch | Backend compared the wrong encoding → it must compute `SHA-256(rawNonce)` (the client sends the **raw** nonce) and compare to the token's `nonce` claim. |
| Apple: "audience … does not match" | Bundle id mismatch → keep Xcode bundle id == plist `BUNDLE_ID` == backend `aud` (`com.yolo.myhabitshub`). |

---

## 8. Testing

- **Android:** run on `emulator-5554` with a Google account added; tap **Sign in with Google** on the
  welcome screen → account chooser → on success the app enters the main flow.
- **iOS:** run on a **physical device** signed into iCloud; tap **Sign in with Apple** → Face/Touch ID
  → on success the app enters the main flow.
- Both require the backend endpoints (§2) to be live and returning the `AuthInfoResponse` shape.
