package com.yolo.myhabitshub.data.source.remote.response

import com.yolo.myhabitshub.domain.exceptions.UnAuthorizedException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseApiResponse<T>(
    @SerialName("statusCode") val statusCode: Int? = null,
    @SerialName("errorMessage") val errorMessage: String? = null,
    @SerialName("data") val data: T? = null
) {
    fun <A> handleAsResult(onSuccess: (T?) -> Result<A>): Result<A> {
        return when (statusCode) {
            in 200..299 -> {
                onSuccess(data)
            }

            401, 403 -> Result.failure(UnAuthorizedException())
            else -> Result.failure(Exception("Error: ${errorMessage ?: ""}"))
        }
    }

}