package com.yolo.auth.presentation.register_success

import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.viewmodel.BaseViewModel

data class RegisterSuccessViewState(
    val registeredEmail: String = "",
    val isResendingVerificationEmail: Boolean = false,
    /** §5.1 resend cooldown — 0 means the resend button is ready. */
    val resendCooldownSeconds: Int = 0,
    val resendVerificationEmailError: UiText? = null,
) : BaseViewModel.ViewState
