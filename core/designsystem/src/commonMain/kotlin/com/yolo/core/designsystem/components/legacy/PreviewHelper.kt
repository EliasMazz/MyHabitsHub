package com.yolo.core.designsystem.components.legacy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import com.yolo.core.designsystem.theme.legacy.AppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreviewHelper(content: @Composable () -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.horizontalItemSpacing),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.verticalListItemSpacing),
        content = { content() },
    )
}
