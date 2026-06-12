package com.yolo.auth.presentation.forgot_password

import androidx.lifecycle.viewModelScope
import com.yolo.auth.domain.EmailValidatorUseCase
import com.yolo.auth.domain.ForgotPasswordResult
import com.yolo.auth.domain.ForgotPasswordUseCase
import com.yolo.core.presentation.util.UiText
import com.yolo.core.presentation.util.toUiText
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_email
import myhabitshub.feature.auth.presentation.generated.resources.forgot_password_email_sent_successfully

class ForgotPasswordViewModel(
    private val emailValidatorUseCase: EmailValidatorUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : BaseViewModel<ForgotPasswordViewIntent, ForgotPasswordViewState, ForgotPasswordViewEvent>(
    initialState = ForgotPasswordViewState()
) {
    override fun onViewIntent(intent: ForgotPasswordViewIntent) {
        when (intent) {
            ForgotPasswordViewIntent.OnBackClick -> sendEvent(ForgotPasswordViewEvent.NavigateBack)
            is ForgotPasswordViewIntent.OnEmailChange -> handleEmailChange(intent.email)
            ForgotPasswordViewIntent.OnSubmitClick -> submitForgotPassword()
        }
    }

    private fun handleEmailChange(email: String) {
        updateState {
            copy(
                email = email,
                emailError = null,
                errorText = null,
                canSubmit = email.isNotBlank(),
            )
        }
    }

    private fun submitForgotPassword() {
        if (state.value.isLoading || !state.value.canSubmit) {
            return
        }

        viewModelScope.launch {
            val email = state.value.email

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
                    updateState {
                        copy(
                            isLoading = false,
                            errorText = result.dataError.toUiText(),
                        )
                    }
                }

                ForgotPasswordResult.Success -> {
                    updateState {
                        copy(
                            isLoading = false,
                            isEmailSentSuccessfully = true,
                            errorText = UiText.Resource(Res.string.forgot_password_email_sent_successfully),
                        )
                    }
                }
            }
        }
    }
}
