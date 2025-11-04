package com.yolo.myhabitshub.presentation.screens.main

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewState
import com.yolo.myhabitshub.presentation.components.bottomnav.BottomNavItem
import myhabitshub.core.designsystem.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_back
import org.jetbrains.compose.resources.DrawableResource


data class MainScreenViewState(
    val bottomNavViewState: BottomNavViewState = BottomNavViewState(),
    val toolbarUiState: ToolbarViewState = ToolbarViewState(),
    override val viewEvent: MainScreenViewEvent? = null
): ViewState<MainScreenViewEvent>{
    override fun consumeEvent(): ViewState<MainScreenViewEvent> {
        return copy(viewEvent = null)
    }
}

data class ToolbarViewState(
    val isVisible: Boolean = false,
    val navigationIconRes: DrawableResource? = Res.drawable.ic_back,
    val text: String? = null,
)

data class BottomNavViewState(
    val isVisible: Boolean = true,
    val selectedBottomNavIndex: Int = 0,
    val items: List<BottomNavItem> = emptyList()
)

