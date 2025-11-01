package com.yolo.core.data.networking

import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.ResultData
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

expect suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> ResultData<T, DataError.Remote>
): ResultData<T, DataError.Remote>

suspend inline fun <reified T> safeCall(
    noinline execute: suspend () -> HttpResponse
): ResultData<T, DataError.Remote> {
    return platformSafeCall(
        execute = execute
    ) { response ->
        responseToResult(response)
    }
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): ResultData<T, DataError.Remote> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                ResultData.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                ResultData.Failure(DataError.Remote.SERIALIZATION)
            }
        }
        400 -> ResultData.Failure(DataError.Remote.BAD_REQUEST)
        401 -> ResultData.Failure(DataError.Remote.UNAUTHORIZED)
        403 -> ResultData.Failure(DataError.Remote.FORBIDDEN)
        404 -> ResultData.Failure(DataError.Remote.NOT_FOUND)
        408 -> ResultData.Failure(DataError.Remote.REQUEST_TIMEOUT)
        413 -> ResultData.Failure(DataError.Remote.PAYLOAD_TOO_LARGE)
        429 -> ResultData.Failure(DataError.Remote.TOO_MANY_REQUESTS)
        500 -> ResultData.Failure(DataError.Remote.SERVER_ERROR)
        503 -> ResultData.Failure(DataError.Remote.SERVICE_UNAVAILABLE)
        else -> ResultData.Failure(DataError.Remote.UNKNOWN)
    }
}

fun constructRoute(route: String): String {
    return when {
        route.contains(UrlConstants.BASE_URL_HTTP) -> route
        route.startsWith("/") -> "${UrlConstants.BASE_URL_HTTP}$route"
        else -> "${UrlConstants.BASE_URL_HTTP}/$route"
    }
}

