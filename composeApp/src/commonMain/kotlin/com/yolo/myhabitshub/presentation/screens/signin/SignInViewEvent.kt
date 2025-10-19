package com.yolo.myhabitshub.presentation.screens.signin

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface SignInViewEvent: ViewEvent {
    data object SignInSuccess : SignInViewEvent
}
