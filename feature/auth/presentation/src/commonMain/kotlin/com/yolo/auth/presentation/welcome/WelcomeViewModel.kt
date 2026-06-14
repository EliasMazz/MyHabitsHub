package com.yolo.auth.presentation.welcome

import androidx.lifecycle.viewModelScope
import com.yolo.auth.domain.SsoLoginParam
import com.yolo.auth.domain.SsoLoginResult
import com.yolo.auth.domain.SsoLoginUseCase
import com.yolo.auth.presentation.sso.SocialSignInResult
import com.yolo.core.domain.auth.SessionStorage
import com.yolo.core.domain.auth.SsoConfig
import com.yolo.core.domain.util.DataError
import com.yolo.core.presentation.util.isAndroidPlatform
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.error_account_exists
import myhabitshub.feature.auth.presentation.generated.resources.error_sso_failed
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

/**
 * Welcome screen (auth entry). Platform-conditional SSO per auth-conversion-spec §2:
 * Android shows Google, iOS shows Apple. The native sheet hands back a raw provider credential
 * ([SocialSignInResult]); this VM exchanges it with the custom backend ([SsoLoginUseCase]),
 * persists the returned session ([SessionStorage]) and navigates into the app. No Firebase.
 */
class WelcomeViewModel(
    private val ssoLoginUseCase: SsoLoginUseCase,
    private val sessionStorage: SessionStorage,
    ssoConfig: SsoConfig,
) : BaseViewModel<WelcomeViewIntent, WelcomeViewState, WelcomeViewEvent>(
    initialState = WelcomeViewState(
        showGoogleButton = isAndroidPlatform,
        showAppleButton = !isAndroidPlatform,
        googleServerClientId = ssoConfig.googleWebServerClientId,
    )
) {
    override fun onViewIntent(intent: WelcomeViewIntent) {
        when (intent) {
            is WelcomeViewIntent.OnSsoResult -> handleSsoResult(intent.result)
            WelcomeViewIntent.OnContinueWithEmailClick -> sendEvent(WelcomeViewEvent.NavigateToRegister)
            WelcomeViewIntent.OnLogInClick -> sendEvent(WelcomeViewEvent.NavigateToLogin)
        }
    }

    private fun handleSsoResult(result: SocialSignInResult) {
        if (state.value.isAuthenticating) return
        when (result) {
            SocialSignInResult.Cancelled -> Unit // user dismissed the sheet — show nothing
            is SocialSignInResult.Failure -> emitError(Res.string.error_sso_failed)
            is SocialSignInResult.GoogleSuccess ->
                authenticate(SsoLoginParam.Google(idToken = result.idToken))
            is SocialSignInResult.AppleSuccess ->
                authenticate(
                    SsoLoginParam.Apple(
                        identityToken = result.identityToken,
                        authorizationCode = result.authorizationCode,
                        nonce = result.nonce,
                    )
                )
        }
    }

    private fun authenticate(param: SsoLoginParam) {
        viewModelScope.launch {
            updateState { copy(isAuthenticating = true) }
            when (val result = ssoLoginUseCase(param)) {
                is SsoLoginResult.Success -> {
                    sessionStorage.set(result.authInfo)
                    updateState { copy(isAuthenticating = false) }
                    sendEvent(WelcomeViewEvent.NavigateToMain)
                }

                is SsoLoginResult.Error -> {
                    updateState { copy(isAuthenticating = false) }
                    emitError(
                        if (result.dataError == DataError.Remote.CONFLICT) {
                            Res.string.error_account_exists
                        } else {
                            Res.string.error_sso_failed
                        }
                    )
                }
            }
        }
    }

    private fun emitError(message: StringResource) {
        viewModelScope.launch { sendEvent(WelcomeViewEvent.ShowError(getString(message))) }
    }
}
