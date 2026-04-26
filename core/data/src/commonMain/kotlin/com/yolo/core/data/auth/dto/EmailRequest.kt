package com.yolo.core.data.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val email: String
)