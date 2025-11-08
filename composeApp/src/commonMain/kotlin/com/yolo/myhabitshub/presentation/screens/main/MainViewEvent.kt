package com.yolo.myhabitshub.presentation.screens.main

import com.yolo.myhabitshub.core.presentation.ScreenRoot
import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface MainViewEvent: ViewEvent {

    /**
     * Event to navigate to a specific route.
     *
     * @param screenRoot The destination route
     * @param popUpToStartDestination If true, pops up to the start destination of the graph
     *                                before navigating. This is common for bottom navigation items
     *                                to prevent building up a large back stack.
     * @param launchSingleTop If true, and the destination is already at the top of the back stack,
     *                        no new instance will be created. This is also common for bottom navigation.
     */

    data class NavigateTo(
        val screenRoot: ScreenRoot<*, *, *, *>,
        val popUpToStartDestination: Boolean = false,
        val launchSingleTop: Boolean = true
    ) : MainViewEvent

    /**
     * Event to navigate up in the back stack (equivalent to pressing the system back button
     * within the current navigation graph).
     */
    data object NavigateUp : MainViewEvent
}