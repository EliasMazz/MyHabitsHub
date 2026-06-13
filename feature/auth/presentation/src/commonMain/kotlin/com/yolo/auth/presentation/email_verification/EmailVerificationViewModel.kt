package com.yolo.auth.presentation.email_verification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yolo.auth.domain.ResendCooldownStreamUseCase
import com.yolo.auth.domain.EmailValidatorUseCase
import com.yolo.auth.domain.EmailVerificationUseCase
import com.yolo.auth.domain.ResendVerificationEmailResult
import com.yolo.auth.domain.ResendVerificationEmailUseCase
import com.yolo.auth.presentation.navigation.AuthGraphRoutes
import com.yolo.core.domain.util.DataError
import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.util.toUiText
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.error_email_required
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_email

class EmailVerificationViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val emailVerificationUseCase: EmailVerificationUseCase,
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase,
    private val emailValidatorUseCase: EmailValidatorUseCase,
    private val resendCooldownStreamUseCase: ResendCooldownStreamUseCase,
) : BaseViewModel<EmailVerificationViewIntent, EmailVerificationViewState, EmailVerificationViewEvent>(
    initialState = EmailVerificationViewState()
) {

    private val token: String =
        savedStateHandle.toRoute<AuthGraphRoutes.EmailVerification>().token

    private var cooldownJob: Job? = null
    private var resendCount = 0

    init {
        when {
            // A blank token can never verify — stay on the default token-failure
            // recovery state instead of POSTing a blank token.
            token.isBlank() -> Unit

            // The token is single-use: re-firing verification after process recreation
            // would turn an already-successful verification into a failure.
            savedStateHandle.get<Boolean>(HAS_ATTEMPTED_VERIFICATION_KEY) == true -> Unit

            else -> {
                savedStateHandle[HAS_ATTEMPTED_VERIFICATION_KEY] = true
                verifyEmail()
            }
        }
    }

    override fun onViewIntent(intent: EmailVerificationViewIntent) {
        when (intent) {
            EmailVerificationViewIntent.OnLoginClick ->
                sendEvent(EmailVerificationViewEvent.NavigateToLoginEvent)

            is EmailVerificationViewIntent.OnEmailChange -> handleEmailChange(intent.email)
            EmailVerificationViewIntent.OnResendClick -> handleResendClick()
            EmailVerificationViewIntent.OnRetryClick -> retryVerification()
        }
    }

    private fun handleEmailChange(email: String) {
        updateState {
            copy(
                email = email,
                emailError = null,
                resendError = null,
            )
        }
    }

    private fun retryVerification() {
        if (state.value.isVerifying) return
        verifyEmail()
    }

    private fun verifyEmail() {
        viewModelScope.launch {
            updateState {
                copy(
                    isVerifying = true,
                    isNetworkFailure = false,
                )
            }

            when (val result = emailVerificationUseCase(token)) {
                EmailVerificationUseCase.EmailVerificationResult.Success -> {
                    updateState {
                        copy(
                            isVerifying = false,
                            isVerified = true,
                        )
                    }
                }

                is EmailVerificationUseCase.EmailVerificationResult.Failure -> {
                    // A network blip must not read as "your link is dead" — only
                    // NO_INTERNET/REQUEST_TIMEOUT get the retryable network-failure state.
                    val isNetworkFailure = result.error == DataError.Remote.NO_INTERNET ||
                        result.error == DataError.Remote.REQUEST_TIMEOUT
                    updateState {
                        copy(
                            isVerifying = false,
                            isVerified = false,
                            isNetworkFailure = isNetworkFailure,
                        )
                    }
                }
            }
        }
    }

    private fun handleResendClick() {
        if (state.value.isResending || state.value.resendCooldownSeconds > 0) return

        viewModelScope.launch {
            val email = state.value.email.trim()

            if (email.isBlank()) {
                updateState { copy(emailError = UiText.Resource(Res.string.error_email_required)) }
                return@launch
            }
            if (!emailValidatorUseCase(email)) {
                updateState { copy(emailError = UiText.Resource(Res.string.error_invalid_email)) }
                return@launch
            }

            updateState {
                copy(
                    isResending = true,
                    emailError = null,
                    resendError = null,
                )
            }

            when (val result = resendVerificationEmailUseCase(email)) {
                ResendVerificationEmailResult.Success -> {
                    resendCount++
                    updateState { copy(isResending = false) }
                    // No cooldown on entry (nothing is auto-sent from this screen).
                    startCooldown(
                        if (resendCount == 1) {
                            FIRST_RESEND_COOLDOWN_SECONDS
                        } else {
                            SUBSEQUENT_RESEND_COOLDOWN_SECONDS
                        }
                    )
                }

                is ResendVerificationEmailResult.Error -> {
                    updateState {
                        copy(
                            isResending = false,
                            resendError = result.dataError.toUiText(),
                        )
                    }
                }
            }
        }
    }

    // Cooldown is not persisted across process death — the server 429 remains the backstop.
    private fun startCooldown(seconds: Int) {
        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            resendCooldownStreamUseCase(seconds).collect { remaining ->
                updateState { copy(resendCooldownSeconds = remaining) }
            }
        }
    }

    private companion object {
        const val HAS_ATTEMPTED_VERIFICATION_KEY = "hasAttemptedVerification"
        const val FIRST_RESEND_COOLDOWN_SECONDS = 30
        const val SUBSEQUENT_RESEND_COOLDOWN_SECONDS = 60
    }
}
