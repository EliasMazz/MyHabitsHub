package com.yolo.myhabitshub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.title_screen_account
import com.yolo.myhabitshub.generated.resources.title_screen_help_and_support
import com.yolo.myhabitshub.generated.resources.title_screen_profile
import com.yolo.myhabitshub.generated.resources.title_screen_progress
import com.yolo.myhabitshub.generated.resources.title_screen_sign_in
import com.yolo.myhabitshub.generated.resources.title_screen_tracking
import com.yolo.myhabitshub.navigation.routes.MainGraphRoutes
import com.yolo.myhabitshub.presentation.screens.account.AccountScreenRoot
import com.yolo.myhabitshub.presentation.screens.account.AccountViewModel
import com.yolo.myhabitshub.presentation.screens.helpandsupport.HelpAndSupportScreenRoot
import com.yolo.myhabitshub.presentation.screens.helpandsupport.HelpAndSupportViewModel
import com.yolo.myhabitshub.presentation.screens.progress.HabitProgressScreenRoot
import com.yolo.myhabitshub.presentation.screens.progress.HabitProgressViewModel
import com.yolo.myhabitshub.presentation.screens.settings.SettingsScreenRoot
import com.yolo.myhabitshub.presentation.screens.settings.SettingsViewModel
import com.yolo.myhabitshub.presentation.screens.signin.SignInScreenRoot
import com.yolo.myhabitshub.presentation.screens.signin.SignInViewModel
import com.yolo.myhabitshub.presentation.screens.tracking.HabitTrackingScreenRoot
import com.yolo.myhabitshub.presentation.screens.tracking.HabitTrackingViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    onToolbarSetTitle: (String?) -> Unit,
    onSignInSuccess: () -> Unit,
) {
    @Composable
    fun SetupScreenTitle(screenTitle: String) =
        LaunchedEffect(Unit) { onToolbarSetTitle(screenTitle) }

    navigation<MainGraphRoutes.Graph>(
        startDestination = MainGraphRoutes.HabitTracking
    ) {
        composable<MainGraphRoutes.HabitTracking> {
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_tracking))
            HabitTrackingScreenRoot().ScreenEntryPoint(
                viewModel = koinViewModel<HabitTrackingViewModel>()
            )
        }

        composable<MainGraphRoutes.HabitProgress> {
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_progress))
            HabitProgressScreenRoot().ScreenEntryPoint(
                viewModel = koinViewModel<HabitProgressViewModel>()
            )
        }

        composable<MainGraphRoutes.Account> {
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_account))
            AccountScreenRoot(
                onNavigateToSignIn = { navController.navigate(MainGraphRoutes.SignIn) },
                onNavigateToSettings = { navController.navigate(MainGraphRoutes.Settings) },
                onNavigateToHelpAndSupport = { navController.navigate(MainGraphRoutes.HelpAndSupport) },
            ).ScreenEntryPoint(
                viewModel = koinViewModel<AccountViewModel>()
            )
        }

        composable<MainGraphRoutes.Settings> {
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_profile))
            SettingsScreenRoot(
                onNavigateToSignIn = { navController.navigate(MainGraphRoutes.SignIn) },
            ).ScreenEntryPoint(
                viewModel = koinViewModel<SettingsViewModel>()
            )
        }

        composable<MainGraphRoutes.SignIn> {
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_sign_in))
            SignInScreenRoot(
                onSignInSuccess = onSignInSuccess,
            ).ScreenEntryPoint(
                viewModel = koinViewModel<SignInViewModel>()
            )
        }

        composable<MainGraphRoutes.HelpAndSupport> {
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_help_and_support))
            HelpAndSupportScreenRoot().ScreenEntryPoint(
                viewModel = koinViewModel<HelpAndSupportViewModel>()
            )
        }
    }
}
