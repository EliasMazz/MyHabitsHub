package com.yolo.myhabitshub.presentation.screens.signin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.myhabitshub.core.presentation.ScreenContract
import com.yolo.myhabitshub.root.LocalNavigator
import com.yolo.myhabitshub.core.presentation.ScreenRoute
import kotlinx.serialization.Serializable

@Serializable
class SignInScreenRoute :
    ScreenRoute<SignInViewModel, SignInViewIntent, SignInViewState, SignInViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: SignInViewModel): ScreenContract<SignInViewState, SignInViewEvent> {
        val navigator = LocalNavigator.current

        return object : ScreenContract<SignInViewState, SignInViewEvent> {
            @Composable
            override fun Screen(viewState: SignInViewState) {
                SignInScreen(
                    modifier = Modifier.fillMaxSize(),
                    viewState = viewState,
                    onSignInViewSuccess = { viewModel.handleIntent(SignInViewIntent.OnSignInSuccess) },
                    onSignInViewFail = { exception -> viewModel.handleIntent(SignInViewIntent.OnSignInFail(exception))}
                )
            }

            override fun handleEvent(event: SignInViewEvent) {
                when (event) {
                    SignInViewEvent.SignInSuccess -> navigator.popBackStack()
                }
            }
        }
    }
}