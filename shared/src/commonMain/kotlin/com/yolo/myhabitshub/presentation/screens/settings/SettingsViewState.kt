package com.yolo.myhabitshub.presentation.screens.settings

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState
import com.yolo.myhabitshub.data.model.UserResponse

data class SettingsViewState(
    val isLoading: Boolean = false,
    val userResponse: UserResponse? = null,
    val deleteUserDialogShown: Boolean = false,
    val errorMessage: String? = null,
): ViewState
