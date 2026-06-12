package com.yolo.account.presentation.account

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface AccountViewEvent: ViewEvent {
   data object NavigateToSignIn: AccountViewEvent
   data object NavigateToSettings: AccountViewEvent
   data object NavigateToHelpAndSupport: AccountViewEvent
}
