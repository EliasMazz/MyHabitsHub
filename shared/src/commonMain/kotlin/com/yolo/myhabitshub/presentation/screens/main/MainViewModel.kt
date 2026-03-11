package com.yolo.myhabitshub.presentation.screens.main

import com.yolo.core.presentation.viewmodel.BaseViewModel
import com.yolo.myhabitshub.presentation.BottomNavItem
import myhabitshub.core.designsystem.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_back

class MainViewModel :
    BaseViewModel<MainViewIntent, MainScreenViewState, MainViewEvent>(
        initialState = MainScreenViewState(bottomNavViewState = BottomNavViewState(items = BottomNavItem.items()))
    ) {

    override fun onViewIntent(intent: MainViewIntent) {
        when (intent) {
            is MainViewIntent.OnNavigationDestinationChanged -> handleNavigationDestinationChanged(intent.route)
            is MainViewIntent.OnBottomNavItemClicked -> handleBottomNavItemClick(intent.bottomNavItem)
            is MainViewIntent.OnToolbarSetTitle -> handleToolbarSetTitle(intent.label)
            MainViewIntent.OnToolbarNavItemClicked -> handleToolbarNavItemClick()
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
        val bottomNavScreenRootPatterns =
            state.value.bottomNavViewState.items.map { it.destination }
        val isBottomNavVisible = bottomNavScreenRootPatterns.any {
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
        updateState { copy(viewEvent = MainViewEvent.NavigateUp) }
    }

    private fun handleBottomNavItemClick(item: BottomNavItem) {
        if (state.value.bottomNavViewState.isVisible) {
            updateState {
                copy(
                    viewEvent = MainViewEvent.NavigateTo(
                        screenRoot = item.screenRoot,
                        popUpToStartDestination = true,
                        launchSingleTop = true
                    )
                )
            }
        }
    }
}
