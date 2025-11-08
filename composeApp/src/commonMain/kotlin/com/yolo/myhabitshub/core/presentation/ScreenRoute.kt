package com.yolo.myhabitshub.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel
import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewEvent
import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewIntent
import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewState
import com.yolo.myhabitshub.root.LocalNavigator
import kotlinx.coroutines.flow.Flow

/**
 * Defines an MVI screen. Implement to connect your UI to your ViewModel.
 *
 * 1. Implement `provideScreenContract` to define:
 *  The UI layout `ScreenContent`.
 *  How the UI handles one-time [EVENT]s (e.g., navigation, toasts) via `handleEvent`.
 * 2. In your UI, send [INTENT]s (user actions) to the [VIEW_MODEL]'s handleIntent` method.
 * 3. Your [ScreenContract] will receive [STATE] to render UI.
 *
 * @param VIEW_MODEL The screen's ViewModel must be provided by the caller (e.g., navigation logic).
 * @param INTENT User actions type (sent from UI to ViewModel).
 * @param STATE UI state type (received by UI from ViewModel).
 * @param EVENT One-time side event type (received by UI from ViewModel).
 */
interface ScreenRoot<
        VIEW_MODEL : BaseViewModel<INTENT, STATE, EVENT>,
        INTENT : ViewIntent,
        STATE : ViewState<EVENT>,
        EVENT : ViewEvent> {


    /** Composable entry point for this MVI screen; call from navigation.*/
    @Composable
    fun ScreenEntryPoint(viewModel: VIEW_MODEL) = SetupBaseScreen(viewModel)

    /** Core MVI screen setup. */
    @Composable
    private fun SetupBaseScreen(viewModel: VIEW_MODEL) {
        val viewState = viewModel.state.collectAsStateWithLifecycle()
        val screenContract = provideScreenContract(viewModel = viewModel)
        val navigator = LocalNavigator.current

        HandleOneTimeUiEvent(
            viewModel.eventsFlow,
            onEvent = { event ->
                screenContract.handleEvent(event, navigator)
            },
            onConsumeEvent = { viewModel.consumeEvent() },
        )

        screenContract.ScreenView(viewState = viewState.value)
    }

    /**
     * Implement this: Define UI (`ScreenContent`) and event handling (`handleEvent`) for your screen.
     * `Screen` uses this to link your UI with the [viewModel] for [STATE] updates, [EVENT] processing, and sending [INTENT]s.
     */
    @Composable
    fun provideScreenContract(viewModel: VIEW_MODEL): ScreenContract<STATE, EVENT>

    /**
     * Handles one-time event from ViewModel, passing them to your screenContract
     * observes and processes ViewModel effects, then consume them.
     * Uses `rememberUpdatedState` to prevent issues with stale lambdas in `LaunchedEffect`.
     *
     * For more on handling one-off events, see:
     * https://proandroiddev.com/android-one-off-events-approaches-evolution-anti-patterns-add887cd0250
     */
    @Composable
    private fun <EVENT> HandleOneTimeUiEvent(
        eventFlow: Flow<EVENT?>,
        onEvent: suspend (EVENT) -> Unit,
        onConsumeEvent: () -> Unit,
    ) {
        val event by eventFlow.collectAsStateWithLifecycle(null)
        val currentOnEvent by rememberUpdatedState(onEvent)
        val currentOnConsumeEvent by rememberUpdatedState(onConsumeEvent)

        LaunchedEffect(event) {
            event?.let {
                currentOnEvent(it)
                currentOnConsumeEvent()
            }
        }
    }
}
