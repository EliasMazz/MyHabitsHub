package com.yolo.myhabitshub.presentation.screens.signin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot
import com.yolo.core.presentation.navigation.NavigationAction
import com.yolo.core.presentation.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
class SignInScreenRoot :
    ScreenRoot<SignInViewModel, SignInViewIntent, SignInViewState, SignInViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: SignInViewModel): ScreenContract<SignInViewState, SignInViewEvent> {
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

            override fun handleEvent(event: SignInViewEvent, navigator: Navigator) {
                when (event) {
                    SignInViewEvent.SignInSuccess -> navigator.execute(NavigationAction.PopBackStack)
                }
            }
        }
    }
}