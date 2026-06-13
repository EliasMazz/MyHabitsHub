package com.yolo.auth.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yolo.auth.domain.ResendCooldownStreamUseCase
import com.yolo.auth.domain.EmailValidatorUseCase
import com.yolo.auth.domain.LoginAuthResult
import com.yolo.auth.domain.LoginAuthUseCase
import com.yolo.auth.domain.ResendVerificationEmailResult
import com.yolo.auth.domain.ResendVerificationEmailUseCase
import com.yolo.auth.domain.entities.LoginAuth
import com.yolo.auth.presentation.navigation.AuthGraphRoutes
import com.yolo.core.domain.auth.SessionStorage
import com.yolo.core.domain.util.DataError
import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.util.toUiText
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.error_email_not_verified
import myhabitshub.feature.auth.presentation.generated.resources.error_email_required
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_credentials
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_email
import myhabitshub.feature.auth.presentation.generated.resources.error_password_required

class LoginViewModel(
    savedStateHandle: SavedStateHandle,
    private val loginAuthUseCase: LoginAuthUseCase,
    private val emailValidatorUseCase: EmailValidatorUseCase,
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase,
    private val sessionStorage: SessionStorage,
    private val resendCooldownStreamUseCase: ResendCooldownStreamUseCase,
) : BaseViewModel<LoginIntent, LoginState, LoginEvent>(
    initialState = LoginState()
) {

    private var emailFormatJob: Job? = null
    private var cooldownJob: Job? = null
    private var resendCount = 0

    init {
        val initialEmail = savedStateHandle.toRoute<AuthGraphRoutes.Login>().email
        if (initialEmail.isNotBlank()) {
            updateState { copy(email = initialEmail) }
            refreshEmailFormatValidity(initialEmail)
        }
    }

    override fun onViewIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.OnForgotPasswordClick ->
                sendEvent(LoginEvent.NavigateToForgotPasswordEvent(email = state.value.email))

            LoginIntent.OnLoginClick -> handleLogin()

            LoginIntent.OnSignupClick ->
                sendEvent(LoginEvent.NavigateToRegisterEvent(email = state.value.email))

            LoginIntent.OnBackClick -> sendEvent(LoginEvent.NavigateBackEvent)

            LoginIntent.OnTogglePasswordVisibility -> handleTogglePasswordVisibility()

            LoginIntent.OnResendVerificationClick -> handleResendVerification()

            is LoginIntent.OnEmailChange -> handleEmailChange(intent.email)

            is LoginIntent.OnPasswordChange -> handlePasswordChange(intent.password)

            is LoginIntent.OnEmailFocusChanged -> handleEmailFocusChanged(intent.isFocused)
        }
    }

    private fun handleTogglePasswordVisibility() {
        updateState {
            copy(
                isPasswordVisible = !isPasswordVisible
            )
        }
    }

    private fun handleEmailChange(email: String) {
        updateState {
            copy(
                email = email,
                emailError = null,
                errorText = null,
                showResendVerification = false,
            )
        }
        refreshEmailFormatValidity(email)
    }

    private fun handlePasswordChange(password: String) {
        updateState {
            copy(
                password = password,
                passwordError = null,
                errorText = null,
                showResendVerification = false,
            )
        }
    }

    // Positive-only live feedback: drives the green check, never an error.
    private fun refreshEmailFormatValidity(email: String) {
        emailFormatJob?.cancel()
        emailFormatJob = viewModelScope.launch {
            val isValid = emailValidatorUseCase(email)
            updateState { copy(isEmailFormatValid = isValid) }
        }
    }

    private fun handleEmailFocusChanged(isFocused: Boolean) {
        if (isFocused) return
        val email = state.value.email
        if (email.isBlank()) return

        viewModelScope.launch {
            if (!emailValidatorUseCase(email)) {
                updateState { copy(emailError = UiText.Resource(Res.string.error_invalid_email)) }
            }
        }
    }

    private fun handleLogin() {
        if (state.value.isLoggingIn) return

        viewModelScope.launch {
            if (!validateForm()) return@launch

            updateState {
                copy(
                    isLoggingIn = true,
                    errorText = null,
                    showResendVerification = false,
                )
            }

            val result = loginAuthUseCase(
                LoginAuth(
                    email = state.value.email,
                    password = state.value.password,
                )
            )

            when (result) {
                is LoginAuthResult.Success -> {
                    sessionStorage.set(result.authInfo)

                    updateState { copy(isLoggingIn = false) }
                    sendEvent(LoginEvent.LoginSuccessEvent)
                }

                is LoginAuthResult.Error -> handleLoginError(result.dataError)
            }
        }
    }

    /**
     * Checks presence + email format only. The registration password policy is never
     * run here — legacy passwords must pass through; the server is the authority.
     */
    private suspend fun validateForm(): Boolean {
        val email = state.value.email
        val password = state.value.password

        val emailError: UiText? = when {
            email.isBlank() -> UiText.Resource(Res.string.error_email_required)
            !emailValidatorUseCase(email) -> UiText.Resource(Res.string.error_invalid_email)
            else -> null
        }
        val passwordError: UiText? = if (password.isEmpty()) {
            UiText.Resource(Res.string.error_password_required)
        } else {
            null
        }

        updateState {
            copy(
                emailError = emailError,
                passwordError = passwordError,
            )
        }

        when {
            emailError != null ->
                sendEvent(LoginEvent.RequestFieldFocusEvent(LoginEvent.LoginField.EMAIL))

            passwordError != null ->
                sendEvent(LoginEvent.RequestFieldFocusEvent(LoginEvent.LoginField.PASSWORD))
        }

        return emailError == null && passwordError == null
    }

    // The top banner carries request-level (server) errors only.
    private fun handleLoginError(dataError: DataError) {
        when (dataError) {
            DataError.Remote.UNAUTHORIZED -> updateState {
                copy(
                    isLoggingIn = false,
                    errorText = UiText.Resource(Res.string.error_invalid_credentials),
                )
            }

            DataError.Remote.FORBIDDEN -> updateState {
                copy(
                    isLoggingIn = false,
                    errorText = UiText.Resource(Res.string.error_email_not_verified),
                    showResendVerification = true,
                )
            }

            else -> updateState {
                copy(
                    isLoggingIn = false,
                    errorText = dataError.toUiText(),
                )
            }
        }
    }

    private fun handleResendVerification() {
        val current = state.value
        if (current.isResendingVerification || current.isLoggingIn || current.resendCooldownSeconds > 0) {
            return
        }

        viewModelScope.launch {
            updateState { copy(isResendingVerification = true) }

            when (val result = resendVerificationEmailUseCase(state.value.email)) {
                is ResendVerificationEmailResult.Success -> {
                    updateState { copy(isResendingVerification = false) }
                    resendCount++
                    startCooldown(
                        if (resendCount == 1) FIRST_SEND_COOLDOWN_SECONDS else RESEND_COOLDOWN_SECONDS
                    )
                }

                is ResendVerificationEmailResult.Error -> {
                    // No email left the server — no cooldown; the user may retry at once.
                    updateState {
                        copy(
                            isResendingVerification = false,
                            errorText = result.dataError.toUiText(),
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
        const val FIRST_SEND_COOLDOWN_SECONDS = 30
        const val RESEND_COOLDOWN_SECONDS = 60
    }
}
