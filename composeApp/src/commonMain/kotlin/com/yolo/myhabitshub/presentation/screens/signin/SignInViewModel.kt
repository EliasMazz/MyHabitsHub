package com.yolo.myhabitshub.presentation.screens.signin

import androidx.lifecycle.viewModelScope
import com.yolo.core.domain.usecase.invoke
import com.yolo.myhabitshub.domain.usecase.SendAuthTokenUseCase
import com.yolo.core.presentation.viewmodel.BaseViewModel
import com.yolo.myhabitshub.domain.usecase.SendAuthResult
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.launch

class SignInViewModel(
    private val sendAuthTokenUseCase: SendAuthTokenUseCase
) : BaseViewModel<SignInViewIntent, SignInViewState, SignInViewEvent>(
    initialState = SignInViewState()
) {
    override fun onViewIntent(intent: SignInViewIntent) {
        when (intent) {
            is SignInViewIntent.OnSignInFail -> handleSignInFail(intent.throwable)
            is SignInViewIntent.OnSignInSuccess -> handleSignInSuccess()
        }
    }

    private fun handleSignInFail(throwable: Throwable?) {
        updateState {
            copy(
                linkAccount = false,
                snackbarMessage = buildErrorMessage(throwable)
            )
        }
    }

    private fun handleSignInSuccess() {
        viewModelScope.launch {
            when(val result = sendAuthTokenUseCase.invoke()){
                is SendAuthResult.Error -> {
                    handleSignInFail(result.throwable)
                }
                is SendAuthResult.Success -> {
                    updateState {
                        copy(
                            linkAccount = true,
                            snackbarMessage = "Sign in successful",
                            viewEvent = SignInViewEvent.SignInSuccess
                        )
                    }
                }
            }

        }
    }

    private fun buildErrorMessage(exception: Throwable?): String {
        var errorMessage = exception?.message ?: "An unknown error occurred"
        if (exception is FirebaseAuthUserCollisionException) {
            errorMessage += " Please, try again."
        }
        return errorMessage
    }
}
