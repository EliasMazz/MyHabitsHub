package com.yolo.myhabitshub.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.yolo.core.designsystem.theme.AppTypographyLegacy
import com.yolo.core.designsystem.theme.MaterialThemAppTypography
import com.yolo.core.designsystem.theme.appTypographyLegacy


internal val LocalThemeIsDark = compositionLocalOf { true }
internal val LocalAppColors = staticCompositionLocalOf { lightModeAppColors }
internal val LocalAppTypographyLegacy =
    staticCompositionLocalOf<AppTypographyLegacy> { error("Typography not provided") }
internal val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }

@Composable
internal fun AppTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkMode,
        LocalAppColors provides if (isDarkMode) darkModeAppColors else lightModeAppColors,
        LocalAppTypographyLegacy provides appTypographyLegacy,
        LocalAppSpacing provides appSpacing
    ) {


        SystemAppearance(isDarkMode)
        MaterialTheme(
            colorScheme = LocalAppColors.current.asMaterialColorScheme(isDarkMode),
            typography = MaterialThemAppTypography,
            content = {
                Surface(
                    content = content,
                    color = AppTheme.colors.background
                )
            }
        )

    }
}


object AppTheme {

    val colors: AppColors
        @Composable @ReadOnlyComposable get() = LocalAppColors.current

    val typography: AppTypographyLegacy
        @Composable @ReadOnlyComposable get() = LocalAppTypographyLegacy.current

    val spacing: AppSpacing
        @Composable @ReadOnlyComposable get() = LocalAppSpacing.current
}

@Composable
internal expect fun SystemAppearance(isDark: Boolean)
