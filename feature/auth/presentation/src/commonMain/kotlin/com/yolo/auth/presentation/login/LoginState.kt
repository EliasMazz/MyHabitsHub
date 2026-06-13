package com.yolo.auth.presentation.login

import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState

/**
 * Login UI state (auth-screens-improvement-spec §4.1).
 *
 * Error geography (E1/E2): [emailError]/[passwordError] live at their fields;
 * [errorText] is the top banner, reserved for request-level (server) errors only.
 */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoggingIn: Boolean = false,
    val errorText: UiText? = null,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    /** V3: positive-only live feedback — drives the green check on the email field. */
    val isEmailFormatValid: Boolean = false,
    /** E3: true when the server said the email isn't verified — the banner offers a resend action. */
    val showResendVerification: Boolean = false,
    val isResendingVerification: Boolean = false,
    /** §5.1: live resend cooldown, VM-owned ticker; 0 = ready. */
    val resendCooldownSeconds: Int = 0,
) : ViewState
