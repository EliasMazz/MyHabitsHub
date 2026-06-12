package com.yolo.onboarding.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface OnBoardingRoutes {

    @Serializable
    data object Graph : OnBoardingRoutes
}
