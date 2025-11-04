package com.yolo.myhabitshub.presentation.components.bottomnav

import com.yolo.myhabitshub.core.presentation.ScreenRoute
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_progress
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_settings
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_tracking
import com.yolo.myhabitshub.presentation.screens.account.AccountScreenRoute
import com.yolo.myhabitshub.presentation.screens.progress.HabitProgressScreenRoute
import com.yolo.myhabitshub.presentation.screens.tracking.HabitTrackingScreenRoute
import myhabitshub.core.designsystem.generated.resources.Res as R
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_habit_progress
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_habit_tracking
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class BottomNavItem(
    val label: StringResource,
    val icon: DrawableResource,
    val screenRoute: ScreenRoute<*, *, *, *>,
    val destination: String
) {
    companion object {

        fun items(): List<BottomNavItem> {
            return listOf(
                BottomNavItem(
                    label = Res.string.bottom_nav_label_tracking,
                    icon =  R.drawable.ic_nav_bottom_habit_tracking,
                    screenRoute = HabitTrackingScreenRoute(),
                    destination = HabitTrackingScreenRoute::class.simpleName!!
                ),
                BottomNavItem(
                    label = Res.string.bottom_nav_label_progress,
                    icon =  R.drawable.ic_nav_bottom_habit_progress,
                    screenRoute = HabitProgressScreenRoute(),
                    destination = HabitProgressScreenRoute::class.simpleName!!
                ),
                BottomNavItem(
                    label = Res.string.bottom_nav_label_settings,
                    icon =  R.drawable.ic_nav_bottom_settings,
                    screenRoute = AccountScreenRoute(),
                    destination = AccountScreenRoute::class.simpleName!!
                ),
            )
        }
    }
}