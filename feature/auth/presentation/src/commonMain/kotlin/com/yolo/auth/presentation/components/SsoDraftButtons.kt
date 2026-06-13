package com.yolo.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
 * DRAFT SSO buttons (auth-conversion-spec §3, §7): visual lookalikes of the official
 * Google/Apple sign-in buttons. NOT wired to any auth SDK — at implementation these are
 * replaced by kmpauth-uihelper containers + official brand assets (multicolor G, Apple logo).
 *
 * Vendor branding exception (sanctioned, auth-conversion-spec §3.1): the literal colors below
 * are the vendors' published button-chrome values; official branding overrides design-system
 * tokens for SSO buttons ONLY. Do not reuse these literals elsewhere.
 */
private object SsoVendorChrome {
    val GoogleFillLight = Color(0xFFFFFFFF)
    val GoogleStrokeLight = Color(0xFF747775)
    val GoogleTextLight = Color(0xFF1F1F1F)
    val GoogleFillDark = Color(0xFF131314)
    val GoogleStrokeDark = Color(0xFF8E918F)
    val GoogleTextDark = Color(0xFFE3E3E3)
    val GoogleGBlue = Color(0xFF4285F4) // placeholder for the official multicolor G asset

    val AppleBlack = Color(0xFF000000)
    val AppleWhite = Color(0xFFFFFFFF)
}

@Composable
private fun SsoButtonFrame(
    label: String,
    fill: Color,
    contentColor: Color,
    stroke: Color?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(YoloTokens.sizing.controlHeight)
            .clip(CircleShape)
            .background(fill)
            .let { if (stroke != null) it.border(1.dp, stroke, CircleShape) else it }
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
    ) {
        leading()
        Text(
            text = label,
            // Vendor specs mandate Roboto/SF on the real buttons — handled by official
            // assets at implementation; the draft approximates with labelLarge.
            style = MaterialTheme.typography.labelLarge,
            color = contentColor,
            modifier = Modifier.padding(start = YoloTokens.spacing.elementGap),
        )
    }
}

@Composable
fun GoogleSignInDraftButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDark = MaterialTheme.colorScheme.extended.isDark
    SsoButtonFrame(
        label = stringResource(Res.string.continue_with_google),
        fill = if (isDark) SsoVendorChrome.GoogleFillDark else SsoVendorChrome.GoogleFillLight,
        contentColor = if (isDark) SsoVendorChrome.GoogleTextDark else SsoVendorChrome.GoogleTextLight,
        stroke = if (isDark) SsoVendorChrome.GoogleStrokeDark else SsoVendorChrome.GoogleStrokeLight,
        onClick = onClick,
        modifier = modifier,
    ) {
        // Branding placeholder — official multicolor G, never recolored, at implementation.
        Text("G", color = SsoVendorChrome.GoogleGBlue, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AppleSignInDraftButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDark = MaterialTheme.colorScheme.extended.isDark
    val fill = if (isDark) SsoVendorChrome.AppleWhite else SsoVendorChrome.AppleBlack
    val content = if (isDark) SsoVendorChrome.AppleBlack else SsoVendorChrome.AppleWhite
    SsoButtonFrame(
        label = stringResource(Res.string.continue_with_apple),
        fill = fill,
        contentColor = content,
        stroke = null,
        onClick = onClick,
        modifier = modifier,
    ) {
        // Branding placeholder — Apple Design Resources logo asset at implementation.
        Text("", color = content, fontWeight = FontWeight.Bold)
    }
}
