package com.yolo.auth.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended

/**
 * Brand treatment for keyboard-free RESULT moments (auth-screens-improvement-spec §2.1):
 * status icon centered on a single auraMint radial wash + title + body. Result screens are
 * the funnel's emotional peaks; with no fields, the distraction-cost evidence doesn't bind.
 * Catalog rule respected: max ONE aura per screen.
 */
@Composable
fun AuthResultHero(
    title: String,
    body: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val aura = MaterialTheme.colorScheme.extended.auraMint
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .padding(YoloTokens.spacing.sectionGap)
                .drawBehind {
                    drawRect(
                        Brush.radialGradient(
                            colors = listOf(aura, Color.Transparent),
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = size.maxDimension,
                        )
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            icon()
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(YoloTokens.spacing.stackGap))
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
