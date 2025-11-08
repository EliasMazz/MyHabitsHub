package com.yolo.myhabitshub.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yolo.myhabitshub.core.presentation.ScreenContract
import com.yolo.myhabitshub.root.LocalNavigator
import com.yolo.myhabitshub.core.presentation.ScreenRoot
import kotlinx.serialization.Serializable

@Serializable
class MainScreenRoot :
    ScreenRoot<MainViewModel, MainScreenViewIntent, MainScreenViewState, MainScreenViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: MainViewModel): ScreenContract<MainScreenViewState, MainScreenViewEvent> {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        LaunchedEffect(navBackStackEntry) {
            currentRoute?.let {
                viewModel.handleIntent(
                    MainScreenViewIntent.OnNavigationDestinationChanged(
                        route = it,
                    )
                )
            }
        }

        return object : ScreenContract<MainScreenViewState, MainScreenViewEvent> {
            @Composable
            override fun ScreenView(viewState: MainScreenViewState) {
                CompositionLocalProvider(LocalNavigator provides navController) {
                    MainScreen(
                        viewState = viewState,
                        onToolbarNavItemClicked = { viewModel.handleIntent(MainScreenViewIntent.OnToolbarNavItemClicked) },
                        onBottomNavItemClicked = { viewModel.handleIntent(MainScreenViewIntent.OnBottomNavItemClicked(bottomNavItem = it)) },
                        mainContent = {
                            MainNavHost(
                                navController = navController,
                                onToolbarSetTitle = { viewModel.handleIntent(MainScreenViewIntent.OnToolbarSetTitle(label = it)) }
                            )
                        }
                    )
                }
            }

            override fun handleEvent(event: MainScreenViewEvent) {
                when (event) {
                    is MainScreenViewEvent.NavigateTo -> {
                        //TODO: Check, for some reason saveState, and restoreState doesn't work
                        navController.navigate(event.screenRoot) {
                            if (event.popUpToStartDestination) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = event.launchSingleTop
                                restoreState = true
                            }
                        }
                    }

                    MainScreenViewEvent.NavigateUp -> {
                        navController.navigateUp()
                    }
                }
            }
        }
    }
}

