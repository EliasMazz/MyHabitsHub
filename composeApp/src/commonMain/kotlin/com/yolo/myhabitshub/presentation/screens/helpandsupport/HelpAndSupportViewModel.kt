package com.yolo.myhabitshub.presentation.screens.helpandsupport

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.item_contact_support
import com.yolo.myhabitshub.generated.resources.privacy_policy
import com.yolo.myhabitshub.generated.resources.terms_conditions
import com.yolo.myhabitshub.presentation.components.SettingsItemUiState

class HelpAndSupportViewModel :
    BaseViewModel<HelpAndSupportViewIntent, HelpAndSupportState, HelpAndSupportEvent>(
        initialState = HelpAndSupportState(
            settingsItemUiState = listOf(
                SettingsItemUiState(textRes = Res.string.item_contact_support),
                SettingsItemUiState(textRes = Res.string.privacy_policy),
                SettingsItemUiState(textRes = Res.string.terms_conditions),
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