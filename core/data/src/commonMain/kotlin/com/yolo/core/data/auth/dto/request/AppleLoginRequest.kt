package com.yolo.core.data.auth.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class AppleLoginRequest(
    val identityToken: String,
    val authorizationCode: String,
    val nonce: String,
)
