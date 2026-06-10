package com.yolo.auth.presentation.forgot_password

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface ForgotPasswordViewEvent : ViewEvent {
    data object NavigateBack : ForgotPasswordViewEvent
}
