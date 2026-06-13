package com.yolo.auth.presentation.welcome

import com.yolo.core.presentation.util.isAndroidPlatform
import com.yolo.core.presentation.viewmodel.BaseViewModel

/**
 * Welcome screen (auth entry). Platform-conditional SSO per auth-conversion-spec §2:
 * Android shows Google + email; iOS shows Apple + email. SSO is a visual draft for now —
 * taps surface a "coming soon" notice until kmpauth wiring lands.
 */
class WelcomeViewModel : BaseViewModel<WelcomeViewIntent, WelcomeViewState, WelcomeViewEvent>(
    initialState = WelcomeViewState(
        showGoogleButton = isAndroidPlatform,
        showAppleButton = !isAndroidPlatform,
    )
) {
    override fun onViewIntent(intent: WelcomeViewIntent) {
        when (intent) {
            WelcomeViewIntent.OnGoogleClick,
            WelcomeViewIntent.OnAppleClick -> sendEvent(WelcomeViewEvent.ShowSsoComingSoon)

            WelcomeViewIntent.OnContinueWithEmailClick ->
                sendEvent(WelcomeViewEvent.NavigateToRegister)

            WelcomeViewIntent.OnLogInClick -> sendEvent(WelcomeViewEvent.NavigateToLogin)
        }
    }
}
