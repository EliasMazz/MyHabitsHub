package com.yolo.auth.presentation.welcome

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yolo.auth.presentation.components.AppleAuthButton
import com.yolo.auth.presentation.components.GoogleAuthButton
import com.yolo.auth.presentation.sso.SocialSignInResult
import com.yolo.auth.presentation.sso.rememberAppleSignIn
import com.yolo.auth.presentation.sso.rememberGoogleSignIn
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.presentation.BaseScreen
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.continue_with_email
import myhabitshub.feature.auth.presentation.generated.resources.onboarding_body_1
import myhabitshub.feature.auth.presentation.generated.resources.onboarding_body_2
import myhabitshub.feature.auth.presentation.generated.resources.onboarding_body_3
import myhabitshub.feature.auth.presentation.generated.resources.onboarding_headline_1
import myhabitshub.feature.auth.presentation.generated.resources.onboarding_headline_2
import myhabitshub.feature.auth.presentation.generated.resources.onboarding_headline_3
import myhabitshub.feature.auth.presentation.generated.resources.onboarding_hero
import myhabitshub.feature.auth.presentation.generated.resources.onboarding_gym
import myhabitshub.feature.auth.presentation.generated.resources.onboarding_water
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = koinViewModel(),
    navigateToRegisterEvent: () -> Unit,
    navigateToLoginEvent: () -> Unit,
    navigateToMainEvent: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                WelcomeViewEvent.NavigateToRegister -> navigateToRegisterEvent()
                WelcomeViewEvent.NavigateToLogin -> navigateToLoginEvent()
                WelcomeViewEvent.NavigateToMain -> navigateToMainEvent()
                is WelcomeViewEvent.ShowError ->
                    snackbarHostState.showSnackbar(message = event.message)
            }
        }
    ) { state, onIntent ->
        WelcomeScreenContent(
            state = state,
            snackbarHostState = snackbarHostState,
            onSsoResult = { onIntent(WelcomeViewIntent.OnSsoResult(it)) },
            onContinueWithEmailClick = { onIntent(WelcomeViewIntent.OnContinueWithEmailClick) },
            onLogInClick = { onIntent(WelcomeViewIntent.OnLogInClick) },
        )
    }
}

private data class ValueProp(
    val image: DrawableResource,
    val headline: StringResource,
    val body: StringResource,
)

private val ValueProps = listOf(
    ValueProp(Res.drawable.onboarding_gym, Res.string.onboarding_headline_1, Res.string.onboarding_body_1),
    ValueProp(Res.drawable.onboarding_water, Res.string.onboarding_headline_2, Res.string.onboarding_body_2),
    ValueProp(Res.drawable.onboarding_hero, Res.string.onboarding_headline_3, Res.string.onboarding_body_3),
)

private const val AutoAdvanceMs = 2000L

// Portrait vertical anchors as fractions of screen height, measured from ref 106298.
private const val PhotoH = 0.547f        // hero photo drawn-region height
private const val FadeStartFrac = 0.455f // where the surface scrim begins, within the photo
private const val DiscCenterFrac = 0.555f
private const val HeadlineTopFrac = 0.620f
private const val DotsFrac = 0.720f
private const val PillTopFrac = 0.825f
private const val LinkCenterFrac = 0.925f

private val DiscSize = 88.dp

@Composable
fun WelcomeScreenContent(
    state: WelcomeViewState,
    snackbarHostState: SnackbarHostState,
    onSsoResult: (SocialSignInResult) -> Unit,
    onContinueWithEmailClick: () -> Unit,
    onLogInClick: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { ValueProps.size })

    val reducedMotion = YoloTokens.motion.reducedMotion
    LaunchedEffect(pagerState.settledPage, reducedMotion) {
        if (reducedMotion) return@LaunchedEffect
        delay(AutoAdvanceMs)
        if (!pagerState.isScrollInProgress) {
            pagerState.animateScrollToPage((pagerState.settledPage + 1) % ValueProps.size)
        }
    }

    // Native SSO launchers — only the platform's own button is shown, so only its launcher runs.
    val onGoogle = rememberGoogleSignIn(state.googleServerClientId, onSsoResult)
    val onApple = rememberAppleSignIn(onSsoResult)
    val ssoButton: @Composable (Modifier) -> Unit = { mod ->
        Box(modifier = mod, contentAlignment = Alignment.Center) {
            when {
                state.showGoogleButton -> GoogleAuthButton(
                    onClick = onGoogle,
                    enabled = !state.isAuthenticating,
                    modifier = Modifier.fillMaxWidth(),
                )

                state.showAppleButton -> AppleAuthButton(
                    onClick = onApple,
                    enabled = !state.isAuthenticating,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (state.isAuthenticating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    strokeWidth = 2.dp,
                )
            }
        }
    }

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val twoPane = maxWidth > maxHeight || maxHeight < 560.dp
            if (twoPane) {
                WideHero(pagerState, ssoButton, onContinueWithEmailClick)
            } else {
                PortraitHero(maxHeight, pagerState, ssoButton, onContinueWithEmailClick)
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
        )
    }
}

