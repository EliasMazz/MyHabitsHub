package com.yolo.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Input idiom for sizing tokens. [Auto] selects [Touch] below 1200dp window width and
 * [Pointer] at >= 1200dp; platform entry points may override (a future desktop main()
 * passes [Pointer], Android/iOS pass nothing).
 */
enum class YoloInputProfile { Auto, Touch, Pointer }

/**
 * Semantic spacing scale, profile-keyed by window width class inside YoloTheme.
 * Feature code uses ONLY these names via YoloTokens.spacing — never raw dp.
 */
@Immutable
data class YoloSpacing(
    val elementGap: Dp = 8.dp,
    val stackGapTight: Dp = 6.dp,
    val stackGap: Dp = 12.dp,
    val iconTextGap: Dp = 12.dp,
    val itemGap: Dp = 16.dp,
    val cardPadding: Dp = 16.dp,
    val bentoGutter: Dp = 12.dp,
    val listRowGap: Dp = 20.dp,
    val sectionHeaderGap: Dp = 20.dp,
    val sectionGap: Dp = 24.dp,
    val screenEdge: Dp = 24.dp,
    val dialogPadding: Dp = 32.dp,
    val heroGap: Dp = 36.dp,
)

/**
 * Sizing and touch-target tokens. minTouchTarget 48dp is a hard floor on all form factors;
 * the Pointer profile reduces visual control heights but keeps 48dp hit areas
 * (Modifier.minimumInteractiveComponentSize()).
 */
@Immutable
data class YoloSizing(
    val minTouchTarget: Dp = 48.dp,
    val controlHeight: Dp = 52.dp,
    val inputHeight: Dp = 56.dp,
    val iconSmall: Dp = 16.dp,
    val icon: Dp = 24.dp,
    val iconLarge: Dp = 32.dp,
    val habitRing: Dp = 64.dp,
    val ringStroke: Dp = 6.dp,
    val navBarHeight: Dp = 80.dp,
    val railWidth: Dp = 80.dp,
    val drawerWidth: Dp = 280.dp,
    val maxFormWidth: Dp = 480.dp,
    val maxReadingWidth: Dp = 640.dp,
    val maxFeedWidth: Dp = 1080.dp,
    val hairline: Dp = 1.dp,
)

internal val LocalYoloSpacing = staticCompositionLocalOf { YoloSpacing() }
internal val LocalYoloSizing = staticCompositionLocalOf { YoloSizing() }

internal fun spacingFor(minWidthDp: Int): YoloSpacing = when {
    minWidthDp >= 1200 -> YoloSpacing(
        cardPadding = 20.dp,
        bentoGutter = 16.dp,
        sectionGap = 32.dp,
        screenEdge = 40.dp,
        heroGap = 48.dp,
    )

    minWidthDp >= 600 -> YoloSpacing(
        screenEdge = 32.dp,
    )

    else -> YoloSpacing()
}

internal fun sizingFor(minWidthDp: Int, profile: YoloInputProfile): YoloSizing {
    val pointer = when (profile) {
        YoloInputProfile.Pointer -> true
        YoloInputProfile.Touch -> false
        YoloInputProfile.Auto -> minWidthDp >= 1200
    }
    return if (pointer) {
        YoloSizing(
            controlHeight = 40.dp,
            inputHeight = 44.dp,
            habitRing = 56.dp,
            ringStroke = 5.dp,
        )
    } else {
        YoloSizing()
    }
}
