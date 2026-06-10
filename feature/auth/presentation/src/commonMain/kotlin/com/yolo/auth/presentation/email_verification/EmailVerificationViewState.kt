package com.yolo.auth.presentation.email_verification

import com.yolo.core.presentation.viewmodel.BaseViewModel

data class EmailVerificationViewState(
    val isVerifying: Boolean = false,
    val isVerified: Boolean = false,
) : BaseViewModel.ViewState