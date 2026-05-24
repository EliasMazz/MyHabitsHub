package com.yolo.core.data.auth.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoResponse (
    val accessToken: String,
    val refreshToken: String,
    val user: UserResponse,
)