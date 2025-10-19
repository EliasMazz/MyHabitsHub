package com.yolo.myhabitshub.data.source.remote.apiservices

import com.yolo.myhabitshub.core.data.BaseApiResponse
import com.yolo.myhabitshub.data.model.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post

class ApiService(private val httpClient: HttpClient) {

    suspend fun sendAuthToken() : BaseApiResponse<UserResponse> {
        return httpClient.post("/api/auth/google") {}.body()
    }
}