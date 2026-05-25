package com.yolo.core.domain.auth

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val hasVerifiedEmail: Boolean,
)