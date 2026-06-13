package com.yolo.auth.presentation.reset_password

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface ResetPasswordViewEvent : ViewEvent {
    data object NavigateToLogin : ResetPasswordViewEvent

    /** Token-invalid recovery: "Request a new link" → ForgotPassword (spec §4.6#1). */
    data object NavigateToForgotPassword : ResetPasswordViewEvent
}
