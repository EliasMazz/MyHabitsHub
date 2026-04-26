package com.yolo.myhabitshub.presentation

import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_progress
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_settings
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_tracking
import com.yolo.myhabitshub.navigation.routes.MainGraphRoutes
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_habit_progress
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_habit_tracking
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class BottomNavItem(
    val label: StringResource,
    val icon: DrawableResource,
    val route: MainGraphRoutes,
    val destination: String
) {
    companion object {
        fun items(): List<BottomNavItem> {
            return listOf(
                BottomNavItem(
                    label = Res.string.bottom_nav_label_tracking,
                    icon =  myhabitshub.core.designsystem.generated.resources.Res.drawable.ic_nav_bottom_habit_tracking,
                    route = MainGraphRoutes.HabitTracking,
                    destination = MainGraphRoutes.HabitTracking::class.simpleName!!
                ),
                BottomNavItem(
                    label = Res.string.bottom_nav_label_progress,
                    icon =  myhabitshub.core.designsystem.generated.resources.Res.drawable.ic_nav_bottom_habit_progress,
                    route = MainGraphRoutes.HabitProgress,
                    destination = MainGraphRoutes.HabitProgress::class.simpleName!!
                ),
                BottomNavItem(
                    label = Res.string.bottom_nav_label_settings,
                    icon =  myhabitshub.core.designsystem.generated.resources.Res.drawable.ic_nav_bottom_settings,
                    route = MainGraphRoutes.Account,
                    destination = MainGraphRoutes.Account::class.simpleName!!
                ),
            )
        }
    }
}
