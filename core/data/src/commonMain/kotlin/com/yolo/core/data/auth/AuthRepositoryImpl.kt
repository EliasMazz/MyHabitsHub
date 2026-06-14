package com.yolo.core.data.auth

import com.yolo.core.data.auth.dto.request.AppleLoginRequest
import com.yolo.core.data.auth.dto.request.EmailRequest
import com.yolo.core.data.auth.dto.request.GoogleLoginRequest
import com.yolo.core.data.auth.dto.request.LoginRequest
import com.yolo.core.data.auth.dto.request.RegisterRequest
import com.yolo.core.data.auth.dto.request.ResetPasswordRequest
import com.yolo.core.data.auth.dto.response.AuthInfoResponse
import com.yolo.core.data.mapper.toDomain
import com.yolo.core.data.networking.get
import com.yolo.core.data.networking.post
import com.yolo.core.domain.auth.AuthInfo
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

    override suspend fun forgotPassword(
        email: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "api/auth/forgot-password",
            body = EmailRequest(email = email)
        )
    }

    override suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/reset-password",
            body = ResetPasswordRequest(
                newPassword = newPassword,
                token = token
            )
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

    override suspend fun loginWithGoogle(
        idToken: String
    ): ResultData<AuthInfo, DataError.Remote> {
        return httpClient.post<GoogleLoginRequest, AuthInfoResponse>(
            route = "api/auth/google",
            body = GoogleLoginRequest(idToken = idToken)
        ).map {
            it.toDomain()
        }
    }

    override suspend fun loginWithApple(
        identityToken: String,
        authorizationCode: String,
        nonce: String,
    ): ResultData<AuthInfo, DataError.Remote> {
        return httpClient.post<AppleLoginRequest, AuthInfoResponse>(
            route = "api/auth/apple",
            body = AppleLoginRequest(
                identityToken = identityToken,
                authorizationCode = authorizationCode,
                nonce = nonce,
            )
        ).map {
            it.toDomain()
        }
    }
}