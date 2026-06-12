package com.yolo.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val ColorScheme.extended: ExtendedColors
    @ReadOnlyComposable
    @Composable
    get() = LocalExtendedColors.current

/**
 * Per-habit categorical accent (8 hue families present in the reference app imagery, see
 * [ExtendedColors.habitAccents]). accent = rings/checks/chart lines/icons; container = card
 * tints/chips. Habit identity always carries color PLUS icon — never color alone.
 */
@Immutable
data class YoloHabitAccent(
    val accent: Color,
    val onAccent: Color,
    val container: Color,
    val onContainer: Color,
)

/**
 * Extended tokens — the ONE global CompositionLocal beyond MaterialTheme.
 * Values: docs/design/design-system-v3-spec.md §3 (pixel-measured / GM-token); ContrastTest locks them.
 */
@Immutable
data class ExtendedColors(
    val isDark: Boolean,

    // Status (M3 has only error)
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
    val info: Color,
    val onInfo: Color,
    val infoContainer: Color,
    val onInfoContainer: Color,

    // Habit domain
    val streak: Color,
    val onStreak: Color,
    val streakContainer: Color,
    val onStreakContainer: Color,
    val celebration: Color,
    val progressTrack: Color,
    val progressIndicator: Color,
    val habitComplete: Color,
    val habitMissed: Color,
    val habitSkipped: Color,
    val habitPending: Color,
    val heatmapLevel0: Color,
    val heatmapLevel1: Color,
    val heatmapLevel2: Color,
    val heatmapLevel3: Color,
    val heatmapLevel4: Color,

    // Brand moments
    val heroSurface: Color,
    val onHeroSurface: Color,
    val heroAccent: Color,
    val auraAmber: Color,
    val auraMint: Color,
    val elevatedCardOutline: Color,

    // Text aliases (migration bridge)
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,

    // Interaction & disabled
    val primaryHover: Color,
    val destructiveHover: Color,
    val disabledFill: Color,
    val disabledOutline: Color,
    val overlay: Color,

    // Surfaces (bridge)
    val surfaceLower: Color,
    val surfaceHigher: Color,
    val surfaceOutline: Color,
    val secondaryFill: Color,

    // Per-habit categorical palette — exactly 8
    val habitAccents: List<YoloHabitAccent>,
)

// 8 hue families measured in the reference app imagery: blue, teal, green, purple, pink, amber, cyan, indigo.
val LightHabitAccents = listOf(
    YoloHabitAccent(YoloBlue700, YoloInk0, YoloBlue100, YoloBlueDeep), // blue
    YoloHabitAccent(YoloBrand800, YoloInk0, YoloBrand100, YoloBrand700), // teal
    YoloHabitAccent(YoloGreen700, YoloInk0, YoloSuccessContainerLight, YoloOnSuccessContainerLight), // green
    YoloHabitAccent(YoloViolet700, YoloInk0, YoloViolet100, YoloViolet700), // purple
    YoloHabitAccent(Color(0xFFC04373), YoloInk0, Color(0xFFFFD8E4), Color(0xFF8E2452)), // pink (AA-fix of #D45988)
    YoloHabitAccent(Color(0xFF96660A), YoloInk0, YoloAmber100, YoloAmber700), // amber
    YoloHabitAccent(YoloCyan700, YoloInk0, YoloCyan100, YoloCyan800), // cyan
    YoloHabitAccent(Color(0xFF4A1982), YoloInk0, Color(0xFFE8DEF8), Color(0xFF4A1982)), // indigo (hypnogram Deep)
)

val DarkHabitAccents = listOf(
    YoloHabitAccent(YoloBlue300, YoloBlueOnDark, YoloBlueDeep, YoloBlue100), // blue
    YoloHabitAccent(YoloBrand300, YoloBrand1000, YoloBrand900, YoloBrand300), // teal
    YoloHabitAccent(YoloGreenOnContainerDark, Color(0xFF00381F), YoloGreenContainerDark, YoloGreenOnContainerDark), // green
    YoloHabitAccent(Color(0xFFEEDCFF), Color(0xFF421379), YoloVioletDarkContainer, YoloVioletContainerOn), // purple
    YoloHabitAccent(Color(0xFFFF9CBB), Color(0xFF5D1135), Color(0xFF702342), Color(0xFFFFD8E4)), // pink (hypnogram Awake)
    YoloHabitAccent(YoloAmber300, YoloAmber800, YoloAmber800, YoloAmber300), // amber
    YoloHabitAccent(Color(0xFFBDEAFF), YoloCyan900, YoloCyan800, YoloCyan100), // cyan (REM)
    YoloHabitAccent(YoloViolet300, YoloVioletDeep, Color(0xFF4A1982), YoloViolet100), // indigo
)

