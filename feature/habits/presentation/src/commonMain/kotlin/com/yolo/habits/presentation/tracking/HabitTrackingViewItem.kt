package com.yolo.habits.presentation.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yolo.habits.domain.entities.HabitTracking
import myhabitshub.feature.habits.presentation.generated.resources.Res
import myhabitshub.feature.habits.presentation.generated.resources.ic_checkmark
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

data class HabitTrackingItemViewState(
    val habitTracking: HabitTracking,
    val isChecked: Boolean = false
)

/**
 * Habit row — Figma node 3311:64 (h-72, px-16 py-8): icon in a 48dp #F2F2F5 rounded-8 square,
 * title 16 Medium + progress 14 Regular #6B7582, then either the toggle checkbox (kept exactly:
 * ic_check/ic_uncheck tinted habitComplete/habitPending) or a trailing value (streak count).
 */
@Composable
fun HabitTrackViewItem(
    icon: DrawableResource,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    checked: Boolean? = null,
    trailingValue: String? = null,
    onClickToggleHabitCheck: () -> Unit = {},
    onClickHabitDetails: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onClickHabitDetails() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = vectorResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        when {
            trailingValue != null -> Text(
                text = trailingValue,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            checked != null -> FigmaCheckbox(checked = checked, onClick = onClickToggleHabitCheck)
        }
    }
}

/**
 * Figma checkbox (3311:77 / 3311:91): a 20dp rounded-4 box with a 2dp #DEE0E3 (outlineVariant)
 * border in BOTH states; the checked state adds the ink checkmark inside. 28dp touch target.
 */
@Composable
private fun FigmaCheckbox(checked: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(28.dp).clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_checkmark),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
