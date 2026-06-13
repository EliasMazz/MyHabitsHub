package com.yolo.auth.presentation.login

import com.yolo.core.presentation.viewmodel.BaseViewModel

sealed interface LoginEvent : BaseViewModel.ViewEvent {
    data object LoginSuccessEvent : LoginEvent

    /** N2/N4: carries the typed email so ForgotPassword can prefill it (pass-through only). */
    data class NavigateToForgotPasswordEvent(val email: String) : LoginEvent

    /** N3: carries the typed email so Register can prefill it. */
    data class NavigateToRegisterEvent(val email: String) : LoginEvent

    data object NavigateBackEvent : LoginEvent

    /** V4: after a failed submit, the first invalid field receives focus. */
    data class RequestFieldFocusEvent(val field: LoginField) : LoginEvent

    enum class LoginField { EMAIL, PASSWORD }
}
