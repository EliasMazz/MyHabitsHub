package com.yolo.core.domain.auth

import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.EmptyResult

interface AuthRepository {

    suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Remote>
}