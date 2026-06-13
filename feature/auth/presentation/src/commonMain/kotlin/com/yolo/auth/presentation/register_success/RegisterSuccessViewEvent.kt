package com.yolo.auth.presentation.register_success

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface RegisterSuccessViewEvent : ViewEvent {
    data object ResendVerificationEmailSuccessEvent : RegisterSuccessViewEvent

    /** 4.3#4 (N1): Log In prefills the just-registered address. */
    data class NavigateToLoginEvent(val email: String) : RegisterSuccessViewEvent

    /** 4.3#5 (N3): wrong-email escape returns to Register prefilled for one-tap correction. */
    data class NavigateToRegisterEvent(val email: String) : RegisterSuccessViewEvent
}
