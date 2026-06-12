package com.yolo.core.designsystem.components.legacy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended

@Composable
fun AppCardContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = MaterialTheme.colorScheme.extended.surfaceHigher,
    onClick: (() -> Unit)? = null,
    contentPaddingValues: PaddingValues = PaddingValues(YoloTokens.spacing.cardPadding),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape = shape)
            .background(backgroundColor)
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            }
            .padding(contentPaddingValues),
    ) {
        content()

    }
}