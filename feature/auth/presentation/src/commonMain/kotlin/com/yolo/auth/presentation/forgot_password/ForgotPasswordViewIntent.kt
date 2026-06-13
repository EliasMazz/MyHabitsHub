package com.yolo.auth.presentation.forgot_password

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface ForgotPasswordViewIntent : ViewIntent {
    data class OnEmailChange(val email: String) : ForgotPasswordViewIntent
    data class OnEmailFocusChanged(val isFocused: Boolean) : ForgotPasswordViewIntent
    data object OnSubmitClick : ForgotPasswordViewIntent
    data object OnResendClick : ForgotPasswordViewIntent
    data object OnBackClick : ForgotPasswordViewIntent
    data object OnBackToLoginClick : ForgotPasswordViewIntent
}
