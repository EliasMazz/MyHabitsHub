package com.yolo.auth.presentation

import androidx.lifecycle.viewModelScope
import com.yolo.auth.domain.RegisterValidatorResult.*
import com.yolo.auth.domain.RegisterValidatorUseCase
import com.yolo.auth.domain.entities.RegisterAuth
import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_email
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_password

class RegisterViewModel(
    private val registerValidatorUseCase: RegisterValidatorUseCase
) : BaseViewModel<RegisterViewIntent, RegisterViewState, RegisterViewEvent>(
    RegisterViewState()
) {
    override fun onViewIntent(intent: RegisterViewIntent) {
        when (intent) {
            RegisterViewIntent.OnRegisterClick -> {
                viewModelScope.launch {
                    validFormInputs()
                }
            }

            RegisterViewIntent.OnInputTextFocusGain -> {
                updateState {
                    copy(
                        emailError = null,
                        passwordError = null,
                        registrationError = null
                    )
                }
            }

            else -> {

            }
        }

    }

    private suspend fun validFormInputs() {
        val email = state.value.emailTextState.text.toString()
        val password = state.value.passwordTextState.text.toString()

        val result = registerValidatorUseCase.invoke(
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
    }
}