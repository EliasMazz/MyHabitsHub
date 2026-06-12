package com.yolo.myhabitshub.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.yolo.account.presentation.navigation.accountGraph
import com.yolo.habits.presentation.navigation.HabitsGraphRoutes
import com.yolo.habits.presentation.navigation.habitsGraph
import com.yolo.myhabitshub.navigation.routes.MainGraphRoutes

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    onToolbarSetTitle: (String?) -> Unit,
    onSignInSuccess: () -> Unit,
) {
    navigation<MainGraphRoutes.Graph>(
        startDestination = HabitsGraphRoutes.Tracking
    ) {
        habitsGraph(onToolbarSetTitle = onToolbarSetTitle)

        accountGraph(
            navController = navController,
            onToolbarSetTitle = onToolbarSetTitle,
            onSignInSuccess = onSignInSuccess,
        )
    }
}
