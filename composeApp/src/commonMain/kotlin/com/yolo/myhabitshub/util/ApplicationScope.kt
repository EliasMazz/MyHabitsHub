package com.yolo.myhabitshub.util

import com.yolo.myhabitshub.util.logging.AppLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class ApplicationScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = CoroutineScope(SupervisorJob() + Dispatchers.IO + coroutineExceptionHandler).coroutineContext
}

private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    AppLogger.e("Unknown exception in application scope", throwable)
}