package com.yolo.account.presentation.helpandsupport

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface HelpAndSupportEvent: ViewEvent {
   data object OpenFeedbackMail: HelpAndSupportEvent
   data object OpenPrivacyPoliceUri: HelpAndSupportEvent
   data object OpenTermsAndConditionsUri: HelpAndSupportEvent
}