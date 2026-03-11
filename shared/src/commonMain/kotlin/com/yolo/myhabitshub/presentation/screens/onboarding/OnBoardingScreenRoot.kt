package com.yolo.myhabitshub.presentation.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot
import com.yolo.myhabitshub.presentation.screens.main.MainScreenRoot
import kotlinx.serialization.Serializable

@Serializable
class OnBoardingScreenRoot :
    ScreenRoot<OnBoardingViewModel, OnBoardingViewIntent, OnBoardingViewState, OnBoardingViewEvent> {
    @Composable
    override fun provideScreenContract(viewModel: OnBoardingViewModel): ScreenContract<OnBoardingViewState, OnBoardingViewEvent> {
        return object : ScreenContract<OnBoardingViewState, OnBoardingViewEvent> {

            @Composable
            override fun Screen(viewState: OnBoardingViewState) {
                OnBoardingScreen(
                    style = OnBoardingScreenStyle.STYLE2,
                    viewState = viewState,
                    onStartClicked = { viewModel.handleIntent(OnBoardingViewIntent.OnStartClicked) }
                )
            }

            override fun handleEvent(event: OnBoardingViewEvent, navigator: NavHostController) {
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

