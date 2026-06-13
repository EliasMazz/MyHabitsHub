package com.yolo.auth.presentation.register

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewIntent

sealed interface RegisterViewIntent : ViewIntent {
    data class OnEmailChange(val email: String) : RegisterViewIntent
    data class OnEmailFocusChanged(val isFocused: Boolean) : RegisterViewIntent
    data class OnPasswordChange(val password: String) : RegisterViewIntent
    data object OnTogglePasswordVisibility : RegisterViewIntent
    data object OnRegisterClick : RegisterViewIntent
    data object OnLoginClick : RegisterViewIntent

    /** E3 recovery action on the account-exists banner (spec 4.2#5). */
    data object OnLoginInsteadClick : RegisterViewIntent
    data object OnBackClick : RegisterViewIntent
}
