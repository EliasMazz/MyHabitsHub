package com.yolo.myhabitshub.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yolo.auth.presentation.navigation.AuthGraphRoutes
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.presentation.BaseScreen
import com.yolo.core.presentation.util.UiText
import com.yolo.myhabitshub.navigation.AppNavigationRoot
import com.yolo.myhabitshub.navigation.DeepLinkListener
import com.yolo.myhabitshub.navigation.routes.AppRoutes
import com.yolo.onboarding.presentation.navigation.OnBoardingRoutes
import com.yolo.core.catalog.DesignSystemCatalogScreen
import com.yolo.myhabitshub.util.extensions.ObserveFlowAsEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppScreen(
    onAuthenticationChecked: () -> Unit = {},
    viewModel: AppViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    DeepLinkListener(navController)

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isCheckingAuth) {
        if (!state.isCheckingAuth) {
            onAuthenticationChecked()
        }
    }

    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                AppViewEvent.SessionExpired -> {
                    navController.navigate(AuthGraphRoutes.Graph) {
                        popUpTo(AuthGraphRoutes.Graph) {
                            inclusive = false
                        }
                    }
                }
            }
        }
    ) { _, _ -> }

    if (!state.isCheckingAuth) {
        YoloTheme {
            if (state.isLoggedIn) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Debug-only: flip to true to browse the Design System Catalog
                    // (core/catalog module — tokens, components, section worlds, both modes).
                    // Compile-time false in commits => R8 strips the catalog from release builds.
                    val showDesignSystemCatalog = false
                    if (showDesignSystemCatalog) DesignSystemCatalogScreen()
                    else
                        AppScaffold(navController)
                }
            } else {
                AppNavigationRoot(
                    navController = navController,
                    startDestination = AuthGraphRoutes.Graph,
                    onLoginSuccess = {
                        viewModel.handleIntent(AppViewIntent.OnLoginSuccess)
                    }
                )
            }
        }
    }


}

@Composable
private fun AppScaffold(navController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }
    var uiText by remember { mutableStateOf<UiText?>(null) }

    ObserveFlowAsEvent(AppGlobalUiState.uiMessageFlow) { uiText = it }

    uiText?.value?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
            uiText = null
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        AppNavigationRoot(
            navController = navController,
            startDestination = OnBoardingRoutes.Graph
        )
    }
}

object AppGlobalUiState {
    private val uiTextChannel = Channel<UiText>(Channel.BUFFERED)
    val uiMessageFlow = uiTextChannel.receiveAsFlow()

    fun showUiMessage(uiText: UiText) {
        uiTextChannel.trySend(uiText)
    }
}