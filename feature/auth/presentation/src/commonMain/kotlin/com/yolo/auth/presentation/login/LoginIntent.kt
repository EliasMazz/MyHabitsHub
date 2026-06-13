package com.yolo.auth.presentation.login

import com.yolo.core.presentation.viewmodel.BaseViewModel

sealed interface LoginIntent : BaseViewModel.ViewIntent {
    data object OnTogglePasswordVisibility : LoginIntent
    data object OnForgotPasswordClick : LoginIntent
    data object OnLoginClick : LoginIntent
    data object OnSignupClick : LoginIntent
    data object OnBackClick : LoginIntent
    data object OnResendVerificationClick : LoginIntent
    data class OnEmailChange(val email: String) : LoginIntent
    data class OnPasswordChange(val password: String) : LoginIntent

    /** V1: email format errors appear on blur, never on keystroke. */
    data class OnEmailFocusChanged(val isFocused: Boolean) : LoginIntent
}
