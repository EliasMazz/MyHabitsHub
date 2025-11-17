package com.yolo.core.data.auth

import com.yolo.core.data.auth.dto.RegisterRequest
import com.yolo.core.data.networking.post
import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(private val httpClient: HttpClient) : AuthRepository {
    override suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }
}