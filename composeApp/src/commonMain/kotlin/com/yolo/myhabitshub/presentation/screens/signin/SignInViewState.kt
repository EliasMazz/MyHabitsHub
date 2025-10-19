package com.yolo.myhabitshub.presentation.screens.signin

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewState

data class SignInViewState(
    val linkAccount : Boolean = true,
    val snackbarMessage : String? = null,
    override val viewEvent: SignInViewEvent? = null
): ViewState<SignInViewEvent>{
    override fun consumeEvent(): ViewState<SignInViewEvent> {
        return this.copy(viewEvent = null)
    }
}