package com.yolo.auth.presentation.register

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface RegisterViewEvent: ViewEvent {
    data class OnRegisterSuccess(val email: String) : RegisterViewEvent
}