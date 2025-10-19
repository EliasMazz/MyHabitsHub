package com.yolo.myhabitshub.presentation.screens.settings

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewIntent

sealed interface SettingsViewIntent : ViewIntent {
    data object OnDeleteAccountClicked : SettingsViewIntent
    data object OnDeleteAccountDialogConfirmed : SettingsViewIntent
    data object OnDeleteAccountDialogDismissed : SettingsViewIntent
    data object OnErrorDialogConfirmed : SettingsViewIntent
}