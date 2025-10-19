package com.yolo.myhabitshub.presentation.screens.helpandsupport

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface HelpAndSupportEvent: ViewEvent {
   data object OpenFeedbackMail: HelpAndSupportEvent
   data object OpenPrivacyPoliceUri: HelpAndSupportEvent
   data object OpenTermsAndConditionsUri: HelpAndSupportEvent
}