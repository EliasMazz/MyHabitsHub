package com.yolo.core.designsystem.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Nocturne motion tokens. Durations in ms. Springs are for the reward register only
 * (check-in, ring fill, numeral tick-up) — navigation never springs.
 * celebration 600ms is a hard cap for everyday check-ins; milestone set-pieces 1200ms max.
 *
 * [reducedMotion]: when true, components replace springs/particles/aura-drift with 150ms
 * fades (haptics kept). Plumbed as a YoloTheme parameter; platform entry points pass the
 * OS setting (Android: animator duration scale == 0, iOS: UIAccessibilityIsReduceMotionEnabled).
 */
@Immutable
data class YoloMotion(
    val instant: Int = 80,
    val quick: Int = 150,
    val standard: Int = 250,
    val gentle: Int = 350,
    val emphasized: Int = 450,
    val celebration: Int = 600,
    val easingStandard: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f),
    val easingDecelerate: Easing = CubicBezierEasing(0f, 0f, 0f, 1f),
    val easingAccelerate: Easing = CubicBezierEasing(0.3f, 0f, 1f, 1f),
    val easingEmphasizedDecelerate: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f),
    val easingEmphasizedAccelerate: Easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f),
    val pressSpring: SpringSpec<Float> = spring(dampingRatio = 0.9f, stiffness = 1200f),
    val settleSpring: SpringSpec<Float> = spring(dampingRatio = 0.65f, stiffness = 380f),
    val celebrateSpring: SpringSpec<Float> = spring(dampingRatio = 0.55f, stiffness = 220f),
    val reducedMotion: Boolean = false,
)

internal val LocalYoloMotion = staticCompositionLocalOf { YoloMotion() }
