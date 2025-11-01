package com.yolo.core.data.networking

import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.ResultData
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.coroutineContext

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> ResultData<T, DataError.Remote>
): ResultData<T, DataError.Remote> {
    return try {
        val response = execute()
        handleResponse(response)
    } catch(e: UnknownHostException) {
        ResultData.Failure(DataError.Remote.NO_INTERNET)
    } catch(e: UnresolvedAddressException) {
        ResultData.Failure(DataError.Remote.NO_INTERNET)
    } catch(e: ConnectException) {
        ResultData.Failure(DataError.Remote.NO_INTERNET)
    } catch(e: SocketTimeoutException) {
        ResultData.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: HttpRequestTimeoutException) {
        ResultData.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: SerializationException) {
        ResultData.Failure(DataError.Remote.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        ResultData.Failure(DataError.Remote.UNKNOWN)
    }
}