package com.yolo.myhabitshub.presentation.screens.signin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.yolo.myhabitshub.core.presentation.ScreenContract
import com.yolo.myhabitshub.core.presentation.ScreenRoot
import kotlinx.serialization.Serializable

@Serializable
class SignInScreenRoot :
    ScreenRoot<SignInViewModel, SignInViewIntent, SignInViewState, SignInViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: SignInViewModel): ScreenContract<SignInViewState, SignInViewEvent> {
        return object : ScreenContract<SignInViewState, SignInViewEvent> {
            @Composable
            override fun ScreenView(viewState: SignInViewState) {
                SignInScreenView(
                    modifier = Modifier.fillMaxSize(),
                    viewState = viewState,
                    onSignInViewSuccess = { viewModel.handleIntent(SignInViewIntent.OnSignInSuccess) },
                    onSignInViewFail = { exception -> viewModel.handleIntent(SignInViewIntent.OnSignInFail(exception))}
                )
            }

            override fun handleEvent(event: SignInViewEvent, navigator: NavHostController) {
                when (event) {
                    SignInViewEvent.SignInSuccess -> navigator.popBackStack()
                }
            }
        }
    }
}