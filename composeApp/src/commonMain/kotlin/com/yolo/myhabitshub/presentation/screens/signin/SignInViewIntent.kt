package com.yolo.myhabitshub.presentation.screens.signin

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewIntent

sealed class SignInViewIntent : ViewIntent {
    data object OnSignInSuccess : SignInViewIntent()
    data class OnSignInFail(val throwable: Throwable?) : SignInViewIntent()
}
