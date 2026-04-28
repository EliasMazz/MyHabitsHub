package com.yolo.auth.presentation.email_verification

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface EmailVerificationViewIntent : ViewIntent {
    data object OnLoginClick : EmailVerificationViewIntent
    data object OnCloseClick : EmailVerificationViewIntent
}