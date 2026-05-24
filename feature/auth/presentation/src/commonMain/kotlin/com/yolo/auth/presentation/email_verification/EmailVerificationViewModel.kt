package com.yolo.auth.presentation.email_verification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yolo.auth.domain.EmailVerificationUseCase
import com.yolo.auth.presentation.navigation.AuthGraphRoutes
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class EmailVerificationViewModel(
    savedStateHandle: SavedStateHandle,
    private val emailVerificationUseCase: EmailVerificationUseCase
) : BaseViewModel<EmailVerificationViewIntent, EmailVerificationViewState, EmailVerificationViewEvent>(
    initialState = EmailVerificationViewState()
) {

    private val token: String = savedStateHandle.get<String>("token")
        ?: throw IllegalStateException("Token must be provided")

    init {
        verifyEmail()
    }

    override fun onViewIntent(intent: EmailVerificationViewIntent) {
        when (intent) {
            EmailVerificationViewIntent.OnCloseClick -> updateState {
                copy(viewEvent = EmailVerificationViewEvent.NavigateBackEvent)
            }

            EmailVerificationViewIntent.OnLoginClick -> updateState {
                copy(viewEvent = EmailVerificationViewEvent.NavigateToLoginEvent)
            }
        }
    }

    private fun verifyEmail() {
        viewModelScope.launch {
            updateState {
                copy(
                    isVerifying = true
                )
            }

            when (emailVerificationUseCase(token)) {
                EmailVerificationUseCase.EmailVerificationResult.Success -> {
                    updateState {
                        copy(
                            isVerifying = false,
                            isVerified = true,
                        )
                    }
                }

                EmailVerificationUseCase.EmailVerificationResult.Failure -> {
                    updateState {
                        copy(
                            isVerifying = false,
                            isVerified = false,
                        )
                    }
                }
            }
        }
    }

}