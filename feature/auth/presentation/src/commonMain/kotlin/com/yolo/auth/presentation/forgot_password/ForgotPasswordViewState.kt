package com.yolo.auth.presentation.forgot_password

import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.viewmodel.BaseViewModel

data class ForgotPasswordViewState(
    val email: String = "",
    val emailError: UiText? = null,
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val isEmailSentSuccessfully: Boolean = false,
    val canSubmit: Boolean = false,
) : BaseViewModel.ViewState
