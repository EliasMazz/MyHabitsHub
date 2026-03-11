package com.yolo.myhabitshub.data.repository

import com.yolo.myhabitshub.data.model.UserResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository{
    fun getCurrentUser(): Flow<Result<UserResponse>>
    suspend fun logOut()
    suspend fun deleteAccount()
    suspend fun sendAuthToken(): Result<UserResponse>
}



