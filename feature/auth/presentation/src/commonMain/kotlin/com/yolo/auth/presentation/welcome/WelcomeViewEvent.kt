package com.yolo.auth.presentation.welcome

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface WelcomeViewEvent : ViewEvent {
    data object NavigateToRegister : WelcomeViewEvent
    data object NavigateToLogin : WelcomeViewEvent
    data object ShowSsoComingSoon : WelcomeViewEvent
}
