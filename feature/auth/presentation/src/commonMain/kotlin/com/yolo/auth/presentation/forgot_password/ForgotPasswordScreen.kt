package com.yolo.auth.presentation.forgot_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.yolo.auth.presentation.components.AuthCompactHeader
import com.yolo.auth.presentation.components.CheckEmailContent
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.components.brand.YoloSuccessIcon
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloIconButton
import com.yolo.core.designsystem.components.layout.YoloAdaptiveFormLayout
import com.yolo.core.designsystem.components.layout.YoloAdaptiveResultLayout
import com.yolo.core.designsystem.components.textfields.YoloTextField
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.presentation.BaseScreen
import com.yolo.core.presentation.util.UiText
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.back_to_login
import myhabitshub.feature.auth.presentation.generated.resources.check_email_title
import myhabitshub.feature.auth.presentation.generated.resources.email
import myhabitshub.feature.auth.presentation.generated.resources.email_placeholder
import myhabitshub.feature.auth.presentation.generated.resources.forgot_password_support
import myhabitshub.feature.auth.presentation.generated.resources.forgot_password_title
import myhabitshub.feature.auth.presentation.generated.resources.resend_email
import myhabitshub.feature.auth.presentation.generated.resources.reset_link_sent_body
import myhabitshub.feature.auth.presentation.generated.resources.send_reset_link
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
    onBack: () -> Unit,
    navigateToLoginEvent: (String) -> Unit
) {
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                ForgotPasswordViewEvent.NavigateBack -> onBack()
                is ForgotPasswordViewEvent.NavigateToLogin -> navigateToLoginEvent(event.email)
            }
        }
    ) { state, onIntent ->
        ForgotPasswordScreenContent(
            state = state,
            onEmailChange = { onIntent(ForgotPasswordViewIntent.OnEmailChange(it)) },
            onEmailFocusChanged = { onIntent(ForgotPasswordViewIntent.OnEmailFocusChanged(it)) },
            onSubmitClick = { onIntent(ForgotPasswordViewIntent.OnSubmitClick) },
            onResendClick = { onIntent(ForgotPasswordViewIntent.OnResendClick) },
            onBackClick = { onIntent(ForgotPasswordViewIntent.OnBackClick) },
            onBackToLoginClick = { onIntent(ForgotPasswordViewIntent.OnBackToLoginClick) },
        )
    }
}

@Composable
fun ForgotPasswordScreenContent(
    state: ForgotPasswordViewState,
    onEmailChange: (String) -> Unit,
    onEmailFocusChanged: (Boolean) -> Unit,
    onSubmitClick: () -> Unit,
    onResendClick: () -> Unit,
    onBackClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
) {
    if (state.isEmailSent) {
        ForgotPasswordSentContent(
            state = state,
            onResendClick = onResendClick,
            onBackToLoginClick = onBackToLoginClick,
        )
    } else {
        ForgotPasswordRequestContent(
            state = state,
            onEmailChange = onEmailChange,
            onEmailFocusChanged = onEmailFocusChanged,
            onSubmitClick = onSubmitClick,
            onBackClick = onBackClick,
        )
    }
}

/** State A (spec §4.5): the request form. */
@Composable
private fun ForgotPasswordRequestContent(
    state: ForgotPasswordViewState,
    onEmailChange: (String) -> Unit,
    onEmailFocusChanged: (Boolean) -> Unit,
    onSubmitClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    YoloAdaptiveFormLayout(
        aura = MaterialTheme.colorScheme.extended.auraAmber,
        auraCenterFraction = Offset(0.85f, 0.1f),
        errorText = state.errorText?.value,
        logo = {
            YoloBrandLogo()
        },
        navigationIcon = {
            YoloIconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.back_to_login),
                )
            }
        },
        formContent = {
            AuthCompactHeader(
                title = stringResource(Res.string.forgot_password_title),
                supportingLine = stringResource(Res.string.forgot_password_support),
            )

            Spacer(modifier = Modifier.height(YoloTokens.spacing.sectionGap))

            YoloTextField(
                value = state.email,
                onValueChange = onEmailChange,
                placeholder = stringResource(Res.string.email_placeholder),
                title = stringResource(Res.string.email),
                isError = state.emailError != null,
                supportingText = state.emailError?.value,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email,
                singleLine = true,
                onFocusChanged = onEmailFocusChanged,
                imeAction = ImeAction.Send,
                onImeAction = onSubmitClick,
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                contentType = ContentType.EmailAddress,
                trailingIcon = if (state.isEmailValid) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.extended.success,
                        )
                    }
                } else {
                    null
                },
            )

            Spacer(modifier = Modifier.height(YoloTokens.spacing.itemGap))

            YoloButton(
                text = stringResource(Res.string.send_reset_link),
                onClick = onSubmitClick,
                isLoading = state.isLoading,
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    )
}

