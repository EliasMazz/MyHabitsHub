package com.yolo.myhabitshub.presentation.screens.account

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.*
import com.yolo.myhabitshub.data.model.UserResponse
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.title_screen_help_and_support
import com.yolo.myhabitshub.generated.resources.ic_settings_item_logout
import com.yolo.myhabitshub.generated.resources.ic_settings_item_support_legal
import com.yolo.myhabitshub.generated.resources.logout
import com.yolo.myhabitshub.presentation.components.SettingsItemUiState

data class AccountViewState(
    val settingsItemList: List<SettingsItemUiState> = listOf(
        SettingsItemUiState(
            startIcon = Res.drawable.ic_settings_item_support_legal,
            textRes = Res.string.title_screen_help_and_support
        ),

        SettingsItemUiState(
            startIcon = Res.drawable.ic_settings_item_logout,
            textRes = Res.string.logout,
            showEndIcon = false,
        ),
    ),
    val userResponse: UserResponse? = null,
    val isLogoutDialogVisible: Boolean = false,
    override val viewEvent: AccountViewEvent? = null,
): ViewState<AccountViewEvent> {
    override fun consumeEvent(): ViewState<AccountViewEvent> {
        return copy(viewEvent = null)
    }
}

