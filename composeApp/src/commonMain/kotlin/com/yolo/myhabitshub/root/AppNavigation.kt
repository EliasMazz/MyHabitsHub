package com.yolo.myhabitshub.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yolo.myhabitshub.presentation.screens.main.MainScreenRoot
import com.yolo.myhabitshub.presentation.screens.main.MainViewModel
import com.yolo.myhabitshub.presentation.screens.onboarding.OnBoardingScreenRoot
import com.yolo.myhabitshub.presentation.screens.onboarding.OnBoardingViewModel
import org.koin.compose.viewmodel.koinViewModel

val LocalNavigator = compositionLocalOf<NavHostController> {
    error("No LocalNavController provided")
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavigator provides navController) {
        NavHost(
            navController = navController,
            startDestination = OnBoardingScreenRoot()
        ) {
            composable<OnBoardingScreenRoot> { navBackStackEntry ->
                navBackStackEntry.toRoute<OnBoardingScreenRoot>().ScreenEntryPoint(
                    viewModel = koinViewModel<OnBoardingViewModel>()
                )
            }
            composable<MainScreenRoot> { navBackStackEntry ->
                navBackStackEntry.toRoute<MainScreenRoot>().ScreenEntryPoint(
                    viewModel = koinViewModel<MainViewModel>()
                )
            }
        }
    }
}