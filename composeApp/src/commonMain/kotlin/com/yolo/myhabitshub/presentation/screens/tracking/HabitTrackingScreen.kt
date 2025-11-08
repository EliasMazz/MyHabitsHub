package com.yolo.myhabitshub.presentation.screens.tracking

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.yolo.myhabitshub.core.presentation.theme.AppTheme
import com.yolo.myhabitshub.domain.model.HabitTracking
import com.yolo.myhabitshub.presentation.components.LoadingProgress
import com.yolo.myhabitshub.presentation.components.LoadingProgressMode

@Composable
fun HabitTrackingScreen(
    viewState: HabitTrackingViewState,
    onToggleHabitClicked: (HabitTrackingItemViewState) -> Unit,
    onHabitDetailsClicked: (HabitTracking) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        if (viewState.isLoading) {
            LoadingProgress(mode = LoadingProgressMode.FULLSCREEN)
        } else {
            HabitTrackingContent(
                modifier = Modifier.fillMaxSize()
                    .background(AppTheme.colors.background),
                habitTrackingList = viewState.listHabitTracking,
                onClickToggleHabitCheck = onToggleHabitClicked,
                onClickHabitDetails = { }
            )
        }
    }
}

@Composable
private fun HabitTrackingContent(
    modifier: Modifier = Modifier,
    habitTrackingList: List<HabitTrackingItemViewState>,
    onClickToggleHabitCheck: (HabitTrackingItemViewState) -> Unit,
    onClickHabitDetails: (HabitTracking) -> Unit
) {

    val tabTextWidths = remember { mutableStateMapOf<Int, Dp>() }
    val density = LocalDensity.current
    val tabItems = listOf("02. Sat", "03. Sun", "04. Mon", "Yesterday", "Today")
    var selectedTabIndex by remember {
        mutableIntStateOf(tabItems.lastIndex)
    }
    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex,
        pageCount = { tabItems.size }
    )

    LaunchedEffect(selectedTabIndex) {
        if (pagerState.currentPage != selectedTabIndex) {
            pagerState.animateScrollToPage(selectedTabIndex)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            if (selectedTabIndex != page) {
                selectedTabIndex = page
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            modifier = Modifier.height(48.dp),
            edgePadding = 200.dp,
            indicator = { tabPositions ->
                if (tabPositions.isNotEmpty() && pagerState.currentPage < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.pagerTextIndicatorOffset(
                            pagerState = pagerState,
                            tabPositions = tabPositions,
                            tabTextWidths = tabTextWidths.toMap()
                        ),
                        height = 3.dp
                    )
                }

            }
        ) {
            tabItems.forEachIndexed { index, title ->
                Tab(
                    selected = index == selectedTabIndex,
                    modifier = Modifier,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = AppTheme.typography.bodyMedium,
                            color = if(index == selectedTabIndex) AppTheme.colors.text.primary else  AppTheme.colors.text.secondary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                tabTextWidths[index] = with(density) { coordinates.size.width.toDp() }
                            }
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxWidth().weight(1f)
        ) { index ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = modifier.fillMaxWidth(),
                ) {
                    items(
                        habitTrackingList.size,
                        key = { index -> habitTrackingList[index].habitTracking.id }) { index ->
                        val habitTrackingItem = habitTrackingList[index]
                        HabitTrackViewItem(
                            habitTrackingItemViewState = habitTrackingItem,
                            onClickToggleHabitCheck = { onClickToggleHabitCheck(habitTrackingItem) },
                            onClickHabitDetails = { onClickHabitDetails(habitTrackingItem.habitTracking) }
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun Modifier.pagerTextIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    tabTextWidths: Map<Int, Dp>
): Modifier = composed {

    if (tabPositions.isEmpty() || pagerState.pageCount == 0) return@composed this
    val currentPage = pagerState.currentPage.coerceIn(0, tabPositions.lastIndex)

    val currentTabPosition = tabPositions[currentPage]
    val currentTextWidth = tabTextWidths[currentPage] ?: currentTabPosition.width // Fallback

    val fraction = pagerState.currentPageOffsetFraction

    val indicatorTargetWidth: Dp
    val indicatorTargetOffset: Dp

    if (fraction > 0f) { // Swiping towards the next tab
        val nextPageIndex = (currentPage + 1).coerceIn(0, tabPositions.lastIndex)
        val nextTabTextWidth = tabTextWidths[nextPageIndex] ?: tabPositions[nextPageIndex].width // Fallback
        val nextTabPosition = tabPositions[nextPageIndex]

        indicatorTargetWidth = lerp(currentTextWidth, nextTabTextWidth, fraction)

        val currentTextStartOffset = currentTabPosition.left + (currentTabPosition.width - currentTextWidth) / 2
        val nextTextStartOffset = nextTabPosition.left + (nextTabPosition.width - nextTabTextWidth) / 2
        indicatorTargetOffset = lerp(currentTextStartOffset, nextTextStartOffset, fraction)

    } else if (fraction < 0f) { // Swiping towards the previous tab
        val prevPageIndex = (currentPage - 1).coerceIn(0, tabPositions.lastIndex)
        val prevTabTextWidth = tabTextWidths[prevPageIndex] ?: tabPositions[prevPageIndex].width // Fallback
        val prevTabPosition = tabPositions[prevPageIndex]

        indicatorTargetWidth = lerp(currentTextWidth, prevTabTextWidth, -fraction)

        val currentTextStartOffset = currentTabPosition.left + (currentTabPosition.width - currentTextWidth) / 2
        val prevTextStartOffset = prevTabPosition.left + (prevTabPosition.width - prevTabTextWidth) / 2
        indicatorTargetOffset = lerp(currentTextStartOffset, prevTextStartOffset, -fraction)

    } else { // No swipe, settled on a page
        indicatorTargetWidth = currentTextWidth
        indicatorTargetOffset = currentTabPosition.left + (currentTabPosition.width - currentTextWidth) / 2
    }

    this.fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorTargetOffset)
        .width(indicatorTargetWidth)

}
