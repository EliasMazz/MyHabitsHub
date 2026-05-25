package com.yolo.core.data.mapper

import com.yolo.core.data.auth.dto.response.AuthInfoResponse
import com.yolo.core.data.auth.dto.response.UserResponse
import com.yolo.core.domain.auth.AuthInfo
import com.yolo.core.domain.auth.User

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

fun AuthInfo.toData() = AuthInfoResponse(
    accessToken = accessToken,
    refreshToken = refreshToken,
    user = user.toData(),
)

fun User.toData() = UserResponse(
    id = id,
    email = email,
    hasVerifiedEmail = hasVerifiedEmail
)

