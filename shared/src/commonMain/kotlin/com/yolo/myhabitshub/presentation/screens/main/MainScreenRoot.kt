package com.yolo.myhabitshub.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot
import kotlinx.serialization.Serializable

@Serializable
class MainScreenRoot :
    ScreenRoot<MainViewModel, MainViewIntent, MainScreenViewState, MainViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: MainViewModel): ScreenContract<MainScreenViewState, MainViewEvent> {
        val navController = rememberNavController()
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
                                onToolbarSetTitle = { viewModel.handleIntent(MainViewIntent.OnToolbarSetTitle(label = it)) }
                            )
                        }
                    )
            }

            override fun handleEvent(event: MainViewEvent, navigator: NavHostController) {
                // We use 'navController' because it's the one that controls MainNavHost.
                // The passed-in 'navigator' is an exception and is ignored here, as it belongs to a parent graph
                when (event) {
                    is MainViewEvent.NavigateTo -> {
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

                    MainViewEvent.NavigateUp -> {
                        navController.navigateUp()
                    }
                }
            }
        }
    }
}

