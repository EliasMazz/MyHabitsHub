package com.yolo.onboarding.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yolo.onboarding.presentation.OnBoardingScreen

fun NavGraphBuilder.onBoardingGraph(
    onOnBoardingComplete: () -> Unit,
) {
    composable<OnBoardingRoutes.Graph> {
        OnBoardingScreen(
            onOnBoardingComplete = onOnBoardingComplete,
        )
    }
}
