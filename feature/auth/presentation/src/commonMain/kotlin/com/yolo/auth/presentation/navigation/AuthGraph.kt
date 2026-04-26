package com.yolo.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yolo.auth.presentation.register.RegisterScreenRoot
import com.yolo.auth.presentation.register.RegisterViewModel
import com.yolo.auth.presentation.register_success.RegisterSuccessScreenRoot
import com.yolo.auth.presentation.register_success.RegisterSuccessViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    navigation<AuthGraphRoutes.Graph>(
        startDestination = AuthGraphRoutes.Register
    ) {
        composable<AuthGraphRoutes.Register> {
            RegisterScreenRoot(
                onRegisterSuccess = { email ->
                    navController.navigate(AuthGraphRoutes.RegisterSuccess(email))
                }
            ).ScreenEntryPoint(
                viewModel = koinViewModel<RegisterViewModel>()
            )
        }

        composable<AuthGraphRoutes.RegisterSuccess> {
            RegisterSuccessScreenRoot(
                onLoginClick = {
                    onLoginSuccess()
                }
            ).ScreenEntryPoint(
                viewModel = koinViewModel<RegisterSuccessViewModel>()
            )
        }
    }
}
