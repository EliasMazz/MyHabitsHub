package com.yolo.account.presentation.helpandsupport

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface HelpAndSupportViewIntent : ViewIntent {
    data object OnContactSupportClicked: HelpAndSupportViewIntent
    data object OnPrivacyPolicyClicked: HelpAndSupportViewIntent
    data object OnTermsAndConditionsClicked: HelpAndSupportViewIntent
}