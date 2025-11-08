package com.yolo.myhabitshub.presentation

import com.yolo.myhabitshub.core.presentation.ScreenRoot
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_progress
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_settings
import com.yolo.myhabitshub.generated.resources.bottom_nav_label_tracking
import com.yolo.myhabitshub.presentation.screens.account.AccountScreenRoot
import com.yolo.myhabitshub.presentation.screens.progress.HabitProgressScreenRoot
import com.yolo.myhabitshub.presentation.screens.tracking.HabitTrackingScreenRoot
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_habit_progress
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_habit_tracking
import myhabitshub.core.designsystem.generated.resources.ic_nav_bottom_settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class BottomNavItem(
    val label: StringResource,
    val icon: DrawableResource,
    val screenRoot: ScreenRoot<*, *, *, *>,
    val destination: String
) {
    companion object {

        fun items(): List<BottomNavItem> {
            return listOf(
                BottomNavItem(
                    label = Res.string.bottom_nav_label_tracking,
                    icon =  myhabitshub.core.designsystem.generated.resources.Res.drawable.ic_nav_bottom_habit_tracking,
                    screenRoot = HabitTrackingScreenRoot(),
                    destination = HabitTrackingScreenRoot::class.simpleName!!
                ),
                BottomNavItem(
                    label = Res.string.bottom_nav_label_progress,
                    icon =  myhabitshub.core.designsystem.generated.resources.Res.drawable.ic_nav_bottom_habit_progress,
                    screenRoot = HabitProgressScreenRoot(),
                    destination = HabitProgressScreenRoot::class.simpleName!!
                ),
                BottomNavItem(
                    label = Res.string.bottom_nav_label_settings,
                    icon =  myhabitshub.core.designsystem.generated.resources.Res.drawable.ic_nav_bottom_settings,
                    screenRoot = AccountScreenRoot(),
                    destination = AccountScreenRoot::class.simpleName!!
                ),
            )
        }
    }
}