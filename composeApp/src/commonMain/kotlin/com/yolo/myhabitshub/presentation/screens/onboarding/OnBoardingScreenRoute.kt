package com.yolo.myhabitshub.presentation.screens.onboarding

import androidx.compose.runtime.Composable
import com.yolo.myhabitshub.core.presentation.ScreenContract
import com.yolo.myhabitshub.core.presentation.ScreenRoute
import com.yolo.myhabitshub.presentation.screens.main.MainScreenRoute
import com.yolo.myhabitshub.root.LocalNavigator
import kotlinx.serialization.Serializable

@Serializable
class OnBoardingScreenRoute :
    ScreenRoute<OnBoardingViewModel, OnBoardingViewIntent, OnBoardingViewState, OnBoardingViewEvent> {
    @Composable
    override fun provideScreenContract(viewModel: OnBoardingViewModel): ScreenContract<OnBoardingViewState, OnBoardingViewEvent> {
        val navigator = LocalNavigator.current

        return object : ScreenContract<OnBoardingViewState, OnBoardingViewEvent> {

            @Composable
            override fun Screen(viewState: OnBoardingViewState) {
                OnBoardingScreen(
                    style = OnBoardingScreenStyle.STYLE2,
                    viewState = viewState,
                    onStartClicked = { viewModel.handleIntent(OnBoardingViewIntent.OnStartClicked) }
                )
            }

            override fun handleEvent(event: OnBoardingViewEvent) {
                when (event) {
                    OnBoardingViewEvent.OnBoardingComplete -> navigator.navigate(MainScreenRoute()) {
                        popUpTo(OnBoardingScreenRoute()) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}