val LightExtendedColors = ExtendedColors(
    isDark = false,

    success = YoloGreen700,
    onSuccess = YoloInk0,
    successContainer = YoloSuccessContainerLight,
    onSuccessContainer = YoloOnSuccessContainerLight,
    warning = YoloWarningLight,
    onWarning = YoloInk0,
    warningContainer = YoloWarningContainerLight,
    onWarningContainer = YoloOnWarningContainerLight,
    info = YoloBlue700,
    onInfo = YoloInk0,
    infoContainer = YoloBlue100,
    onInfoContainer = YoloBlueDeep,

    streak = YoloAmber700,
    onStreak = YoloInk0,
    streakContainer = YoloAmber100,
    onStreakContainer = YoloAmber700,
    celebration = YoloAmber300,
    progressTrack = YoloInk200,
    progressIndicator = YoloBlueData500,
    habitComplete = YoloBrand600,
    habitMissed = YoloInk550,
    habitSkipped = YoloInk500,
    habitPending = YoloInk800,
    heatmapLevel0 = YoloInk200,
    heatmapLevel1 = YoloBrand100,
    heatmapLevel2 = YoloBrand500,
    heatmapLevel3 = YoloBrand600,
    heatmapLevel4 = YoloBrand800,

    heroSurface = YoloDeep400,
    onHeroSurface = YoloInkText,
    heroAccent = YoloBlue300,
    auraAmber = Color(0x2EFDDEA4),
    auraMint = Color(0x3341DDD0),
    elevatedCardOutline = Color.Transparent,

    textPrimary = YoloInk800,
    textSecondary = YoloInk600,
    textTertiary = YoloInk550,
    textPlaceholder = YoloInk550,
    textDisabled = Color(0x611A1C1E), // onSurface @38%

    primaryHover = YoloPrimaryHoverLight,
    destructiveHover = YoloRedDeep800,
    disabledFill = Color(0x1F1A1C1E), // onSurface @12%
    disabledOutline = Color(0x1F1A1C1E),
    overlay = Color(0xCC1A1C1E),

    surfaceLower = YoloInk150,
    surfaceHigher = YoloInk0,
    surfaceOutline = Color(0x141A1C1E), // 8% alpha hairline
    secondaryFill = YoloInk150,

    habitAccents = LightHabitAccents,
)

val DarkExtendedColors = ExtendedColors(
    isDark = true,

    success = YoloGreen300,
    onSuccess = YoloGreen900,
    successContainer = YoloGreenContainerDark,
    onSuccessContainer = YoloGreenOnContainerDark,
    warning = YoloWarningDark,
    onWarning = YoloOnWarningDark,
    warningContainer = YoloWarningContainerDark,
    onWarningContainer = YoloOnWarningContainerDark,
    info = YoloBlue300,
    onInfo = YoloBlueOnDark,
    infoContainer = YoloBlueDeep,
    onInfoContainer = YoloBlue100,

    streak = YoloAmber300,
    onStreak = YoloAmber800,
    streakContainer = YoloAmberDeepContainer,
    onStreakContainer = YoloAmberContainerOn,
    celebration = YoloAmber300,
    progressTrack = YoloDeep100,
    progressIndicator = YoloBlueData300,
    habitComplete = YoloBrand500,
    habitMissed = YoloInk400,
    habitSkipped = YoloInk450,
    habitPending = YoloInkText,
    heatmapLevel0 = YoloDeep100,
    heatmapLevel1 = YoloBrand1000,
    heatmapLevel2 = YoloBrand900,
    heatmapLevel3 = YoloTealHeatmapDark3,
    heatmapLevel4 = YoloBrand500,

    heroSurface = YoloDeep400,
    onHeroSurface = YoloInkText,
    heroAccent = YoloBlue300,
    auraAmber = Color(0x1FFDDEA4),
    auraMint = Color(0x2941DDD0),
    elevatedCardOutline = Color(0x1AFAF9F8), // 10% alpha hairline

    textPrimary = YoloInkText,
    textSecondary = YoloInk350,
    textTertiary = YoloInk400,
    textPlaceholder = YoloInk400,
    textDisabled = Color(0x61E3E3E3), // onSurface @38%

    primaryHover = YoloBlueHoverDark,
    destructiveHover = YoloRed200,
    disabledFill = Color(0x1FE3E3E3), // onSurface @12%
    disabledOutline = Color(0x1FE3E3E3),
    overlay = Color(0xCC0E0E0F),

    surfaceLower = YoloDeep500,
    surfaceHigher = YoloDeep100,
    surfaceOutline = Color(0x1AFAF9F8),
    secondaryFill = YoloDeep50,

    habitAccents = DarkHabitAccents,
)

