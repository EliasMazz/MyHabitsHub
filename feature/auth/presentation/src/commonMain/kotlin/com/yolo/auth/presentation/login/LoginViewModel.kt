package com.yolo.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.yolo.auth.domain.AuthValidatorResult.InvalidEmailAndPasswordError
import com.yolo.auth.domain.AuthValidatorResult.InvalidEmailError
import com.yolo.auth.domain.AuthValidatorResult.InvalidPasswordError
import com.yolo.auth.domain.AuthValidatorResult.Success
import com.yolo.auth.domain.AuthValidatorUseCase
import com.yolo.auth.domain.LoginAuthResult
import com.yolo.auth.domain.LoginAuthUseCase
import com.yolo.auth.domain.entities.LoginAuth
import com.yolo.auth.domain.entities.RegisterAuth
import com.yolo.core.domain.auth.SessionStorage
import com.yolo.core.domain.util.DataError
import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.util.toUiText
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.error_email_not_verified
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_credentials
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_email
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_password

class LoginViewModel(
    val loginAuthUseCase: LoginAuthUseCase,
    private val authValidatorUseCase: AuthValidatorUseCase,
    private val sessionStorage: SessionStorage
) : BaseViewModel<LoginIntent, LoginState, LoginEvent>(
    initialState = LoginState()
) {
    override fun onViewIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.OnForgotPasswordClick -> TODO()
            LoginIntent.OnLoginClick -> viewModelScope.launch { handleLogin() }
            LoginIntent.OnSignupClick -> sendEvent(LoginEvent.NavigateToRegisterEvent)
            LoginIntent.OnTogglePasswordVisibility -> handleTogglePasswordVisibility()
            is LoginIntent.OnEmailChange -> updateState { copy(email = intent.email) }
            is LoginIntent.OnPasswordChange -> updateState { copy(password = intent.password) }
        }
    }

    private fun handleTogglePasswordVisibility() {
        updateState {
            copy(
                isPasswordVisible = !isPasswordVisible
            )
        }
    }

    private suspend fun handleLogin() {
        if (!validFormInputs()) return

        updateState { copy(isLoggingIn = true) }

        val result = loginAuthUseCase(
            LoginAuth(
                email = state.value.email,
                password = state.value.password
            )
        )

        when (result) {
            is LoginAuthResult.Success -> {
                sessionStorage.set(result.authInfo)

                updateState { copy(isLoggingIn = false) }
                sendEvent(LoginEvent.LoginSuccessEvent)
            }

            is LoginAuthResult.Error -> {
                val errorText = when (result.dataError) {
                    DataError.Remote.UNAUTHORIZED -> UiText.Resource(Res.string.error_invalid_credentials)
                    DataError.Remote.FORBIDDEN -> UiText.Resource(Res.string.error_email_not_verified)
                    else -> result.dataError.toUiText()
                }

                updateState {
                    copy(
                        errorText = errorText,
                        isLoggingIn = false,
                    )
                }
            }
        }
    }

    private suspend fun validFormInputs(): Boolean {
        clearAllTextFieldErrors()

        val email = state.value.email
        val password = state.value.password

        val result = authValidatorUseCase.invoke(
            RegisterAuth(
                email = email,
                password = password
            )
        )

        val errorText = when (result) {

            InvalidEmailError -> {
                UiText.Resource(Res.string.error_invalid_email)
            }

            InvalidPasswordError -> {
                UiText.Resource(Res.string.error_invalid_password)
            }

            InvalidEmailAndPasswordError -> {
                UiText.Resource(Res.string.error_invalid_credentials)
            }

            Success -> {
                null
            }
        }

        updateState {
            copy(
                errorText = errorText,
            )
        }

        val isValidFormInputs = errorText == null
        return isValidFormInputs
    }

    private fun clearAllTextFieldErrors() {
        updateState {
            copy(
                errorText = null,
            )
        }
    }
}