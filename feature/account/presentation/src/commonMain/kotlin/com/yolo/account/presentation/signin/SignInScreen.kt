package com.yolo.account.presentation.signin

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import myhabitshub.feature.account.presentation.generated.resources.Res
import myhabitshub.feature.account.presentation.generated.resources.sign_in_to
import myhabitshub.feature.account.presentation.generated.resources.txt_main_action_to_sign_in
import com.yolo.account.presentation.components.AgreePrivacyPolicyTermsConditionsText
import com.yolo.account.presentation.components.AuthUIHelperButtons
import com.yolo.core.designsystem.components.legacy.LogoImage
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.data.logging.AppLogger
import com.yolo.core.presentation.BaseScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = koinViewModel(),
    onSignInSuccess: () -> Unit = {},
) {
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                SignInViewEvent.SignInSuccess -> onSignInSuccess()
            }
        }
    ) { state, onIntent ->
        SignInScreenContent(
            modifier = Modifier.fillMaxSize(),
            viewState = state,
            onSignInViewSuccess = { onIntent(SignInViewIntent.OnSignInSuccess) },
            onSignInViewFail = { exception -> onIntent(SignInViewIntent.OnSignInFail(exception))}
        )
    }
}

@Composable
fun SignInScreenContent(
    modifier: Modifier = Modifier,
    viewState: SignInViewState,
    onSignInViewSuccess: () -> Unit,
    onSignInViewFail: (Throwable?) -> Unit
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(true) {
        scrollState.animateScrollTo(scrollState.maxValue, tween(500))
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = YoloTokens.spacing.screenEdge,
                end = YoloTokens.spacing.screenEdge,
                top = YoloTokens.spacing.elementGap,
                bottom = YoloTokens.spacing.screenEdge
            ),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            LogoImage(
                modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp).padding(
                    YoloTokens.spacing.elementGap
                )
            )
            Spacer(modifier = Modifier.weight(1f))

            TitleText(modifier = Modifier.padding(top = YoloTokens.spacing.heroGap))

            AuthUIHelperButtons(
                linkAccount = viewState.linkAccount,
                modifier = Modifier.padding(top = YoloTokens.spacing.heroGap).fillMaxWidth(),
                onFirebaseResult = { result ->
                    if (result.isSuccess) {
                        AppLogger.d("Successful sign in")
                        onSignInViewSuccess()
                    } else {
                        val exception = result.exceptionOrNull()
                        onSignInViewFail(exception)
                        AppLogger.e("Error occurred while signing in, ${exception?.message}")
                    }
                    viewState.snackbarMessage?.let {
                        coroutineScope.launch { snackbarHostState.showSnackbar(it) }
                    }
                }
            )

            AgreePrivacyPolicyTermsConditionsText(
                modifier = Modifier.padding(top = YoloTokens.spacing.heroGap).fillMaxWidth(),
            )
        }
    }
}


@Composable
private fun TitleText(modifier: Modifier) {
    val annotatedString = buildAnnotatedString {
        append(stringResource(Res.string.sign_in_to))
        appendLine()
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append(stringResource(Res.string.txt_main_action_to_sign_in))
        }
    }
    Text(
        modifier = modifier,
        text = annotatedString,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.headlineLarge
    )
}