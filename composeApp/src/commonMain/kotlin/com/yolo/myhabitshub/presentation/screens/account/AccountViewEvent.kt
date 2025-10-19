package com.yolo.myhabitshub.presentation.screens.account

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.*

sealed interface AccountViewEvent: ViewEvent {
   data object NavigateToSignIn: AccountViewEvent
   data object NavigateToSettings: AccountViewEvent
   data object NavigateToHelpAndSupport: AccountViewEvent
}
