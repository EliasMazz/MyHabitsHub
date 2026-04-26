package com.yolo.auth.presentation.register_success

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface RegisterSuccessViewEvent: ViewEvent {
    data object NavigateToLogin : RegisterSuccessViewEvent
}
