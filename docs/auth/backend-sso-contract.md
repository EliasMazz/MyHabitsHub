# Backend Contract — Social Sign-In (Google + Apple)

Hand this to whoever implements the server. It is self-contained: you do **not** need the mobile
code. The mobile app is already done and calls exactly what's below.

## 0. What you're building

Two endpoints that take a **raw provider token from the mobile app**, **verify it yourself**
(against Google/Apple public keys — no Firebase, no Google/Apple SDK on your side), then return
**your app's own session** (the same `{accessToken, refreshToken, user}` your `/api/auth/login`
already returns). The app then uses those tokens exactly like a password login.

```
App (Android)  Google ID token ─┐
                                ├─▶  POST /api/auth/google|apple ─▶ you verify + resolve account
App (iOS)      Apple ID token ──┘                                    ─▶ 200 {accessToken, refreshToken, user}
```

There is **no client secret** anywhere in this flow. Don't ask the mobile team for one.

---

## 1. Endpoints

Both are **unauthenticated** (no `Authorization` bearer — the user has no session yet). They DO
receive the standard `x-api-key` header and `Content-Type: application/json`, same as every other
endpoint.

### `POST /api/auth/google`
Request body (exact field name):
```json
{ "idToken": "<Google ID token — a JWT>" }
```

### `POST /api/auth/apple`
Request body (exact field names):
```json
{
  "identityToken": "<Apple identity token — a JWT>",
  "authorizationCode": "<short-lived, single-use code>",
  "nonce": "<RAW nonce — NOT hashed; see §3>"
}
```

### Success response (BOTH endpoints) — `200 OK`
Must be **exactly** this shape. All fields are **required and non-null** — the app deserializes
strictly; a missing field is treated as a failure.
```json
{
  "accessToken": "<your app's JWT access token>",
  "refreshToken": "<your app's refresh token>",
  "user": {
    "id": "<your stable user id, string>",
    "email": "<user email, string>",
    "hasVerifiedEmail": true
  }
}
```
- `hasVerifiedEmail` → `true` for SSO users (the provider verified the email).
- `accessToken`/`refreshToken` must be the **same token system** as `/api/auth/login`, because the
  app refreshes via the existing `POST /api/auth/refresh` `{ "refreshToken": "..." }` → `200` with
  the same response shape. SSO and password users are indistinguishable after login.

---

## 2. Verify the Google ID token (`/api/auth/google`)

Use a maintained library (e.g. `google-auth` Python, `google-api-client` Java,
`google-auth-library-nodejs`) — don't hand-roll JWT verification. Validate ALL of:

1. **Signature** — RS256, against Google's JWKS `https://www.googleapis.com/oauth2/v3/certs`
   (match by the JWT header `kid`; cache keys per the response `Cache-Control`).
2. **`iss`** ∈ { `accounts.google.com`, `https://accounts.google.com` }.
3. **`aud`** == our **Web** OAuth client ID:
   `220287993884-2p9hfn15bskognngnthsv0qjrf9se88h.apps.googleusercontent.com`
   (the "Web application" client in the same Google Cloud project; **not** the Android client ID).
4. **`exp`** in the future (allow ±5 min clock skew).
5. **`email_verified`** — must be `true` to trust `email` for account-linking (§4).
6. Identity: use **`sub`** as the stable Google user id. Also read `email`, `name`, `picture`.

Reject (`401`) if signature/iss/aud/exp fail.

---

## 3. Verify the Apple identity token (`/api/auth/apple`)

Validate ALL of:

1. **Signature** — against Apple's JWKS `https://appleid.apple.com/auth/keys` (match by `kid`).
2. **`iss`** == `https://appleid.apple.com`.
3. **`aud`** == our iOS bundle id: `com.yolo.myhabitshub`.
4. **`exp`** in the future (Apple tokens live ~10 min; allow ±5 min skew).
5. **`nonce`** — THIS IS THE EASY ONE TO GET WRONG:
   - The app sends you the **raw** nonce in the `nonce` field (un-hashed).
   - Apple put `SHA-256(rawNonce)` **lowercase-hex** into the token's `nonce` claim (the app hashed
     it before sending to Apple; Apple copies it verbatim).
   - So you must compute `lowercase_hex(SHA-256(request.nonce))` and **constant-time compare** it to
     the token's `nonce` claim. They must be equal.
   - Encoding matters: it is **lowercase hex**, not base64url. (Mismatched encoding here is the
     single most common Apple-login bug.)
