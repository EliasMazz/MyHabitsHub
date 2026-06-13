package com.yolo.auth.presentation.register_success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yolo.auth.domain.ResendCooldownStreamUseCase
import com.yolo.auth.domain.ResendVerificationEmailResult
import com.yolo.auth.domain.ResendVerificationEmailUseCase
import com.yolo.auth.presentation.navigation.AuthGraphRoutes
import com.yolo.core.presentation.util.toUiText
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterSuccessViewModel(
    savedStateHandle: SavedStateHandle,
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase,
    private val resendCooldownStreamUseCase: ResendCooldownStreamUseCase,
) : BaseViewModel<RegisterSuccessViewIntent, RegisterSuccessViewState, RegisterSuccessViewEvent>(
    RegisterSuccessViewState()
) {

    // 4.3#1: the route arg is the single source of truth for the address shown verbatim —
    // wrong-email detection is impossible without it.
    private val email = savedStateHandle.toRoute<AuthGraphRoutes.RegisterSuccess>().email

    private var cooldownJob: Job? = null

    init {
        updateState { copy(registeredEmail = email) }
        // §5.1: registration itself just sent the verification email, so the cooldown
        // starts on screen entry.
        startCooldown(INITIAL_COOLDOWN_SECONDS)
    }

    override fun onViewIntent(intent: RegisterSuccessViewIntent) {
        when (intent) {
            RegisterSuccessViewIntent.OnLoginClick ->
                sendEvent(RegisterSuccessViewEvent.NavigateToLoginEvent(email))

            RegisterSuccessViewIntent.OnEditEmailClick ->
                sendEvent(RegisterSuccessViewEvent.NavigateToRegisterEvent(email))

            RegisterSuccessViewIntent.OnResendVerificationEmailClick -> handleResendVerification()
        }
    }

    private fun handleResendVerification() {
        // B3 in-flight guard + §5.1 cooldown backstop (UI already disables the button).
        if (state.value.isResendingVerificationEmail) return
        if (state.value.resendCooldownSeconds > 0) return

        viewModelScope.launch {
            updateState {
                copy(
                    isResendingVerificationEmail = true,
                    resendVerificationEmailError = null,
                )
            }
            when (val result = resendVerificationEmailUseCase(email)) {
                is ResendVerificationEmailResult.Success -> {
                    updateState { copy(isResendingVerificationEmail = false) }
                    // §5.1: 60s after every manual resend on this screen instance.
                    startCooldown(SUBSEQUENT_COOLDOWN_SECONDS)
                    sendEvent(RegisterSuccessViewEvent.ResendVerificationEmailSuccessEvent)
                }

                is ResendVerificationEmailResult.Error -> {
                    updateState {
                        copy(
                            isResendingVerificationEmail = false,
                            resendVerificationEmailError = result.dataError.toUiText()
                        )
                    }
                }
            }
        }
    }

    private fun startCooldown(seconds: Int) {
        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            resendCooldownStreamUseCase(seconds).collect { remaining ->
                updateState { copy(resendCooldownSeconds = remaining) }
            }
        }
    }

    private companion object {
        /** §5.1: 30s after the send that created this screen. */
        const val INITIAL_COOLDOWN_SECONDS = 30

        /** §5.1: 60s for every subsequent resend on the same screen instance. */
        const val SUBSEQUENT_COOLDOWN_SECONDS = 60
    }
}
