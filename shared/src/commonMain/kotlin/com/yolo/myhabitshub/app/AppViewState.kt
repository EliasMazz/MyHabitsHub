package com.yolo.myhabitshub.app

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState

data class AppViewState(
    val isCheckingAuth: Boolean = true,
    val isLoggedIn: Boolean = false,
    override val viewEvent: AppViewEvent? = null
): ViewState<AppViewEvent>{
    override fun consumeEvent(): ViewState<AppViewEvent> {
        return copy(viewEvent = null)
    }
}