@Composable
private fun PortraitHero(
    screenHeight: Dp,
    pagerState: PagerState,
    ssoButton: @Composable (Modifier) -> Unit,
    onContinueWithEmailClick: () -> Unit,
) {
    val contentWidth = Modifier.widthIn(max = YoloTokens.sizing.maxFormWidth)
    val h = screenHeight
    Box(Modifier.fillMaxSize()) {
        val photoHeight = h * PhotoH

        // 1+2. Full-screen carousel: each page carries its OWN photo (top, fading) + text, so a
        // swipe anywhere — incl. over the image — pages it and the image slides with the page.
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            val prop = ValueProps[page]
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                HeroPhoto(prop.image, photoHeight)
                Spacer(Modifier.height(h * HeadlineTopFrac - photoHeight))
                ValuePropText(
                    prop,
                    modifier = contentWidth.fillMaxWidth().padding(horizontal = 24.dp),
                )
            }
        }

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(h * DiscCenterFrac - DiscSize / 2))
            BrandDisc()
        }

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(h * DotsFrac))
            PagerDots(ValueProps.size, pagerState.currentPage)
        }

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(h * PillTopFrac))
            ssoButton(contentWidth.fillMaxWidth().padding(horizontal = 16.dp))
            Spacer(Modifier.height(h * (LinkCenterFrac - PillTopFrac) - 56.dp - 24.dp))
            EmailLink(onContinueWithEmailClick)
        }
    }
}

/**
 * Two-pane hero for landscape & desktop: photo beside the content. The content column is
 * width-capped, vertically centered, and scrollable so it never clips on short windows.
 */
@Composable
private fun WideHero(
    pagerState: PagerState,
    ssoButton: @Composable (Modifier) -> Unit,
    onContinueWithEmailClick: () -> Unit,
) {
    Row(Modifier.fillMaxSize()) {
        // Photo pane — crossfades to the current page's image as the carousel advances.
        Box(Modifier.weight(1f).fillMaxHeight()) {
            Crossfade(targetState = pagerState.currentPage, label = "heroPhoto") { page ->
                Image(
                    painter = painterResource(ValueProps[page].image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            BrandDisc()
            Spacer(Modifier.height(28.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth().widthIn(max = YoloTokens.sizing.maxFormWidth),
            ) { page ->
                ValuePropText(ValueProps[page], modifier = Modifier.fillMaxWidth())
            }
            Spacer(Modifier.height(16.dp))
            PagerDots(ValueProps.size, pagerState.currentPage)
            Spacer(Modifier.height(32.dp))
            ssoButton(Modifier.widthIn(max = YoloTokens.sizing.maxFormWidth).fillMaxWidth())
            Spacer(Modifier.height(20.dp))
            EmailLink(onContinueWithEmailClick)
        }
    }
}

@Composable
private fun ValuePropText(prop: ValueProp, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(prop.headline),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(prop.body),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.extended.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun HeroPhoto(image: DrawableResource, height: Dp, modifier: Modifier = Modifier) {
    val surface = MaterialTheme.colorScheme.surface
    Image(
        painter = painterResource(image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .drawWithContent {
                drawContent()
                drawRect(
                    Brush.verticalGradient(
                        colors = listOf(surface.copy(alpha = 0f), surface),
                        startY = size.height * FadeStartFrac,
                        endY = size.height,
                    )
                )
            },
    )
}

@Composable
private fun BrandDisc(modifier: Modifier = Modifier) {
    val isDark = MaterialTheme.colorScheme.extended.isDark
    Box(
        modifier = modifier
            .size(DiscSize)
            .background(if (isDark) Color(0xFF16181B) else Color.White, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        // Disc + mark follow the theme so they match the app icon's light/dark variant.
        YoloBrandLogo(modifier = Modifier.size(DiscSize / 2))
    }
}

@Composable
private fun EmailLink(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(Res.string.continue_with_email),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .wrapContentSize(Alignment.Center),
    )
}

@Composable
private fun PagerDots(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    val active = MaterialTheme.colorScheme.primary
    val inactive = active.copy(alpha = 0.4f)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(if (isActive) 16.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (isActive) active else inactive),
            )
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
            onSsoResult = {},
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
            onSsoResult = {},
            onContinueWithEmailClick = {},
            onLogInClick = {},
        )
    }
}
