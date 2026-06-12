package com.yolo.account.presentation.account

import com.yolo.core.presentation.viewmodel.BaseViewModel.*
import com.yolo.account.domain.entities.UserResponse
import myhabitshub.feature.account.presentation.generated.resources.title_screen_help_and_support
import myhabitshub.feature.account.presentation.generated.resources.logout
import com.yolo.account.presentation.components.SettingsAction
import com.yolo.account.presentation.components.SettingsItemViewData
import myhabitshub.core.designsystem.generated.resources.Res as R
import myhabitshub.feature.account.presentation.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_settings_item_logout
import myhabitshub.core.designsystem.generated.resources.ic_settings_item_support_legal

data class AccountViewState(
    val settingsItemList: List<SettingsItemViewData> = listOf(
        SettingsItemViewData(
            action = SettingsAction.HELP_AND_SUPPORT,
            startIcon = R.drawable.ic_settings_item_support_legal,
            textRes = Res.string.title_screen_help_and_support,
        ),
        SettingsItemViewData(
            action = SettingsAction.LOGOUT,
            startIcon = R.drawable.ic_settings_item_logout,
            textRes = Res.string.logout,
            showEndIcon = false,
        ),
    ),
    val userResponse: UserResponse? = null,
    val isLogoutDialogVisible: Boolean = false,
): ViewState

