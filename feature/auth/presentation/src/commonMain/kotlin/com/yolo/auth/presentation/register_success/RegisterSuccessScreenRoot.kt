package com.yolo.auth.presentation.register_success

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.resent_verification_email
import org.jetbrains.compose.resources.getString

class RegisterSuccessScreenRoot(
    private val onLoginClick: () -> Unit = {},
) : ScreenRoot<RegisterSuccessViewModel, RegisterSuccessViewIntent, RegisterSuccessViewState, RegisterSuccessViewEvent> {
    @Composable
    override fun provideScreenContract(viewModel: RegisterSuccessViewModel): ScreenContract<RegisterSuccessViewState, RegisterSuccessViewEvent> {
        val snackbarHostState = remember { SnackbarHostState() }

        return object : ScreenContract<RegisterSuccessViewState, RegisterSuccessViewEvent> {
            @Composable
            override fun Screen(viewState: RegisterSuccessViewState) {
                RegisterSuccessScreen(
                    state = viewState,
                    snackbarHostState = snackbarHostState,
                    onLoginClick = { viewModel.handleIntent(RegisterSuccessViewIntent.OnLoginClick) },
                    onResendVerificationEmailClick = { viewModel.handleIntent(RegisterSuccessViewIntent.OnResendVerificationEmailClick) }
                )
            }

            override suspend fun handleEvent(event: RegisterSuccessViewEvent) {
                when (event) {
                    RegisterSuccessViewEvent.NavigateToLogin -> onLoginClick()
                    RegisterSuccessViewEvent.ResentVerificationEmailSuccess -> {
                        snackbarHostState.showSnackbar(
                            message = getString(Res.string.resent_verification_email),
                        )
                    }
                }
            }
        }
    }

    @Composable
    @Preview
    fun RegisterSuccessScreenPreview() {
        RegisterSuccessScreen(
            state = RegisterSuccessViewState(),
            snackbarHostState = remember { SnackbarHostState() },
            onLoginClick = {  },
            onResendVerificationEmailClick = {  }
        )
    }
}
