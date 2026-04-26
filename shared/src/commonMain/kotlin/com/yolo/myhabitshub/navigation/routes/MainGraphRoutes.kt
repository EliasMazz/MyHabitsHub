package com.yolo.myhabitshub.navigation.routes

import kotlinx.serialization.Serializable

sealed interface MainGraphRoutes {

    @Serializable
    data object Graph : MainGraphRoutes

    @Serializable
    data object HabitTracking : MainGraphRoutes

    @Serializable
    data object HabitProgress : MainGraphRoutes

    @Serializable
    data object Account : MainGraphRoutes

    @Serializable
    data object Settings : MainGraphRoutes

    @Serializable
    data object SignIn : MainGraphRoutes

    @Serializable
    data object HelpAndSupport : MainGraphRoutes
}