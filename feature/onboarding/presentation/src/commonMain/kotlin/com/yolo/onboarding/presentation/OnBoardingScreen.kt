package com.yolo.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.core.presentation.BaseScreen
import com.yolo.core.designsystem.components.legacy.LogoImage
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
    BaseScreen(
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
                        .background(MaterialTheme.colorScheme.background),
                    uiState = viewState,
                    onGetStartedClick = onStartClicked
                )
            }

            OnBoardingScreenStyle.STYLE2 -> {
                OnBoardingContentVariation2(
                    modifier = modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    uiState = viewState,
                    onGetStartedClick = onStartClicked
                )
            }
        }
    }
}



