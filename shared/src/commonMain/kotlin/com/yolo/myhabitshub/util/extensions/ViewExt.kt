package com.yolo.myhabitshub.util.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext



/**
 * val uiMessageChannel = Channel<UiMessage>()
 * val uiMessageFlow = uiMessageChannel.receiveAsFlow()
 */
@Composable
fun <T> ObserveFlowAsEvent(flow: Flow<T>, onEvent: (T) -> Unit) {
    val lifecycle = LocalLifecycleOwner.current
    val updatedOnEvent by rememberUpdatedState(onEvent)
    LaunchedEffect(flow, lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(updatedOnEvent)
            }
        }
    }
}