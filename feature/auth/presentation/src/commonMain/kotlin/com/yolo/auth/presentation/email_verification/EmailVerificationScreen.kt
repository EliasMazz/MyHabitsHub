package com.yolo.auth.presentation.email_verification

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.yolo.auth.presentation.components.AuthResultHero
import com.yolo.auth.presentation.components.ResendCountdownButton
import com.yolo.core.designsystem.components.brand.YoloFailureIcon
import com.yolo.core.designsystem.components.brand.YoloSuccessIcon
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import com.yolo.core.designsystem.components.layout.YoloAdaptiveResultLayout
import com.yolo.core.designsystem.components.legacy.LoadingProgress
import com.yolo.core.designsystem.components.legacy.LoadingProgressMode
import com.yolo.core.designsystem.components.textfields.YoloTextField
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.presentation.BaseScreen
import com.yolo.core.presentation.util.UiText
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.back_to_login
import myhabitshub.feature.auth.presentation.generated.resources.email
import myhabitshub.feature.auth.presentation.generated.resources.email_placeholder
import myhabitshub.feature.auth.presentation.generated.resources.email_verified_failed
import myhabitshub.feature.auth.presentation.generated.resources.email_verified_failed_desc
import myhabitshub.feature.auth.presentation.generated.resources.email_verified_successfully
import myhabitshub.feature.auth.presentation.generated.resources.email_verified_successfully_desc
import myhabitshub.feature.auth.presentation.generated.resources.login
import myhabitshub.feature.auth.presentation.generated.resources.resend_verification_email
import myhabitshub.feature.auth.presentation.generated.resources.retry
import myhabitshub.feature.auth.presentation.generated.resources.verification_failed_network
import myhabitshub.feature.auth.presentation.generated.resources.verifying_account
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * Email-verification deep-link target (auth-screens-improvement-spec §4.4). Four states, all
 * rendered via [AuthResultHero]: verifying, success, network failure (retryable — same token),
 * and token failure (recovery: user-supplied email + resend + back to log in — never a dead end).
 * No back affordance — deep-link entry, exits are explicit (§7).
 */
@Composable
fun EmailVerificationScreen(
    viewModel: EmailVerificationViewModel = koinViewModel(),
    navigateToLoginEvent: () -> Unit,
    navigateBackEvent: () -> Unit,
) {
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                EmailVerificationViewEvent.NavigateBackEvent -> navigateBackEvent()
                EmailVerificationViewEvent.NavigateToLoginEvent -> navigateToLoginEvent()
            }
        }
    ) { state, onIntent ->
        EmailVerificationScreenContent(
            state = state,
            onLoginClick = { onIntent(EmailVerificationViewIntent.OnLoginClick) },
            onEmailChange = { onIntent(EmailVerificationViewIntent.OnEmailChange(it)) },
            onResendClick = { onIntent(EmailVerificationViewIntent.OnResendClick) },
            onRetryClick = { onIntent(EmailVerificationViewIntent.OnRetryClick) },
        )
    }
}

@Composable
fun EmailVerificationScreenContent(
    state: EmailVerificationViewState,
    onLoginClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onResendClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    YoloAdaptiveResultLayout {
        when {
            state.isVerifying -> VerifyingContent()

            state.isVerified -> VerifiedContent(onLoginClick = onLoginClick)

            state.isNetworkFailure -> NetworkFailureContent(onRetryClick = onRetryClick)

            else -> TokenFailureContent(
                state = state,
                onEmailChange = onEmailChange,
                onResendClick = onResendClick,
                onBackToLoginClick = onLoginClick,
            )
        }
    }
}

@Composable
private fun VerifyingContent() {
    AuthResultHero(
        title = stringResource(Res.string.verifying_account),
        body = "",
        icon = { LoadingProgress(LoadingProgressMode.CIRCULAR) },
    )
}

