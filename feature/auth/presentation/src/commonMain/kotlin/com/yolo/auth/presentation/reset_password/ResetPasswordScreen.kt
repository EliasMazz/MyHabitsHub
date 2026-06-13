package com.yolo.auth.presentation.reset_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.text.input.ImeAction
import com.yolo.auth.presentation.components.AuthCompactHeader
import com.yolo.auth.presentation.components.AuthResultHero
import com.yolo.auth.presentation.components.PasswordRuleChecklist
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.components.brand.YoloFailureIcon
import com.yolo.core.designsystem.components.brand.YoloSuccessIcon
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import com.yolo.core.designsystem.components.layout.YoloAdaptiveFormLayout
import com.yolo.core.designsystem.components.layout.YoloAdaptiveResultLayout
import com.yolo.core.designsystem.components.layout.YoloSimpleResultLayout
import com.yolo.core.designsystem.components.textfields.YoloPasswordTextField
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.presentation.BaseScreen
import com.yolo.core.presentation.util.UiText
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.back_to_login
import myhabitshub.feature.auth.presentation.generated.resources.error_reset_password_token_invalid
import myhabitshub.feature.auth.presentation.generated.resources.login
import myhabitshub.feature.auth.presentation.generated.resources.new_password
import myhabitshub.feature.auth.presentation.generated.resources.request_new_link
import myhabitshub.feature.auth.presentation.generated.resources.reset_password
import myhabitshub.feature.auth.presentation.generated.resources.reset_password_successfully
import myhabitshub.feature.auth.presentation.generated.resources.reset_password_support
import myhabitshub.feature.auth.presentation.generated.resources.save_new_password
import myhabitshub.feature.auth.presentation.generated.resources.set_new_password
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * Reset-password screen, reached from the forgot-password email deep link.
 * One single new-password field — never a confirm field (auth-conversion-spec §2.4).
 * Three states: token-invalid recovery (§4.6#1), form, success.
 * No back affordance — deep-link entry, exits are explicit (§4.6#10, §7).
 */
@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel = koinViewModel(),
    navigateToLoginEvent: () -> Unit,
    navigateToForgotPasswordEvent: () -> Unit,
) {
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                ResetPasswordViewEvent.NavigateToLogin -> navigateToLoginEvent()
                ResetPasswordViewEvent.NavigateToForgotPassword -> navigateToForgotPasswordEvent()
            }
        }
    ) { state, onIntent ->
        ResetPasswordScreenContent(
            state = state,
            onPasswordChange = { onIntent(ResetPasswordViewIntent.OnPasswordChange(it)) },
            onTogglePasswordVisibility = { onIntent(ResetPasswordViewIntent.OnTogglePasswordVisibility) },
            onSubmitClick = { onIntent(ResetPasswordViewIntent.OnSubmitClick) },
            onLogInClick = { onIntent(ResetPasswordViewIntent.OnLogInClick) },
            onRequestNewLinkClick = { onIntent(ResetPasswordViewIntent.OnRequestNewLinkClick) },
        )
    }
}

@Composable
fun ResetPasswordScreenContent(
    state: ResetPasswordViewState,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onSubmitClick: () -> Unit,
    onLogInClick: () -> Unit,
    onRequestNewLinkClick: () -> Unit,
) {
    if (state.isSuccess) {
        YoloAdaptiveResultLayout {
            YoloSimpleResultLayout(
                title = stringResource(Res.string.reset_password),
                description = stringResource(Res.string.reset_password_successfully),
                icon = { YoloSuccessIcon() },
                primaryButton = {
                    YoloButton(
                        text = stringResource(Res.string.login),
                        onClick = onLogInClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
            )
        }
        return
    }

    if (state.isTokenInvalid) {
        // Dedicated full-screen recovery state (spec §4.6#1): the link is dead, so the form is
        // never shown — only the exits. Never a banner over a form that can't succeed.
        YoloAdaptiveResultLayout {
            AuthResultHero(
                title = stringResource(Res.string.reset_password),
                body = stringResource(Res.string.error_reset_password_token_invalid),
                icon = { YoloFailureIcon() },
            )
            Spacer(modifier = Modifier.height(YoloTokens.spacing.sectionGap))
            YoloButton(
                text = stringResource(Res.string.request_new_link),
                onClick = onRequestNewLinkClick,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(YoloTokens.spacing.elementGap))
            YoloButton(
                text = stringResource(Res.string.back_to_login),
                onClick = onLogInClick,
                style = YoloButtonStyle.TEXT,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        return
    }

    YoloAdaptiveFormLayout(
        aura = MaterialTheme.colorScheme.extended.auraMint,
        auraCenterFraction = Offset(0.15f, 0.3f),
        // Request-level (server) errors only — token errors render as the recovery state above.
        errorText = state.errorText?.value,
        logo = { YoloBrandLogo() },
        // Deep-link entry: no back affordance, explicit exits only (§4.6#10).
        navigationIcon = null,
        formContent = {
            AuthCompactHeader(
                title = stringResource(Res.string.set_new_password),
                supportingLine = stringResource(Res.string.reset_password_support),
            )

            Spacer(modifier = Modifier.height(YoloTokens.spacing.sectionGap))

            YoloPasswordTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                placeholder = stringResource(Res.string.new_password),
                title = stringResource(Res.string.new_password),
                supportingText = state.passwordError?.value,
                isError = state.passwordError != null,
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibilityClick = onTogglePasswordVisibility,
                imeAction = ImeAction.Done,
                onImeAction = onSubmitClick,
                contentType = ContentType.NewPassword,
                modifier = Modifier.fillMaxWidth(),
            )

            // Live rule checklist replaces the old (wrong) password_hint (§4.6#3).
            PasswordRuleChecklist(password = state.password)

            Spacer(modifier = Modifier.height(YoloTokens.spacing.itemGap))

            YoloButton(
                text = stringResource(Res.string.save_new_password),
                onClick = onSubmitClick,
                isLoading = state.isLoading,
                // B1/B5: never gated on field validity; disabled only during the request (B2).
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    )
}

@Preview
@Composable
private fun ResetPasswordScreenPreview() {
    YoloTheme {
        ResetPasswordScreenContent(
            state = ResetPasswordViewState(password = "NewPassword1"),
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onSubmitClick = {},
            onLogInClick = {},
            onRequestNewLinkClick = {},
        )
    }
}

@Preview
@Composable
private fun ResetPasswordScreenErrorPreview() {
    YoloTheme(darkTheme = true) {
        ResetPasswordScreenContent(
            state = ResetPasswordViewState(
                password = "short",
                passwordError = UiText.Message("Your password needs at least 9 characters, one number, and one uppercase letter."),
            ),
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onSubmitClick = {},
            onLogInClick = {},
            onRequestNewLinkClick = {},
        )
    }
}

@Preview
@Composable
private fun ResetPasswordScreenTokenInvalidPreview() {
    YoloTheme {
        ResetPasswordScreenContent(
            state = ResetPasswordViewState(isTokenInvalid = true),
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onSubmitClick = {},
            onLogInClick = {},
            onRequestNewLinkClick = {},
        )
    }
}

@Preview
@Composable
private fun ResetPasswordScreenSuccessPreview() {
    YoloTheme {
        ResetPasswordScreenContent(
            state = ResetPasswordViewState(isSuccess = true),
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onSubmitClick = {},
            onLogInClick = {},
            onRequestNewLinkClick = {},
        )
    }
}
