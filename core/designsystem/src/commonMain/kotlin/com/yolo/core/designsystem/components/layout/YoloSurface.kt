package com.yolo.core.designsystem.components.layout

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.YoloTokens
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun YoloSurface(
    modifier: Modifier = Modifier,
    header: @Composable ColumnScope.() -> Unit = {},
    aura: Color? = null,
    auraCenterFraction: Offset = Offset(0.85f, 0.05f),
    content: @Composable ColumnScope.() -> Unit
) {
    // ONE continuous plain surface — the old inner rounded sheet visibly clipped the aura.
    // The glow now spans the whole screen, header/back-button zone included.
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        // Slow organic drift (two phases with different periods -> a gentle Lissajous
        // path). Phases are read ONLY inside drawBehind: the animation invalidates the
        // draw layer, never the composition - typing/fields are completely unaffected.
        // Static under reducedMotion per the motion tokens.
        val reducedMotion = YoloTokens.motion.reducedMotion
        val animateAura = aura != null && !reducedMotion
        val phaseSlow = if (animateAura) {
            rememberInfiniteTransition(label = "auraDrift").animateFloat(
                initialValue = 0f,
                targetValue = (2 * PI).toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 5_000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "auraPhaseSlow",
            )
        } else null
        val phaseFast = if (animateAura) {
            rememberInfiniteTransition(label = "auraBreath").animateFloat(
                initialValue = 0f,
                targetValue = (2 * PI).toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 3_200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "auraPhaseFast",
            )
        } else null

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    if (aura != null) {
                        val p1 = phaseSlow?.value ?: 0f
                        val p2 = phaseFast?.value ?: 0f
                        val driftX = sin(p1) * size.width * 0.08f
                        val driftY = cos(p2) * size.width * 0.05f
                        val breathing = 1f + 0.14f * sin(p2)
                        drawRect(
                            Brush.radialGradient(
                                colors = listOf(aura, Color.Transparent),
                                center = Offset(
                                    size.width * auraCenterFraction.x + driftX,
                                    size.height * auraCenterFraction.y + driftY,
                                ),
                                radius = size.width * 0.9f * breathing,
                            )
                        )
                    }
                }
        ) {
            header()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
                    // Shrinks content above the keyboard so the form actually scrolls.
                    .imePadding()
            ) {
                content()
            }
        }
    }
}

@Composable
@Preview
fun YoloSurfacePreview() {
    YoloTheme {
        YoloSurface(
            modifier = Modifier
                .fillMaxSize(),
            header = {
                YoloBrandLogo(modifier = Modifier.padding(vertical = 32.dp))
            },
            content = {
                Text(
                    text = "Welcome to Yolo!",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        )
    }
}