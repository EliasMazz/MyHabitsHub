package com.yolo.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.yolo.core.presentation.viewmodel.BaseViewModel.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

/**
 * Base for MVI ViewModels: transforms user actions [INTENT]s into UI data [STATE] and one-time events [EVENT]s.
 *
 * - UI sends [INTENT]s to `handleIntent()`
 * - Implement `onViewIntent()` to process [INTENT]s and call `updateState()` to modify state
 * - Call `sendEvent()` to emit a one-time [EVENT]
 * - UI collects `state` (StateFlow) for screen data.
 * - UI collects `events` (a [Channel]-backed [Flow]) for one-time events.
 */
abstract class BaseViewModel<INTENT : ViewIntent, STATE : ViewState, EVENT : ViewEvent>(
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

    /** Marker interface for the UI state. */
    interface ViewState

    private var _state = MutableStateFlow(initialState)

    /** Emits the current UI [STATE]. Collected by the UI to observe state changes. */
    val state: StateFlow<STATE>
        get() = _state

    /** Updates the current [STATE], call this from `onViewIntent` to modify the state.*/
    protected fun updateState(reduce: STATE.() -> STATE) {
        _state.update { it.reduce() }
    }

    /** A marker interface for one-time UI events (e.g., navigation, toast).*/
    interface ViewEvent

    /**
     * Backs [events]. A [Channel] is used (not StateFlow/SharedFlow) so events are delivered
     * exactly once and never replayed — no duplicate navigation/toast on recompose or config change.
     * UNLIMITED so `trySend` can never fail on capacity (its only failure mode is a closed channel).
     */
    private val eventChannel = Channel<EVENT>(Channel.UNLIMITED)

    /** One-time [EVENT]s as a hot, non-replayed, FIFO stream. Each event is collected exactly once. */
    val events: Flow<EVENT> = eventChannel.receiveAsFlow()

    /**
     * Send a one-time [EVENT] to the UI.
     * Uses non-suspending [Channel.trySend] (no coroutine needed); with UNLIMITED capacity it always
     * enqueues immediately, in caller order.
     */
    protected fun sendEvent(event: EVENT) {
        eventChannel.trySend(event)
    }
}