val LightColorScheme = lightColorScheme(
    primary = YoloBlue700,
    onPrimary = YoloInk0,
    primaryContainer = YoloBlue100,
    onPrimaryContainer = YoloBlueDeep,
    inversePrimary = YoloBlue300,

    secondary = YoloCyan700,
    onSecondary = YoloInk0,
    secondaryContainer = YoloCyan100,
    onSecondaryContainer = YoloCyan800,

    tertiary = YoloGreen700,
    onTertiary = YoloInk0,
    tertiaryContainer = YoloGreen100,
    onTertiaryContainer = YoloGreen800,

    error = YoloRed700,
    onError = YoloInk0,
    errorContainer = YoloRed100,
    onErrorContainer = YoloRedDeep800,

    background = YoloInk50,
    onBackground = YoloInk800,
    surface = YoloInk50,
    onSurface = YoloInk800,
    surfaceVariant = YoloSurfaceVariantLight,
    onSurfaceVariant = YoloInk600,
    surfaceDim = YoloInk300,
    surfaceBright = YoloInk0,
    surfaceContainerLowest = YoloInk0,
    surfaceContainerLow = YoloInk100,
    surfaceContainer = YoloInk150,
    surfaceContainerHigh = YoloInk200,
    surfaceContainerHighest = YoloInk250,

    outline = YoloInk500,
    outlineVariant = YoloInk350,
    inverseSurface = YoloInk700,
    inverseOnSurface = YoloNeutral95,
    scrim = Color.Black,
    surfaceTint = YoloBlue700,
)

val DarkColorScheme = darkColorScheme(
    primary = YoloBlue300,
    onPrimary = YoloBlueOnDark,
    primaryContainer = YoloBlueDeep,
    onPrimaryContainer = YoloBlue100,
    inversePrimary = YoloBlue700,

    secondary = YoloCyan300,
    onSecondary = YoloCyan900,
    secondaryContainer = YoloCyan800,
    onSecondaryContainer = YoloCyan100,

    tertiary = YoloGreen300,
    onTertiary = YoloGreen900,
    tertiaryContainer = YoloGreen800,
    onTertiaryContainer = YoloGreen100,

    error = YoloRed300,
    onError = YoloRedDeep900,
    errorContainer = YoloRedDeep800,
    onErrorContainer = YoloRed100,

    background = YoloDeep400,
    onBackground = YoloInkText,
    surface = YoloDeep400,
    onSurface = YoloInkText,
    surfaceVariant = YoloInk600,
    onSurfaceVariant = YoloInk350,
    surfaceDim = YoloDeep400,
    surfaceBright = YoloDeep50,
    surfaceContainerLowest = YoloDeep500,
    surfaceContainerLow = YoloDeep300,
    surfaceContainer = YoloDeep200,
    surfaceContainerHigh = YoloDeep100,
    surfaceContainerHighest = YoloDeep50,

    outline = YoloInk450,
    outlineVariant = YoloInk600,
    inverseSurface = YoloInkText,
    inverseOnSurface = YoloInk700,
    scrim = Color.Black,
    surfaceTint = YoloBlue300,
)
