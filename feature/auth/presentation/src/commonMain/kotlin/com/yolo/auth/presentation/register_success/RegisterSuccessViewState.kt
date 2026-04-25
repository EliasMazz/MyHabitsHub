package com.yolo.auth.presentation.register_success

import com.yolo.auth.presentation.register.RegisterViewEvent
import com.yolo.core.presentation.viewmodel.BaseViewModel

data class RegisterSuccessViewState(

    override val viewEvent: RegisterSuccessViewEvent? = null,
) : BaseViewModel.ViewState<RegisterSuccessViewEvent> {
    override fun consumeEvent(): BaseViewModel.ViewState<RegisterSuccessViewEvent> {
        return copy(viewEvent = null)
    }
}