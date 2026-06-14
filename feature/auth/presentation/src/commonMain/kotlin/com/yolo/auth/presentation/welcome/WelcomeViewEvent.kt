package com.yolo.auth.presentation.welcome

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface WelcomeViewEvent : ViewEvent {
    data object NavigateToRegister : WelcomeViewEvent
    data object NavigateToLogin : WelcomeViewEvent
    /** SSO succeeded and a session was persisted — enter the app. */
    data object NavigateToMain : WelcomeViewEvent
    data class ShowError(val message: String) : WelcomeViewEvent
}
