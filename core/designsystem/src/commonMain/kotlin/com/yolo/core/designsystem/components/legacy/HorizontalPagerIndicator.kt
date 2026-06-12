package com.yolo.core.designsystem.components.legacy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


enum class HorizontalPagerIndicatorStyle {
    STYLE1,
    STYLE2
}

@Composable
fun HorizontalPagerIndicator(
    size: Int,
    selectedIndex: Int,
    style: HorizontalPagerIndicatorStyle = HorizontalPagerIndicatorStyle.STYLE1,
    modifier: Modifier = Modifier,
    onClickIndicator: (Int) -> Unit,
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(size) { iteration ->
            val isSelected = iteration == selectedIndex
            val color =
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant

            when (style) {
                HorizontalPagerIndicatorStyle.STYLE1 -> {
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(13.dp)
                            .clip(CircleShape)
                            .background(color = color, shape = CircleShape)
                            .clickable { onClickIndicator(iteration) }

                    )
                }

                HorizontalPagerIndicatorStyle.STYLE2 -> {
                    val width = if (isSelected) 32.dp else 8.dp
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(width, 8.dp)
                            .clip(CircleShape)
                            .background(color = color, shape = CircleShape)
                            .clickable { onClickIndicator(iteration) }

                    )
                }
            }


        }
    }
}

