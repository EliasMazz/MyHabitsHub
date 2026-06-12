package com.yolo.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * WCAG 2.2 contrast gate for the Nocturne palette (docs/design/theme-spec.md §2.7).
 * Locks every number in the spec: text pairs >= 4.5:1, component/large-text pairs >= 3:1,
 * both modes, including the 8 habit accent quads and habit state tokens against every fill
 * §6.2 actually places them on (surface, card = surfaceHigher, bento tile = surfaceContainer).
 * Exempt by spec: celebration, outlineVariant, aura*, disabled*, heatmap level adjacency.
 */
class ContrastTest {

    private fun channel(c: Float): Double {
        val v = c.toDouble()
        return if (v <= 0.04045) v / 12.92 else ((v + 0.055) / 1.055).pow(2.4)
    }

    private fun luminance(color: Color): Double =
        0.2126 * channel(color.red) + 0.7152 * channel(color.green) + 0.0722 * channel(color.blue)

    private fun ratio(foreground: Color, background: Color): Double {
        val fg = if (foreground.alpha < 1f) foreground.compositeOver(background) else foreground
        val l1 = luminance(fg)
        val l2 = luminance(background)
        return (maxOf(l1, l2) + 0.05) / (minOf(l1, l2) + 0.05)
    }

    private fun assertContrast(fg: Color, bg: Color, floor: Double, label: String) {
        val r = ratio(fg, bg)
        val rounded = (r * 100).toInt() / 100.0
        assertTrue(r >= floor, "$label: $rounded:1 < $floor:1")
    }

    private fun checkScheme(scheme: ColorScheme, mode: String) {
        // Text pairs (4.5:1)
        assertContrast(scheme.onPrimary, scheme.primary, 4.5, "$mode onPrimary/primary")
        assertContrast(scheme.onPrimaryContainer, scheme.primaryContainer, 4.5, "$mode onPrimaryContainer/primaryContainer")
        assertContrast(scheme.onSecondary, scheme.secondary, 4.5, "$mode onSecondary/secondary")
        assertContrast(scheme.onSecondaryContainer, scheme.secondaryContainer, 4.5, "$mode onSecondaryContainer/secondaryContainer")
        assertContrast(scheme.onTertiaryContainer, scheme.tertiaryContainer, 4.5, "$mode onTertiaryContainer/tertiaryContainer")
        assertContrast(scheme.onError, scheme.error, 4.5, "$mode onError/error")
        assertContrast(scheme.onErrorContainer, scheme.errorContainer, 4.5, "$mode onErrorContainer/errorContainer")
        assertContrast(scheme.onBackground, scheme.background, 4.5, "$mode onBackground/background")
        assertContrast(scheme.onSurface, scheme.surface, 4.5, "$mode onSurface/surface")
        assertContrast(scheme.onSurfaceVariant, scheme.surface, 4.5, "$mode onSurfaceVariant/surface")
        assertContrast(scheme.inverseOnSurface, scheme.inverseSurface, 4.5, "$mode inverseOnSurface/inverseSurface")
        // onSurface must survive every container fill
        assertContrast(scheme.onSurface, scheme.surfaceContainerLowest, 4.5, "$mode onSurface/surfaceContainerLowest")
        assertContrast(scheme.onSurface, scheme.surfaceContainerLow, 4.5, "$mode onSurface/surfaceContainerLow")
        assertContrast(scheme.onSurface, scheme.surfaceContainer, 4.5, "$mode onSurface/surfaceContainer")
        assertContrast(scheme.onSurface, scheme.surfaceContainerHigh, 4.5, "$mode onSurface/surfaceContainerHigh")
        assertContrast(scheme.onSurface, scheme.surfaceContainerHighest, 4.5, "$mode onSurface/surfaceContainerHighest")
        assertContrast(scheme.onSurface, scheme.surfaceBright, 4.5, "$mode onSurface/surfaceBright")
        // Large-text/icon/component pairs (3:1)
        assertContrast(scheme.onTertiary, scheme.tertiary, 3.0, "$mode onTertiary/tertiary (large/icon)")
        assertContrast(scheme.primary, scheme.surface, 3.0, "$mode primary/surface")
        assertContrast(scheme.tertiary, scheme.surface, 3.0, "$mode tertiary/surface")
        assertContrast(scheme.error, scheme.surface, 3.0, "$mode error/surface")
        assertContrast(scheme.outline, scheme.surface, 3.0, "$mode outline/surface")
    }

