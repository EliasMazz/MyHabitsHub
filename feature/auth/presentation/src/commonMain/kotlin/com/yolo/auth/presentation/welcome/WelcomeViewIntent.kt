package com.yolo.auth.presentation.welcome

import com.yolo.auth.presentation.sso.SocialSignInResult
import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewIntent

sealed interface WelcomeViewIntent : ViewIntent {
    /** Result of the native Google/Apple sign-in sheet, raised by the screen. */
    data class OnSsoResult(val result: SocialSignInResult) : WelcomeViewIntent
    data object OnContinueWithEmailClick : WelcomeViewIntent
    data object OnLogInClick : WelcomeViewIntent
}
