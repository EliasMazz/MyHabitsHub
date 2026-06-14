package com.yolo.auth.presentation.sso

import androidx.compose.runtime.Composable

/**
 * Returns a launcher that starts the native **Google** sign-in (AndroidX Credential Manager).
 * Implemented only on Android; the iOS actual returns a no-op (Google is never shown on iOS).
 *
 * @param serverClientId the OAuth Web client ID ([com.yolo.core.domain.auth.SsoConfig.googleWebServerClientId]).
 */
@Composable
expect fun rememberGoogleSignIn(
    serverClientId: String,
    onResult: (SocialSignInResult) -> Unit,
): () -> Unit

/**
 * Returns a launcher that starts the native **Apple** sign-in (AuthenticationServices /
 * ASAuthorization). Implemented only on iOS; the Android actual returns a no-op (Apple is never
 * shown on Android).
 */
@Composable
expect fun rememberAppleSignIn(
    onResult: (SocialSignInResult) -> Unit,
): () -> Unit
