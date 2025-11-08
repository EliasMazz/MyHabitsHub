package com.yolo.myhabitshub.presentation.screens.signin

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewIntent

sealed interface SignInViewIntent : ViewIntent {
    data object OnSignInSuccess : SignInViewIntent
    data class OnSignInFail(val throwable: Throwable?) : SignInViewIntent
}
