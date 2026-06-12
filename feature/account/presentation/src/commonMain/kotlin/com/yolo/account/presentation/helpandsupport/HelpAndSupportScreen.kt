package com.yolo.account.presentation.helpandsupport

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.yolo.core.presentation.BaseScreen
import myhabitshub.feature.account.presentation.generated.resources.Res
import myhabitshub.feature.account.presentation.generated.resources.item_contact_support
import myhabitshub.feature.account.presentation.generated.resources.privacy_policy
import myhabitshub.feature.account.presentation.generated.resources.terms_conditions
import com.yolo.account.presentation.components.SettingItemListContainer
import com.yolo.account.presentation.components.SettingsItemViewData
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.presentation.util.AppUtil
import com.yolo.core.domain.Constants
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HelpAndSupportScreen(
    viewModel: HelpAndSupportViewModel = koinViewModel(),
) {
    val localUriHandler = LocalUriHandler.current
    val appUtil = koinInject<AppUtil>()

    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                HelpAndSupportEvent.OpenFeedbackMail -> appUtil.openFeedbackMail()
                HelpAndSupportEvent.OpenPrivacyPoliceUri -> localUriHandler.openUri(Constants.URL_PRIVACY_POLICY)
                HelpAndSupportEvent.OpenTermsAndConditionsUri -> localUriHandler.openUri(Constants.URL_TERMS_CONDITIONS)
            }
        }
    ) { state, onIntent ->
        HelpAndSupportScreenContent(
            modifier = Modifier.fillMaxSize(),
            itemList = state.settingsItemViewData,
            onContactSupportClicked = { onIntent(HelpAndSupportViewIntent.OnContactSupportClicked) },
            onTermsAndConditionsClicked = { onIntent(HelpAndSupportViewIntent.OnTermsAndConditionsClicked) },
            onPrivacyPolicyClicked = { onIntent(HelpAndSupportViewIntent.OnPrivacyPolicyClicked) }
        )
    }
}

@Composable
fun HelpAndSupportScreenContent(
    modifier: Modifier = Modifier,
    itemList: List<SettingsItemViewData>,
    onContactSupportClicked : () -> Unit,
    onPrivacyPolicyClicked : () -> Unit,
    onTermsAndConditionsClicked : () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
            .padding(horizontal = YoloTokens.spacing.screenEdge)
            .verticalScroll(rememberScrollState())
            .padding(
                top = YoloTokens.spacing.elementGap,
                bottom = YoloTokens.spacing.screenEdge,
            )
    ) {
        SettingItemListContainer(
            itemList = itemList,
            itemTextStyle = MaterialTheme.typography.titleLarge,
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
