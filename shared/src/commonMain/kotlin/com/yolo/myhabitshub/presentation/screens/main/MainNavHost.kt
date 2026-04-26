package com.yolo.myhabitshub.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.yolo.myhabitshub.navigation.routes.MainGraphRoutes
import com.yolo.myhabitshub.navigation.mainGraph

@Composable
fun MainNavHost(
    navController: NavHostController,
    onSignInSuccess: () -> Unit,
    onToolbarSetTitle: (String?) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = MainGraphRoutes.Graph
    ) {
        mainGraph(
            navController = navController,
            onToolbarSetTitle = onToolbarSetTitle,
            onSignInSuccess = onSignInSuccess,
        )
    }
}
