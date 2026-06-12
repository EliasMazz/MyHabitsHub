package com.yolo.myhabitshub.presentation.screens.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yolo.core.presentation.BaseScreen
import com.yolo.core.designsystem.components.legacy.AppToolbar
import com.yolo.core.designsystem.theme.YoloSection
import com.yolo.core.designsystem.theme.YoloSectionTheme
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.colors
import com.yolo.core.designsystem.theme.extended
import com.yolo.myhabitshub.presentation.BottomNavItem
import com.yolo.myhabitshub.presentation.components.bottomnav.BottomNavigationBar
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    navController: NavHostController,
    onSignInSuccess: () -> Unit = {},
    onNavigateTo: (MainViewEvent.NavigateTo) -> Unit = {},
    onNavigateUp: () -> Unit = {},
) {
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                is MainViewEvent.NavigateTo -> onNavigateTo(event)
                MainViewEvent.NavigateUp -> onNavigateUp()
            }
        }
    ) { state, onIntent ->
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        LaunchedEffect(navBackStackEntry) {
            currentRoute?.let {
                onIntent(
                    MainViewIntent.OnNavigationDestinationChanged(
                        route = it,
                    )
                )
            }
        }

        MainScreenContent(
            modifier = Modifier.fillMaxSize(),
            viewState = state,
            onToolbarNavItemClicked = { onIntent(MainViewIntent.OnToolbarNavItemClicked) },
            onBottomNavItemClicked = { onIntent(MainViewIntent.OnBottomNavItemClicked(bottomNavItem = it)) },
            mainContent = {
                MainNavHost(
                    navController = navController,
                    onSignInSuccess = onSignInSuccess,
                    onToolbarSetTitle = { onIntent(MainViewIntent.OnToolbarSetTitle(label = it)) }
                )
            }
        )
    }
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    viewState: MainScreenViewState,
    onToolbarNavItemClicked:() -> Unit,
    onBottomNavItemClicked: (BottomNavItem) -> Unit,
    mainContent: @Composable () -> Unit,
) {
    val navItems = viewState.bottomNavViewState.items.ifEmpty { BottomNavItem.items() }
    val currentSection = navItems
        .getOrNull(viewState.bottomNavViewState.selectedBottomNavIndex)?.section
        ?: YoloSection.Tracking

    // The one continuously-tinting plane (section-color-worlds spec §3.6): a single animated
    // wash drawn behind toolbar + content in the draw phase. The section scheme itself is
    // never animated — worlds blend optically through the navigation crossfade.
    val isDark = MaterialTheme.colorScheme.extended.isDark
    val motion = YoloTokens.motion
    val wash by animateColorAsState(
        targetValue = currentSection.colors(isDark).surfaceTintWash,
        animationSpec = if (motion.reducedMotion) {
            snap()
        } else {
            tween(motion.standard, easing = motion.easingStandard)
        },
        label = "sectionWash",
    )

    YoloSectionTheme(section = currentSection) {
        Box(modifier = modifier.drawBehind { drawRect(wash) }) {
            Column(modifier = Modifier.fillMaxSize()) {
                //AppBar — transparent so the animated section wash shows through
                if (viewState.toolbarUiState.isVisible) {
                    AppToolbar(
                        title = viewState.toolbarUiState.text,
                        navigationIcon = viewState.toolbarUiState.navigationIconRes?.let {
                            painterResource(it)
                        },
                        containerColor = Color.Transparent,
                        onNavigationIconClick = { onToolbarNavItemClicked() }
                    )
                }

                //Main Content
                Box(modifier = Modifier.weight(1f)) {
                    mainContent()
                }
                //BottomNavigation Bar
                if (viewState.bottomNavViewState.isVisible) {
                    BottomNavigationBar(
                        modifier = Modifier.fillMaxWidth(),
                        items = navItems,
                        selectedIndex = viewState.bottomNavViewState.selectedBottomNavIndex
                    ) {
                        onBottomNavItemClicked(it)
                    }
                }
            }
        }
    }
}


