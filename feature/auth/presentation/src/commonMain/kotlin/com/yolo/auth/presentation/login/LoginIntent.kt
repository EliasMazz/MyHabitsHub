package com.yolo.auth.presentation.login

import com.yolo.auth.presentation.register.RegisterViewIntent
import com.yolo.core.presentation.viewmodel.BaseViewModel

sealed interface LoginIntent : BaseViewModel.ViewIntent {
    data object OnTogglePasswordVisibility : LoginIntent
    data object OnForgotPasswordClick : LoginIntent
    data object OnLoginClick : LoginIntent
    data object OnSignupClick : LoginIntent
}