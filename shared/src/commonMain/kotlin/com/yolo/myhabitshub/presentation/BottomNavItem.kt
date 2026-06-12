package com.yolo.myhabitshub.presentation

import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_progress
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_settings
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_tracking
import com.yolo.account.presentation.navigation.AccountGraphRoutes
import com.yolo.habits.presentation.navigation.HabitsGraphRoutes
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_habit_progress
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_habit_tracking
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class BottomNavItem(
    val label: StringResource,
    val icon: DrawableResource,
    val route: Any,
    val destination: String
) {
    companion object {
        fun items(): List<BottomNavItem> {
            return listOf(
                BottomNavItem(
                    label = Res.string.bottom_nav_label_tracking,
                    icon =  myhabitshub.core.designsystem.generated.resources.Res.drawable.ic_nav_bottom_habit_tracking,
                    route = HabitsGraphRoutes.Tracking,
                    destination = HabitsGraphRoutes.Tracking::class.simpleName!!
                ),
                BottomNavItem(
                    label = Res.string.bottom_nav_label_progress,
                    icon =  myhabitshub.core.designsystem.generated.resources.Res.drawable.ic_nav_bottom_habit_progress,
                    route = HabitsGraphRoutes.Progress,
                    destination = HabitsGraphRoutes.Progress::class.simpleName!!
                ),
                BottomNavItem(
                    label = Res.string.bottom_nav_label_settings,
                    icon =  myhabitshub.core.designsystem.generated.resources.Res.drawable.ic_nav_bottom_settings,
                    route = AccountGraphRoutes.Account,
                    destination = AccountGraphRoutes.Account::class.simpleName!!
                ),
            )
        }
    }
}