@Composable
private fun VerifiedContent(onLoginClick: () -> Unit) {
    AuthResultHero(
        title = stringResource(Res.string.email_verified_successfully),
        body = stringResource(Res.string.email_verified_successfully_desc),
        icon = { YoloSuccessIcon() },
    )
    Spacer(modifier = Modifier.height(YoloTokens.spacing.sectionGap))
    YoloButton(
        text = stringResource(Res.string.login),
        onClick = onLoginClick,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun NetworkFailureContent(onRetryClick: () -> Unit) {
    // §4.4#2: distinct copy — a network blip must not read as "your link is dead".
    AuthResultHero(
        title = stringResource(Res.string.email_verified_failed),
        body = stringResource(Res.string.verification_failed_network),
        icon = { YoloFailureIcon() },
    )
    Spacer(modifier = Modifier.height(YoloTokens.spacing.sectionGap))
    YoloButton(
        text = stringResource(Res.string.retry),
        onClick = onRetryClick,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun TokenFailureContent(
    state: EmailVerificationViewState,
    onEmailChange: (String) -> Unit,
    onResendClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
) {
    AuthResultHero(
        title = stringResource(Res.string.email_verified_failed),
        body = stringResource(Res.string.email_verified_failed_desc),
        icon = { YoloFailureIcon() },
    )
    Spacer(modifier = Modifier.height(YoloTokens.spacing.sectionGap))
    // The deep link only carries a token — the user supplies the address the verification
    // email should be resent to (§4.4#1). K1 keyboard + autofill semantics (K3).
    YoloTextField(
        value = state.email,
        onValueChange = onEmailChange,
        title = stringResource(Res.string.email),
        placeholder = stringResource(Res.string.email_placeholder),
        supportingText = state.emailError?.value,
        isError = state.emailError != null,
        singleLine = true,
        keyboardType = KeyboardType.Email,
        capitalization = KeyboardCapitalization.None,
        autoCorrectEnabled = false,
        contentType = ContentType.EmailAddress,
        imeAction = ImeAction.Send,
        onImeAction = onResendClick,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(YoloTokens.spacing.itemGap))
    ResendCountdownButton(
        readyLabel = stringResource(Res.string.resend_verification_email),
        secondsRemaining = state.resendCooldownSeconds,
        onClick = onResendClick,
        isLoading = state.isResending,
    )
    state.resendError?.let { error ->
        Spacer(modifier = Modifier.height(YoloTokens.spacing.stackGapTight))
        Text(
            text = error.value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    Spacer(modifier = Modifier.height(YoloTokens.spacing.elementGap))
    YoloButton(
        text = stringResource(Res.string.back_to_login),
        onClick = onBackToLoginClick,
        style = YoloButtonStyle.TEXT,
        // B2: all sibling buttons disable during the resend request — no navigating away
        // mid-flight; re-enabled when the request completes.
        enabled = !state.isResending,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview
@Composable
private fun EmailVerificationTokenFailurePreview() {
    YoloTheme {
        EmailVerificationScreenContent(
            state = EmailVerificationViewState(),
            onLoginClick = {},
            onEmailChange = {},
            onResendClick = {},
            onRetryClick = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationTokenFailureCooldownPreview() {
    YoloTheme {
        EmailVerificationScreenContent(
            state = EmailVerificationViewState(
                email = "elias@example.com",
                resendCooldownSeconds = 27,
            ),
            onLoginClick = {},
            onEmailChange = {},
            onResendClick = {},
            onRetryClick = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationTokenFailureEmailErrorPreview() {
    YoloTheme {
        EmailVerificationScreenContent(
            state = EmailVerificationViewState(
                email = "not-an-email",
                emailError = UiText.Message("Enter a valid email address, like you@example.com."),
            ),
            onLoginClick = {},
            onEmailChange = {},
            onResendClick = {},
            onRetryClick = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationNetworkFailurePreview() {
    YoloTheme {
        EmailVerificationScreenContent(
            state = EmailVerificationViewState(isNetworkFailure = true),
            onLoginClick = {},
            onEmailChange = {},
            onResendClick = {},
            onRetryClick = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationVerifyingPreview() {
    YoloTheme {
        EmailVerificationScreenContent(
            state = EmailVerificationViewState(isVerifying = true),
            onLoginClick = {},
            onEmailChange = {},
            onResendClick = {},
            onRetryClick = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationSuccessPreview() {
    YoloTheme {
        EmailVerificationScreenContent(
            state = EmailVerificationViewState(isVerified = true),
            onLoginClick = {},
            onEmailChange = {},
            onResendClick = {},
            onRetryClick = {},
        )
    }
}
