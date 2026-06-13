package com.yolo.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import myhabitshub.core.designsystem.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.manrope_variable
import org.jetbrains.compose.resources.Font

// Design System v3 typography — Google Sans Flex (SIL OFL 1.1 since Nov 2025; license at
// docs/licenses/google-sans-flex-OFL.txt), the genuine reference typeface.
// Scale = exact M3 tokens: display/headline/titleLarge use the M3 Expressive *Emphasized*
// styles (weight 500, tracking 0); title/body/label slots use the M3 baseline values.
// Spec: docs/design/design-system-v3-spec.md §5. "tnum" keeps stat digits from jiggling.

// Manrope (SIL OFL) — the typeface used in the Figma design (D1FpX5… / team library nodes
// 3311:2 / 3311:135). Variable font, weight-instanced to the M3 scale.
private val Manrope: FontFamily
    @Composable get() = FontFamily(
        listOf(400, 500, 700).map { weight ->
            Font(
                Res.font.manrope_variable,
                weight = FontWeight(weight),
                variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
            )
        }
    )

private const val TABULAR_NUMERALS = "tnum"

val YoloTypography: Typography
    @Composable get() {
        val family = Manrope
        return Typography(
            displayLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 57.sp,
                lineHeight = 64.sp,
                letterSpacing = 0.sp,
                fontFeatureSettings = TABULAR_NUMERALS,
            ),
            displayMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 45.sp,
                lineHeight = 52.sp,
                letterSpacing = 0.sp,
                fontFeatureSettings = TABULAR_NUMERALS,
            ),
            displaySmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = 0.sp,
            ),
            headlineLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = 0.sp,
            ),
            headlineMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.sp,
            ),
            headlineSmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
            ),
            titleLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
            ),
            titleMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
            ),
            titleSmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
            ),
            bodyLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
            bodyMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp,
            ),
            bodySmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
            ),
            labelLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
            ),
            labelMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
            labelSmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
        )
    }

/**
 * Extension styles outside the M3 scale (design-system-v3-spec §5.3).
 * statHero = displayLargeEmphasized class — the reference app hero numerals are display-large size,
 * not poster-size; render single-line with auto-shrink.
 * kicker caps are applied via text.uppercase() at the call site, on data/stat tiles only.
 */
object YoloTypeExtras {
    val statHero: TextStyle
        @Composable get() = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = 0.sp,
            fontFeatureSettings = TABULAR_NUMERALS,
        )

    val kicker: TextStyle
        @Composable get() = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        )

    val statUnit: TextStyle
        @Composable get() = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
            fontFeatureSettings = TABULAR_NUMERALS,
        )
}
