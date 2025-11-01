package com.yolo.core.data.networking

import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.ResultData
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import platform.Foundation.NSURLErrorCallIsActive
import platform.Foundation.NSURLErrorCannotFindHost
import platform.Foundation.NSURLErrorDNSLookupFailed
import platform.Foundation.NSURLErrorDataNotAllowed
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorInternationalRoamingOff
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorResourceUnavailable
import platform.Foundation.NSURLErrorTimedOut
import kotlin.coroutines.coroutineContext

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> ResultData<T, DataError.Remote>
): ResultData<T, DataError.Remote> {
    return try {
        val response = execute()
        handleResponse(response)
    } catch(e: DarwinHttpRequestException) {
        handleDarwinException(e)
    } catch(e: UnresolvedAddressException) {
        ResultData.Failure(DataError.Remote.NO_INTERNET)
    } catch(e: HttpRequestTimeoutException) {
        ResultData.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: SerializationException) {
        ResultData.Failure(DataError.Remote.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        ResultData.Failure(DataError.Remote.UNKNOWN)
    }
}

private fun handleDarwinException(e: DarwinHttpRequestException): ResultData<Nothing, DataError.Remote> {
    val nsError = e.origin

    return if(nsError.domain == NSURLErrorDomain) {
        when(nsError.code) {
            NSURLErrorNotConnectedToInternet,
            NSURLErrorNetworkConnectionLost,
            NSURLErrorCannotFindHost,
            NSURLErrorDNSLookupFailed,
            NSURLErrorResourceUnavailable,
            NSURLErrorInternationalRoamingOff,
            NSURLErrorCallIsActive,
            NSURLErrorDataNotAllowed -> {
                ResultData.Failure(DataError.Remote.NO_INTERNET)
            }

            NSURLErrorTimedOut -> ResultData.Failure(DataError.Remote.REQUEST_TIMEOUT)
            else -> ResultData.Failure(DataError.Remote.UNKNOWN)
        }
    } else ResultData.Failure(DataError.Remote.UNKNOWN)
}