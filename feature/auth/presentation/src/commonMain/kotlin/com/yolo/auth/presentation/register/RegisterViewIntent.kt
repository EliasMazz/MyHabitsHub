package com.yolo.auth.presentation.register

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface RegisterViewIntent : ViewIntent {
    data class OnEmailChange(val email: String) : RegisterViewIntent
    data class OnPasswordChange(val password: String) : RegisterViewIntent
    data object OnLogicClick : RegisterViewIntent
    data object OnInputTextFocusGain : RegisterViewIntent
    data object OnRegisterClick : RegisterViewIntent
    data object OnTogglePasswordVisibility : RegisterViewIntent
}