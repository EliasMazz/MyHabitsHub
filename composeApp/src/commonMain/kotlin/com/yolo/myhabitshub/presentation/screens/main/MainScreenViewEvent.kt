package com.yolo.myhabitshub.presentation.screens.main

import com.yolo.myhabitshub.core.presentation.ScreenRoute
import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.*

sealed interface MainScreenViewEvent: ViewEvent {

    /**
     * Event to navigate to a specific route.
     *
     * @param screenRoute The destination route
     * @param popUpToStartDestination If true, pops up to the start destination of the graph
     *                                before navigating. This is common for bottom navigation items
     *                                to prevent building up a large back stack.
     * @param launchSingleTop If true, and the destination is already at the top of the back stack,
     *                        no new instance will be created. This is also common for bottom navigation.
     */

    data class NavigateTo(
        val screenRoute: ScreenRoute<*, *, *, *>,
        val popUpToStartDestination: Boolean = false,
        val launchSingleTop: Boolean = true
    ) : MainScreenViewEvent

    /**
     * Event to navigate up in the back stack (equivalent to pressing the system back button
     * within the current navigation graph).
     */
    data object NavigateUp : MainScreenViewEvent
}