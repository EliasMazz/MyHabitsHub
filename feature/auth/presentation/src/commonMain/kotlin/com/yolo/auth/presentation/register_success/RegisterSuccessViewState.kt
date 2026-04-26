package com.yolo.auth.presentation.register_success

import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.viewmodel.BaseViewModel

data class RegisterSuccessViewState(
    val registeredEmail: String = "",
    val isResendingVerificationEmail: Boolean = false,
    val resendVerificationEmailError: UiText? = null,

    override val viewEvent: RegisterSuccessViewEvent? = null,
) : BaseViewModel.ViewState<RegisterSuccessViewEvent> {
    override fun consumeEvent(): BaseViewModel.ViewState<RegisterSuccessViewEvent> {
        return copy(viewEvent = null)
    }
}