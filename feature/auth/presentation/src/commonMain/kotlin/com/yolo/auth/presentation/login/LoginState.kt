package com.yolo.auth.presentation.login

import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState

data class LoginState(
    val email: String,
    val password: String,
    val isPasswordVisible: Boolean = false,
    val isLoggingIn: Boolean = false,
    val canLogin: Boolean = false,
    val errorText: UiText? = null,
    override val viewEvent: LoginEvent? = null,
) : ViewState<LoginEvent> {
    override fun consumeEvent(): ViewState<LoginEvent> {
        return copy(viewEvent = null)
    }
}