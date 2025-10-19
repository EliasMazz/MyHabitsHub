package com.yolo.myhabitshub.presentation.screens.main

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewIntent
import com.yolo.myhabitshub.presentation.components.bottomnav.BottomNavItem

sealed interface MainScreenViewIntent : ViewIntent {
    data class OnBottomNavItemClicked(
        val bottomNavItem: BottomNavItem,
    ) : MainScreenViewIntent
    data class OnNavigationDestinationChanged(val route: String) : MainScreenViewIntent
    data class OnToolbarSetTitle(val label: String?) : MainScreenViewIntent
    data object OnToolbarNavItemClicked : MainScreenViewIntent
}