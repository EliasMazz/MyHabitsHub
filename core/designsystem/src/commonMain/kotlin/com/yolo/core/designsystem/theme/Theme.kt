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
    YoloHabitAccent(YoloBlue700, YoloInk0, YoloCyan100, YoloCyan800), // blue
    YoloHabitAccent(YoloBrand800, YoloInk0, YoloBrand500, YoloBrand900), // teal
    YoloHabitAccent(YoloGreen700, YoloInk0, YoloSuccessContainerLight, YoloOnSuccessContainerLight), // green
    YoloHabitAccent(YoloViolet700, YoloInk0, YoloViolet200, YoloViolet700), // purple
    YoloHabitAccent(YoloPeach900, YoloInk0, YoloPeach100, YoloPeach900), // peach
    YoloHabitAccent(Color(0xFF96660A), YoloInk0, YoloAmber100, YoloAmber700), // amber
    YoloHabitAccent(YoloCyan700, YoloInk0, YoloCyan150, YoloCyan850), // cyan
    YoloHabitAccent(YoloPeriwinkle500, YoloPeriwinkleInk, YoloPeriwinkle300, YoloPeriwinkleInk), // periwinkle
)

val DarkHabitAccents = listOf(
    YoloHabitAccent(YoloBlue300, YoloBlueOnDark, YoloCyan800, YoloCyan100), // blue
    YoloHabitAccent(YoloBrand300, YoloBrand1000, YoloBrand700, YoloBrand300), // teal
    YoloHabitAccent(YoloGreen400, YoloGreen900, YoloGreenBandDark, YoloGreen400), // green
    YoloHabitAccent(YoloViolet300, YoloVioletDeep, YoloVioletDarkContainer, YoloViolet100), // purple
    YoloHabitAccent(YoloPeach400, YoloPeach900, YoloPeach900, YoloPeach100), // peach
    YoloHabitAccent(YoloAmber300, YoloAmber800, YoloAmber800, YoloAmber300), // amber
    YoloHabitAccent(YoloCyan300, YoloCyan900, YoloCyan850, YoloCyan150), // cyan
    YoloHabitAccent(YoloPeriwinkle300, YoloPeriwinkleInk, YoloBlueDeep, YoloBlue100), // periwinkle
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
    infoContainer = YoloCyan100,
    onInfoContainer = YoloCyan800,

    streak = YoloAmber700,
    onStreak = YoloInk0,
    streakContainer = YoloAmber100,
    onStreakContainer = YoloAmber700,
    celebration = YoloAmber500,
    progressTrack = YoloInk200,
    progressIndicator = YoloPeriwinkle500,
    habitComplete = YoloBrand600,
    habitMissed = YoloInk550,
    habitSkipped = YoloInk500,
    habitPending = YoloInk800,
    heatmapLevel0 = YoloInk250,
    heatmapLevel1 = YoloBrand100,
    heatmapLevel2 = YoloBrand500,
    heatmapLevel3 = YoloBrand600,
    heatmapLevel4 = YoloBrand800,

    heroSurface = YoloDeep400,
    onHeroSurface = YoloInkText,
    heroAccent = YoloBlue300,
    auraAmber = Color(0x2EE09F00),
    auraMint = Color(0x3341D5CA),
    elevatedCardOutline = Color.Transparent,

    textPrimary = YoloInk800,
    textSecondary = YoloInk600,
    textTertiary = YoloInk550,
    textPlaceholder = YoloInk550,
    textDisabled = Color(0x611F1F1F), // onSurface @38%

    primaryHover = YoloPrimaryHoverLight,
    destructiveHover = YoloRedDeep800,
    disabledFill = Color(0x1F1F1F1F), // onSurface @12%
    disabledOutline = Color(0x1F1F1F1F),
    overlay = Color(0xCC1F1F1F),

    surfaceLower = YoloInk150,
    surfaceHigher = YoloInk0,
    surfaceOutline = Color(0x141F1F1F), // 8% alpha hairline
    secondaryFill = YoloInk150,

    habitAccents = LightHabitAccents,
)

val DarkExtendedColors = ExtendedColors(
    isDark = true,

    success = YoloGreen400,
    onSuccess = YoloGreen900,
    successContainer = YoloGreenBandDark,
    onSuccessContainer = YoloGreen400,
    warning = YoloWarningDark,
    onWarning = YoloOnWarningDark,
    warningContainer = YoloWarningContainerDark,
    onWarningContainer = YoloOnWarningContainerDark,
    info = YoloBlue300,
    onInfo = YoloBlueOnDark,
    infoContainer = YoloCyan800,
    onInfoContainer = YoloCyan100,

    streak = YoloAmber300,
    onStreak = YoloAmber800,
    streakContainer = YoloAmberDeepContainer,
    onStreakContainer = YoloAmberContainerOn,
    celebration = YoloAmber300,
    progressTrack = YoloDeep50,
    progressIndicator = YoloPeriwinkle300,
    habitComplete = Color(0xFF43DCD0),
    habitMissed = YoloInk400,
    habitSkipped = YoloInk450,
    habitPending = YoloInkText,
    heatmapLevel0 = YoloDeep50,
    heatmapLevel1 = YoloBrand1000,
    heatmapLevel2 = YoloTealHeatmapDark3,
    heatmapLevel3 = Color(0xFF00867D),
    heatmapLevel4 = Color(0xFF43DCD0),

    heroSurface = YoloDeep400,
    onHeroSurface = YoloInkText,
    heroAccent = YoloBlue300,
    auraAmber = Color(0x1FFFBA2B),
    auraMint = Color(0x2943DCD0),
    elevatedCardOutline = Color(0x1AFFFFFF), // 10% alpha hairline

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
    surfaceHigher = YoloDeep200,
    surfaceOutline = Color(0x1AFFFFFF),
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
    surfaceVariant = YoloInk250,
    onSurfaceVariant = YoloInk600,
    surfaceDim = YoloInk250,
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
