package com.yolo.auth.presentation.sso

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

/**
 * Google Sign-In via AndroidX **Credential Manager** (`GetSignInWithGoogleOption`, the explicit
 * "Sign in with Google" button flow — always shows the account chooser). Yields the raw Google ID
 * token, which the caller exchanges with the backend. No Firebase, no Play-Services GoogleSignIn.
 *
 * [LocalContext] here is the host Activity (the app sets content directly in `AppActivity`), which
 * is what Credential Manager needs to present its UI.
 */
@Composable
actual fun rememberGoogleSignIn(
    serverClientId: String,
    onResult: (SocialSignInResult) -> Unit,
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentOnResult by rememberUpdatedState(onResult)
    // Guards the window between tapping the button and the system sheet returning — during which
    // the VM's isAuthenticating flag (which disables the button) is not yet set.
    val inFlight = remember { InFlight() }
    return remember(context, serverClientId) {
        launcher@{
            if (inFlight.value) return@launcher
            inFlight.value = true
            scope.launch {
                // Everything that can throw — getCredential AND GoogleIdTokenCredential.createFrom
                // (which throws GoogleIdTokenParsingException on a malformed credential) — runs inside
                // runCatching so no exception escapes the coroutine.
                val result = runCatching {
                    val credentialManager = CredentialManager.create(context)
                    val googleOption = GetSignInWithGoogleOption.Builder(serverClientId).build()
                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleOption)
                        .build()
                    val credential = credentialManager.getCredential(context, request).credential
                    if (credential is CustomCredential &&
                        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                    ) {
                        GoogleIdTokenCredential.createFrom(credential.data).idToken
                    } else {
                        null
                    }
                }.fold(
                    onSuccess = { idToken ->
                        if (idToken != null) {
                            SocialSignInResult.GoogleSuccess(idToken)
                        } else {
                            SocialSignInResult.Failure("Unexpected credential type")
                        }
                    },
                    onFailure = { error ->
                        when (error) {
                            is GetCredentialCancellationException -> SocialSignInResult.Cancelled
                            else -> SocialSignInResult.Failure(error.message)
                        }
                    },
                )
                inFlight.value = false
                currentOnResult(result)
            }
        }
    }
}

/** Simple non-observable in-flight flag (mutated only on the main thread). */
private class InFlight {
    var value: Boolean = false
}

/** Apple sign-in is never shown on Android. */
@Composable
actual fun rememberAppleSignIn(
    onResult: (SocialSignInResult) -> Unit,
): () -> Unit = remember { {} }
