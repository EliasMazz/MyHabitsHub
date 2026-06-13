package com.yolo.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Section Color Worlds (Nocturne v2.1 addendum, docs/design/section-color-worlds.md).
 *
 * Each bottom-nav tab is a hue world: Tracking = Cobalt (the reference "Today"
 * lift), Progress = Emerald (the brand — near-identical to base Nocturne), Settings = Violet
 * (derived from the reference Sleep hue). Section hue colors chrome and surfaces only; habit
 * accents, the forgiveness palette, the amber streak channel and status colors stay global.
 */
enum class YoloSection { Tracking, Progress, Settings }

@Immutable
data class SectionColors(
    val accent: Color,
    val onAccent: Color,
    val accentContainer: Color,
    val onAccentContainer: Color,
    val surfaceTintWash: Color,
    val sheetSurface: Color,
    val onSheetSurface: Color,
    val onSheetSurfaceVariant: Color,
    val sheetIconChip: Color,
    val onSheetIconChip: Color,
    val navIndicator: Color,
    val onNavIndicator: Color,
    val navSelectedText: Color,
    val sheetDragHandle: Color,
)

private fun sectionColors(
    accent: Color,
    onAccent: Color,
    accentContainer: Color,
    onAccentContainer: Color,
    surfaceTintWash: Color,
    sheetSurface: Color,
    onSheetSurface: Color,
    onSheetSurfaceVariant: Color,
    sheetIconChip: Color,
    onSheetIconChip: Color,
    navIndicator: Color,
    onNavIndicator: Color,
    navSelectedText: Color,
) = SectionColors(
    accent = accent,
    onAccent = onAccent,
    accentContainer = accentContainer,
    onAccentContainer = onAccentContainer,
    surfaceTintWash = surfaceTintWash,
    sheetSurface = sheetSurface,
    onSheetSurface = onSheetSurface,
    onSheetSurfaceVariant = onSheetSurfaceVariant,
    sheetIconChip = sheetIconChip,
    onSheetIconChip = onSheetIconChip,
    // v4-D1: the nav chrome is GLOBAL (measured constant on every tab) — the trio equals
    // the M3 secondary family and is identical across worlds per mode.
    navIndicator = navIndicator,
    onNavIndicator = onNavIndicator,
    navSelectedText = navSelectedText,
    sheetDragHandle = onSheetSurfaceVariant.copy(alpha = 0.40f),
)

// Values per design-system-v3-spec.md §4. Dark mode: all worlds share the measured neutral
// canvas (#131314 wash / #1E1F20 sheet) — the reference app carries dark world identity via accents/chips
// only (§4.4). Tracking is the brand-identical world (the reference app's global accent IS Today blue).
val TrackingSectionLight = sectionColors(
    accent = YoloCobalt700,
    onAccent = YoloInk0,
    accentContainer = YoloCobalt150,
    onAccentContainer = YoloCyan800,
    surfaceTintWash = YoloCobaltWashLight,
    sheetSurface = YoloCobaltSheetLight,
    onSheetSurface = YoloInk800,
    onSheetSurfaceVariant = YoloCobaltVariantLight,
    sheetIconChip = YoloCobalt150,
    onSheetIconChip = YoloCyan800,
    navIndicator = YoloCyan100,
    onNavIndicator = YoloCyan800,
    navSelectedText = YoloCyan700,
)

val TrackingSectionDark = sectionColors(
    accent = YoloCobalt300,
    onAccent = YoloBlueOnDark,
    accentContainer = YoloCobaltDarkContainer,
    onAccentContainer = YoloCobaltContainerOn,
    surfaceTintWash = YoloCobaltWashDark,
    sheetSurface = YoloCobaltSheetDark,
    onSheetSurface = YoloInkText,
    onSheetSurfaceVariant = YoloCobaltVariantDark,
    sheetIconChip = YoloCobaltChipDark,
    onSheetIconChip = YoloCobaltContainerOn,
    navIndicator = YoloCyan800,
    onNavIndicator = YoloCyan100,
    navSelectedText = YoloCyan300,
)

