package com.yolo.auth.presentation.register_success

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface RegisterSuccessViewIntent : ViewIntent {
    data object OnLoginClick : RegisterSuccessViewIntent
    data object OnResendVerificationEmailClick : RegisterSuccessViewIntent
}