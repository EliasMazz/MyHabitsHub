package com.yolo.auth.presentation

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface RegisterViewEvent: ViewEvent {
    data class Success(val email: String) : RegisterViewEvent
}