package com.yolo.habits.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class HabitTracking(
    val id: Int=0,
    val title: String = "",
    val name: String = "",
    val description: String = "",
)
