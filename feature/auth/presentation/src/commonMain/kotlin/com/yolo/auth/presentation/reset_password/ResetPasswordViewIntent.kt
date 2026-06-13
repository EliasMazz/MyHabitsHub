package com.yolo.auth.presentation.reset_password

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewIntent

sealed interface ResetPasswordViewIntent : ViewIntent {
    data class OnPasswordChange(val password: String) : ResetPasswordViewIntent
    data object OnTogglePasswordVisibility : ResetPasswordViewIntent
    data object OnSubmitClick : ResetPasswordViewIntent
    data object OnLogInClick : ResetPasswordViewIntent

    /** Token-invalid recovery exit → ForgotPassword (auth-screens-improvement-spec §4.6#1). */
    data object OnRequestNewLinkClick : ResetPasswordViewIntent
}
