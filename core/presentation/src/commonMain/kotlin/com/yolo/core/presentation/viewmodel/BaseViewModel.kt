package com.yolo.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.yolo.core.presentation.viewmodel.BaseViewModel.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Base for MVI ViewModels: transforms user actions [INTENT]s into UI data [STATE] and one-time events [EVENT]s managed as part of the state.
 *
 * - UI sends [INTENT]s to `handleIntent()`
 * - Implement `onViewIntent()` to process [INTENT]s and call `updateState()` to modify state including [EVENT]s
 * - UI collects `state` (StateFlow) for screen data.
 * - UI collects `eventsFlow` (derived from state) for one time events and calls `consumeEvent()` after handling.
 */
abstract class BaseViewModel<INTENT : ViewIntent, STATE : ViewState<EVENT>, EVENT : ViewEvent>(
    initialState: STATE
) : ViewModel(){

    /** Marker interface for actions sent from the UI to the ViewModel. */
    interface ViewIntent

    /** Implement to process [INTENT]s from the UI. Called by `handleIntent`. */
    protected abstract fun onViewIntent(intent: INTENT)

    /** Call to send an [INTENT] from the UI to this ViewModel. */
    fun handleIntent(viewIntent: INTENT) {
        onViewIntent(viewIntent)
    }

    /**
     * Represents the UI state. One-time [ViewEvent]s are also part of this state,
     * making them part of the state until consumed via [consumeEvent]
     * This aligns with the philosophy that ViewModel events should result in a UI state update.
     *
     * More on one-time events:
     * Problem  - https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95
     * Solution - https://proandroiddev.com/android-one-off-events-approaches-evolution-anti-patterns-add887cd0250
     */
    interface ViewState<EVENT : ViewEvent>{
        /** Current one-time event null if none. */
        val viewEvent: EVENT?

        /**
         * **Implement:** Returns a **copy** of this state with [viewEvent]
         * Typically implemented in data classes using `copy(viewEvents = null)
         * This marks the current event as consumed by the UI.
         * Called by `BaseViewModel.consumeEvent()`.
         */
        fun consumeEvent(): ViewState<EVENT>
    }

    private var _state = MutableStateFlow(initialState)

    /** Emits the current UI [STATE]. Collected by the UI to observe state changes. */
    val state: StateFlow<STATE>
        get() = _state

    /** Updates the current [STATE], call this from `onViewIntent` to modify the state.*/
    protected fun updateState(reduce: STATE.() -> STATE) {
        _state.value = _state.value.reduce()
    }

    /** A marker interface for one-time UI events (e.g., navigation, toast).*/
    interface ViewEvent

    /**
     * Emits distinct one-time [EVENT]s derived from the main [state].
     * UI collects this to handle events and must call `consumeEvent()` afterwards.
     */
    val eventsFlow: Flow<EVENT?> = state.map { it.viewEvent }.distinctUntilChanged()

    /** Consume the current [ViewEvent] in the state. */
    fun consumeEvent() {
        if (_state.value.viewEvent != null) {
            @Suppress("UNCHECKED_CAST")
            _state.value = _state.value.consumeEvent() as STATE
        }
    }
}