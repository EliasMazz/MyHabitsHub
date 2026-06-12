package com.yolo.account.data.remote.apiservices

import com.yolo.account.data.network.BaseApiResponse
import com.yolo.account.domain.entities.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post

class ApiService(private val httpClient: HttpClient) {

    suspend fun sendAuthToken() : BaseApiResponse<UserResponse> {
        return httpClient.post("/api/auth/google") {}.body()
    }
}