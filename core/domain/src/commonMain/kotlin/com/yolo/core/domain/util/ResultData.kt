package com.yolo.core.domain.util


sealed interface ResultData<out D, out E: Error> {
    data class Success<out D>(val data: D): ResultData<D, Nothing>
    data class Failure<out E: Error>(val error: E): ResultData<Nothing, E>
}

inline fun <T, E: Error, R> ResultData<T, E>.map(map: (T) -> R): ResultData<R, E> {
    return when(this) {
        is ResultData.Failure -> ResultData.Failure(error)
        is ResultData.Success -> ResultData.Success(map(this.data))
    }
}

inline fun <T, E: Error> ResultData<T, E>.onSuccess(action: (T) -> Unit): ResultData<T, E> {
    return when(this) {
        is ResultData.Failure -> this
        is ResultData.Success -> {
            action(this.data)
            this
        }
    }
}

inline fun <T, E: Error> ResultData<T, E>.onFailure(action: (E) -> Unit): ResultData<T, E> {
    return when(this) {
        is ResultData.Failure -> {
            action(error)
            this
        }
        is ResultData.Success -> this
    }
}

fun <T, E: Error> ResultData<T, E>.asEmptyResult(): EmptyResult<E> {
    return map {  }
}

typealias EmptyResult<E> = ResultData<Unit, E>