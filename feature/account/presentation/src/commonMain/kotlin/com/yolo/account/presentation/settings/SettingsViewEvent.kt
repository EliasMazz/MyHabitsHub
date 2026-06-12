package com.yolo.account.presentation.settings

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface SettingsViewEvent: ViewEvent {
    data object NavigateToSign: SettingsViewEvent
}