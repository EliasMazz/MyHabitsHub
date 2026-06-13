package com.yolo.habits.presentation.tracking

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.yolo.core.designsystem.components.legacy.LoadingProgress
import com.yolo.core.designsystem.components.legacy.LoadingProgressMode
import com.yolo.core.presentation.BaseScreen
import myhabitshub.feature.habits.presentation.generated.resources.Res
import myhabitshub.feature.habits.presentation.generated.resources.ic_habit_journal
import myhabitshub.feature.habits.presentation.generated.resources.ic_habit_meditate
import myhabitshub.feature.habits.presentation.generated.resources.ic_habit_streak
import myhabitshub.feature.habits.presentation.generated.resources.ic_sched_book
import myhabitshub.feature.habits.presentation.generated.resources.ic_sched_dumbbell
import myhabitshub.feature.habits.presentation.generated.resources.ic_sched_water
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HabitTrackingScreen(
    viewModel: HabitTrackingViewModel = koinViewModel(),
) {
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { }
    ) { state, onIntent ->
        HabitTrackingScreenContent(
            viewState = state,
            onToggleHabitClicked = { onIntent(HabitTrackingViewIntent.OnToggleHabitClicked(it)) },
            onHabitDetailsClicked = {}
        )
    }
}

// ── Hardcoded DRAFT content — exact copy from Figma node 3311:2 ───────────────
private data class ScheduleEntry(val icon: DrawableResource, val title: String, val time: String)
private data class HabitEntry(
    val icon: DrawableResource,
    val title: String,
    val subtitle: String,
    val checked: Boolean? = null,
    val trailingValue: String? = null,
)

private const val HERO_TEXT = "It's been a week since your last gym visit – time to hit the weights!"

private val schedule = listOf(
    ScheduleEntry(Res.drawable.ic_sched_water, "Drink Water", "8:00 AM"),
    ScheduleEntry(Res.drawable.ic_sched_dumbbell, "Exercise", "3:00 PM - 3:45 PM"),
    ScheduleEntry(Res.drawable.ic_sched_book, "Read", "9:00 PM - 9:30 PM"),
)
private val habits = listOf(
    HabitEntry(Res.drawable.ic_habit_meditate, "Meditate", "3/3", checked = false),
    HabitEntry(Res.drawable.ic_habit_journal, "Journal", "2/3", checked = true),
    HabitEntry(Res.drawable.ic_habit_streak, "Streak", "5 days", trailingValue = "5"),
)

// Exact Figma colors that aren't 1:1 with M3 tokens.
private val FigDivider = Color(0xFFDEE0E3)

@Composable
fun HabitTrackingScreenContent(
    viewState: HabitTrackingViewState,
    onToggleHabitClicked: (HabitTrackingItemViewState) -> Unit,
    onHabitDetailsClicked: (com.yolo.habits.domain.entities.HabitTracking) -> Unit,
) {
    if (viewState.isLoading) {
        LoadingProgress(mode = LoadingProgressMode.FULLSCREEN)
        return
    }
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        Spacer(Modifier.height(8.dp))
        HeroCard()
        HabitTrackingContent()
    }
}

