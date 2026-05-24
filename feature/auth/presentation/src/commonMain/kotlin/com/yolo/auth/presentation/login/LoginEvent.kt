package com.yolo.auth.presentation.login

import com.yolo.core.presentation.viewmodel.BaseViewModel

sealed interface LoginEvent : BaseViewModel.ViewEvent {
    data object LoginSuccessEvent : LoginEvent
    data object NavigateToForgotPasswordEvent : LoginEvent
    data object NavigateToRegisterEvent : LoginEvent
}