package com.yolo.core.domain.auth

import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.EmptyResult
import com.yolo.core.domain.util.ResultData

interface AuthRepository {

    suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Remote>

    suspend fun resendVerificationEmail(
        email: String
    ): EmptyResult<DataError.Remote>

    suspend fun verifyEmail(
        token: String
    ): EmptyResult<DataError.Remote>

    suspend fun forgotPassword(
        email: String
    ): EmptyResult<DataError.Remote>

    suspend fun login(
        email: String,
        password: String
    ) : ResultData<AuthInfo, DataError.Remote>
}