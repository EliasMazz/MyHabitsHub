package com.yolo.core.data.auth.dto.request

import kotlinx.serialization.Serializable

@Serializable
class RefreshTokenRequest (
    val refreshToken: String
)