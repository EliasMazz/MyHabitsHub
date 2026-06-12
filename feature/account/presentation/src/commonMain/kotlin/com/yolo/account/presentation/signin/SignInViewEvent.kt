package com.yolo.account.presentation.signin

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface SignInViewEvent: ViewEvent {
    data object SignInSuccess : SignInViewEvent
}
