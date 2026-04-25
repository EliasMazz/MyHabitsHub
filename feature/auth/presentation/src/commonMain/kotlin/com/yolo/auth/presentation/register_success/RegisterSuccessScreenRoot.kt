package com.yolo.auth.presentation.register_success

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot
import com.yolo.core.presentation.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
class RegisterSuccessScreenRoot :
    ScreenRoot<RegisterSuccessViewModel, RegisterSuccessViewIntent, RegisterSuccessViewState, RegisterSuccessViewEvent> {
    @Composable
    override fun provideScreenContract(viewModel: RegisterSuccessViewModel): ScreenContract<RegisterSuccessViewState, RegisterSuccessViewEvent> {

        return object : ScreenContract<RegisterSuccessViewState, RegisterSuccessViewEvent> {
            @Composable
            override fun Screen(viewState: RegisterSuccessViewState) {
                RegisterSuccessScreen(
                    state = viewState,
                    onLoginClick = { viewModel.handleIntent(RegisterSuccessViewIntent.OnLoginClick) },
                    onResendVerificationEmailClick = { viewModel.handleIntent(RegisterSuccessViewIntent.OnResendVerificationEmailClick) }
                )
            }

            override fun handleEvent(
                event: RegisterSuccessViewEvent,
                navigator: Navigator
            ) {

            }
        }
    }

    @Composable
    @Preview
    fun RegisterSuccessScreenPreview() {
        RegisterSuccessScreen(
            state = RegisterSuccessViewState(),
            onLoginClick = {  },
            onResendVerificationEmailClick = {  }
        )
    }


}