package com.yolo.core.designsystem.components.brand

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.core.designsystem.theme.extended
import myhabitshub.core.designsystem.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.brand_logo_dark
import myhabitshub.core.designsystem.generated.resources.brand_logo_light
import org.jetbrains.compose.resources.painterResource

/**
 * The MyHabitsHub brand mark — a checkmark inside a rounded-square keyline. It is two-tone (keyline
 * + check), so it is theme-aware artwork rather than a tinted glyph.
 *
 * @param dark which variant to draw. Defaults to the active theme; pass an explicit value when the
 * logo sits on a surface that doesn't follow the theme (e.g. the always-white welcome disc → `false`).
 */
@Composable
fun YoloBrandLogo(
    modifier: Modifier = Modifier,
    dark: Boolean = MaterialTheme.colorScheme.extended.isDark,
) {
    Image(
        painter = painterResource(if (dark) Res.drawable.brand_logo_dark else Res.drawable.brand_logo_light),
        contentDescription = null,
        modifier = modifier,
    )
}
