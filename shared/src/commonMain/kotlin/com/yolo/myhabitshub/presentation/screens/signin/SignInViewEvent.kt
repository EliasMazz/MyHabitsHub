package com.yolo.myhabitshub.presentation.screens.signin

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface SignInViewEvent: ViewEvent {
    data object SignInSuccess : SignInViewEvent
}
