package com.yolo.myhabitshub.presentation.screens.helpandsupport

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.*

sealed interface HelpAndSupportViewIntent : ViewIntent {
    data object OnContactSupportClicked: HelpAndSupportViewIntent
    data object OnPrivacyPolicyClicked: HelpAndSupportViewIntent
    data object OnTermsAndConditionsClicked: HelpAndSupportViewIntent
}