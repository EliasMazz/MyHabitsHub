package com.yolo.auth.presentation.register_success

import androidx.compose.runtime.Composable
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
                RegisterSuccessScreen()
            }

            override fun handleEvent(
                event: RegisterSuccessViewEvent,
                navigator: Navigator
            ) {

            }
        }
    }


}