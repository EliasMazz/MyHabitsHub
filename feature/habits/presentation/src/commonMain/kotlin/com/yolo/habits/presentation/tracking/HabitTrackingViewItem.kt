package com.yolo.habits.presentation.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yolo.habits.domain.entities.HabitTracking
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.designsystem.theme.section
import myhabitshub.core.designsystem.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_check_habit
import myhabitshub.core.designsystem.generated.resources.ic_habit_icon_test
import myhabitshub.core.designsystem.generated.resources.ic_uncheck_habit
import org.jetbrains.compose.resources.vectorResource

data class HabitTrackingItemViewState(
    val habitTracking: HabitTracking,
    val isChecked: Boolean = false
)

@Composable
fun HabitTrackViewItem(
    modifier: Modifier = Modifier,
    habitTrackingItemViewState: HabitTrackingItemViewState,
    onClickToggleHabitCheck: () -> Unit,
    onClickHabitDetails: () -> Unit
) {
    val habitTracking = habitTrackingItemViewState.habitTracking
    val isChecked = habitTrackingItemViewState.isChecked

    Row(
        modifier = modifier.fillMaxWidth().height(56.dp)
            .background(MaterialTheme.colorScheme.extended.surfaceHigher)
            .padding(start = 16.dp)
            .clickable { onClickHabitDetails() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.itemGap)
    ) {
        // Icon-on-tinted-chip (section-color-worlds spec §6.5). Falls back to the section
        // container pair until per-habit accents are wired to data — then this becomes
        // habitAccents[n].container / onContainer.
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.section.accentContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_habit_icon_test),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.section.onAccentContainer,
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = habitTracking.title,
            style = MaterialTheme.typography.titleMedium,
        )

        IconButton(onClick = onClickToggleHabitCheck) {
            // Completion stays brand emerald in every world; pending is the ink ring —
            // forgiveness palette, never section-colored (spec §6.6).
            val icon =
                if (isChecked) vectorResource(Res.drawable.ic_check_habit) else vectorResource(Res.drawable.ic_uncheck_habit)
            val tint = if (isChecked) {
                MaterialTheme.colorScheme.extended.habitComplete
            } else {
                MaterialTheme.colorScheme.extended.habitPending
            }
            Icon(icon, contentDescription = null, tint = tint)
        }
    }
}