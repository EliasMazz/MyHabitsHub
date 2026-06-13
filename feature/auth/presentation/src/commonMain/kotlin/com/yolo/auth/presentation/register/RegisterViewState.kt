package com.yolo.auth.presentation.register

import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.viewmodel.BaseViewModel

data class RegisterViewState(
    val email: String = "",
    val emailError: UiText? = null,
    /** V3 positive validation: drives the green-check trailing icon, written on every keystroke. */
    val isEmailValid: Boolean = false,
    val password: String = "",
    val passwordError: UiText? = null,
    /** E2: request-level (server) errors only — rendered in the top banner. */
    val registrationError: UiText? = null,
    /** True when [registrationError] is the account-exists conflict, enabling the E3 recovery action. */
    val isAccountConflict: Boolean = false,
    val isLoading: Boolean = false,
    /** K5: password is shown by default during creation. */
    val isPasswordVisible: Boolean = true,
) : BaseViewModel.ViewState
