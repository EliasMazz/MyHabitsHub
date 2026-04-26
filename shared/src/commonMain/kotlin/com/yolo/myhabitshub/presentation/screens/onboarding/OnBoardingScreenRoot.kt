package com.yolo.myhabitshub.presentation.screens.onboarding

import androidx.compose.runtime.Composable
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot

class OnBoardingScreenRoot(
    private val onOnBoardingComplete: () -> Unit = {},
) : ScreenRoot<OnBoardingViewModel, OnBoardingViewIntent, OnBoardingViewState, OnBoardingViewEvent> {
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

            override suspend fun handleEvent(event: OnBoardingViewEvent) {
                when (event) {
                    OnBoardingViewEvent.OnBoardingComplete -> onOnBoardingComplete()
                }
            }
        }
    }
}
