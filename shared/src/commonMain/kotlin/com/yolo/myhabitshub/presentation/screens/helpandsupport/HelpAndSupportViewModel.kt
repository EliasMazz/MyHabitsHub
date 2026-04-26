package com.yolo.myhabitshub.presentation.screens.helpandsupport

import com.yolo.core.presentation.viewmodel.BaseViewModel
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.item_contact_support
import com.yolo.myhabitshub.generated.resources.privacy_policy
import com.yolo.myhabitshub.generated.resources.terms_conditions
import com.yolo.myhabitshub.presentation.components.SettingsAction
import com.yolo.myhabitshub.presentation.components.SettingsItemViewData

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
            HelpAndSupportViewIntent.OnContactSupportClicked -> updateState { copy(viewEvent = HelpAndSupportEvent.OpenFeedbackMail) }
            HelpAndSupportViewIntent.OnPrivacyPolicyClicked ->  updateState { copy(viewEvent = HelpAndSupportEvent.OpenPrivacyPoliceUri) }
            HelpAndSupportViewIntent.OnTermsAndConditionsClicked -> updateState { copy(viewEvent = HelpAndSupportEvent.OpenTermsAndConditionsUri) }
        }
    }
}