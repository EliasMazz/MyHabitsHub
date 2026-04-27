package com.yolo.myhabitshub.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yolo.core.presentation.MviScreen
import com.yolo.myhabitshub.presentation.components.AppToolbar
import com.yolo.myhabitshub.presentation.BottomNavItem
import com.yolo.myhabitshub.presentation.components.bottomnav.BottomNavigationBar
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    navController: NavHostController,
    onSignInSuccess: () -> Unit = {},
    onNavigateTo: (MainViewEvent.NavigateTo) -> Unit = {},
    onNavigateUp: () -> Unit = {},
) {
    MviScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                is MainViewEvent.NavigateTo -> onNavigateTo(event)
                MainViewEvent.NavigateUp -> onNavigateUp()
            }
        }
    ) { state, onIntent ->
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        LaunchedEffect(navBackStackEntry) {
            currentRoute?.let {
                onIntent(
                    MainViewIntent.OnNavigationDestinationChanged(
                        route = it,
                    )
                )
            }
        }

        MainScreenContent(
            modifier = Modifier.fillMaxSize(),
            viewState = state,
            onToolbarNavItemClicked = { onIntent(MainViewIntent.OnToolbarNavItemClicked) },
            onBottomNavItemClicked = { onIntent(MainViewIntent.OnBottomNavItemClicked(bottomNavItem = it)) },
            mainContent = {
                MainNavHost(
                    navController = navController,
                    onSignInSuccess = onSignInSuccess,
                    onToolbarSetTitle = { onIntent(MainViewIntent.OnToolbarSetTitle(label = it)) }
                )
            }
        )
    }
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    viewState: MainScreenViewState,
    onToolbarNavItemClicked:() -> Unit,
    onBottomNavItemClicked: (BottomNavItem) -> Unit,
    mainContent: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            //AppBar
            if (viewState.toolbarUiState.isVisible) {
                AppToolbar(
                    title = viewState.toolbarUiState.text,
                    navigationIcon = viewState.toolbarUiState.navigationIconRes?.let {
                        painterResource(it)
                    },
                    onNavigationIconClick = { onToolbarNavItemClicked() }
                )
            }

            //Main Content
            Box(modifier = Modifier.weight(1f)) {
                mainContent()
            }
            //BottomNavigation Bar
            if (viewState.bottomNavViewState.isVisible) {
                BottomNavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    items = BottomNavItem.items(),
                    selectedIndex = viewState.bottomNavViewState.selectedBottomNavIndex
                ) {
                    onBottomNavItemClicked(it)
                }
            }
        }
    }
}


