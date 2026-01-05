package com.yolo.core.data.logging

import com.yolo.core.domain.logging.Logger
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object AppLogger : Logger {

    override fun initialize(isDebug: Boolean) {
        if (isDebug) Napier.base(DebugAntilog())
    }

    override fun e(message: String, throwable: Throwable?, tag: String?) {
        Napier.e(message, throwable, tag)
        println("E/$tag: $message" + (throwable?.let { "\n${it.stackTraceToString()}" } ?: ""))
    }

    override fun d(message: String, throwable: Throwable?, tag: String?) {
        Napier.d(message, throwable, tag)
    }

    override fun i(message: String, throwable: Throwable?, tag: String?) {
        Napier.i(message, throwable, tag)
    }
}