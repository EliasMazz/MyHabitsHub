package com.yolo.myhabitshub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("uid")
    val id: String = "",
    @SerialName("email")
    val email: String? = null,
    @SerialName("name")
    val displayName: String? = null,
    @SerialName("picture")
    val photoUrl: String? = null,
    val isAnonymous: Boolean = false,
)