    private fun checkExtended(x: ExtendedColors, scheme: ColorScheme, mode: String) {
        val surface = scheme.surface
        val card = x.surfaceHigher
        val tile = scheme.surfaceContainer

        // Status pairs (text on fills, 4.5:1)
        assertContrast(x.onSuccess, x.success, 4.5, "$mode onSuccess/success")
        assertContrast(x.onSuccessContainer, x.successContainer, 4.5, "$mode onSuccessContainer/successContainer")
        assertContrast(x.onWarning, x.warning, 4.5, "$mode onWarning/warning")
        assertContrast(x.onWarningContainer, x.warningContainer, 4.5, "$mode onWarningContainer/warningContainer")
        assertContrast(x.onInfo, x.info, 4.5, "$mode onInfo/info")
        assertContrast(x.onInfoContainer, x.infoContainer, 4.5, "$mode onInfoContainer/infoContainer")
        assertContrast(x.onStreak, x.streak, 4.5, "$mode onStreak/streak")
        assertContrast(x.onStreakContainer, x.streakContainer, 4.5, "$mode onStreakContainer/streakContainer")
        assertContrast(x.onHeroSurface, x.heroSurface, 4.5, "$mode onHeroSurface/heroSurface")
        assertContrast(x.heroAccent, x.heroSurface, 3.0, "$mode heroAccent/heroSurface")

        // Text aliases
        assertContrast(x.textPrimary, surface, 4.5, "$mode textPrimary/surface")
        assertContrast(x.textSecondary, surface, 4.5, "$mode textSecondary/surface")
        assertContrast(x.textTertiary, surface, 3.0, "$mode textTertiary/surface (large/icon only)")
        assertContrast(x.textPrimary, card, 4.5, "$mode textPrimary/card")
        assertContrast(x.textSecondary, card, 4.5, "$mode textSecondary/card")
        // Placeholder vs actual input fills (NOT exempt per spec)
        val inputFill = if (x.isDark) scheme.surfaceContainerHighest else scheme.surfaceContainerLow
        assertContrast(x.textPlaceholder, inputFill, 4.5, "$mode textPlaceholder/inputFill")

        // Habit states vs every placement fill (3:1 floor) — closes the audit blind spot
        for ((fillName, fill) in listOf("surface" to surface, "card" to card, "tile" to tile)) {
            assertContrast(x.habitMissed, fill, 3.0, "$mode habitMissed/$fillName")
            assertContrast(x.habitSkipped, fill, 3.0, "$mode habitSkipped/$fillName")
            assertContrast(x.habitPending, fill, 3.0, "$mode habitPending/$fillName")
            assertContrast(x.habitComplete, fill, 3.0, "$mode habitComplete/$fillName")
        }
        assertContrast(x.progressIndicator, x.progressTrack, 3.0, "$mode progressIndicator/progressTrack")

        // 8 habit accent quads
        x.habitAccents.forEachIndexed { i, quad ->
            assertContrast(quad.accent, surface, 3.0, "$mode habitAccent[$i].accent/surface")
            assertContrast(quad.onAccent, quad.accent, 4.5, "$mode habitAccent[$i].onAccent/accent")
            assertContrast(quad.onContainer, quad.container, 4.5, "$mode habitAccent[$i].onContainer/container")
        }
        assertTrue(x.habitAccents.size == 8, "$mode habitAccents must have exactly 8 entries")
    }

    @Test
    fun lightSchemeMeetsWcag() = checkScheme(LightColorScheme, "light")

    @Test
    fun darkSchemeMeetsWcag() = checkScheme(DarkColorScheme, "dark")

    @Test
    fun lightExtendedMeetsWcag() = checkExtended(LightExtendedColors, LightColorScheme, "light")

    @Test
    fun darkExtendedMeetsWcag() = checkExtended(DarkExtendedColors, DarkColorScheme, "dark")

