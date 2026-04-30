package com.yolo.auth.presentation.email_verification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yolo.auth.domain.EmailVerificationUseCase
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class EmailVerificationViewModel(
    savedStateHandle: SavedStateHandle,
    private val emailVerificationUseCase: EmailVerificationUseCase
) : BaseViewModel<EmailVerificationViewIntent, EmailVerificationViewState, EmailVerificationViewEvent>(
    initialState = EmailVerificationViewState()
) {

    init {
        verifyEmail()
    }

    private val token =
        savedStateHandle.get<String>("token") ?: throw IllegalStateException("Invalid token")

    override fun onViewIntent(intent: EmailVerificationViewIntent) {

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