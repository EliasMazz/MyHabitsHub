package com.yolo.myhabitshub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HabitTracking(
    val id: Int=0,
    val title: String = "",
    val name: String = "",
    val description: String = "",
)
