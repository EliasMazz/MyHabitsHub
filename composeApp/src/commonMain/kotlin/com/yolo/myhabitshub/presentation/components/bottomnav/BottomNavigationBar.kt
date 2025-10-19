package com.yolo.myhabitshub.presentation.components.bottomnav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.yolo.myhabitshub.core.presentation.theme.AppTheme
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
        containerColor = AppTheme.colors.bottomNav.background
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
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
                        style = AppTheme.typography.bodyExtraSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppTheme.colors.bottomNav.selectedTextIcon,
                    selectedTextColor = AppTheme.colors.bottomNav.selectedTextIcon,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = AppTheme.colors.bottomNav.unselectedTextIcon,
                    unselectedTextColor = AppTheme.colors.bottomNav.unselectedTextIcon,
                ),
                selected = isSelected,
                enabled = true,
                onClick = { onClickItem(item) },
            )
        }
    }
}

/*
@Preview
@Composable
fun BottomNavigationBarPreview() {
    PreviewHelper {
        val emptyScreenRoute = object : ScreenRoute<Nothing, Nothing, Nothing, Nothing> {
            @Composable
            override fun provideScreenContract(viewModel: Nothing): ScreenContract<Nothing, Nothing> {
                TODO("Not yet implemented")
            }
        }
        val bottomNavItems = listOf(
            BottomNavItem(
                label = "Home",
                icon = Icons.Default.Home,
                screenRoute = emptyScreenRoute,
                destination = ""
            ),
            BottomNavItem(
                label = "Favorite",
                icon = Icons.Default.Favorite,
                screenRoute = emptyScreenRoute,
                destination = ""
            ),
            BottomNavItem(
                label = "Profile",
                icon = Icons.Default.AccountCircle,
                screenRoute = emptyScreenRoute,
                destination = ""
            )
        )
        var currentSelectedIndex by remember { mutableStateOf(0) }
        BottomNavigationBar(
            selectedIndex = currentSelectedIndex,
            items = bottomNavItems,
            onClickItem = { clickedItem ->
                currentSelectedIndex = bottomNavItems.indexOfFirst {
                    it.label == clickedItem.label
                }
            }
        )
    }
}

 */
