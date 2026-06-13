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
 * Per-habit categorical accent. In the clean (monochrome) system habit identity is carried by
 * the ICON, not color, so all eight slots resolve to the same neutral pair.
 */
@Immutable
data class YoloHabitAccent(
    val accent: Color,
    val onAccent: Color,
    val container: Color,
    val onContainer: Color,
)

/**
 * Extended tokens — the one global CompositionLocal beyond MaterialTheme. Clean monochrome
 * system (Figma team-library jXo8JKNjTc1sfBQmcxT9UJ, nodes 3311:2 light / 3311:135 dark): only
 * `success` keeps hue; everything else is ink / slate / neutral-gray. Trimmed to the tokens
 * actually consumed. ContrastTest locks the WCAG floors.
 */
@Immutable
data class ExtendedColors(
    val isDark: Boolean,
    val success: Color,              // positive/validation accent (checkmarks, valid fields)
    val info: Color,                 // content on [infoContainer] (dialogs)
    val infoContainer: Color,
    // Text aliases
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,
    // Disabled / surfaces
    val disabledFill: Color,
    val disabledOutline: Color,
    val surfaceHigher: Color,
    val secondaryFill: Color,
    // Per-habit categorical palette — 8 neutral quads
    val habitAccents: List<YoloHabitAccent>,
)

// ── Clean monochrome palette (Figma 3311:2 / 3311:135) ─────────────────────────
// Light — white surfaces, near-black ink, slightly-cool slate, neutral grays.
private val LWhite = Color(0xFFFFFFFF)
private val LInk = Color(0xFF121417)
private val LSlate = Color(0xFF6B7582)
private val LSlateDim = Color(0xFF8B9199)
private val LGray50 = Color(0xFFF7F8F9)
private val LGray100 = Color(0xFFF2F3F5)
private val LGray150 = Color(0xFFECEDEF)
private val LGray200 = Color(0xFFE7E9EB)
private val LOutlineVar = Color(0xFFDEE0E3) // Figma divider/border/checkbox

// Dark — near-black surfaces, near-white ink, cool slate, neutral dark grays.
private val DBase = Color(0xFF121417)
private val DInk = Color(0xFFF4F5F6)
private val DSlate = Color(0xFF8E969E)
private val DSlateDim = Color(0xFF6E767E)
private val DLowest = Color(0xFF0D0F11)
private val DGrayLow = Color(0xFF17191C)
private val DGray = Color(0xFF1D2227)
private val DGrayHigh = Color(0xFF2A2E33)
private val DGrayHighest = Color(0xFF32373D)
private val DOutline = Color(0xFF46474A)

// Functional hues — the only color the clean system keeps (status + critical feedback).
private val LError = Color(0xFFB3261E)
private val LErrorContainer = Color(0xFFF9DEDC)
private val LOnErrorContainer = Color(0xFF410E0B)
private val DError = Color(0xFFF2B8B5)
private val DOnError = Color(0xFF601410)
private val DErrorContainer = Color(0xFF8C1D18)
private val LSuccess = Color(0xFF1E7B34)
private val DSuccess = Color(0xFF6FD58A)

private val LightNeutralAccent = YoloHabitAccent(LInk, LWhite, LGray100, LInk)
private val DarkNeutralAccent = YoloHabitAccent(DInk, DBase, DGray, DInk)
val LightHabitAccents = List(8) { LightNeutralAccent }
val DarkHabitAccents = List(8) { DarkNeutralAccent }

val LightExtendedColors = ExtendedColors(
    isDark = false,
    success = LSuccess,
    info = LInk,
    infoContainer = LGray150,
    textPrimary = LInk,
    textSecondary = LSlate,
    textTertiary = LSlateDim,
    textPlaceholder = LSlate,
    textDisabled = Color(0x61121417), // onSurface @38%
    disabledFill = Color(0x1F121417), // onSurface @12%
    disabledOutline = Color(0x1F121417),
    surfaceHigher = LWhite,
    secondaryFill = LGray100,
    habitAccents = LightHabitAccents,
)

val DarkExtendedColors = ExtendedColors(
    isDark = true,
    success = DSuccess,
    info = DInk,
    infoContainer = DGrayHigh,
    textPrimary = DInk,
    textSecondary = DSlate,
    textTertiary = DSlateDim,
    textPlaceholder = DSlate,
    textDisabled = Color(0x61F4F5F6),
    disabledFill = Color(0x1FF4F5F6),
    disabledOutline = Color(0x1FF4F5F6),
    surfaceHigher = DGrayHigh,
    secondaryFill = DGray,
    habitAccents = DarkHabitAccents,
)

val LightColorScheme = lightColorScheme(
    primary = LInk,
    onPrimary = LWhite,
    primaryContainer = LGray150,
    onPrimaryContainer = LInk,
    inversePrimary = LWhite,
    secondary = LSlate,
    onSecondary = LWhite,
    secondaryContainer = LGray100,
    onSecondaryContainer = LInk,
    tertiary = LSlate,
    onTertiary = LWhite,
    tertiaryContainer = LGray100,
    onTertiaryContainer = LInk,
    error = LError,
    onError = LWhite,
    errorContainer = LErrorContainer,
    onErrorContainer = LOnErrorContainer,
    background = LWhite,
    onBackground = LInk,
    surface = LWhite,
    onSurface = LInk,
    surfaceVariant = LGray100,
    onSurfaceVariant = LSlate,
    surfaceDim = LGray150,
    surfaceBright = LWhite,
    surfaceContainerLowest = LWhite,
    surfaceContainerLow = LGray50,
    surfaceContainer = LGray100,
    surfaceContainerHigh = LGray150,
    surfaceContainerHighest = LGray200,
    outline = LSlateDim,
    outlineVariant = LOutlineVar,
    inverseSurface = LInk,
    inverseOnSurface = Color(0xFFF4F5F6),
    scrim = Color.Black,
    surfaceTint = LInk,
)

val DarkColorScheme = darkColorScheme(
    primary = DInk,
    onPrimary = DBase,
    primaryContainer = DGrayHigh,
    onPrimaryContainer = DInk,
    inversePrimary = LInk,
    secondary = DSlate,
    onSecondary = DBase,
    secondaryContainer = DGray,
    onSecondaryContainer = DInk,
    tertiary = DSlate,
    onTertiary = DBase,
    tertiaryContainer = DGray,
    onTertiaryContainer = DInk,
    error = DError,
    onError = DOnError,
    errorContainer = DErrorContainer,
    onErrorContainer = LErrorContainer,
    background = DBase,
    onBackground = DInk,
    surface = DBase,
    onSurface = DInk,
    surfaceVariant = DGray,
    onSurfaceVariant = DSlate,
    surfaceDim = DBase,
    surfaceBright = DGrayHigh,
    surfaceContainerLowest = DLowest,
    surfaceContainerLow = DGrayLow,
    surfaceContainer = DGray,
    surfaceContainerHigh = DGrayHigh,
    surfaceContainerHighest = DGrayHighest,
    outline = DSlateDim,
    outlineVariant = DOutline,
    inverseSurface = DInk,
    inverseOnSurface = DBase,
    scrim = Color.Black,
    surfaceTint = DInk,
)
