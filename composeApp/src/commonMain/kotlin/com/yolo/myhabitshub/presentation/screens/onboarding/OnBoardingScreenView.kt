package com.yolo.myhabitshub.presentation.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.myhabitshub.core.presentation.theme.AppTheme
import com.yolo.myhabitshub.presentation.components.LogoImage

enum class OnBoardingScreenStyle {
    STYLE1,
    STYLE2
}

@Composable
fun OnBoardingScreenView(
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



