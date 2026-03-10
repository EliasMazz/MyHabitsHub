package com.yolo.auth.presentation

import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.viewmodel.BaseViewModel.*

data class RegisterViewState(
    val email: String = "",
    val isEmailValid: Boolean = false,
    val emailError: UiText? = null,
    val password: String = "",
    val isPasswordValid: Boolean = false,
    val passwordError: UiText? = null,
    val registrationError: UiText? = null,
    val isLoading: Boolean = false,
    val canRegister : Boolean = true,
    val isPasswordVisible: Boolean = false,
    override val viewEvent: RegisterViewEvent? = null,
) : ViewState<RegisterViewEvent> {
    override fun consumeEvent(): ViewState<RegisterViewEvent> {
        return copy(viewEvent = null)
    }
}


