package com.yolo.auth.presentation.email_verification

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface EmailVerificationViewEvent : ViewEvent {
    data object NavigateBackEvent : EmailVerificationViewEvent
    data object NavigateToLoginEvent : EmailVerificationViewEvent
}
