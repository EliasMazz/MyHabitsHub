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
import com.yolo.myhabitshub.presentation.screens.account.AccountScreen
import com.yolo.myhabitshub.presentation.screens.helpandsupport.HelpAndSupportScreen
import com.yolo.myhabitshub.presentation.screens.progress.HabitProgressScreen
import com.yolo.myhabitshub.presentation.screens.settings.SettingsScreen
import com.yolo.myhabitshub.presentation.screens.signin.SignInScreen
import com.yolo.myhabitshub.presentation.screens.tracking.HabitTrackingScreen
import org.jetbrains.compose.resources.stringResource

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
            HabitTrackingScreen()
        }

        composable<MainGraphRoutes.HabitProgress> {
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_progress))
            HabitProgressScreen()
        }

        composable<MainGraphRoutes.Account> {
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_account))
            AccountScreen(
                onNavigateToSignIn = { navController.navigate(MainGraphRoutes.SignIn) },
                onNavigateToSettings = { navController.navigate(MainGraphRoutes.Settings) },
                onNavigateToHelpAndSupport = { navController.navigate(MainGraphRoutes.HelpAndSupport) },
            )
        }

        composable<MainGraphRoutes.Settings> {
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_profile))
            SettingsScreen(
                onNavigateToSignIn = { navController.navigate(MainGraphRoutes.SignIn) },
            )
        }

        composable<MainGraphRoutes.SignIn> {
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_sign_in))
            SignInScreen(
                onSignInSuccess = onSignInSuccess,
            )
        }

        composable<MainGraphRoutes.HelpAndSupport> {
            SetupScreenTitle(screenTitle = stringResource(Res.string.title_screen_help_and_support))
            HelpAndSupportScreen()
        }
    }
}
