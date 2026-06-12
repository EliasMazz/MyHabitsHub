package com.yolo.core.designsystem.components.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.designsystem.theme.section

enum class YoloSheetActionRowStyle { Default, Destructive, Disabled }

/**
 * The reference app-inspired icon-on-tinted-chip row for bottom sheets
 * (section-color-worlds spec §4.1): a 40dp tonal chip with a colored icon of the same hue,
 * tinted by the current section world. Destructive rows use the error family; disabled/no-data
 * rows fall back to neutral fills — color is earned by data.
 */
@Composable
fun YoloSheetActionRow(
    icon: Painter,
    label: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    style: YoloSheetActionRowStyle = YoloSheetActionRowStyle.Default,
    onClick: (() -> Unit)? = null,
) {
    val sectionColors = MaterialTheme.colorScheme.section
    val extendedColors = MaterialTheme.colorScheme.extended

    val chipColor: Color
    val iconColor: Color
    val labelColor: Color
    val supportingColor: Color
    when (style) {
        YoloSheetActionRowStyle.Default -> {
            chipColor = sectionColors.sheetIconChip
            iconColor = sectionColors.onSheetIconChip
            labelColor = sectionColors.onSheetSurface
            supportingColor = sectionColors.onSheetSurfaceVariant
        }

        YoloSheetActionRowStyle.Destructive -> {
            chipColor = MaterialTheme.colorScheme.errorContainer
            iconColor = MaterialTheme.colorScheme.onErrorContainer
            labelColor = MaterialTheme.colorScheme.error
            supportingColor = sectionColors.onSheetSurfaceVariant
        }

        YoloSheetActionRowStyle.Disabled -> {
            chipColor = MaterialTheme.colorScheme.surfaceContainerHighest
            iconColor = extendedColors.textDisabled
            labelColor = extendedColors.textDisabled
            supportingColor = extendedColors.textDisabled
        }
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 56.dp)
            .let { base ->
                if (onClick != null && style != YoloSheetActionRowStyle.Disabled) {
                    base.clickable(onClick = onClick)
                } else {
                    base
                }
            }
            .padding(horizontal = YoloTokens.spacing.screenEdge),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(chipColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = YoloTokens.spacing.iconTextGap),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = labelColor,
            )
            if (supportingText != null) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = supportingColor,
                )
            }
        }
    }
}
