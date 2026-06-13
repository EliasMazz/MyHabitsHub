package com.yolo.auth.presentation.register_success

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.yolo.auth.presentation.components.CheckEmailContent
import com.yolo.core.designsystem.components.brand.YoloSuccessIcon
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.layout.YoloAdaptiveResultLayout
import com.yolo.core.designsystem.components.layout.YoloSnackbarScaffold
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.presentation.BaseScreen
import com.yolo.core.presentation.util.UiText
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.account_successfully_created
import myhabitshub.feature.auth.presentation.generated.resources.edit_email
import myhabitshub.feature.auth.presentation.generated.resources.login
import myhabitshub.feature.auth.presentation.generated.resources.resend_verification_email
import myhabitshub.feature.auth.presentation.generated.resources.resent_verification_email
import myhabitshub.feature.auth.presentation.generated.resources.verification_email_sent_to_x
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * Verification interstitial (auth-screens-improvement-spec §4.3): the body is the shared
 * [CheckEmailContent] (component-level merge with ForgotPassword's sent-state) showing the
 * registered address verbatim, resend with §5.1 cooldown, Log In prefill, and the §5.2
 * wrong-email escape back to Register.
 */
@Composable
fun RegisterSuccessScreen(
    viewModel: RegisterSuccessViewModel = koinViewModel(),
    navigateToLoginEvent: (String) -> Unit,
    navigateToRegisterEvent: (String) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                is RegisterSuccessViewEvent.NavigateToLoginEvent ->
                    navigateToLoginEvent(event.email)

                is RegisterSuccessViewEvent.NavigateToRegisterEvent ->
                    navigateToRegisterEvent(event.email)

                RegisterSuccessViewEvent.ResendVerificationEmailSuccessEvent -> {
                    snackbarHostState.showSnackbar(
                        message = getString(Res.string.resent_verification_email),
                    )
                }
            }
        }
    ) { state, onIntent ->
        RegisterSuccessScreenContent(
            state = state,
            snackbarHostState = snackbarHostState,
            onLoginClick = { onIntent(RegisterSuccessViewIntent.OnLoginClick) },
            onResendVerificationEmailClick = {
                onIntent(RegisterSuccessViewIntent.OnResendVerificationEmailClick)
            },
            onEditEmailClick = { onIntent(RegisterSuccessViewIntent.OnEditEmailClick) },
        )
    }
}

@Composable
fun RegisterSuccessScreenContent(
    state: RegisterSuccessViewState,
    snackbarHostState: SnackbarHostState,
    onLoginClick: () -> Unit,
    onResendVerificationEmailClick: () -> Unit,
    onEditEmailClick: () -> Unit,
) {
    YoloSnackbarScaffold(snackbarHostState = snackbarHostState) {
        YoloAdaptiveResultLayout {
            CheckEmailContent(
                title = stringResource(Res.string.account_successfully_created),
                body = stringResource(
                    Res.string.verification_email_sent_to_x,
                    state.registeredEmail
                ),
                email = state.registeredEmail,
                icon = { YoloSuccessIcon() },
                resendReadyLabel = stringResource(Res.string.resend_verification_email),
                resendSecondsRemaining = state.resendCooldownSeconds,
                onResendClick = onResendVerificationEmailClick,
                isResending = state.isResendingVerificationEmail,
                primaryButton = {
                    YoloButton(
                        text = stringResource(Res.string.login),
                        onClick = onLoginClick,
                        modifier = Modifier.fillMaxWidth(),
                        // B2: sibling buttons disable while the resend request is in flight.
                        enabled = !state.isResendingVerificationEmail,
                    )
                },
                escapeLabel = stringResource(Res.string.edit_email),
                onEscapeClick = onEditEmailClick,
                resendError = state.resendVerificationEmailError?.value,
            )
        }
    }
}

@Preview
@Composable
private fun RegisterSuccessScreenCooldownPreview() {
    YoloTheme {
        RegisterSuccessScreenContent(
            state = RegisterSuccessViewState(
                registeredEmail = "you@example.com",
                resendCooldownSeconds = 27,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onLoginClick = {},
            onResendVerificationEmailClick = {},
            onEditEmailClick = {},
        )
    }
}

@Preview
@Composable
private fun RegisterSuccessScreenResendReadyPreview() {
    YoloTheme {
        RegisterSuccessScreenContent(
            state = RegisterSuccessViewState(
                registeredEmail = "you@example.com",
                resendCooldownSeconds = 0,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onLoginClick = {},
            onResendVerificationEmailClick = {},
            onEditEmailClick = {},
        )
    }
}

@Preview
@Composable
private fun RegisterSuccessScreenResendErrorPreview() {
    YoloTheme {
        RegisterSuccessScreenContent(
            state = RegisterSuccessViewState(
                registeredEmail = "you@example.com",
                resendCooldownSeconds = 0,
                resendVerificationEmailError = UiText.Message("Something went wrong. Try again."),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onLoginClick = {},
            onResendVerificationEmailClick = {},
            onEditEmailClick = {},
        )
    }
}
