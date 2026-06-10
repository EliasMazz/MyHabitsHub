package com.yolo.myhabitshub.app

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState

data class AppViewState(
    val isCheckingAuth: Boolean = true,
    val isLoggedIn: Boolean = false,
): ViewState