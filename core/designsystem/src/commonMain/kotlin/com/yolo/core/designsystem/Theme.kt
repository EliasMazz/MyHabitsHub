package com.yolo.core.designsystem

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

@Immutable
data class ExtendedColors(
    // Button states
    val primaryHover: Color,
    val destructiveHover: Color,
    val destructiveSecondaryOutline: Color,
    val disabledOutline: Color,
    val disabledFill: Color,
    val successOutline: Color,
    val success: Color,
    val onSuccess: Color,
    val secondaryFill: Color,

    // Text variants
    val textPrimary: Color,
    val textTertiary: Color,
    val textSecondary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,

    // Surface variants
    val surfaceLower: Color,
    val surfaceHigher: Color,
    val surfaceOutline: Color,
    val overlay: Color,

    // Accent colors
    val accentBlue: Color,
    val accentPurple: Color,
    val accentViolet: Color,
    val accentPink: Color,
    val accentOrange: Color,
    val accentYellow: Color,
    val accentGreen: Color,
    val accentTeal: Color,
    val accentLightBlue: Color,
    val accentGrey: Color,

    // Cake colors for chat bubbles
    val cakeViolet: Color,
    val cakeGreen: Color,
    val cakeBlue: Color,
    val cakePink: Color,
    val cakeOrange: Color,
    val cakeYellow: Color,
    val cakeTeal: Color,
    val cakePurple: Color,
    val cakeRed: Color,
    val cakeMint: Color,
)

val LightExtendedColors = ExtendedColors(
    primaryHover = YoloBrand600,
    destructiveHover = YoloRed600,
    destructiveSecondaryOutline = YoloRed200,
    disabledOutline = YoloBase200,
    disabledFill = YoloBase150,
    successOutline = YoloBrand100,
    success = YoloBrand600,
    onSuccess = YoloBase0,
    secondaryFill = YoloBase100,

    textPrimary = YoloBase1000,
    textTertiary = YoloBase800,
    textSecondary = YoloBase900,
    textPlaceholder = YoloBase700,
    textDisabled = YoloBase400,

    surfaceLower = YoloBase100,
    surfaceHigher = YoloBase100,
    surfaceOutline = YoloBase1000Alpha14,
    overlay = YoloBase1000Alpha80,

    accentBlue = YoloBlue,
    accentPurple = YoloPurple,
    accentViolet = YoloViolet,
    accentPink = YoloPink,
    accentOrange = YoloOrange,
    accentYellow = YoloYellow,
    accentGreen = YoloGreen,
    accentTeal = YoloTeal,
    accentLightBlue = YoloLightBlue,
    accentGrey = YoloGrey,

    cakeViolet = YoloCakeLightViolet,
    cakeGreen = YoloCakeLightGreen,
    cakeBlue = YoloCakeLightBlue,
    cakePink = YoloCakeLightPink,
    cakeOrange = YoloCakeLightOrange,
    cakeYellow = YoloCakeLightYellow,
    cakeTeal = YoloCakeLightTeal,
    cakePurple = YoloCakeLightPurple,
    cakeRed = YoloCakeLightRed,
    cakeMint = YoloCakeLightMint,
)

val DarkExtendedColors = ExtendedColors(
    primaryHover = YoloBrand600,
    destructiveHover = YoloRed600,
    destructiveSecondaryOutline = YoloRed200,
    disabledOutline = YoloBase900,
    disabledFill = YoloBase1000,
    successOutline = YoloBrand500Alpha40,
    success = YoloBrand500,
    onSuccess = YoloBase1000,
    secondaryFill = YoloBase900,

    textPrimary = YoloBase0,
    textTertiary = YoloBase200,
    textSecondary = YoloBase150,
    textPlaceholder = YoloBase400,
    textDisabled = YoloBase500,

    surfaceLower = YoloBase1000,
    surfaceHigher = YoloBase900,
    surfaceOutline = YoloBase100Alpha10Alt,
    overlay = YoloBase1000Alpha80,

    accentBlue = YoloBlue,
    accentPurple = YoloPurple,
    accentViolet = YoloViolet,
    accentPink = YoloPink,
    accentOrange = YoloOrange,
    accentYellow = YoloYellow,
    accentGreen = YoloGreen,
    accentTeal = YoloTeal,
    accentLightBlue = YoloLightBlue,
    accentGrey = YoloGrey,

    cakeViolet = YoloCakeDarkViolet,
    cakeGreen = YoloCakeDarkGreen,
    cakeBlue = YoloCakeDarkBlue,
    cakePink = YoloCakeDarkPink,
    cakeOrange = YoloCakeDarkOrange,
    cakeYellow = YoloCakeDarkYellow,
    cakeTeal = YoloCakeDarkTeal,
    cakePurple = YoloCakeDarkPurple,
    cakeRed = YoloCakeDarkRed,
    cakeMint = YoloCakeDarkMint,
)

val LightColorScheme = lightColorScheme(
    primary = YoloBrand500,
    onPrimary = YoloBrand1000,
    primaryContainer = YoloBrand100,
    onPrimaryContainer = YoloBrand900,

    secondary = YoloBase700,
    onSecondary = YoloBase0,
    secondaryContainer = YoloBase100,
    onSecondaryContainer = YoloBase900,

    tertiary = YoloBrand900,
    onTertiary = YoloBase0,
    tertiaryContainer = YoloBrand100,
    onTertiaryContainer = YoloBrand1000,

    error = YoloRed500,
    onError = YoloBase0,
    errorContainer = YoloRed200,
    onErrorContainer = YoloRed600,

    background = YoloBrand1000,
    onBackground = YoloBase0,
    surface = YoloBase0,
    onSurface = YoloBase1000,
    surfaceVariant = YoloBase100,
    onSurfaceVariant = YoloBase900,

    outline = YoloBase1000Alpha8,
    outlineVariant = YoloBase200,
)

val DarkColorScheme = darkColorScheme(
    primary = YoloBrand500,
    onPrimary = YoloBrand1000,
    primaryContainer = YoloBrand900,
    onPrimaryContainer = YoloBrand500,

    secondary = YoloBase400,
    onSecondary = YoloBase1000,
    secondaryContainer = YoloBase900,
    onSecondaryContainer = YoloBase150,

    tertiary = YoloBrand500,
    onTertiary = YoloBase1000,
    tertiaryContainer = YoloBrand900,
    onTertiaryContainer = YoloBrand500,

    error = YoloRed500,
    onError = YoloBase0,
    errorContainer = YoloRed600,
    onErrorContainer = YoloRed200,

    background = YoloBase1000,
    onBackground = YoloBase0,
    surface = YoloBase950,
    onSurface = YoloBase0,
    surfaceVariant = YoloBase900,
    onSurfaceVariant = YoloBase150,

    outline = YoloBase100Alpha10,
    outlineVariant = YoloBase800,
)