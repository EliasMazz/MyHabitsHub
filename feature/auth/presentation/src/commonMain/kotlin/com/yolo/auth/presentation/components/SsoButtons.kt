package com.yolo.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.continue_with_apple
import myhabitshub.feature.auth.presentation.generated.resources.continue_with_google
import org.jetbrains.compose.resources.stringResource

/**
 * Brand-compliant Google / Apple sign-in buttons (auth-conversion-spec §3.1). The literal colors
 * below are the vendors' published button-chrome values — a sanctioned exception to the design
 * system (SSO buttons only). Drop in the official multicolor "G" and Apple-logo assets before store
 * submission; the lettermark/glyph here is a brand-aligned approximation.
 */
private object SsoVendorChrome {
    val GoogleFillLight = Color(0xFFFFFFFF)
    val GoogleStrokeLight = Color(0xFF747775)
    val GoogleTextLight = Color(0xFF1F1F1F)
    val GoogleFillDark = Color(0xFF131314)
    val GoogleStrokeDark = Color(0xFF8E918F)
    val GoogleTextDark = Color(0xFFE3E3E3)
    val GoogleGBlue = Color(0xFF4285F4)

    val AppleBlack = Color(0xFF000000)
    val AppleWhite = Color(0xFFFFFFFF)
}

@Composable
private fun SsoButtonFrame(
    label: String,
    fill: Color,
    contentColor: Color,
    stroke: Color?,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(YoloTokens.sizing.controlHeight)
            .alpha(if (enabled) 1f else 0.5f)
            .clip(CircleShape)
            .background(fill)
            .let { if (stroke != null) it.border(1.dp, stroke, CircleShape) else it }
            .clickable(enabled = enabled, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        leading()
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = contentColor,
            modifier = Modifier.padding(start = YoloTokens.spacing.elementGap),
        )
    }
}

@Composable
fun GoogleAuthButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val isDark = MaterialTheme.colorScheme.extended.isDark
    SsoButtonFrame(
        label = stringResource(Res.string.continue_with_google),
        fill = if (isDark) SsoVendorChrome.GoogleFillDark else SsoVendorChrome.GoogleFillLight,
        contentColor = if (isDark) SsoVendorChrome.GoogleTextDark else SsoVendorChrome.GoogleTextLight,
        stroke = if (isDark) SsoVendorChrome.GoogleStrokeDark else SsoVendorChrome.GoogleStrokeLight,
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
    ) {
        Text("G", color = SsoVendorChrome.GoogleGBlue, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AppleAuthButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val isDark = MaterialTheme.colorScheme.extended.isDark
    val fill = if (isDark) SsoVendorChrome.AppleWhite else SsoVendorChrome.AppleBlack
    val content = if (isDark) SsoVendorChrome.AppleBlack else SsoVendorChrome.AppleWhite
    SsoButtonFrame(
        label = stringResource(Res.string.continue_with_apple),
        fill = fill,
        contentColor = content,
        stroke = null,
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
    ) {
        Text("", color = content, fontWeight = FontWeight.Bold)
    }
}
