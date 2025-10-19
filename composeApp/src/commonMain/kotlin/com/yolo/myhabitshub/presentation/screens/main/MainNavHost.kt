package com.yolo.myhabitshub.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.title_screen_help_and_support
import com.yolo.myhabitshub.generated.resources.title_screen_account
import com.yolo.myhabitshub.generated.resources.title_screen_progress
import com.yolo.myhabitshub.generated.resources.title_screen_profile
import com.yolo.myhabitshub.generated.resources.title_screen_tracking
import com.yolo.myhabitshub.generated.resources.title_screen_sign_in
import com.yolo.myhabitshub.presentation.screens.account.AccountScreenRoute
import com.yolo.myhabitshub.presentation.screens.account.AccountViewModel
import com.yolo.myhabitshub.presentation.screens.progress.HabitProgressScreenRoute
import com.yolo.myhabitshub.presentation.screens.helpandsupport.HelpAndSupportScreenRoute
import com.yolo.myhabitshub.presentation.screens.helpandsupport.HelpAndSupportViewModel
import com.yolo.myhabitshub.presentation.screens.progress.HabitProgressViewModel
import com.yolo.myhabitshub.presentation.screens.tracking.HabitTrackingScreenRoute
import com.yolo.myhabitshub.presentation.screens.settings.SettingsScreenRoute
import com.yolo.myhabitshub.presentation.screens.settings.SettingsViewModel
import com.yolo.myhabitshub.presentation.screens.signin.SignInScreenRoute
import com.yolo.myhabitshub.presentation.screens.signin.SignInViewModel
import com.yolo.myhabitshub.presentation.screens.tracking.HabitTrackingViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    onToolbarSetTitle: (String?) -> Unit,
) {
    // Toolbar title didn't update reliably setting navBackStackEntry.destination.label = screenTitle
    @Composable
    fun SetupScreenTitle(
        screenTitle: String,
    )  = LaunchedEffect(Unit) { onToolbarSetTitle(screenTitle) }

    NavHost(
        navController = navController,
        startDestination = HabitTrackingScreenRoute()
    ) {
        composable<HabitTrackingScreenRoute> { navBackStackEntry ->
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_tracking))
            navBackStackEntry.toRoute<HabitTrackingScreenRoute>().ScreenEntryPoint(
                viewModel = koinViewModel<HabitTrackingViewModel>()
            )
        }
        composable<SettingsScreenRoute> { navBackStackEntry ->
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_profile))
            navBackStackEntry.toRoute<SettingsScreenRoute>().ScreenEntryPoint(
                viewModel = koinViewModel<SettingsViewModel>()
            )
        }
        composable<SignInScreenRoute> { navBackStackEntry ->
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_sign_in))
            navBackStackEntry.toRoute<SignInScreenRoute>().ScreenEntryPoint(
                viewModel = koinViewModel<SignInViewModel>()
            )
        }
        composable<AccountScreenRoute> { navBackStackEntry ->
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_account))
            navBackStackEntry.toRoute<AccountScreenRoute>().ScreenEntryPoint(
                viewModel = koinViewModel<AccountViewModel>()
            )
        }
        composable<HabitProgressScreenRoute> { navBackStackEntry ->
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_progress))
            navBackStackEntry.toRoute<HabitProgressScreenRoute>().ScreenEntryPoint(
                viewModel = koinViewModel<HabitProgressViewModel>()
            )
        }
        composable<HelpAndSupportScreenRoute> { navBackStackEntry ->
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_help_and_support))
            navBackStackEntry.toRoute<HelpAndSupportScreenRoute>().ScreenEntryPoint(
                viewModel = koinViewModel<HelpAndSupportViewModel>()
            )
        }
    }
}

