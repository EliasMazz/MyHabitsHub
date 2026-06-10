package com.yolo.myhabitshub.presentation.screens.helpandsupport

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState
import com.yolo.myhabitshub.presentation.components.SettingsItemViewData

data class HelpAndSupportViewState(
    val settingsItemViewData: List<SettingsItemViewData>,
): ViewState
