package com.yolo.account.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yolo.account.presentation.account.AccountScreen
import com.yolo.account.presentation.helpandsupport.HelpAndSupportScreen
import com.yolo.account.presentation.settings.SettingsScreen
import com.yolo.account.presentation.signin.SignInScreen
import myhabitshub.feature.account.presentation.generated.resources.Res
import myhabitshub.feature.account.presentation.generated.resources.title_screen_account
import myhabitshub.feature.account.presentation.generated.resources.title_screen_help_and_support
import myhabitshub.feature.account.presentation.generated.resources.title_screen_profile
import myhabitshub.feature.account.presentation.generated.resources.title_screen_sign_in
import org.jetbrains.compose.resources.stringResource

fun NavGraphBuilder.accountGraph(
    navController: NavController,
    onToolbarSetTitle: (String?) -> Unit,
    onSignInSuccess: () -> Unit,
) {
    composable<AccountGraphRoutes.Account> {
        SetupScreenTitle(onToolbarSetTitle, stringResource(Res.string.title_screen_account))
        AccountScreen(
            onNavigateToSignIn = { navController.navigate(AccountGraphRoutes.SignIn) },
            onNavigateToSettings = { navController.navigate(AccountGraphRoutes.Settings) },
            onNavigateToHelpAndSupport = { navController.navigate(AccountGraphRoutes.HelpAndSupport) },
        )
    }

    composable<AccountGraphRoutes.Settings> {
        SetupScreenTitle(onToolbarSetTitle, stringResource(Res.string.title_screen_profile))
        SettingsScreen(
            onNavigateToSignIn = { navController.navigate(AccountGraphRoutes.SignIn) },
        )
    }

    composable<AccountGraphRoutes.SignIn> {
        SetupScreenTitle(onToolbarSetTitle, stringResource(Res.string.title_screen_sign_in))
        SignInScreen(
            onSignInSuccess = onSignInSuccess,
        )
    }

    composable<AccountGraphRoutes.HelpAndSupport> {
        SetupScreenTitle(onToolbarSetTitle, stringResource(Res.string.title_screen_help_and_support))
        HelpAndSupportScreen()
    }
}

@Composable
private fun SetupScreenTitle(onToolbarSetTitle: (String?) -> Unit, screenTitle: String) =
    LaunchedEffect(Unit) { onToolbarSetTitle(screenTitle) }
