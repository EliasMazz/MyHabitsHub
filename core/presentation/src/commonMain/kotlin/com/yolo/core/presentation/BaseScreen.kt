package com.yolo.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yolo.core.presentation.viewmodel.BaseViewModel
import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent
import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewIntent
import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState

/**
 * A functional helper for MVI screens that enforces lifecycle-safe,
 * sequential handling of one-time events.
 *
 * @param viewModel The ViewModel for this screen.
 * @param handleEvent Callback to handle one-time side [EVENT]s.
 * @param content The UI content of the screen, receiving the current [STATE] and an intent dispatcher.
 */
@Composable
fun <INTENT : ViewIntent, STATE : ViewState, EVENT : ViewEvent> BaseScreen(
    viewModel: BaseViewModel<INTENT, STATE, EVENT>,
    handleEvent: suspend (EVENT) -> Unit = {},
    content: @Composable (STATE, (INTENT) -> Unit) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(
        flow = viewModel.events,
        key1 = viewModel
    ) { event ->
        handleEvent(event)
    }

    content(state, viewModel::handleIntent)
}
