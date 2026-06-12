package com.yolo.onboarding.presentation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import myhabitshub.feature.onboarding.presentation.generated.resources.Res
import myhabitshub.feature.onboarding.presentation.generated.resources.btn_get_started
import myhabitshub.feature.onboarding.presentation.generated.resources.btn_next
import myhabitshub.feature.onboarding.presentation.generated.resources.btn_skip
import com.yolo.core.designsystem.components.legacy.AnimatedHorizontalPager
import com.yolo.core.designsystem.components.legacy.AppButton
import com.yolo.core.designsystem.components.legacy.HorizontalPagerIndicator
import com.yolo.core.designsystem.components.legacy.ScreenTitle
import com.yolo.core.designsystem.theme.YoloTokens
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun OnBoardingContentVariation1(
    modifier: Modifier = Modifier,
    uiState: OnBoardingViewState,
    onGetStartedClick: () -> Unit
) {

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scrollState.animateScrollTo(scrollState.maxValue, tween(300))
    }

    Column(
        modifier = modifier.fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = YoloTokens.spacing.heroGap),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { uiState.pages.size }
        )
        val isLastPage = pagerState.currentPage == (pagerState.pageCount - 1)
        Row(
            modifier = Modifier.fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(horizontal = YoloTokens.spacing.screenEdge)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            androidx.compose.animation.AnimatedVisibility(
                visible = isLastPage.not(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SkipButton(
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(uiState.pages.lastIndex) }
                    })
            }

        }

        AnimatedHorizontalPager(
            pagerState = pagerState,
            modifier = Modifier
                .padding(top = YoloTokens.spacing.sectionGap)
                .heightIn(min = 450.dp)
        ) { pageIndex ->
            val onBoardingScreenData = uiState.pages[pageIndex]
            OnBoardingPager(
                item = onBoardingScreenData,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = YoloTokens.spacing.screenEdge)
            )
        }
        HorizontalPagerIndicator(
            modifier = Modifier.padding(top = YoloTokens.spacing.sectionGap),
            size = pagerState.pageCount,
            selectedIndex = pagerState.currentPage,
            style = com.yolo.core.designsystem.components.legacy.HorizontalPagerIndicatorStyle.STYLE1,
            onClickIndicator = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(
                        page = index,
                        animationSpec = tween()
                    )
                }
            }

        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.padding(
                start = YoloTokens.spacing.screenEdge,
                end = YoloTokens.spacing.screenEdge,
                top = YoloTokens.spacing.sectionGap
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                if (isLastPage.not()) {
                    AppButton(
                        text = stringResource(Res.string.btn_next),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            coroutineScope.launch {
                                val nextPage = kotlin.math.min(
                                    pagerState.currentPage + 1,
                                    uiState.pages.lastIndex
                                )
                                pagerState.animateScrollToPage(
                                    page = nextPage,
                                    animationSpec = tween()
                                )

                            }
                        })
                }

                if (isLastPage) {
                    AppButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.btn_get_started),
                        onClick = onGetStartedClick
                    )
                }

            }

        }
    }

}

@Composable
private fun OnBoardingPager(
    modifier: Modifier = Modifier,
    item: OnBoardingScreenData,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            YoloTokens.spacing.sectionGap,
            Alignment.CenterVertically
        )
    ) {

        Image(
            painter = painterResource(item.imageRes),
            contentDescription = null,
            modifier = Modifier.height(250.dp)
        )

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap)
        ) {
            ScreenTitle(
                text = stringResource(item.title),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(item.description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SkipButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    TextButton(
        //This 12 dp extra padding comes from Material Design so we remove that
        modifier = modifier.offset(x = 12.dp),
        onClick = { onClick() }
    ) {
        Text(
            text = stringResource(Res.string.btn_skip),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

