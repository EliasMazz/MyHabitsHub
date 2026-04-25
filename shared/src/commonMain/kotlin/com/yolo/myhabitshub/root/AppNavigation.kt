package com.yolo.myhabitshub.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yolo.auth.presentation.register.RegisterScreenRoot
import com.yolo.auth.presentation.register.RegisterViewModel
import com.yolo.core.presentation.LocalNavigator
import com.yolo.myhabitshub.navigation.ComposeNavigator
import com.yolo.myhabitshub.presentation.screens.main.MainScreenRoot
import com.yolo.myhabitshub.presentation.screens.main.MainViewModel
import com.yolo.myhabitshub.presentation.screens.onboarding.OnBoardingScreenRoot
import com.yolo.myhabitshub.presentation.screens.onboarding.OnBoardingViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val navigator = remember(navController) { ComposeNavigator(navController) }
    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavHost(
            navController = navController,
            // Add RegisterScreenRoot for OnBoardingScreenRoot onboarding screen
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