val ProgressSectionLight = sectionColors(
    accent = YoloBrand800,
    onAccent = YoloInk0,
    accentContainer = YoloBrand500,
    onAccentContainer = YoloBrand900,
    surfaceTintWash = YoloBrandWashLight,
    sheetSurface = YoloBrandSheetLight,
    onSheetSurface = YoloInk800,
    onSheetSurfaceVariant = YoloBrandVariantLight,
    sheetIconChip = YoloBrandChipLight,
    onSheetIconChip = YoloBrand800,
    navIndicator = YoloCyan100,
    onNavIndicator = YoloCyan800,
    navSelectedText = YoloCyan700,
)

val ProgressSectionDark = sectionColors(
    accent = YoloBrand300,
    onAccent = YoloBrand1000,
    accentContainer = YoloBrand700,
    onAccentContainer = YoloBrand300,
    surfaceTintWash = YoloBrandWashDark,
    sheetSurface = YoloDeep200,
    onSheetSurface = YoloInkText,
    onSheetSurfaceVariant = YoloBrandVariantDark,
    sheetIconChip = YoloBrand200,
    onSheetIconChip = YoloBrand1000,
    navIndicator = YoloCyan800,
    onNavIndicator = YoloCyan100,
    navSelectedText = YoloCyan300,
)

val SettingsSectionLight = sectionColors(
    accent = YoloViolet700,
    onAccent = YoloInk0,
    accentContainer = YoloViolet100,
    onAccentContainer = YoloViolet700,
    surfaceTintWash = YoloVioletWashLight,
    sheetSurface = YoloVioletSheetLight,
    onSheetSurface = YoloInk800,
    onSheetSurfaceVariant = YoloVioletVariantLight,
    sheetIconChip = YoloViolet150,
    onSheetIconChip = YoloViolet700,
    navIndicator = YoloCyan100,
    onNavIndicator = YoloCyan800,
    navSelectedText = YoloCyan700,
)

val SettingsSectionDark = sectionColors(
    accent = YoloViolet300,
    onAccent = YoloVioletDeep,
    accentContainer = YoloVioletDarkContainer,
    onAccentContainer = YoloVioletContainerOn,
    surfaceTintWash = YoloVioletWashDark,
    sheetSurface = YoloVioletSheetDark,
    onSheetSurface = YoloInkText,
    onSheetSurfaceVariant = YoloVioletVariantDark,
    sheetIconChip = YoloVioletDeep,
    onSheetIconChip = YoloViolet100,
    navIndicator = YoloCyan800,
    onNavIndicator = YoloCyan100,
    navSelectedText = YoloCyan300,
)

fun YoloSection.colors(isDark: Boolean): SectionColors = when (this) {
    YoloSection.Tracking -> if (isDark) TrackingSectionDark else TrackingSectionLight
    YoloSection.Progress -> if (isDark) ProgressSectionDark else ProgressSectionLight
    YoloSection.Settings -> if (isDark) SettingsSectionDark else SettingsSectionLight
}

val LocalYoloSection = staticCompositionLocalOf { ProgressSectionLight }

val ColorScheme.section: SectionColors
    @Composable
    @ReadOnlyComposable
    get() = LocalYoloSection.current

/**
 * Wraps a tab subtree in its section world: provides [LocalYoloSection] and overlays
 * MaterialTheme's scheme (primary quartet, inversePrimary, secondaryContainer pair,
 * surfaceTint) so stock M3 components pick up the world automatically. The scheme is
 * remember-ed per (base, section) — nothing is built during scroll/animation frames.
 * Never animate this — the tab wash crossfade is a single animateColorAsState at the
 * chrome host, drawn in drawBehind (spec §3.6).
 */
@Composable
fun YoloSectionTheme(
    section: YoloSection,
    content: @Composable () -> Unit,
) {
    val isDark = MaterialTheme.colorScheme.extended.isDark
    val colors = section.colors(isDark)
    val inverseAccent = section.colors(!isDark).accent
    val base = MaterialTheme.colorScheme
    val scheme = remember(base, colors, inverseAccent) {
        base.copy(
            primary = colors.accent,
            onPrimary = colors.onAccent,
            primaryContainer = colors.accentContainer,
            onPrimaryContainer = colors.onAccentContainer,
            inversePrimary = inverseAccent,
            secondaryContainer = colors.navIndicator,
            onSecondaryContainer = colors.onNavIndicator,
            surfaceTint = colors.accent,
        )
    }
    CompositionLocalProvider(LocalYoloSection provides colors) {
        MaterialTheme(
            colorScheme = scheme,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            content = content,
        )
    }
}
