package com.yolo.core.domain.logging

interface Logger {
    fun initialize(isDebug: Boolean = true)
    fun e(message: String, throwable: Throwable? = null, tag: String? = null)
    fun d(message: String, throwable: Throwable? = null, tag: String? = null)
    fun i(message: String, throwable: Throwable? = null, tag: String? = null)
}
