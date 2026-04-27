package com.yolo.myhabitshub.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yolo.auth.presentation.navigation.AuthGraphRoutes
import com.yolo.auth.presentation.navigation.authGraph
import com.yolo.myhabitshub.navigation.routes.AppRoutes
import com.yolo.myhabitshub.presentation.screens.main.MainScreen
import com.yolo.myhabitshub.presentation.screens.onboarding.OnBoardingScreen

@Composable
fun AppNavigationRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppRoutes.OnBoarding
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(AppRoutes.OnBoarding) {
                    popUpTo(AuthGraphRoutes.Graph) { inclusive = true }
                }
            }
        )
        composable<AppRoutes.OnBoarding> {
            OnBoardingScreen(
                onOnBoardingComplete = {
                    navController.navigate(AppRoutes.Main) {
                        popUpTo(AppRoutes.OnBoarding) { inclusive = true }
                    }
                }
            )
        }
        composable<AppRoutes.Main> {
            // separate 'mainNavController' is used because it's the one that controls MainNavHost for bottomsheet
            val mainNavController = rememberNavController()
            MainScreen(
                navController = mainNavController,
                onSignInSuccess = {
                    navController.navigate(AppRoutes.Main) {
                        popUpTo(AppRoutes.Main) { inclusive = true }
                    }
                },
                onNavigateTo = { event ->
                    //TODO: Check, for some reason saveState, and restoreState doesn't work
                    mainNavController.navigate(event.route) {
                        if (event.popUpToStartDestination) {
                            popUpTo(mainNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = event.launchSingleTop
                            restoreState = true
                        }
                    }
                },
                onNavigateUp = { mainNavController.navigateUp() },
            )
        }
    }
}
