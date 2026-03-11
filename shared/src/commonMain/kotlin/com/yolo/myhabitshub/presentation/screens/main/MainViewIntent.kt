package com.yolo.myhabitshub.presentation.screens.main

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewIntent
import com.yolo.myhabitshub.presentation.BottomNavItem

sealed interface MainViewIntent : ViewIntent {
    data class OnBottomNavItemClicked(
        val bottomNavItem: BottomNavItem,
    ) : MainViewIntent
    data class OnNavigationDestinationChanged(val route: String) : MainViewIntent
    data class OnToolbarSetTitle(val label: String?) : MainViewIntent
    data object OnToolbarNavItemClicked : MainViewIntent
}