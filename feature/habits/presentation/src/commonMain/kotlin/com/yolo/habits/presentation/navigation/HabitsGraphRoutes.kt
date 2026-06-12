package com.yolo.habits.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface HabitsGraphRoutes {

    @Serializable
    data object Tracking : HabitsGraphRoutes

    @Serializable
    data object Progress : HabitsGraphRoutes
}
