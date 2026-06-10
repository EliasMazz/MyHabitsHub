package com.yolo.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Lifecycle-aware collector for one-time event streams.
 *
 * Collects [flow] only while the lifecycle is at least [Lifecycle.State.STARTED],
 * cancelling on STOP and restarting on START. Uses [Dispatchers.Main.immediate]
 * so an event emitted on the main thread is handled without an extra dispatch,
 * minimizing the chance of a missed emission around lifecycle transitions.
 *
 * @param flow The event stream to observe.
 * @param key1 Optional key to restart collection (mirrors LaunchedEffect keys).
 * @param key2 Optional key to restart collection.
 * @param onEvent Suspending, sequential handler invoked per emitted event.
 */
@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: suspend (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner, key1, key2) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}
