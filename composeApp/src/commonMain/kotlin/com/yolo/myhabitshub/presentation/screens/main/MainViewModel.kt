package com.yolo.myhabitshub.presentation.screens.main

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.ic_back
import com.yolo.myhabitshub.presentation.components.bottomnav.BottomNavItem

class MainViewModel :
    BaseViewModel<MainScreenViewIntent, MainScreenViewState, MainScreenViewEvent>(
        initialState = MainScreenViewState(bottomNavViewState = BottomNavViewState(items = BottomNavItem.items()))
    ) {

    override fun onViewIntent(intent: MainScreenViewIntent) {
        when (intent) {
            is MainScreenViewIntent.OnNavigationDestinationChanged -> handleNavigationDestinationChanged(intent.route)
            is MainScreenViewIntent.OnBottomNavItemClicked -> handleBottomNavItemClick(intent.bottomNavItem)
            is MainScreenViewIntent.OnToolbarSetTitle -> handleToolbarSetTitle(intent.label)
            MainScreenViewIntent.OnToolbarNavItemClicked -> handleToolbarNavItemClick()
        }
    }

    private fun handleToolbarSetTitle(label: String?) {
        val isToolbarVisible = !label.isNullOrBlank()
        updateState {
            copy(
                bottomNavViewState = bottomNavViewState,
                toolbarUiState = toolbarUiState.copy(
                    isVisible = isToolbarVisible,
                    text = label,
                )
            )
        }
    }

    private fun handleNavigationDestinationChanged(currentRoute: String) {
        val isBottomNavVisible = checkBottomNavVisibility(currentRoute)
        val selectedIndex = getSelectedNavIndex(state.value, currentRoute)
        updateState {
            copy(
                bottomNavViewState = bottomNavViewState.copy(
                    isVisible = isBottomNavVisible,
                    selectedBottomNavIndex = selectedIndex
                ),
                toolbarUiState = toolbarUiState.copy(
                    navigationIconRes = if (!isBottomNavVisible && toolbarUiState.isVisible) Res.drawable.ic_back else null
                )
            )
        }
    }

    private fun checkBottomNavVisibility(currentRoute: String): Boolean {
        val bottomNavScreenRoutePatterns =
            state.value.bottomNavViewState.items.map { it.destination }
        val isBottomNavVisible = bottomNavScreenRoutePatterns.any {
            currentRoute.contains(it)
        }
        return isBottomNavVisible
    }

    private fun getSelectedNavIndex(
        currentState: MainScreenViewState,
        currentRoute: String
    ): Int = currentState.bottomNavViewState.items.indexOfFirst { item ->
        currentRoute.contains(item.destination, ignoreCase = true)
    }

    private fun handleToolbarNavItemClick() {
        updateState { copy(viewEvent = MainScreenViewEvent.NavigateUp) }
    }

    private fun handleBottomNavItemClick(item: BottomNavItem) {
        if (state.value.bottomNavViewState.isVisible) {
            updateState {
                copy(
                    viewEvent = MainScreenViewEvent.NavigateTo(
                        screenRoute = item.screenRoute,
                        popUpToStartDestination = true,
                        launchSingleTop = true
                    )
                )
            }
        }
    }
}
