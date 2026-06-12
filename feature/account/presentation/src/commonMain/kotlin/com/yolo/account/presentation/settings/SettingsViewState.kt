package com.yolo.account.presentation.settings

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState
import com.yolo.account.domain.entities.UserResponse

data class SettingsViewState(
    val isLoading: Boolean = false,
    val userResponse: UserResponse? = null,
    val deleteUserDialogShown: Boolean = false,
    val errorMessage: String? = null,
): ViewState