/** State B (spec §4.5): the sent interstitial — also shown for NOT_FOUND (anti-enumeration). */
@Composable
private fun ForgotPasswordSentContent(
    state: ForgotPasswordViewState,
    onResendClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
) {
    YoloAdaptiveResultLayout {
        CheckEmailContent(
            title = stringResource(Res.string.check_email_title),
            body = stringResource(Res.string.reset_link_sent_body, state.email),
            // The address is already embedded in the body string; pass empty to
            // avoid rendering it twice.
            email = "",
            icon = { YoloSuccessIcon() },
            resendReadyLabel = stringResource(Res.string.resend_email),
            resendSecondsRemaining = state.resendCooldownSeconds,
            onResendClick = onResendClick,
            isResending = state.isResending,
            primaryButton = {
                YoloButton(
                    text = stringResource(Res.string.back_to_login),
                    onClick = onBackToLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            resendError = state.resendError?.value,
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenContentPreview() {
    YoloTheme {
        ForgotPasswordScreenContent(
            state = ForgotPasswordViewState(),
            onEmailChange = {},
            onEmailFocusChanged = {},
            onSubmitClick = {},
            onResendClick = {},
            onBackClick = {},
            onBackToLoginClick = {},
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenContentValidEmailPreview() {
    YoloTheme {
        ForgotPasswordScreenContent(
            state = ForgotPasswordViewState(
                email = "test@yolo.com",
                isEmailValid = true,
            ),
            onEmailChange = {},
            onEmailFocusChanged = {},
            onSubmitClick = {},
            onResendClick = {},
            onBackClick = {},
            onBackToLoginClick = {},
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenContentErrorPreview() {
    YoloTheme {
        ForgotPasswordScreenContent(
            state = ForgotPasswordViewState(
                email = "invalid-email",
                emailError = UiText.Message("Enter a valid email address, like you@example.com."),
            ),
            onEmailChange = {},
            onEmailFocusChanged = {},
            onSubmitClick = {},
            onResendClick = {},
            onBackClick = {},
            onBackToLoginClick = {},
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenContentLoadingPreview() {
    YoloTheme {
        ForgotPasswordScreenContent(
            state = ForgotPasswordViewState(
                email = "test@yolo.com",
                isEmailValid = true,
                isLoading = true,
            ),
            onEmailChange = {},
            onEmailFocusChanged = {},
            onSubmitClick = {},
            onResendClick = {},
            onBackClick = {},
            onBackToLoginClick = {},
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenContentSentCooldownPreview() {
    YoloTheme {
        ForgotPasswordScreenContent(
            state = ForgotPasswordViewState(
                email = "test@yolo.com",
                isEmailSent = true,
                resendCooldownSeconds = 27,
            ),
            onEmailChange = {},
            onEmailFocusChanged = {},
            onSubmitClick = {},
            onResendClick = {},
            onBackClick = {},
            onBackToLoginClick = {},
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenContentSentReadyPreview() {
    YoloTheme {
        ForgotPasswordScreenContent(
            state = ForgotPasswordViewState(
                email = "test@yolo.com",
                isEmailSent = true,
                resendCooldownSeconds = 0,
            ),
            onEmailChange = {},
            onEmailFocusChanged = {},
            onSubmitClick = {},
            onResendClick = {},
            onBackClick = {},
            onBackToLoginClick = {},
        )
    }
}
