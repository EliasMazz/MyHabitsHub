package com.yolo.auth.presentation.forgot_password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yolo.auth.domain.ResendCooldownStreamUseCase
import com.yolo.auth.domain.EmailValidatorUseCase
import com.yolo.auth.domain.ForgotPasswordResult
import com.yolo.auth.domain.ForgotPasswordUseCase
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

class ForgotPasswordViewModel(
    savedStateHandle: SavedStateHandle,
    private val emailValidatorUseCase: EmailValidatorUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val resendCooldownStreamUseCase: ResendCooldownStreamUseCase,
) : BaseViewModel<ForgotPasswordViewIntent, ForgotPasswordViewState, ForgotPasswordViewEvent>(
    initialState = ForgotPasswordViewState(
        email = savedStateHandle.toRoute<AuthGraphRoutes.ForgotPassword>().email
    )
) {
    private var cooldownJob: Job? = null

    init {
        val initialEmail = state.value.email
        if (initialEmail.isNotBlank()) {
            viewModelScope.launch {
                val isValid = emailValidatorUseCase(initialEmail)
                updateState { if (email == initialEmail) copy(isEmailValid = isValid) else this }
            }
        }
    }

    override fun onViewIntent(intent: ForgotPasswordViewIntent) {
        when (intent) {
            ForgotPasswordViewIntent.OnBackClick -> sendEvent(ForgotPasswordViewEvent.NavigateBack)
            ForgotPasswordViewIntent.OnBackToLoginClick ->
                sendEvent(ForgotPasswordViewEvent.NavigateToLogin(state.value.email))

            is ForgotPasswordViewIntent.OnEmailChange -> handleEmailChange(intent.email)
            is ForgotPasswordViewIntent.OnEmailFocusChanged -> handleEmailFocusChanged(intent.isFocused)
            ForgotPasswordViewIntent.OnSubmitClick -> submitForgotPassword()
            ForgotPasswordViewIntent.OnResendClick -> resendResetLink()
        }
    }

    private fun handleEmailChange(email: String) {
        updateState {
            copy(
                email = email,
                emailError = null,
                errorText = null,
            )
        }
        viewModelScope.launch {
            val isValid = emailValidatorUseCase(email)
            // Guard against out-of-order completions across keystrokes.
            updateState { if (this.email == email) copy(isEmailValid = isValid) else this }
        }
    }

    private fun handleEmailFocusChanged(isFocused: Boolean) {
        if (isFocused) return
        val email = state.value.email
        if (email.isBlank()) return

        viewModelScope.launch {
            if (!emailValidatorUseCase(email)) {
                updateState {
                    if (this.email == email) {
                        copy(emailError = UiText.Resource(Res.string.error_invalid_email))
                    } else {
                        this
                    }
                }
            }
        }
    }

    private fun submitForgotPassword() {
        if (state.value.isLoading) return

        viewModelScope.launch {
            val email = state.value.email

            if (email.isBlank()) {
                updateState {
                    copy(emailError = UiText.Resource(Res.string.error_email_required))
                }
                return@launch
            }

            if (!emailValidatorUseCase(email)) {
                updateState {
                    copy(emailError = UiText.Resource(Res.string.error_invalid_email))
                }
                return@launch
            }

            updateState {
                copy(
                    isLoading = true,
                    emailError = null,
                    errorText = null,
                )
            }

            when (val result = forgotPasswordUseCase(email)) {
                is ForgotPasswordResult.Error -> {
                    if (result.dataError == DataError.Remote.NOT_FOUND) {
                        // Anti-enumeration: an unknown account shows the SAME sent-state
                        // as success ("If an account exists for …").
                        enterSentState()
                    } else {
                        updateState {
                            copy(
                                isLoading = false,
                                errorText = result.dataError.toUiText(),
                            )
                        }
                    }
                }

                ForgotPasswordResult.Success -> enterSentState()
            }
        }
    }

    private fun resendResetLink() {
        val current = state.value
        if (current.isResending || current.resendCooldownSeconds > 0) return

        viewModelScope.launch {
            updateState { copy(isResending = true, resendError = null) }

            when (val result = forgotPasswordUseCase(state.value.email)) {
                is ForgotPasswordResult.Error -> {
                    if (result.dataError == DataError.Remote.NOT_FOUND) {
                        // Same anti-enumeration masking on resend.
                        updateState { copy(isResending = false) }
                        startCooldown(SUBSEQUENT_RESEND_COOLDOWN_SECONDS)
                    } else {
                        // Genuine failure (e.g. network): keep the button usable, no cooldown.
                        updateState {
                            copy(
                                isResending = false,
                                resendError = result.dataError.toUiText(),
                            )
                        }
                    }
                }

                ForgotPasswordResult.Success -> {
                    updateState { copy(isResending = false) }
                    startCooldown(SUBSEQUENT_RESEND_COOLDOWN_SECONDS)
                }
            }
        }
    }

    private fun enterSentState() {
        updateState {
            copy(
                isLoading = false,
                isEmailSent = true,
            )
        }
        // Cooldown starts on entering the sent-state — the triggering email was just sent.
        startCooldown(FIRST_SEND_COOLDOWN_SECONDS)
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
        const val FIRST_SEND_COOLDOWN_SECONDS = 30
        const val SUBSEQUENT_RESEND_COOLDOWN_SECONDS = 60
    }
}
