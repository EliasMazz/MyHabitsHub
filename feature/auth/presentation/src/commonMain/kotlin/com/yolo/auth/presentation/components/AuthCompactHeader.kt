package com.yolo.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended

/**
 * The compact brand band for auth FORM screens (auth-screens-improvement-spec §2.2):
 * title + one supporting line. No hero, no aura — nothing pushing fields under the
 * keyboard (MoneYou +14.86% distraction-removal evidence). The logo stays in
 * YoloAdaptiveFormLayout's logo slot; back affordance in its navigationIcon slot.
 */
@Composable
fun AuthCompactHeader(
    title: String,
    modifier: Modifier = Modifier,
    supportingLine: String? = null,
    textAlign: TextAlign = TextAlign.Center,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth(),
        )
        if (supportingLine != null) {
            Spacer(Modifier.height(YoloTokens.spacing.stackGapTight))
            Text(
                text = supportingLine,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.extended.textTertiary,
                textAlign = textAlign,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
