package com.yolo.core.data.mapper

import com.yolo.core.data.auth.dto.response.AuthInfoResponse
import com.yolo.core.data.auth.dto.response.UserResponse
import com.yolo.core.domain.AuthInfo
import com.yolo.core.domain.User

fun AuthInfoResponse.toDomain() = AuthInfo(
    accessToken = accessToken,
    refreshToken = refreshToken,
    user = user.toDomain(),
)

fun UserResponse.toDomain() = User(
    id = id,
    email = email,
    hasVerifiedEmail = hasVerifiedEmail
)
