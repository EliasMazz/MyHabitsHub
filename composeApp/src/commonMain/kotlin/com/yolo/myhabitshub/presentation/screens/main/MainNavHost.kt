package com.yolo.myhabitshub.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.title_screen_help_and_support
import com.yolo.myhabitshub.generated.resources.title_screen_account
import com.yolo.myhabitshub.generated.resources.title_screen_progress
import com.yolo.myhabitshub.generated.resources.title_screen_profile
import com.yolo.myhabitshub.generated.resources.title_screen_tracking
import com.yolo.myhabitshub.generated.resources.title_screen_sign_in
import com.yolo.myhabitshub.presentation.screens.account.AccountScreenRoot
import com.yolo.myhabitshub.presentation.screens.account.AccountViewModel
import com.yolo.myhabitshub.presentation.screens.progress.HabitProgressScreenRoot
import com.yolo.myhabitshub.presentation.screens.helpandsupport.HelpAndSupportScreenRoot
import com.yolo.myhabitshub.presentation.screens.helpandsupport.HelpAndSupportViewModel
import com.yolo.myhabitshub.presentation.screens.progress.HabitProgressViewModel
import com.yolo.myhabitshub.presentation.screens.tracking.HabitTrackingScreenRoot
import com.yolo.myhabitshub.presentation.screens.settings.SettingsScreenRoot
import com.yolo.myhabitshub.presentation.screens.settings.SettingsViewModel
import com.yolo.myhabitshub.presentation.screens.signin.SignInScreenRoot
import com.yolo.myhabitshub.presentation.screens.signin.SignInViewModel
import com.yolo.myhabitshub.presentation.screens.tracking.HabitTrackingViewModel
import com.yolo.myhabitshub.root.LocalNavigator
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
    ) = LaunchedEffect(Unit) { onToolbarSetTitle(screenTitle) }

    CompositionLocalProvider(LocalNavigator provides navController) {
        NavHost(
            navController = navController,
            startDestination = HabitTrackingScreenRoot()
        ) {
            composable<HabitTrackingScreenRoot> { navBackStackEntry ->
                SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_tracking))
                navBackStackEntry.toRoute<HabitTrackingScreenRoot>().ScreenEntryPoint(
                    viewModel = koinViewModel<HabitTrackingViewModel>()
                )
            }
            composable<SettingsScreenRoot> { navBackStackEntry ->
                SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_profile))
                navBackStackEntry.toRoute<SettingsScreenRoot>().ScreenEntryPoint(
                    viewModel = koinViewModel<SettingsViewModel>()
                )
            }
            composable<SignInScreenRoot> { navBackStackEntry ->
                SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_sign_in))
                navBackStackEntry.toRoute<SignInScreenRoot>().ScreenEntryPoint(
                    viewModel = koinViewModel<SignInViewModel>()
                )
            }
            composable<AccountScreenRoot> { navBackStackEntry ->
                SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_account))
                navBackStackEntry.toRoute<AccountScreenRoot>().ScreenEntryPoint(
                    viewModel = koinViewModel<AccountViewModel>()
                )
            }
            composable<HabitProgressScreenRoot> { navBackStackEntry ->
                SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_progress))
                navBackStackEntry.toRoute<HabitProgressScreenRoot>().ScreenEntryPoint(
                    viewModel = koinViewModel<HabitProgressViewModel>()
                )
            }
            composable<HelpAndSupportScreenRoot> { navBackStackEntry ->
                SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_help_and_support))
                navBackStackEntry.toRoute<HelpAndSupportScreenRoot>().ScreenEntryPoint(
                    viewModel = koinViewModel<HelpAndSupportViewModel>()
                )
            }
        }
    }
}

