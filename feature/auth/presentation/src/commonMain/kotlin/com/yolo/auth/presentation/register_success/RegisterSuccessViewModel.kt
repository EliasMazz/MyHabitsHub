package com.yolo.auth.presentation.register_success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yolo.auth.domain.AuthValidatorResult.*
import com.yolo.auth.domain.AuthValidatorUseCase
import com.yolo.auth.domain.RegisterAuthResult
import com.yolo.auth.domain.RegisterAuthUseCase
import com.yolo.auth.domain.ResendVerificationEmailResult
import com.yolo.auth.domain.ResendVerificationEmailUseCase
import com.yolo.auth.domain.entities.RegisterAuth
import com.yolo.auth.presentation.register.RegisterViewEvent
import com.yolo.auth.presentation.register.RegisterViewIntent
import com.yolo.auth.presentation.register.RegisterViewState
import com.yolo.core.domain.util.DataError
import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.util.toUiText
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.error_account_exists
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_email
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_password

class RegisterSuccessViewModel(
    savedStateHandle: SavedStateHandle,
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase
) : BaseViewModel<RegisterSuccessViewIntent, RegisterSuccessViewState, RegisterSuccessViewEvent>(
    RegisterSuccessViewState()
) {

    private val email = savedStateHandle.get<String>("email")
        ?: throw IllegalStateException("Email must be provided")


    override fun onViewIntent(intent: RegisterSuccessViewIntent) {
        when (intent) {
            RegisterSuccessViewIntent.OnLoginClick -> TODO()
            RegisterSuccessViewIntent.OnResendVerificationEmailClick -> handleResendVerification()
        }
    }

    private fun handleResendVerification() {
        if (state.value.isResendingVerificationEmail) return

        viewModelScope.launch {
            updateState { copy(isResendingVerificationEmail = true) }
            when (val result = resendVerificationEmailUseCase(email)) {
                is ResendVerificationEmailResult.Success -> {
                    updateState {
                        copy(
                            isResendingVerificationEmail = false,
                            viewEvent = RegisterSuccessViewEvent.ResentVerificationEmailSuccess
                        )
                    }
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
}