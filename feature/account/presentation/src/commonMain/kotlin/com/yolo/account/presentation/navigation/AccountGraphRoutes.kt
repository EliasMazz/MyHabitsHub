package com.yolo.account.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AccountGraphRoutes {

    @Serializable
    data object Account : AccountGraphRoutes

    @Serializable
    data object Settings : AccountGraphRoutes

    @Serializable
    data object SignIn : AccountGraphRoutes

    @Serializable
    data object HelpAndSupport : AccountGraphRoutes
}
