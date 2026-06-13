package com.yolo.auth.presentation.email_verification

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface EmailVerificationViewIntent : ViewIntent {
    /** Success state's "Log In" and the token-failure state's "Back to log in" (§4.4#1). */
    data object OnLoginClick : EmailVerificationViewIntent

    /** Token-failure state's email field (§4.4#1). */
    data class OnEmailChange(val email: String) : EmailVerificationViewIntent

    /** Token-failure state's resend with §5.1 cooldown (§4.4#1). */
    data object OnResendClick : EmailVerificationViewIntent

    /** Network-failure state's retry — re-runs verification with the same token (§4.4#2). */
    data object OnRetryClick : EmailVerificationViewIntent
}
