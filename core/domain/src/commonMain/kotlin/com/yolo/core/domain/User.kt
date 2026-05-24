package com.yolo.core.domain

data class User(
    val id: String,
    val email: String,
    val hasVerifiedEmail: Boolean,
)