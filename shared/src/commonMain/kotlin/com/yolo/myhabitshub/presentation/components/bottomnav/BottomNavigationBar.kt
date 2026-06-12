package com.yolo.myhabitshub.presentation.components.bottomnav

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.yolo.core.designsystem.theme.colors
import com.yolo.core.designsystem.theme.extended
import com.yolo.myhabitshub.presentation.BottomNavItem
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    items: List<BottomNavItem>,
    selectedIndex: Int = 0,
    onClickItem: (BottomNavItem) -> Unit
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        val isDark = MaterialTheme.colorScheme.extended.isDark
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            // Each item is colored by its OWN section world, so the selected pill
            // always shows the destination's hue (section-color-worlds spec §5).
            val section = item.section.colors(isDark)
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = vectorResource(item.icon),
                        contentDescription = stringResource(item.label),
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.label),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = section.onNavIndicator,
                    selectedTextColor = section.navSelectedText,
                    indicatorColor = section.navIndicator,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                selected = isSelected,
                enabled = true,
                onClick = { onClickItem(item) },
            )
        }
    }
}
