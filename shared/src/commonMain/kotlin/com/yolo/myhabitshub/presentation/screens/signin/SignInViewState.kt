package com.yolo.myhabitshub.presentation.screens.signin

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState

data class SignInViewState(
    val linkAccount : Boolean = true,
    val snackbarMessage : String? = null,
): ViewState