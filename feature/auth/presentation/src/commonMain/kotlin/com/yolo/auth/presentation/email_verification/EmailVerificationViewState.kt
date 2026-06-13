package com.yolo.auth.presentation.email_verification

import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.viewmodel.BaseViewModel

data class EmailVerificationViewState(
    val isVerifying: Boolean = false,
    val isVerified: Boolean = false,
    // NO_INTERNET/REQUEST_TIMEOUT only (spec §4.4#2): a network blip must not read as
    // "your link is dead" — it gets distinct copy and a retry with the same token.
    val isNetworkFailure: Boolean = false,
    // Token-failure recovery (spec §4.4#1): the deep link only carries a token, so the
    // user supplies the address the verification email should be resent to.
    val email: String = "",
    val emailError: UiText? = null,
    val isResending: Boolean = false,
    val resendError: UiText? = null,
    // §5.1 cooldown: 0 on entry (nothing was just sent from this screen), 30s after the
    // first resend, 60s after each subsequent one.
    val resendCooldownSeconds: Int = 0,
) : BaseViewModel.ViewState
