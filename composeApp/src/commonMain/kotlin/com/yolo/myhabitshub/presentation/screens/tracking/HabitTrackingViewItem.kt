package com.yolo.myhabitshub.presentation.screens.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yolo.myhabitshub.domain.model.HabitTracking
import com.yolo.myhabitshub.core.presentation.theme.AppTheme
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
            .background(Color.White)
            .padding(start = 16.dp)
            .clickable { onClickHabitDetails() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.horizontalItemSpacing)
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.ic_habit_icon_test),
            contentDescription = null,
            modifier = Modifier.size(42.dp),
            tint = Color.Unspecified
        )
        Text(
            modifier = Modifier.weight(1f),
            text = habitTracking.title,
            style = AppTheme.typography.medium,
        )

        IconButton(onClick = onClickToggleHabitCheck) {
            val icon =
                if (isChecked) vectorResource(Res.drawable.ic_check_habit) else vectorResource(Res.drawable.ic_uncheck_habit)
            Icon(icon, contentDescription = null, tint = Color.Unspecified)
        }
    }
}