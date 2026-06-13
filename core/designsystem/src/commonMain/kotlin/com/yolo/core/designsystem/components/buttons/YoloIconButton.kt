package com.yolo.core.designsystem.components.buttons


import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun YoloIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Borderless, container-less affordance — just the glyph, with the full minTouchTarget
    // kept as the clickable/ripple area (no box around the arrow).
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(YoloTokens.sizing.minTouchTarget),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.extended.textSecondary
        )
    ) {
        content()
    }
}

@Composable
@Preview
fun YoloButtonPreview() {
    YoloTheme {
        YoloIconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }
}

@Composable
@Preview
fun YoloButtonDarkThemePreview() {
    YoloTheme(darkTheme = true) {
        YoloIconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }
}