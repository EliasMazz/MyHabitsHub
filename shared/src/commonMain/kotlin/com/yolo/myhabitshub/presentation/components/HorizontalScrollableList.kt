package com.yolo.myhabitshub.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import com.yolo.myhabitshub.core.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HorizontalScrollableList(
    modifier: Modifier = Modifier,
    ignoreParentHorizontalPadding: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = if (ignoreParentHorizontalPadding) AppTheme.spacing.outerSpacing
        else 0.dp
    ),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(AppTheme.spacing.horizontalItemSpacing),
    content: LazyListScope.() -> Unit
) {

    val updatedModifier = if (ignoreParentHorizontalPadding) {
        val horizontalPaddingInPx = with(LocalDensity.current) {
            val startPaddingInPx =
                contentPadding.calculateStartPadding(LocalLayoutDirection.current).toPx()
            val endPaddingInPx =
                contentPadding.calculateEndPadding(LocalLayoutDirection.current).toPx()
            startPaddingInPx + endPaddingInPx
        }
        modifier.fillWidthOfParent(totalParentHorizontalPaddingInPx = horizontalPaddingInPx)
    } else modifier

    LazyRow(
        modifier = updatedModifier,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        content = content
    )
}

// This is to force layout to go beyond the borders of its parentAdd commentMore actions
fun Modifier.fillWidthOfParent(totalParentHorizontalPaddingInPx: Float) =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(
            constraints.copy(
                maxWidth = constraints.maxWidth + totalParentHorizontalPaddingInPx.fastRoundToInt()
            )
        )
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }

@Preview
@Composable
fun HorizontalScrollableListPreview() {
    PreviewHelper {
        HorizontalScrollableList(
            ignoreParentHorizontalPadding = true,
            contentPadding = PaddingValues(horizontal = AppTheme.spacing.outerSpacing)
        ) {
            items(10) {
                Text("Item $it")
            }
        }
    }
}