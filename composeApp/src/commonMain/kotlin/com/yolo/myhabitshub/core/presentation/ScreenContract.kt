package com.yolo.myhabitshub.core.presentation

import androidx.compose.runtime.Composable
import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.*

/**
 * Defines a screen's UI and its logic for handling one-time side [EVENT]s.
 * Implemented for each screen and used by `ScreenRoute` to link UI with ViewModel.
*/
interface ScreenContract<STATE : ViewState<EVENT>, EVENT : ViewEvent> {
    /**
     * Composable UI representation of the screen for the given [viewState].
     * User interactions here typically send Intents to the ViewModel.
     *
     * @param viewState The current UI state to render.
     */
    @Composable
    fun Screen(viewState: STATE)

    /**
     * Processes one-time [ViewEvent]s emitted by the ViewModel.
     *
     * @param event The [EVENT] to handle.
     */
    fun handleEvent(event: EVENT)
}