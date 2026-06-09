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

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    navigation<AuthGraphRoutes.Graph>(
        startDestination = AuthGraphRoutes.Login
    ) {
        composable<AuthGraphRoutes.Login> {
            LoginScreen(
                loginSuccessEvent = {
                    onLoginSuccess()
                },
                navigateToForgotPasswordEvent = {
                    navController.navigate(AuthGraphRoutes.ForgotPassword)
                },
                navigateToRegisterEvent = {
                    navController.navigate(AuthGraphRoutes.Register) {
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<AuthGraphRoutes.Register> {
            RegisterScreen(
                navigateToRegisterSuccessEvent = { email ->
                    navController.navigate(AuthGraphRoutes.RegisterSuccess(email))
                },
                navigateToLoginEvent = {
                    navController.navigate(AuthGraphRoutes.Login) {
                        popUpTo<AuthGraphRoutes.Register> {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable<AuthGraphRoutes.ForgotPassword> {
            ForgotPasswordScreen()
        }

        composable<AuthGraphRoutes.RegisterSuccess> {
            RegisterSuccessScreen(
                navigateToLoginEvent = {
                    navController.navigate(AuthGraphRoutes.Login) {
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
                    navController.navigate(AuthGraphRoutes.Login) {
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
