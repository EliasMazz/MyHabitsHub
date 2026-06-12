package com.yolo.account.domain.repository

import com.yolo.account.domain.entities.UserResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository{
    fun getCurrentUser(): Flow<Result<UserResponse>>
    suspend fun logOut()
    suspend fun deleteAccount()
    suspend fun sendAuthToken(): Result<UserResponse>
}



