package com.yolo.myhabitshub.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yolo.auth.presentation.RegisterScreenRoot
import com.yolo.auth.presentation.RegisterViewModel
import com.yolo.core.presentation.LocalNavigator
import com.yolo.myhabitshub.presentation.screens.main.MainScreenRoot
import com.yolo.myhabitshub.presentation.screens.main.MainViewModel
import com.yolo.myhabitshub.presentation.screens.onboarding.OnBoardingScreenRoot
import com.yolo.myhabitshub.presentation.screens.onboarding.OnBoardingViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavigator provides navController) {
        NavHost(
            navController = navController,
            startDestination = RegisterScreenRoot()
        ) {
            composable<RegisterScreenRoot>{ navBackStackEntry ->
                navBackStackEntry.toRoute<RegisterScreenRoot>().ScreenEntryPoint(
                    viewModel = koinViewModel<RegisterViewModel>()
                )
            }
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