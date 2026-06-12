package com.yolo.core.designsystem.components.legacy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yolo.core.designsystem.theme.YoloTokens

@Composable
fun SectionContainer(
    title: String? = null,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {

    Column(modifier = modifier) {
        if (title != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.elementGap),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(text = title)
                subtitle?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(YoloTokens.spacing.sectionHeaderGap))
        }
        content()
    }
}