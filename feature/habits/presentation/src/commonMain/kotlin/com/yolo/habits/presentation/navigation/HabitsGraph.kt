package com.yolo.habits.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yolo.habits.presentation.progress.HabitProgressScreen
import com.yolo.habits.presentation.tracking.HabitTrackingScreen
import myhabitshub.feature.habits.presentation.generated.resources.Res
import myhabitshub.feature.habits.presentation.generated.resources.title_screen_progress
import myhabitshub.feature.habits.presentation.generated.resources.title_screen_tracking
import org.jetbrains.compose.resources.stringResource

fun NavGraphBuilder.habitsGraph(
    onToolbarSetTitle: (String?) -> Unit,
) {
    composable<HabitsGraphRoutes.Tracking> {
        SetupScreenTitle(onToolbarSetTitle, stringResource(Res.string.title_screen_tracking))
        HabitTrackingScreen()
    }

    composable<HabitsGraphRoutes.Progress> {
        SetupScreenTitle(onToolbarSetTitle, stringResource(Res.string.title_screen_progress))
        HabitProgressScreen()
    }
}

@Composable
private fun SetupScreenTitle(onToolbarSetTitle: (String?) -> Unit, screenTitle: String) =
    LaunchedEffect(Unit) { onToolbarSetTitle(screenTitle) }