6. Identity: use **`sub`** as the stable Apple user id. Read `email` from the token claims.

Reject (`401`) if any check fails (incl. nonce mismatch).

Notes:
- Apple's `email` may be a private relay (`...@privaterelay.appleid.com`) — treat it as a valid,
  unique email.
- Apple sends the user's **name only on the very first authorization**, and only in a field the app
  does **not** forward. So you will generally NOT receive a name — default a display name from the
  email's local part.
- `authorizationCode` is **optional for login** — ignore it unless/until you implement Apple token
  revocation for account deletion (App Store Guideline 5.1.1(v)), which needs Apple's `.p8` key.

---

## 4. Account resolution & linking — the decision logic (DO THIS EXACTLY)

After the token is verified, you have a verified `provider` (`google`|`apple`), `sub`, `email`,
and `email_verified`. Resolve the user in this order:

```
1. Is there a user already LINKED to this (provider, sub)?
      → YES: log them in. Issue tokens. 200.        // returning SSO user — match by sub, not email

2. Else, is there a user with this email?
      ├─ provider email verified AND that user's email is verified?
      │       → YES: LINK (provider, sub) to that user, then log in. 200.   // safe auto-link
      └─ otherwise
              → 409 Conflict.                         // unsafe: do NOT silently link

3. Else (no user):
      → create a new user from the SSO identity (email, verified=true),
        store (provider, sub), log in. 200.
```

Why the guard in step 2: if you auto-link to an account whose email was **never verified**, an
attacker who pre-registered `victim@email` with a password would silently capture the victim's
Google/Apple login (account "pre-hijacking"). Only auto-link when **both** the provider and the
existing account have a **verified** email.

In practice this app already requires email verification for password signups, so step 2 will
almost always be the safe auto-link; `409` only fires for the genuine edge case.

Always **match returning users by `sub`** (step 1), not by email — emails can change, `sub` never
does. Store `sub` per provider on the user record.

---

## 5. Status codes & what the app does with each

| You return | App behavior |
|---|---|
| `200` + the body in §1 | Stores the session, enters the app. (Success — all of steps 1/2-link/3.) |
| `409 Conflict` | Shows "you already have an account" (the unsafe-link case in §4 step 2). |
| `401` / `403` | Shows a generic "could not sign you in" error. (Invalid/expired token, etc.) |
| any other non-2xx | Generic error. |

Do not invent other success shapes — only `200` + the exact body logs the user in.

---

## 6. Hard do / don't

**DO**
- Verify the token **signature first**, then claims, before trusting anything.
- Use `aud` = our **Web** client ID (Google) / **bundle id** `com.yolo.myhabitshub` (Apple).
- Treat `nonce` as the **raw** value and compare `lowercase_hex(SHA-256(nonce))` to the claim.
- Return the **full** `{accessToken, refreshToken, user{id,email,hasVerifiedEmail}}`, all non-null.
- Issue the **same** token type as `/api/auth/login` (must work with `/api/auth/refresh`).
- Match returning users by `(provider, sub)`; store it.

**DON'T**
- Don't expect a client secret, an `Authorization` bearer on these two endpoints, or any Firebase.
- Don't return only a `user` object (an earlier draft of `/api/auth/google` did — that does **not**
  log the user in; it needs the tokens).
- Don't trust `email` when `email_verified` is false.
- Don't auto-link to an unverified existing account (use `409`).
- Don't match returning users by email alone.

---

## 7. Quick test checklist

- New email, never seen → `200`, new account created.
- Same SSO user signs in again → `200`, same account (matched by `sub`).
- Email exists as a **verified** password account → `200`, provider linked, same account.
- Email exists as an **unverified** password account → `409`.
- Tampered/expired token, wrong `aud`, or wrong `nonce` → `401`.
