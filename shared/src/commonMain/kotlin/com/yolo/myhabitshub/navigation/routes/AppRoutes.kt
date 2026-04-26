package com.yolo.myhabitshub.navigation.routes

import kotlinx.serialization.Serializable

sealed interface AppRoutes {

    @Serializable
    data object OnBoarding : AppRoutes

    @Serializable
    data object Main : AppRoutes
}