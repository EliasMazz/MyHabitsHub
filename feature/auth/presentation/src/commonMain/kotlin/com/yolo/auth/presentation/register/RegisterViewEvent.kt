package com.yolo.auth.presentation.register

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface RegisterViewEvent : ViewEvent {
    data class NavigateToRegisterSuccessEvent(val email: String) : RegisterViewEvent

    /** Carries the typed email so Login is prefilled (N1) — sent by both "Log In" and "Log in instead". */
    data class NavigateToLoginEvent(val email: String) : RegisterViewEvent
    data object NavigateBackEvent : RegisterViewEvent

    /** V4: on failed submit, the first invalid field receives focus (and is scrolled into view). */
    data class FocusFirstInvalidFieldEvent(val field: RegisterFormField) : RegisterViewEvent
}

enum class RegisterFormField { EMAIL, PASSWORD }
