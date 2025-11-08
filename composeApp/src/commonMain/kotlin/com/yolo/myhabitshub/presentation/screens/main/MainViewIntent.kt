package com.yolo.myhabitshub.presentation.screens.main

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewIntent
import com.yolo.myhabitshub.presentation.components.bottomnav.BottomNavItem

sealed interface MainViewIntent : ViewIntent {
    data class OnBottomNavItemClicked(
        val bottomNavItem: BottomNavItem,
    ) : MainViewIntent
    data class OnNavigationDestinationChanged(val route: String) : MainViewIntent
    data class OnToolbarSetTitle(val label: String?) : MainViewIntent
    data object OnToolbarNavItemClicked : MainViewIntent
}