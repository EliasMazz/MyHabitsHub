package com.yolo.core.data.auth

import com.yolo.core.data.auth.dto.request.EmailRequest
import com.yolo.core.data.auth.dto.request.LoginRequest
import com.yolo.core.data.auth.dto.request.RegisterRequest
import com.yolo.core.data.auth.dto.response.AuthInfoResponse
import com.yolo.core.data.mapper.toDomain
import com.yolo.core.data.networking.get
import com.yolo.core.data.networking.post
import com.yolo.core.domain.AuthInfo
import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.EmptyResult
import com.yolo.core.domain.util.ResultData
import com.yolo.core.domain.util.map
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient
) : AuthRepository {
    override suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "api/auth/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "api/auth/resend-verification",
            body = EmailRequest(email = email)
        )
    }

    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        return httpClient.get(
            route = "api/auth/verify",
            queryParams = mapOf("token" to token)
        )
    }

    override suspend fun login(
        email: String,
        password: String
    ): ResultData<AuthInfo, DataError.Remote> {
        return httpClient.post<LoginRequest, AuthInfoResponse>(
            route = "api/auth/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        ).map {
            it.toDomain()
        }
    }
}