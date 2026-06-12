package com.yolo.myhabitshub.navigation.routes

import kotlinx.serialization.Serializable

sealed interface MainGraphRoutes {

    @Serializable
    data object Graph : MainGraphRoutes
}