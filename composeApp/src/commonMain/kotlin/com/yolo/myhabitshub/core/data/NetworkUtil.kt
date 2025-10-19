package com.yolo.myhabitshub.core.data

import com.yolo.myhabitshub.util.logging.AppLogger
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.io.IOException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

@Serializable
data class BaseApiResponse<T>(
    @SerialName("statusCode") val statusCode: Int? = null,
    @SerialName("errorMessage") val errorMessage: String? = null,
    @SerialName("data") val data: T? = null
){
    /**
     * Converts the API response into a Kotlin Result, using the provided onSuccess lambda
     * to transform the data if the call was successful according to the API's own statusCode.
     *
     * @param A The target type for the Result's success value.
     * @param onSuccess A lambda function that takes the nullable data (T?) from the API response
     *                  and should return a Result<A>. This allows for further validation or mapping
     *                  of the data.
     */
    fun <A> handleAsResult(onSuccess: (responseData: T?) -> Result<A>): Result<A> {
        return when (statusCode) {
            null -> {
                Result.failure(Exception("API Error: Response did not include a status code."))
            }
            in 200..299 -> {
                onSuccess(data)
            }
            401, 403 -> {
                Result.failure(UnAuthorizedException(errorMessage ?: "Unauthorized access (Status: $statusCode)"))
            }
            else -> {
                // For other non-2xx status codes from the API
                Result.failure(Exception("API Error: ${errorMessage ?: "Unknown server error"} (Status: $statusCode)"))
            }
        }
    }
}

class UnAuthorizedException(message: String) : Exception(message)

object NetworkUtils {
    private const val LOG_TAG = "SafeApiCall"

    /**
     * Safely executes a network [call] expected to return a `BaseApiResponse<T>`,
     * handling common network exceptions and processing the response.
     *
     * It uses `BaseApiResponse.handleAsResult` with the provided [mapSuccess] lambda
     * to convert the API's `data` (type T?) into the desired domain model `Result<R>`.
     *
     * @param T The type of the `data` field within `BaseApiResponse`.
     * @param R The target domain model type for a successful `Result`.
     * @param call Suspend lambda performing the Ktor request (e.g., `client.get(...).body<BaseApiResponse<T>()`).
     * @param mapSuccess Suspend lambda to transform `BaseApiResponse.data: T?` into `Result<R>` on success.
     */
    suspend fun <T : Any, R : Any> safeApiCall(
        call: suspend () -> BaseApiResponse<T>,
        mapSuccess: (responseData: T?) -> Result<R>
    ): Result<R> {
        return try {
            AppLogger.d(tag = LOG_TAG, message = "Executing API call...")
            val response: BaseApiResponse<T> = call()
            AppLogger.d(tag =  LOG_TAG, message = "Received API response. Status: ${response.statusCode}, Data: ${response.data != null}")
            response.handleAsResult(mapSuccess)
        } catch (e: CancellationException) {
            AppLogger.d(tag =  LOG_TAG, message = "API call cancelled." )
            throw e // Re-throw cancellation exceptions as they are used for coroutine cancellation
        }
        catch (e: SerializationException) {
            AppLogger.e(tag = LOG_TAG, message = "API call failed - JSON Deserialization error: ${e.message}", throwable = e)
            Result.failure(Exception("Network Error: Could not understand the server's response (bad JSON format or structure).", e))
        } catch (e: HttpRequestTimeoutException) {
            AppLogger.e(tag =  LOG_TAG, message = "API call failed - Request timeout: ${e.message}", throwable =  e )
            Result.failure(Exception("Network Error: The request timed out.", e))
        } catch (e: IOException) {
            AppLogger.e(tag =  LOG_TAG, message = "API call failed - Network I/O error: ${e.message}" , throwable =  e)
            Result.failure(Exception("Network Error: Could not connect to the server.", e))
        }
        catch (e: Exception) {
            AppLogger.e(tag = LOG_TAG, message = "API call failed - Unexpected error: ${e.message}", throwable = e)
            Result.failure(Exception("An unexpected error occurred: ${e.message}", e))
        }
    }
}
