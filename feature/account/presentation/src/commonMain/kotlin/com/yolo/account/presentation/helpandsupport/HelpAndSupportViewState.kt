package com.yolo.account.presentation.helpandsupport

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState
import com.yolo.account.presentation.components.SettingsItemViewData

data class HelpAndSupportViewState(
    val settingsItemViewData: List<SettingsItemViewData>,
): ViewState
