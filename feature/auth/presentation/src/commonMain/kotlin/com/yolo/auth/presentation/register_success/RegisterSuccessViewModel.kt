package com.yolo.auth.presentation.register_success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yolo.auth.domain.ResendVerificationEmailResult
import com.yolo.auth.domain.ResendVerificationEmailUseCase
import com.yolo.core.presentation.util.toUiText
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

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
            RegisterSuccessViewIntent.OnLoginClick -> updateState { copy(viewEvent = RegisterSuccessViewEvent.NavigateToLoginEvent) }
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
                            viewEvent = RegisterSuccessViewEvent.ResendVerificationEmailSuccessEvent
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