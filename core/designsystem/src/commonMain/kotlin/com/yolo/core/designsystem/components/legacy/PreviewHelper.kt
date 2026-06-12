package com.yolo.core.designsystem.components.legacy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.YoloTokens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreviewHelper(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    YoloTheme(darkTheme = darkTheme) {
        Surface {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.itemGap),
                verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.listRowGap),
                content = { content() },
            )
        }
    }
}
