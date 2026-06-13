package com.yolo.auth.presentation.welcome

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.yolo.auth.presentation.components.AppleSignInDraftButton
import com.yolo.auth.presentation.components.GoogleSignInDraftButton
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import com.yolo.core.designsystem.components.layout.YoloSnackbarScaffold
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.presentation.BaseScreen
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.already_have_account_log_in
import myhabitshub.feature.auth.presentation.generated.resources.continue_with_email
import myhabitshub.feature.auth.presentation.generated.resources.legal_agree_continue
import myhabitshub.feature.auth.presentation.generated.resources.sso_coming_soon
import myhabitshub.feature.auth.presentation.generated.resources.welcome_to
import myhabitshub.feature.auth.presentation.generated.resources.welcome_value_prop
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Auth entry point: brand hero on top, button stack in the thumb zone.
 * SSO buttons (Google on Android, Apple on iOS) are visual drafts.
 */
@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = koinViewModel(),
    navigateToRegisterEvent: () -> Unit,
    navigateToLoginEvent: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                WelcomeViewEvent.NavigateToRegister -> navigateToRegisterEvent()
                WelcomeViewEvent.NavigateToLogin -> navigateToLoginEvent()
                WelcomeViewEvent.ShowSsoComingSoon -> {
                    snackbarHostState.showSnackbar(message = getString(Res.string.sso_coming_soon))
                }
            }
        }
    ) { state, onIntent ->
        WelcomeScreenContent(
            state = state,
            snackbarHostState = snackbarHostState,
            onGoogleClick = { onIntent(WelcomeViewIntent.OnGoogleClick) },
            onAppleClick = { onIntent(WelcomeViewIntent.OnAppleClick) },
            onContinueWithEmailClick = { onIntent(WelcomeViewIntent.OnContinueWithEmailClick) },
            onLogInClick = { onIntent(WelcomeViewIntent.OnLogInClick) },
        )
    }
}

@Composable
fun WelcomeScreenContent(
    state: WelcomeViewState,
    snackbarHostState: SnackbarHostState,
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit,
    onContinueWithEmailClick: () -> Unit,
    onLogInClick: () -> Unit,
) {
    // The hero canvas is the dark teal-ink surface in BOTH themes: forcing the dark token
    // world keeps every child hero-legible in light mode; no-op in dark.
    YoloTheme(darkTheme = true) {
    val x = MaterialTheme.colorScheme.extended
    YoloSnackbarScaffold(snackbarHostState = snackbarHostState) {
        // Background drift, drawn at ROOT scope so the glow flows seamlessly behind the
        // buttons. Animated values are read in the draw phase only (inside drawBehind),
        // never in composition; static under reducedMotion.
        val reducedMotion = YoloTokens.motion.reducedMotion
        val heroPhaseSlow = if (!reducedMotion) {
            rememberInfiniteTransition(label = "heroDrift").animateFloat(
                initialValue = 0f,
                targetValue = (2 * PI).toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 5_000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "heroPhaseSlow",
            )
        } else null
        val heroPhaseFast = if (!reducedMotion) {
            rememberInfiniteTransition(label = "heroBreath").animateFloat(
                initialValue = 0f,
                targetValue = (2 * PI).toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 3_200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "heroPhaseFast",
            )
        } else null
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(x.heroSurface)
                .drawBehind {
                    val p1 = heroPhaseSlow?.value ?: 0f
                    val p2 = heroPhaseFast?.value ?: 0f
                    drawRect(
                        Brush.radialGradient(
                            colors = listOf(x.auraMint, Color.Transparent),
                            center = Offset(
                                size.width * 0.3f + sin(p1) * size.width * 0.09f,
                                size.height * 0.22f + cos(p2) * size.height * 0.06f,
                            ),
                            radius = size.width * 0.95f * (1f + 0.14f * sin(p2)),
                        )
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.45f)
                    .padding(horizontal = YoloTokens.spacing.screenEdge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                YoloBrandLogo()
                Spacer(Modifier.height(YoloTokens.spacing.stackGap))
                Text(
                    text = stringResource(Res.string.welcome_to),
                    style = MaterialTheme.typography.headlineMedium,
                    color = x.onHeroSurface,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(YoloTokens.spacing.stackGapTight))
                Text(
                    text = stringResource(Res.string.welcome_value_prop),
                    style = MaterialTheme.typography.bodyLarge,
                    color = x.onHeroSurface,
                    textAlign = TextAlign.Center,
                )
            }

            Column(
                modifier = Modifier
                    .weight(0.55f)
                    .widthIn(max = YoloTokens.sizing.maxFormWidth)
                    .padding(
                        start = YoloTokens.spacing.screenEdge,
                        end = YoloTokens.spacing.screenEdge,
                        top = YoloTokens.spacing.heroGap,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGap),
                ) {
                    if (state.showGoogleButton) {
                        GoogleSignInDraftButton(onClick = onGoogleClick)
                    }
                    if (state.showAppleButton) {
                        AppleSignInDraftButton(onClick = onAppleClick)
                    }
                    YoloButton(
                        text = stringResource(Res.string.continue_with_email),
                        onClick = onContinueWithEmailClick,
                        style = YoloButtonStyle.SECONDARY,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Spacer(Modifier.height(YoloTokens.spacing.itemGap))

                Text(
                    text = stringResource(Res.string.legal_agree_continue),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.weight(1f))

                YoloButton(
                    text = stringResource(Res.string.already_have_account_log_in),
                    onClick = onLogInClick,
                    style = YoloButtonStyle.TEXT,
                )
                Spacer(Modifier.height(YoloTokens.spacing.sectionGap))
            }
        }
    }
    }
}

@Preview
@Composable
private fun WelcomeScreenAndroidPreview() {
    YoloTheme {
        WelcomeScreenContent(
            state = WelcomeViewState(showGoogleButton = true),
            snackbarHostState = SnackbarHostState(),
            onGoogleClick = {},
            onAppleClick = {},
            onContinueWithEmailClick = {},
            onLogInClick = {},
        )
    }
}

@Preview
@Composable
private fun WelcomeScreenIosDarkPreview() {
    YoloTheme(darkTheme = true) {
        WelcomeScreenContent(
            state = WelcomeViewState(showAppleButton = true),
            snackbarHostState = SnackbarHostState(),
            onGoogleClick = {},
            onAppleClick = {},
            onContinueWithEmailClick = {},
            onLogInClick = {},
        )
    }
}
