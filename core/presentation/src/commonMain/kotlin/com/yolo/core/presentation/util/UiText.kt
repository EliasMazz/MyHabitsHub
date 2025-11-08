package com.yolo.core.presentation.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface UiText {

    @get:Composable
    val value: String

    data class Resource(val id: StringResource) : UiText {

        override val value: String
            @Composable get() = stringResource(id)
    }

    data class Message(val message: String?) : UiText {
        override val value: String
            @Composable get() = message ?: ""
    }
}