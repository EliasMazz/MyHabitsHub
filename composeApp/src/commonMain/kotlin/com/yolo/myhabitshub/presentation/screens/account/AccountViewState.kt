package com.yolo.myhabitshub.presentation.screens.account

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.*
import com.yolo.myhabitshub.data.model.UserResponse
import com.yolo.myhabitshub.generated.resources.title_screen_help_and_support
import com.yolo.myhabitshub.generated.resources.logout
import com.yolo.myhabitshub.presentation.components.SettingsItemViewData
import myhabitshub.core.designsystem.generated.resources.Res as R
import com.yolo.myhabitshub.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_settings_item_logout
import myhabitshub.core.designsystem.generated.resources.ic_settings_item_support_legal

data class AccountViewState(
    val settingsItemList: List<SettingsItemViewData> = listOf(
        SettingsItemViewData(
            startIcon = R.drawable.ic_settings_item_support_legal,
            textRes = Res.string.title_screen_help_and_support
        ),

        SettingsItemViewData(
            startIcon = R.drawable.ic_settings_item_logout,
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

