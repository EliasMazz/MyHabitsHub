package com.yolo.myhabitshub.presentation.screens.helpandsupport

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewState
import com.yolo.myhabitshub.presentation.components.SettingsItemUiState

data class HelpAndSupportState(
    val settingsItemUiState: List<SettingsItemUiState>,
    override val viewEvent: HelpAndSupportEvent? = null
): ViewState<HelpAndSupportEvent> {
    override fun consumeEvent(): ViewState<HelpAndSupportEvent> {
        return copy(viewEvent = null)
    }
}
