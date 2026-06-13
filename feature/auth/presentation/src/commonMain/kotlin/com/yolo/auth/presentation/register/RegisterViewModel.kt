package com.yolo.auth.presentation.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yolo.auth.domain.EmailValidatorUseCase
import com.yolo.auth.domain.RegisterAuthResult
import com.yolo.auth.domain.RegisterAuthUseCase
import com.yolo.auth.domain.entities.RegisterAuth
import com.yolo.auth.presentation.navigation.AuthGraphRoutes
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.validator.PasswordValidatorUseCase
import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.util.toUiText
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.error_account_exists
import myhabitshub.feature.auth.presentation.generated.resources.error_email_required
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_email
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_password
import myhabitshub.feature.auth.presentation.generated.resources.error_password_required

class RegisterViewModel(
    savedStateHandle: SavedStateHandle,
    private val emailValidatorUseCase: EmailValidatorUseCase,
    private val passwordValidatorUseCase: PasswordValidatorUseCase,
    private val registerUseCase: RegisterAuthUseCase,
) : BaseViewModel<RegisterViewIntent, RegisterViewState, RegisterViewEvent>(
    RegisterViewState(
        email = savedStateHandle.toRoute<AuthGraphRoutes.Register>().email
    )
) {
    init {
        val initialEmail = state.value.email
        if (initialEmail.isNotBlank()) {
            viewModelScope.launch {
                val isValid = emailValidatorUseCase(initialEmail)
                updateState { if (email == initialEmail) copy(isEmailValid = isValid) else this }
            }
        }
    }

    override fun onViewIntent(intent: RegisterViewIntent) {
        when (intent) {
            is RegisterViewIntent.OnEmailChange -> handleEmailChange(intent.email)
            is RegisterViewIntent.OnEmailFocusChanged -> handleEmailFocusChanged(intent.isFocused)
            is RegisterViewIntent.OnPasswordChange -> handlePasswordChange(intent.password)
            RegisterViewIntent.OnTogglePasswordVisibility -> handleTogglePasswordVisibility()
            RegisterViewIntent.OnRegisterClick -> viewModelScope.launch { handleRegisterClick() }

            RegisterViewIntent.OnLoginClick ->
                sendEvent(RegisterViewEvent.NavigateToLoginEvent(email = state.value.email))

            RegisterViewIntent.OnLoginInsteadClick ->
                sendEvent(RegisterViewEvent.NavigateToLoginEvent(email = state.value.email))

            RegisterViewIntent.OnBackClick -> sendEvent(RegisterViewEvent.NavigateBackEvent)
        }
    }

    private fun handleEmailChange(email: String) {
        updateState {
            copy(
                email = email,
                emailError = null,
                registrationError = null,
                isAccountConflict = false,
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

    private fun handlePasswordChange(password: String) {
        updateState {
            copy(
                password = password,
                passwordError = null,
                registrationError = null,
                isAccountConflict = false,
            )
        }
    }

    private fun handleTogglePasswordVisibility() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    private suspend fun handleRegisterClick() {
        if (state.value.isLoading) return

        if (!validateFormForSubmit()) return

        updateState {
            copy(
                isLoading = true,
                registrationError = null,
                isAccountConflict = false,
            )
        }

        val email = state.value.email
        val password = state.value.password

        when (val result = registerUseCase(RegisterAuth(email = email, password = password))) {
            is RegisterAuthResult.Error -> {
                val isConflict = result.dataError == DataError.Remote.CONFLICT
                updateState {
                    copy(
                        isLoading = false,
                        registrationError = if (isConflict) {
                            UiText.Resource(Res.string.error_account_exists)
                        } else {
                            result.dataError.toUiText()
                        },
                        isAccountConflict = isConflict,
                    )
                }
            }

            RegisterAuthResult.Success -> {
                updateState { copy(isLoading = false) }
                sendEvent(RegisterViewEvent.NavigateToRegisterSuccessEvent(email))
            }
        }
    }

    private suspend fun validateFormForSubmit(): Boolean {
        val email = state.value.email
        val password = state.value.password

        val emailError = when {
            email.isBlank() -> UiText.Resource(Res.string.error_email_required)
            !emailValidatorUseCase(email) -> UiText.Resource(Res.string.error_invalid_email)
            else -> null
        }
        val passwordError = when {
            password.isBlank() -> UiText.Resource(Res.string.error_password_required)
            !passwordValidatorUseCase(password) -> UiText.Resource(Res.string.error_invalid_password)
            else -> null
        }

        updateState {
            copy(
                emailError = emailError,
                passwordError = passwordError,
            )
        }

        val firstInvalidField = when {
            emailError != null -> RegisterFormField.EMAIL
            passwordError != null -> RegisterFormField.PASSWORD
            else -> null
        }
        if (firstInvalidField != null) {
            sendEvent(RegisterViewEvent.FocusFirstInvalidFieldEvent(firstInvalidField))
            return false
        }
        return true
    }
}
