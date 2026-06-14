package com.yolo.auth.presentation.sso

/**
 * Outcome of a native social sign-in (Google on Android, Apple on iOS). The success variants carry
 * only the raw provider credentials — they are exchanged for the app session by the backend, so no
 * Firebase or provider SDK session is involved.
 */
sealed interface SocialSignInResult {
    /** Google ID token from AndroidX Credential Manager. */
    data class GoogleSuccess(val idToken: String) : SocialSignInResult

    /** Apple identity token + authorization code from ASAuthorization, plus the raw (un-hashed) nonce. */
    data class AppleSuccess(
        val identityToken: String,
        val authorizationCode: String,
        val nonce: String,
    ) : SocialSignInResult

    /** User dismissed the system sheet — not an error; show nothing. */
    data object Cancelled : SocialSignInResult

    /** The flow failed before producing a credential. */
    data class Failure(val message: String?) : SocialSignInResult
}
