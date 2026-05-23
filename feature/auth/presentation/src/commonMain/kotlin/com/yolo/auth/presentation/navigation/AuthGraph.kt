package com.yolo.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.yolo.auth.presentation.email_verification.EmailVerificationScreen
import com.yolo.auth.presentation.register.RegisterScreen
import com.yolo.auth.presentation.register_success.RegisterSuccessScreen

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    navigation<AuthGraphRoutes.Graph>(
        startDestination = AuthGraphRoutes.Register
    ) {
        composable<AuthGraphRoutes.Register> {
            RegisterScreen(
                onRegisterSuccess = { email ->
                    navController.navigate(AuthGraphRoutes.RegisterSuccess(email))
                }
            )
        }

        composable<AuthGraphRoutes.RegisterSuccess> {
            RegisterSuccessScreen(
                onLoginClick = {
                    onLoginSuccess()
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
            EmailVerificationScreen()
        }
        
    }
}