@Composable
private fun HabitTrackingContent(modifier: Modifier = Modifier) {
    val tabTextWidths = remember { mutableStateMapOf<Int, Dp>() }
    val density = LocalDensity.current
    val tabItems = listOf("Yesterday", "Today", "Week")
    var selectedTabIndex by remember { mutableIntStateOf(1) }
    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex,
        pageCount = { tabItems.size }
    )

    LaunchedEffect(selectedTabIndex) {
        if (pagerState.currentPage != selectedTabIndex) pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            if (selectedTabIndex != page) selectedTabIndex = page
        }
    }

    // Draft toggle state so the habit checkboxes actually respond to clicks.
    val checkedHabits = remember { mutableStateMapOf("Meditate" to false, "Journal" to true) }

    Column(modifier = modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.height(48.dp),
            edgePadding = 16.dp,
            divider = {}, // full-width divider drawn below the row instead (ScrollableTabRow's is inset)
            indicator = { tabPositions ->
                if (tabPositions.isNotEmpty() && pagerState.currentPage < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.pagerTextIndicatorOffset(
                            pagerState = pagerState,
                            tabPositions = tabPositions,
                            tabTextWidths = tabTextWidths.toMap()
                        ),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        ) {
            tabItems.forEachIndexed { index, title ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (index == selectedTabIndex) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant,
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

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) { _ ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item { SectionHeader("Today's Schedule") }
                items(schedule.size) { i ->
                    ScheduleRow(
                        schedule[i],
                        drawTop = i != 0,
                        drawBottom = i != schedule.lastIndex,
                    )
                }
                item { SectionHeader("Habits") }
                items(habits.size) { i ->
                    val h = habits[i]
                    HabitTrackViewItem(
                        icon = h.icon,
                        title = h.title,
                        subtitle = h.subtitle,
                        checked = if (h.trailingValue != null) null else checkedHabits[h.title],
                        trailingValue = h.trailingValue,
                        onClickToggleHabitCheck = {
                            checkedHabits[h.title] = !(checkedHabits[h.title] ?: false)
                        },
                    )
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun HeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(254.dp)
            .clip(RoundedCornerShape(12.dp)),
    ) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerHigh))
        // Figma 3311:7: gradient transparent (top) → 40% black (bottom).
        Box(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(0f to Color.Transparent, 1f to Color(0x66000000))
            )
        )
        Text(
            text = HERO_TEXT,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 24.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontSize = 18.sp,
        lineHeight = 23.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
    )
}

@Composable
private fun ScheduleRow(entry: ScheduleEntry, drawTop: Boolean, drawBottom: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp).padding(horizontal = 16.dp)) {
        // Leading rail (Figma w-40): connector segment above + icon + segment below.
        Column(
            modifier = Modifier.width(40.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (drawTop) Box(Modifier.width(2.dp).height(8.dp).background(FigDivider))
            Icon(
                imageVector = vectorResource(entry.icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = if (drawTop) 4.dp else 12.dp)
                    .size(24.dp),
            )
            if (drawBottom) {
                Spacer(Modifier.height(4.dp))
                Box(Modifier.width(2.dp).weight(1f).background(FigDivider))
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = entry.time,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
    val currentTextWidth = tabTextWidths[currentPage] ?: currentTabPosition.width

    val fraction = pagerState.currentPageOffsetFraction

    val indicatorTargetWidth: Dp
    val indicatorTargetOffset: Dp

    if (fraction > 0f) {
        val nextPageIndex = (currentPage + 1).coerceIn(0, tabPositions.lastIndex)
        val nextTabTextWidth = tabTextWidths[nextPageIndex] ?: tabPositions[nextPageIndex].width
        val nextTabPosition = tabPositions[nextPageIndex]

        indicatorTargetWidth = lerp(currentTextWidth, nextTabTextWidth, fraction)

        val currentTextStartOffset = currentTabPosition.left + (currentTabPosition.width - currentTextWidth) / 2
        val nextTextStartOffset = nextTabPosition.left + (nextTabPosition.width - nextTabTextWidth) / 2
        indicatorTargetOffset = lerp(currentTextStartOffset, nextTextStartOffset, fraction)

    } else if (fraction < 0f) {
        val prevPageIndex = (currentPage - 1).coerceIn(0, tabPositions.lastIndex)
        val prevTabTextWidth = tabTextWidths[prevPageIndex] ?: tabPositions[prevPageIndex].width
        val prevTabPosition = tabPositions[prevPageIndex]

        indicatorTargetWidth = lerp(currentTextWidth, prevTabTextWidth, -fraction)

        val currentTextStartOffset = currentTabPosition.left + (currentTabPosition.width - currentTextWidth) / 2
        val prevTextStartOffset = prevTabPosition.left + (prevTabPosition.width - prevTabTextWidth) / 2
        indicatorTargetOffset = lerp(currentTextStartOffset, prevTextStartOffset, -fraction)

    } else {
        indicatorTargetWidth = currentTextWidth
        indicatorTargetOffset = currentTabPosition.left + (currentTabPosition.width - currentTextWidth) / 2
    }

    this.fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorTargetOffset)
        .width(indicatorTargetWidth)
}
