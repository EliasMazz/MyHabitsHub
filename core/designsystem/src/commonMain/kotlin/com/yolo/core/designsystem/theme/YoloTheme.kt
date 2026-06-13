package com.yolo.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Nocturne (Yolo Design Language v2) theme root. Pure commonMain — Android, iOS and desktop.
 *
 * @param darkTheme dark is the brand-canonical mode; light "paper" mode is co-equal.
 * @param inputProfile sizing idiom; Auto picks Pointer at >= 1200dp window width.
 * @param reducedMotion platform entry points pass the OS reduce-motion setting.
 */
@Composable
fun YoloTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    inputProfile: YoloInputProfile = YoloInputProfile.Auto,
    reducedMotion: Boolean = false,
    content: @Composable () -> Unit,
) {
    val minWidthDp = currentWindowAdaptiveInfo().windowSizeClass.minWidthDp

    CompositionLocalProvider(
        LocalExtendedColors provides if (darkTheme) DarkExtendedColors else LightExtendedColors,
        // Non-tab screens (auth) degrade to the brand (Progress) world.
        LocalYoloSection provides YoloSection.Progress.colors(darkTheme),
        LocalYoloSpacing provides spacingFor(minWidthDp),
        LocalYoloSizing provides sizingFor(minWidthDp, inputProfile),
        LocalYoloMotion provides YoloMotion(reducedMotion = reducedMotion),
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            typography = YoloTypography,
            shapes = YoloShapes,
            content = content,
        )
    }
}

/** Non-color token accessors. Colors stay on MaterialTheme.colorScheme / .extended. */
object YoloTokens {
    val spacing: YoloSpacing
        @Composable get() = LocalYoloSpacing.current

    val sizing: YoloSizing
        @Composable get() = LocalYoloSizing.current

    val motion: YoloMotion
        @Composable get() = LocalYoloMotion.current
}
