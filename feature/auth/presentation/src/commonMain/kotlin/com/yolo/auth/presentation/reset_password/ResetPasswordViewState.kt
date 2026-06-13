package com.yolo.auth.presentation.reset_password

import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.viewmodel.BaseViewModel

data class ResetPasswordViewState(
    val password: String = "",
    val passwordError: UiText? = null,
    // Shown by default at creation time (K5, auth-screens-improvement-spec §4.6#4).
    val isPasswordVisible: Boolean = true,
    val isLoading: Boolean = false,
    // Request-level (server) errors ONLY — never field errors, never token errors (E2/§4.6#6).
    val errorText: UiText? = null,
    val isSuccess: Boolean = false,
    // Dedicated full-screen recovery state for a blank/expired/invalid token (§4.6#1/#2).
    val isTokenInvalid: Boolean = false,
) : BaseViewModel.ViewState