    // Section Color Worlds gate (docs/design/section-color-worlds.md §8):
    // 14 pairs x 6 palettes + cross-world streak/error/habitPending pairs + alias invariants.
    private fun checkSection(section: YoloSection, isDark: Boolean) {
        val mode = "${section.name}-${if (isDark) "dark" else "light"}"
        val s = section.colors(isDark)
        val scheme = if (isDark) DarkColorScheme else LightColorScheme
        val x = if (isDark) DarkExtendedColors else LightExtendedColors

        assertContrast(s.onAccent, s.accent, 4.5, "$mode onAccent/accent")
        assertContrast(s.onAccentContainer, s.accentContainer, 4.5, "$mode onAccentContainer/accentContainer")
        assertContrast(s.onSheetSurface, s.sheetSurface, 4.5, "$mode onSheetSurface/sheetSurface")
        assertContrast(s.onSheetSurfaceVariant, s.sheetSurface, 4.5, "$mode onSheetSurfaceVariant/sheetSurface")
        assertContrast(s.onSheetIconChip, s.sheetIconChip, 3.0, "$mode onSheetIconChip/sheetIconChip")
        assertContrast(s.onNavIndicator, s.navIndicator, 3.0, "$mode onNavIndicator/navIndicator")
        assertContrast(s.navSelectedText, scheme.surfaceContainer, 4.5, "$mode navSelectedText/navContainer")
        assertContrast(scheme.onSurface, s.surfaceTintWash, 4.5, "$mode onSurface/surfaceTintWash")
        assertContrast(scheme.onSurfaceVariant, s.surfaceTintWash, 4.5, "$mode onSurfaceVariant/surfaceTintWash")
        assertContrast(s.accent, s.surfaceTintWash, 3.0, "$mode accent/surfaceTintWash")
        assertContrast(s.accent, s.sheetSurface, 3.0, "$mode accent/sheetSurface")
        assertContrast(s.accent, scheme.surface, 3.0, "$mode accent/surface")
        assertContrast(scheme.error, s.sheetSurface, 4.5, "$mode error/sheetSurface")
        assertContrast(scheme.onSurfaceVariant, s.sheetSurface, 4.5, "$mode onSurfaceVariant/sheetSurface")

        // Cross-world: globals must survive every world's fills
        assertContrast(scheme.tertiary, s.sheetSurface, 3.0, "$mode streak/sheetSurface")
        assertContrast(scheme.error, s.surfaceTintWash, 4.5, "$mode error/surfaceTintWash")
        assertContrast(x.habitPending, s.surfaceTintWash, 3.0, "$mode habitPending/surfaceTintWash")

        // Structural alias invariants (spec §3.1)
        assertTrue(s.navIndicator == s.accentContainer, "$mode navIndicator must alias accentContainer")
        assertTrue(s.onNavIndicator == s.onAccentContainer, "$mode onNavIndicator must alias onAccentContainer")
        assertTrue(s.navSelectedText == s.accent, "$mode navSelectedText must alias accent")
        assertTrue(
            s.sheetDragHandle == s.onSheetSurfaceVariant.copy(alpha = 0.40f),
            "$mode sheetDragHandle must be onSheetSurfaceVariant @40% alpha"
        )
    }

    @Test
    fun sectionWorldsMeetWcag() {
        for (section in YoloSection.entries) {
            checkSection(section, isDark = false)
            checkSection(section, isDark = true)
        }
    }

    @Test
    fun trackingWorldStaysBrandIdentical() {
        // design-system-v3-spec §8: the reference app's global accent IS the Today blue, so the Tracking
        // world's primary roles are byte-identical to the base scheme by construction.
        assertTrue(TrackingSectionLight.accent == LightColorScheme.primary, "Tracking light accent == base primary")
        assertTrue(TrackingSectionDark.accent == DarkColorScheme.primary, "Tracking dark accent == base primary")
        assertTrue(TrackingSectionLight.accentContainer == LightColorScheme.primaryContainer, "Tracking light container == base primaryContainer")
    }

    @Test
    fun darkOutlineBannedOnElevatedContainers() {
        // Spec hard rule 5: dark outline fails 3:1 on elevated containers — interactive borders
        // there must use onSurfaceVariant. This test documents the constraint stays true.
        assertContrast(
            DarkColorScheme.onSurfaceVariant, DarkColorScheme.surfaceContainerHighest,
            3.0, "dark onSurfaceVariant/surfaceContainerHighest (outline replacement)"
        )
    }
}
