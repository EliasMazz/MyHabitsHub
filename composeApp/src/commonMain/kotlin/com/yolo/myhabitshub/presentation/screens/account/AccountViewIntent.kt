package com.yolo.myhabitshub.presentation.screens.account

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel
import com.yolo.myhabitshub.presentation.components.SettingsItemViewData

sealed interface AccountViewIntent: BaseViewModel.ViewIntent {
    data class OnSettingsItemClicked(val item: SettingsItemViewData) : AccountViewIntent
    data object OnHelpAndSupportClicked : AccountViewIntent
    data object OnLogoutDialogConfirmed : AccountViewIntent
    data object OnLogoutDialogDismissed : AccountViewIntent
    data object OnSignInClicked : AccountViewIntent
    data object OnProfileClicked : AccountViewIntent
}