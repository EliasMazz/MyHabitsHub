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

// Clean redesign: the three color-worlds are RETIRED — every tab shares one neutral world that
// equals the M3 scheme (accent = primary, container = secondaryContainer, nav = secondary family,
// flat wash = surface). Identity now comes from icons + content, not per-tab hue.
private val NeutralSectionLight = sectionColors(
    accent = Color(0xFF121417),
    onAccent = Color(0xFFFFFFFF),
    accentContainer = Color(0xFFF2F3F5),
    onAccentContainer = Color(0xFF121417),
    surfaceTintWash = Color(0xFFFFFFFF),
    sheetSurface = Color(0xFFFFFFFF),
    onSheetSurface = Color(0xFF121417),
    onSheetSurfaceVariant = Color(0xFF6B7582),
    sheetIconChip = Color(0xFFF2F3F5),
    onSheetIconChip = Color(0xFF121417),
    navIndicator = Color(0xFFF2F3F5),
    onNavIndicator = Color(0xFF121417),
    navSelectedText = Color(0xFF121417),
)

private val NeutralSectionDark = sectionColors(
    accent = Color(0xFFF4F5F6),
    onAccent = Color(0xFF121417),
    accentContainer = Color(0xFF1D2227),
    onAccentContainer = Color(0xFFF4F5F6),
    surfaceTintWash = Color(0xFF121417),
    sheetSurface = Color(0xFF121417),
    onSheetSurface = Color(0xFFF4F5F6),
    onSheetSurfaceVariant = Color(0xFF8E969E),
    sheetIconChip = Color(0xFF1D2227),
    onSheetIconChip = Color(0xFFF4F5F6),
    navIndicator = Color(0xFF1D2227),
    onNavIndicator = Color(0xFFF4F5F6),
    navSelectedText = Color(0xFFF4F5F6),
)

val TrackingSectionLight = NeutralSectionLight
val ProgressSectionLight = NeutralSectionLight
val SettingsSectionLight = NeutralSectionLight
val TrackingSectionDark = NeutralSectionDark
val ProgressSectionDark = NeutralSectionDark
val SettingsSectionDark = NeutralSectionDark

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
