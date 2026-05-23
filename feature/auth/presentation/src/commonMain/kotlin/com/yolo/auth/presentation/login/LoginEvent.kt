package com.yolo.auth.presentation.login

import com.yolo.core.presentation.viewmodel.BaseViewModel

sealed interface LoginEvent : BaseViewModel.ViewEvent {
    data object OnSuccessLogin : LoginEvent
    data object OnForgotPassword : LoginEvent
    data object OnRegister : LoginEvent
}