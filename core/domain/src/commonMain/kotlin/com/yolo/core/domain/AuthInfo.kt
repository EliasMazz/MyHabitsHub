package com.yolo.core.domain

data class AuthInfo (
    val accessToken: String,
    val refreshToken: String,
    val user: User,
)