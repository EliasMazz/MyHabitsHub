package com.yolo.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import com.yolo.core.designsystem.theme.YoloTokens
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.check_spam_hint
import org.jetbrains.compose.resources.stringResource

/**
 * The "check your inbox" interstitial body (auth-screens-improvement-spec §2.3), shared by
 * RegisterSuccess and ForgotPassword's sent-state: AuthResultHero + the EXACT address entered
 * (verbatim, prominent — wrong-email detection is impossible without it) + spam hint +
 * primary CTA + resend-with-countdown + escape link.
 */
@Composable
fun CheckEmailContent(
    title: String,
    body: String,
    email: String,
    icon: @Composable () -> Unit,
    resendReadyLabel: String,
    resendSecondsRemaining: Int,
    onResendClick: () -> Unit,
    isResending: Boolean,
    primaryButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    escapeLabel: String? = null,
    onEscapeClick: (() -> Unit)? = null,
    resendError: String? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AuthResultHero(
            title = title,
            body = body,
            icon = icon,
        )
        if (email.isNotBlank()) {
            Spacer(Modifier.height(YoloTokens.spacing.stackGapTight))
            Text(
                text = email,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.height(YoloTokens.spacing.stackGap))
        Text(
            text = stringResource(Res.string.check_spam_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(YoloTokens.spacing.sectionGap))
        primaryButton()
        Spacer(Modifier.height(YoloTokens.spacing.stackGap))
        ResendCountdownButton(
            readyLabel = resendReadyLabel,
            secondsRemaining = resendSecondsRemaining,
            onClick = onResendClick,
            isLoading = isResending,
        )
        if (resendError != null) {
            Spacer(Modifier.height(YoloTokens.spacing.stackGapTight))
            Text(
                text = resendError,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
        }
        if (escapeLabel != null && onEscapeClick != null) {
            Spacer(Modifier.height(YoloTokens.spacing.stackGapTight))
            YoloButton(
                text = escapeLabel,
                onClick = onEscapeClick,
                style = YoloButtonStyle.TEXT,
            )
        }
    }
}
