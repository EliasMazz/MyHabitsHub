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

    private val token: String

    init {
        // Debug: see everything in SavedStateHandle
        println("[DEBUG] SavedStateHandle keys: ${savedStateHandle.keys()}")
        savedStateHandle.keys().forEach { key ->
            println("[DEBUG] SavedStateHandle[$key] = ${savedStateHandle.get<Any>(key)}")
        }

        val tokenFromRoute = savedStateHandle.toRoute<AuthGraphRoutes.EmailVerification>().token
        val tokenDirect = savedStateHandle.get<String>("token")
        println("[DEBUG] token via toRoute(): '$tokenFromRoute'")
        println("[DEBUG] token via get(): '$tokenDirect'")

        token = tokenDirect ?: tokenFromRoute
        verifyEmail()
    }
    
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