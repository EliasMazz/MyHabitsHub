package com.yolo.myhabitshub.presentation.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.core.presentation.MviScreen
import com.yolo.myhabitshub.core.presentation.theme.AppTheme
import com.yolo.myhabitshub.presentation.components.LogoImage
import org.koin.compose.viewmodel.koinViewModel

enum class OnBoardingScreenStyle {
    STYLE1,
    STYLE2
}

@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = koinViewModel(),
    onOnBoardingComplete: () -> Unit,
) {
    MviScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                OnBoardingViewEvent.OnBoardingComplete -> onOnBoardingComplete()
            }
        }
    ) { state, onIntent ->
        OnBoardingScreenContent(
            style = OnBoardingScreenStyle.STYLE2,
            viewState = state,
            onStartClicked = { onIntent(OnBoardingViewIntent.OnStartClicked) }
        )
    }
}

@Composable
fun OnBoardingScreenContent(
    modifier: Modifier = Modifier,
    style: OnBoardingScreenStyle = OnBoardingScreenStyle.STYLE2,
    viewState: OnBoardingViewState,
    onStartClicked: () -> Unit
) {
    if (viewState.isLoading) {
        LogoImage(modifier = modifier.fillMaxSize())
    } else {
        when (style) {
            OnBoardingScreenStyle.STYLE1 -> {
                OnBoardingContentVariation1(
                    modifier = modifier.fillMaxSize()
                        .background(AppTheme.colors.background),
                    uiState = viewState,
                    onGetStartedClick = onStartClicked
                )
            }

            OnBoardingScreenStyle.STYLE2 -> {
                OnBoardingContentVariation2(
                    modifier = modifier.fillMaxSize()
                        .background(AppTheme.colors.background),
                    uiState = viewState,
                    onGetStartedClick = onStartClicked
                )
            }
        }
    }
}



