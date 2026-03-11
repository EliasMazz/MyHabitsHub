package com.yolo.myhabitshub.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.myhabitshub.presentation.components.AppToolbar
import com.yolo.myhabitshub.presentation.BottomNavItem
import com.yolo.myhabitshub.presentation.components.bottomnav.BottomNavigationBar
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreen(
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


