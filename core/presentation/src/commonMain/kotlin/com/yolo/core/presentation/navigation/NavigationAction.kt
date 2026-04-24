package com.yolo.core.presentation.navigation

sealed interface NavigationAction {
    data class Navigate(val route: Any) : NavigationAction
    data class NavigatePopUpTo(
        val route: Any,
        val popUpTo: Any,
        val inclusive: Boolean = false,
    ) : NavigationAction
    data object PopBackStack : NavigationAction
}
