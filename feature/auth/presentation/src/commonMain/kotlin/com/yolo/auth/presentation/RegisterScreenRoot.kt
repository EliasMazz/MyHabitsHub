package com.yolo.auth.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot
import kotlinx.serialization.Serializable

@Serializable
class RegisterScreenRoot :
    ScreenRoot<RegisterViewModel, RegisterViewIntent, RegisterViewState, RegisterViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: RegisterViewModel): ScreenContract<RegisterViewState, RegisterViewEvent> {

        return object : ScreenContract<RegisterViewState, RegisterViewEvent> {

            @Composable
            override fun Screen(viewState: RegisterViewState) {
                RegisterScreen(
                    state = viewState,
                    onRegisterClick = { viewModel.handleIntent(RegisterViewIntent.OnRegisterClick) },
                    onInputTextFocusGain = { viewModel.handleIntent(RegisterViewIntent.OnInputTextFocusGain) }
                )
            }

            override fun handleEvent(
                event: RegisterViewEvent,
                navigator: NavHostController
            ) {

            }
        }
    }


}