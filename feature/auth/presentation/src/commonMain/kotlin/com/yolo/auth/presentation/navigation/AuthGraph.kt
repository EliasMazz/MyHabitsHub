package com.yolo.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.yolo.auth.presentation.email_verification.EmailVerificationScreen
import com.yolo.auth.presentation.forgot_password.ForgotPasswordScreen
import com.yolo.auth.presentation.login.LoginScreen
import com.yolo.auth.presentation.register.RegisterScreen
import com.yolo.auth.presentation.register_success.RegisterSuccessScreen
import com.yolo.auth.presentation.reset_password.ResetPasswordScreen
import com.yolo.auth.presentation.welcome.WelcomeScreen

// Navigation rules per auth-screens-improvement-spec §3 (N1-N7): routes carry the email so no
// user ever retypes an address the app already knows; auth forms always start fresh, seeded
// only by explicit route args (no save/restore of stale form state).
fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    navigation<AuthGraphRoutes.Graph>(
        startDestination = AuthGraphRoutes.Welcome
    ) {
        composable<AuthGraphRoutes.Welcome> {
            WelcomeScreen(
                navigateToRegisterEvent = {
                    navController.navigate(AuthGraphRoutes.Register()) {
                        launchSingleTop = true
                    }
                },
                navigateToLoginEvent = {
                    navController.navigate(AuthGraphRoutes.Login()) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<AuthGraphRoutes.Login> {
            LoginScreen(
                loginSuccessEvent = {
                    onLoginSuccess()
                },
                navigateToForgotPasswordEvent = { email ->
                    navController.navigate(AuthGraphRoutes.ForgotPassword(email = email))
                },
                navigateToRegisterEvent = { email ->
                    navController.navigate(AuthGraphRoutes.Register(email = email)) {
                        launchSingleTop = true
                    }
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<AuthGraphRoutes.Register> {
            RegisterScreen(
                navigateToRegisterSuccessEvent = { email ->
                    navController.navigate(AuthGraphRoutes.RegisterSuccess(email)) {
                        // N5: back from the interstitial must never land on the stale
                        // filled form (resubmit would hit error_account_exists).
                        popUpTo<AuthGraphRoutes.Register> {
                            inclusive = true
                        }
                    }
                },
                navigateToLoginEvent = { email ->
                    navController.navigate(AuthGraphRoutes.Login(email = email)) {
                        popUpTo<AuthGraphRoutes.Register> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<AuthGraphRoutes.ForgotPassword> {
            ForgotPasswordScreen(
                onBack = {
                    navController.navigateUp()
                },
                navigateToLoginEvent = { email ->
                    navController.navigate(AuthGraphRoutes.Login(email = email)) {
                        popUpTo<AuthGraphRoutes.ForgotPassword> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<AuthGraphRoutes.ResetPassword>(
            deepLinks = listOf(
                navDeepLink<AuthGraphRoutes.ResetPassword>(
                    basePath = "https://myhabitshub.com/api/auth/reset-password"
                ),
                navDeepLink<AuthGraphRoutes.ResetPassword>(
                    basePath = "myhabitshub://myhabitshub.com/api/auth/reset-password"
                ),
            )
        ) {
            ResetPasswordScreen(
                navigateToLoginEvent = {
                    navController.navigate(AuthGraphRoutes.Login()) {
                        popUpTo<AuthGraphRoutes.ResetPassword> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateToForgotPasswordEvent = {
                    navController.navigate(AuthGraphRoutes.ForgotPassword()) {
                        popUpTo<AuthGraphRoutes.ResetPassword> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<AuthGraphRoutes.RegisterSuccess> {
            RegisterSuccessScreen(
                navigateToLoginEvent = { email ->
                    navController.navigate(AuthGraphRoutes.Login(email = email)) {
                        popUpTo<AuthGraphRoutes.RegisterSuccess> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateToRegisterEvent = { email ->
                    navController.navigate(AuthGraphRoutes.Register(email = email)) {
                        popUpTo<AuthGraphRoutes.RegisterSuccess> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<AuthGraphRoutes.EmailVerification>(
            deepLinks = listOf(
                navDeepLink<AuthGraphRoutes.EmailVerification>(
                    basePath = "https://myhabitshub.com/api/auth/verify"
                ),
                navDeepLink<AuthGraphRoutes.EmailVerification>(
                    basePath = "myhabitshub://myhabitshub.com/api/auth/verify"
                ),
            )
        ) {
            EmailVerificationScreen(
                navigateToLoginEvent = {
                    navController.navigate(AuthGraphRoutes.Login()) {
                        popUpTo<AuthGraphRoutes.EmailVerification> {
                            inclusive = true
                        }
                    }
                },
                navigateBackEvent = {
                    navController.popBackStack()
                },
            )
        }
    }
}
