package com.yolo.core.data.auth.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val email: String,
    val hasVerifiedEmail: Boolean,
)