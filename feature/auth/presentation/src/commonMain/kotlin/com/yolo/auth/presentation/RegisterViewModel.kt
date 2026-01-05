package com.yolo.auth.presentation

import androidx.lifecycle.viewModelScope
import com.yolo.auth.domain.AuthValidatorResult.*
import com.yolo.auth.domain.AuthValidatorUseCase
import com.yolo.auth.domain.RegisterAuthResult
import com.yolo.auth.domain.RegisterAuthUseCase
import com.yolo.auth.domain.entities.RegisterAuth
import com.yolo.core.domain.util.DataError
import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.util.toUiText
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.error_account_exists
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_email
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_password

class RegisterViewModel(
    private val authValidatorUseCase: AuthValidatorUseCase,
    private val registerUseCase: RegisterAuthUseCase,
) : BaseViewModel<RegisterViewIntent, RegisterViewState, RegisterViewEvent>(
    RegisterViewState()
) {
    override fun onViewIntent(intent: RegisterViewIntent) {
        when (intent) {
            RegisterViewIntent.OnRegisterClick -> viewModelScope.launch { handleRegisterClick() }
            RegisterViewIntent.OnLogicClick -> TODO()
            RegisterViewIntent.OnInputTextFocusGain -> handleInputTextFocusGain()
            RegisterViewIntent.OnTogglePasswordVisibility -> handleTogglePasswordVisibility()
        }
    }

    private fun handleTogglePasswordVisibility() {
        updateState {
            copy(
                isPasswordVisible = !isPasswordVisible
            )
        }
    }

    private fun handleInputTextFocusGain() {
        updateState {
            copy(
                emailError = null,
                passwordError = null,
                registrationError = null
            )
        }
    }

    private suspend fun handleRegisterClick() {
        if (!validFormInputs()) {
            return
        }

        updateState {
            copy(isLoading = true)
        }

        val email = state.value.emailTextState.text.toString()
        val password = state.value.passwordTextState.text.toString()

        val result = registerUseCase(
            RegisterAuth(
                email = email,
                password = password
            )
        )

        when (result) {
            is RegisterAuthResult.Error -> {
                val registrationError = when (result.dataError) {
                    DataError.Remote.CONFLICT -> UiText.Resource(Res.string.error_account_exists)
                    else -> result.dataError.toUiText()
                }
                updateState {
                    copy(
                        isLoading = false,
                        registrationError = registrationError
                    )
                }
            }

            RegisterAuthResult.Success -> updateState {
                copy(
                    isLoading = false,
                    viewEvent = RegisterViewEvent.Success(email)
                )
            }
        }
    }

    private suspend fun validFormInputs(): Boolean {
        clearAllTextFieldErrors()

        val email = state.value.emailTextState.text.toString()
        val password = state.value.passwordTextState.text.toString()

        val result = authValidatorUseCase.invoke(
            RegisterAuth(
                email = email,
                password = password
            )
        )

        val (emailError, passwordError) = when (result) {
            InvalidEmailAndPasswordError -> {
                UiText.Resource(Res.string.error_invalid_email) to UiText.Resource(Res.string.error_invalid_password)
            }

            InvalidEmailError -> {
                UiText.Resource(Res.string.error_invalid_email) to null
            }

            InvalidPasswordError -> {
                null to UiText.Resource(Res.string.error_invalid_password)
            }

            Success -> {
                null to null
            }
        }

        updateState {
            copy(
                emailError = emailError,
                passwordError = passwordError,
            )
        }

        val isValidFormInputs = emailError == null && passwordError == null
        return isValidFormInputs
    }

    private fun clearAllTextFieldErrors() {
        updateState {
            copy(
                emailError = null,
                passwordError = null,
                registrationError = null
            )
        }
    }
}