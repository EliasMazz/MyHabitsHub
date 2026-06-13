package com.yolo.auth.presentation.welcome

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewIntent

sealed interface WelcomeViewIntent : ViewIntent {
    data object OnGoogleClick : WelcomeViewIntent
    data object OnAppleClick : WelcomeViewIntent
    data object OnContinueWithEmailClick : WelcomeViewIntent
    data object OnLogInClick : WelcomeViewIntent
}
