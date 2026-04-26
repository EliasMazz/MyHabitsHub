package com.yolo.myhabitshub.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot

class MainScreenRoot(
    private val navController: NavHostController,
    private val onSignInSuccess: () -> Unit = {},
    private val onNavigateTo: (MainViewEvent.NavigateTo) -> Unit = {},
    private val onNavigateUp: () -> Unit = {},
) : ScreenRoot<MainViewModel, MainViewIntent, MainScreenViewState, MainViewEvent> {
    @Composable
    override fun provideScreenContract(viewModel: MainViewModel): ScreenContract<MainScreenViewState, MainViewEvent> {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        LaunchedEffect(navBackStackEntry) {
            currentRoute?.let {
                viewModel.handleIntent(
                    MainViewIntent.OnNavigationDestinationChanged(
                        route = it,
                    )
                )
            }
        }
        return object : ScreenContract<MainScreenViewState, MainViewEvent> {
            @Composable
            override fun Screen(viewState: MainScreenViewState) {
                    MainScreen(
                        viewState = viewState,
                        onToolbarNavItemClicked = { viewModel.handleIntent(MainViewIntent.OnToolbarNavItemClicked) },
                        onBottomNavItemClicked = { viewModel.handleIntent(MainViewIntent.OnBottomNavItemClicked(bottomNavItem = it)) },
                        mainContent = {
                            MainNavHost(
                                navController = navController,
                                onSignInSuccess = onSignInSuccess,
                                onToolbarSetTitle = { viewModel.handleIntent(MainViewIntent.OnToolbarSetTitle(label = it)) }
                            )
                        }
                    )
            }
            override suspend fun handleEvent(event: MainViewEvent) {
                when (event) {
                    is MainViewEvent.NavigateTo -> onNavigateTo(event)
                    MainViewEvent.NavigateUp -> onNavigateUp()
                }
            }
        }
    }
}
