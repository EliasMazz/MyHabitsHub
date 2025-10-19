package com.yolo.myhabitshub.presentation.screens.settings

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface SettingsViewEvent: ViewEvent {
    data object NavigateToSign: SettingsViewEvent
}