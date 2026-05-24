package com.yolo.auth.presentation.register

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface RegisterViewEvent : ViewEvent {
    data class NavigateToRegisterSuccessEvent(val email: String) : RegisterViewEvent
    data object NavigateToLoginEvent : RegisterViewEvent
}