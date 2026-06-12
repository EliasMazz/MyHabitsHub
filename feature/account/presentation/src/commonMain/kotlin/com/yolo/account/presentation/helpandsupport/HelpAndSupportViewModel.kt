package com.yolo.account.presentation.helpandsupport

import com.yolo.core.presentation.viewmodel.BaseViewModel
import myhabitshub.feature.account.presentation.generated.resources.Res
import myhabitshub.feature.account.presentation.generated.resources.item_contact_support
import myhabitshub.feature.account.presentation.generated.resources.privacy_policy
import myhabitshub.feature.account.presentation.generated.resources.terms_conditions
import com.yolo.account.presentation.components.SettingsAction
import com.yolo.account.presentation.components.SettingsItemViewData

class HelpAndSupportViewModel :
    BaseViewModel<HelpAndSupportViewIntent, HelpAndSupportViewState, HelpAndSupportEvent>(
        initialState = HelpAndSupportViewState(
            settingsItemViewData = listOf(
                SettingsItemViewData(action = SettingsAction.CONTACT_SUPPORT, textRes = Res.string.item_contact_support),
                SettingsItemViewData(action = SettingsAction.PRIVACY_POLICY, textRes = Res.string.privacy_policy),
                SettingsItemViewData(action = SettingsAction.TERMS_AND_CONDITIONS, textRes = Res.string.terms_conditions),
            )
        )
    ) {

    override fun onViewIntent(intent: HelpAndSupportViewIntent) {
        when (intent) {
            HelpAndSupportViewIntent.OnContactSupportClicked -> sendEvent(HelpAndSupportEvent.OpenFeedbackMail)
            HelpAndSupportViewIntent.OnPrivacyPolicyClicked -> sendEvent(HelpAndSupportEvent.OpenPrivacyPoliceUri)
            HelpAndSupportViewIntent.OnTermsAndConditionsClicked -> sendEvent(HelpAndSupportEvent.OpenTermsAndConditionsUri)
        }
    }
}