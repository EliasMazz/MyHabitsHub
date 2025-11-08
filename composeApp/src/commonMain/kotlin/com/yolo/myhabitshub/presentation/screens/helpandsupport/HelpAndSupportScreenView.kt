package com.yolo.myhabitshub.presentation.screens.helpandsupport

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.item_contact_support
import com.yolo.myhabitshub.generated.resources.privacy_policy
import com.yolo.myhabitshub.generated.resources.terms_conditions
import com.yolo.myhabitshub.presentation.components.SettingItemListContainer
import com.yolo.myhabitshub.presentation.components.SettingsItemViewData
import com.yolo.myhabitshub.core.presentation.theme.AppTheme

@Composable
fun HelpAndSupportScreen(
    modifier: Modifier = Modifier,
    itemList: List<SettingsItemViewData>,
    onContactSupportClicked : () -> Unit,
    onPrivacyPolicyClicked : () -> Unit,
    onTermsAndConditionsClicked : () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
            .padding(horizontal = AppTheme.spacing.outerSpacing)
            .verticalScroll(rememberScrollState())
            .padding(
                top = AppTheme.spacing.defaultSpacing,
                bottom = AppTheme.spacing.outerSpacing,
            )
    ) {
        SettingItemListContainer(
            itemList = itemList,
            itemTextStyle = AppTheme.typography.h5.copy(fontWeight = FontWeight.SemiBold),
            onClick = {
                when (it.textRes) {
                    Res.string.item_contact_support -> onContactSupportClicked()
                    Res.string.privacy_policy -> onPrivacyPolicyClicked()
                    Res.string.terms_conditions -> onTermsAndConditionsClicked()
                }
            }
        )
    }
}
