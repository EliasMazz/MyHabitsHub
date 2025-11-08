package com.yolo.myhabitshub.presentation.screens.onboarding

import androidx.compose.runtime.Composable
import com.yolo.myhabitshub.core.presentation.ScreenContract
import com.yolo.myhabitshub.core.presentation.ScreenRoot
import com.yolo.myhabitshub.presentation.screens.main.MainScreenRoot
import com.yolo.myhabitshub.root.LocalNavigator
import kotlinx.serialization.Serializable

@Serializable
class OnBoardingScreenRoot :
    ScreenRoot<OnBoardingViewModel, OnBoardingViewIntent, OnBoardingViewState, OnBoardingViewEvent> {
    @Composable
    override fun provideScreenContract(viewModel: OnBoardingViewModel): ScreenContract<OnBoardingViewState, OnBoardingViewEvent> {
        val navigator = LocalNavigator.current

        return object : ScreenContract<OnBoardingViewState, OnBoardingViewEvent> {

            @Composable
            override fun ScreenView(viewState: OnBoardingViewState) {
                OnBoardingScreen(
                    style = OnBoardingScreenStyle.STYLE2,
                    viewState = viewState,
                    onStartClicked = { viewModel.handleIntent(OnBoardingViewIntent.OnStartClicked) }
                )
            }

            override fun handleEvent(event: OnBoardingViewEvent) {
                when (event) {
                    OnBoardingViewEvent.OnBoardingComplete -> navigator.navigate(MainScreenRoot()) {
                        popUpTo(OnBoardingScreenRoot()) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}

