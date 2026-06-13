package com.yolo.auth.presentation.welcome

import com.yolo.core.presentation.viewmodel.BaseViewModel

data class WelcomeViewState(
    val showGoogleButton: Boolean = false,
    val showAppleButton: Boolean = false,
) : BaseViewModel.ViewState
