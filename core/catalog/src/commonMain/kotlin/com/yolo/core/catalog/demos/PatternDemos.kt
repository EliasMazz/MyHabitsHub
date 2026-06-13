package com.yolo.core.catalog.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yolo.core.catalog.CatalogEntry
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended

/**
 * DRAFT — Auth Welcome screen (docs/design/auth-conversion-spec.md §2, §7).
 *
 * Visual-only blueprint demo: shows WHERE the SSO + email buttons live per platform.
 * NOTHING is wired — no kmpauth/Firebase calls, no navigation, production auth screens
 * untouched. The Google/Apple buttons are LOOKALIKES approximating official vendor chrome;
 * at implementation they are replaced by kmpauth-uihelper containers + official assets.
 *
 * Vendor branding exception (sanctioned, auth-conversion-spec §3.1): official Google/Apple
 * button chrome overrides design-system tokens — the literal colors below are the vendors'
 * published values and exist ONLY for SSO button rendering. Do not reuse them elsewhere.
 */
private object SsoVendorChrome {
    // Google button (official spec) — light / dark variants
    val GoogleFillLight = Color(0xFFFFFFFF)
    val GoogleStrokeLight = Color(0xFF747775)
    val GoogleTextLight = Color(0xFF1F1F1F)
    val GoogleFillDark = Color(0xFF131314)
    val GoogleStrokeDark = Color(0xFF8E918F)
    val GoogleTextDark = Color(0xFFE3E3E3)
    val GoogleGBlue = Color(0xFF4285F4) // placeholder for the multicolor G asset

    // Apple button (official spec) — Black on light backgrounds, White on dark
    val AppleBlack = Color(0xFF000000)
    val AppleWhite = Color(0xFFFFFFFF)
}

private enum class DraftPlatform { Android, Ios }

@Composable
private fun DraftSsoButton(
    label: String,
    fill: Color,
    contentColor: Color,
    stroke: Color?,
    leading: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = YoloTokens.sizing.maxFormWidth)
            .height(YoloTokens.sizing.controlHeight)
            .clip(CircleShape)
            .background(fill)
            .let { if (stroke != null) it.border(1.dp, stroke, CircleShape) else it },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        leading()
        Text(
            label,
            // Vendor specs mandate Roboto/SF for real buttons — kmpauth/official assets
            // handle that at implementation; the draft approximates with labelLarge.
            style = MaterialTheme.typography.labelLarge,
            color = contentColor,
            modifier = Modifier.padding(start = YoloTokens.spacing.elementGap),
        )
    }
}

@Composable
private fun GoogleButtonDraft(isDark: Boolean) {
    DraftSsoButton(
        label = "Continue with Google",
        fill = if (isDark) SsoVendorChrome.GoogleFillDark else SsoVendorChrome.GoogleFillLight,
        contentColor = if (isDark) SsoVendorChrome.GoogleTextDark else SsoVendorChrome.GoogleTextLight,
        stroke = if (isDark) SsoVendorChrome.GoogleStrokeDark else SsoVendorChrome.GoogleStrokeLight,
    ) {
        // Branding placeholder — real button uses the official multicolor G, never recolored.
        Text("G", color = SsoVendorChrome.GoogleGBlue, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AppleButtonDraft(isDark: Boolean) {
    val fill = if (isDark) SsoVendorChrome.AppleWhite else SsoVendorChrome.AppleBlack
    val content = if (isDark) SsoVendorChrome.AppleBlack else SsoVendorChrome.AppleWhite
    DraftSsoButton(
        label = "Continue with Apple",
        fill = fill,
        contentColor = content,
        stroke = null,
    ) {
        // Branding placeholder — real button uses the Apple Design Resources logo asset.
        Text("", color = content, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun EmailButtonDraft() {
    Box(modifier = Modifier.fillMaxWidth().widthIn(max = YoloTokens.sizing.maxFormWidth)) {
        YoloButton(
            text = "Continue with email",
            onClick = { /* DRAFT — routes to existing Register/Login at implementation */ },
            style = YoloButtonStyle.SECONDARY,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun AuthWelcomeDraftDemo() {
    var platform by remember { mutableStateOf(DraftPlatform.Android) }
    val isDark = MaterialTheme.colorScheme.extended.isDark
    val x = MaterialTheme.colorScheme.extended

    Column(verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap)) {
        // DRAFT banner
        Text(
            "DRAFT — no auth wired. Layout blueprint per auth-conversion-spec §2.",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(YoloTokens.spacing.elementGap),
        )

        // Platform ordering toggle
        Row(horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.elementGap)) {
            DraftPlatform.entries.forEach { p ->
                FilterChip(
                    selected = platform == p,
                    onClick = { platform = p },
                    label = { Text(p.name, style = MaterialTheme.typography.labelMedium) },
                )
            }
        }

        // The Welcome screen mock (phone-shaped column)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 1. Hero zone — heroSurface + ONE auraMint blob + brand + value line
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(x.heroSurface)
                    .drawBehind {
                        drawRect(
                            Brush.radialGradient(
                                colors = listOf(x.auraMint, Color.Transparent),
                                center = Offset(size.width * 0.3f, size.height * 0.25f),
                                radius = size.width * 0.7f,
                            )
                        )
                    }
                    .padding(
                        horizontal = YoloTokens.spacing.screenEdge,
                        vertical = YoloTokens.spacing.sectionGap,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap),
            ) {
                YoloBrandLogo()
                Text("MyHabitsHub", style = MaterialTheme.typography.headlineMedium, color = x.onHeroSurface)
                Text(
                    "Build habits that stick.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = x.onHeroSurface,
                    textAlign = TextAlign.Center,
                )
            }

            // 2-3. heroGap, then the button stack in the lower half (thumb zone)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = YoloTokens.spacing.screenEdge,
                        end = YoloTokens.spacing.screenEdge,
                        top = YoloTokens.spacing.heroGap,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap),
            ) {
                when (platform) {
                    // Android (§2.2): Google FIRST, sole primary; Apple omitted in v1.
                    DraftPlatform.Android -> {
                        GoogleButtonDraft(isDark)
                        EmailButtonDraft()
                    }
                    // iOS (§2.3 as amended): Apple + email only in v1 — Google-on-iOS
                    // deferred by product decision (4.8 obligation not triggered).
                    DraftPlatform.Ios -> {
                        AppleButtonDraft(isDark)
                        EmailButtonDraft()
                    }
                }

                // 4. Legal line — implicit consent (§5); real screen reuses
                // AgreePrivacyPolicyTermsConditionsText (unreachable from this module).
                Text(
                    "By continuing you agree to our Terms & Privacy Policy",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = YoloTokens.spacing.itemGap / 4),
                )

                // 5. Returning-user escape, bottom thumb zone
                YoloButton(
                    text = "Already have an account? Log in",
                    onClick = { /* DRAFT */ },
                    style = YoloButtonStyle.TEXT,
                )
            }

            Box(Modifier.height(YoloTokens.spacing.sectionGap))
        }

        Text(
            "Order rationale: platform-native first (Apple HIG prominence rule on iOS; " +
                "Google Credential Manager fast path on Android), exactly 2 SSO + collapsed " +
                "email (choice-overload data), stack in thumb zone (~10-20% lift, bottom-anchored CTAs).",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

val patternEntries: List<CatalogEntry> = listOf(
    CatalogEntry(
        "Auth Welcome (DRAFT)",
        "Sign-in screen blueprint — per-platform SSO ordering, visual only, no auth wired",
    ) { AuthWelcomeDraftDemo() },
)
