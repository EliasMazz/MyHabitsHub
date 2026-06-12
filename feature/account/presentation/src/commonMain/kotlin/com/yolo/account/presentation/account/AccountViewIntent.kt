package com.yolo.account.presentation.account

import com.yolo.core.presentation.viewmodel.BaseViewModel
import com.yolo.account.presentation.components.SettingsItemViewData

sealed interface AccountViewIntent: BaseViewModel.ViewIntent {
    data class OnSettingsItemClicked(val item: SettingsItemViewData) : AccountViewIntent
    data object OnHelpAndSupportClicked : AccountViewIntent
    data object OnLogoutDialogConfirmed : AccountViewIntent
    data object OnLogoutDialogDismissed : AccountViewIntent
    data object OnSignInClicked : AccountViewIntent
    data object OnProfileClicked : AccountViewIntent
}