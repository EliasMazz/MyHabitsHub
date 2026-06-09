package com.yolo.myhabitshub.presentation.screens.root

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState

data class RootViewState(
    val isCheckingAuth: Boolean = true,
    val isLoggedIn: Boolean = false,
    override val viewEvent: RootViewEvent? = null
): ViewState<RootViewEvent>{
    override fun consumeEvent(): ViewState<RootViewEvent> {
        return copy(viewEvent = null)
    }
}