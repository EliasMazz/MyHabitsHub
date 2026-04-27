package com.yolo.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.yolo.core.presentation.viewmodel.BaseViewModel
import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent
import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewIntent
import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState
import kotlinx.coroutines.launch

/**
 * A functional helper for MVI screens that enforces lifecycle safety,
 * parallel event execution, and atomic consumption.
 *
 * @param viewModel The ViewModel for this screen.
 * @param handleEvent Callback to handle one-time side [EVENT]s.
 * @param content The UI content of the screen, receiving the current [STATE] and an intent dispatcher.
 */
@Composable
fun <INTENT : ViewIntent, STATE : ViewState<EVENT>, EVENT : ViewEvent> MviScreen(
    viewModel: BaseViewModel<INTENT, STATE, EVENT>,
    handleEvent: suspend (EVENT) -> Unit = {},
    content: @Composable (STATE, (INTENT) -> Unit) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.eventsFlow, lifecycleOwner) {
        viewModel.eventsFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                if (event != null) {
                    viewModel.consumeEvent()
                    launch {
                        handleEvent(event)
                    }
                }
            }
    }

    content(state, viewModel::handleIntent)
}
