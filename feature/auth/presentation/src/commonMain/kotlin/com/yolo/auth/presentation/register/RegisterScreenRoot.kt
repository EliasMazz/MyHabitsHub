package com.yolo.auth.presentation.register

import androidx.compose.runtime.Composable
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot

class RegisterScreenRoot(
    private val onRegisterSuccess: (String) -> Unit,
) : ScreenRoot<RegisterViewModel, RegisterViewIntent, RegisterViewState, RegisterViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: RegisterViewModel): ScreenContract<RegisterViewState, RegisterViewEvent> {
        return object : ScreenContract<RegisterViewState, RegisterViewEvent> {

            @Composable
            override fun Screen(viewState: RegisterViewState) {
                RegisterScreen(
                    state = viewState,
                    onRegisterClick = { viewModel.handleIntent(RegisterViewIntent.OnRegisterClick) },
                    onEmailChange = { viewModel.handleIntent(RegisterViewIntent.OnEmailChange(it)) },
                    onPasswordChange = { viewModel.handleIntent(RegisterViewIntent.OnPasswordChange(it)) },
                    onInputTextFocusGain = { viewModel.handleIntent(RegisterViewIntent.OnInputTextFocusGain) },
                    onTogglePasswordVisibility = { viewModel.handleIntent(RegisterViewIntent.OnTogglePasswordVisibility) }
                )
            }

            override suspend fun handleEvent(event: RegisterViewEvent) {
                when (event) {
                    is RegisterViewEvent.OnRegisterSuccess -> {
                        onRegisterSuccess(event.email)
                    }
                }
            }
        }
    }
}
