package com.yolo.auth.presentation.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.yolo.auth.presentation.components.AuthCompactHeader
import com.yolo.auth.presentation.components.ResendCountdownButton
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import com.yolo.core.designsystem.components.buttons.YoloIconButton
import com.yolo.core.designsystem.components.layout.YoloAdaptiveFormLayout
import com.yolo.core.designsystem.components.textfields.YoloPasswordTextField
import com.yolo.core.designsystem.components.textfields.YoloTextField
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.presentation.BaseScreen
import com.yolo.core.presentation.util.UiText
import myhabitshub.core.designsystem.generated.resources.ic_back
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.navigate_back
import myhabitshub.feature.auth.presentation.generated.resources.create_account
import myhabitshub.feature.auth.presentation.generated.resources.email
import myhabitshub.feature.auth.presentation.generated.resources.email_placeholder
import myhabitshub.feature.auth.presentation.generated.resources.error_email_not_verified
import myhabitshub.feature.auth.presentation.generated.resources.error_invalid_email
import myhabitshub.feature.auth.presentation.generated.resources.error_password_required
import myhabitshub.feature.auth.presentation.generated.resources.forgot_password
import myhabitshub.feature.auth.presentation.generated.resources.login
import myhabitshub.feature.auth.presentation.generated.resources.password
import myhabitshub.feature.auth.presentation.generated.resources.resend_verification_email
import myhabitshub.feature.auth.presentation.generated.resources.welcome_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import myhabitshub.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    loginSuccessEvent: () -> Unit,
    navigateToForgotPasswordEvent: (String) -> Unit,
    navigateToRegisterEvent: (String) -> Unit,
    onBack: () -> Unit,
) {
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                LoginEvent.LoginSuccessEvent -> loginSuccessEvent()

                is LoginEvent.NavigateToForgotPasswordEvent ->
                    navigateToForgotPasswordEvent(event.email)

                is LoginEvent.NavigateToRegisterEvent ->
                    navigateToRegisterEvent(event.email)

                LoginEvent.NavigateBackEvent -> onBack()

                is LoginEvent.RequestFieldFocusEvent -> when (event.field) {
                    LoginEvent.LoginField.EMAIL -> emailFocusRequester.requestFocus()
                    LoginEvent.LoginField.PASSWORD -> passwordFocusRequester.requestFocus()
                }
            }
        }
    ) { state, onIntent ->
        LoginScreenContent(
            state = state,
            emailFocusRequester = emailFocusRequester,
            passwordFocusRequester = passwordFocusRequester,
            onTogglePasswordVisibility = { onIntent(LoginIntent.OnTogglePasswordVisibility) },
            onForgotPasswordClick = { onIntent(LoginIntent.OnForgotPasswordClick) },
            onLoginClick = { onIntent(LoginIntent.OnLoginClick) },
            onSignupClick = { onIntent(LoginIntent.OnSignupClick) },
            onBackClick = { onIntent(LoginIntent.OnBackClick) },
            onResendVerificationClick = { onIntent(LoginIntent.OnResendVerificationClick) },
            onPasswordChange = { onIntent(LoginIntent.OnPasswordChange(it)) },
            onEmailChange = { onIntent(LoginIntent.OnEmailChange(it)) },
            onEmailFocusChanged = { onIntent(LoginIntent.OnEmailFocusChanged(it)) },
        )
    }
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    onTogglePasswordVisibility: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    onBackClick: () -> Unit,
    onResendVerificationClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onEmailFocusChanged: (Boolean) -> Unit,
    emailFocusRequester: FocusRequester = remember { FocusRequester() },
    passwordFocusRequester: FocusRequester = remember { FocusRequester() },
) {
    YoloAdaptiveFormLayout(
        logo = null,
        modifier = Modifier.fillMaxSize(),
        errorText = state.errorText?.value,
        navigationIcon = {
            YoloIconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(DesignSystemRes.drawable.ic_back),
                    contentDescription = stringResource(Res.string.navigate_back),
                )
            }
        },
        errorAction = if (state.showResendVerification) {
            {
                ResendCountdownButton(
                    readyLabel = stringResource(Res.string.resend_verification_email),
                    secondsRemaining = state.resendCooldownSeconds,
                    onClick = onResendVerificationClick,
                    isLoading = state.isResendingVerification,
                    style = YoloButtonStyle.TEXT,
                )
            }
        } else {
            null
        },
        formContent = {
            AuthCompactHeader(
                title = stringResource(Res.string.welcome_back),
            )

            Spacer(modifier = Modifier.height(YoloTokens.spacing.sectionGap))

            YoloTextField(
                value = state.email,
                onValueChange = onEmailChange,
                singleLine = true,
                placeholder = stringResource(Res.string.email_placeholder),
                title = stringResource(Res.string.email),
                keyboardType = KeyboardType.Email,
                isError = state.emailError != null,
                supportingText = state.emailError?.value,
                onFocusChanged = onEmailFocusChanged,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                contentType = ContentType.EmailAddress,
                trailingIcon = if (state.isEmailFormatValid) {
                    {
                        Text(
                            text = "✓",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.extended.success,
                        )
                    }
                } else {
                    null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester),
            )

            Spacer(modifier = Modifier.height(YoloTokens.spacing.itemGap))

            YoloPasswordTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                placeholder = stringResource(Res.string.password),
                title = stringResource(Res.string.password),
                onToggleVisibilityClick = onTogglePasswordVisibility,
                isPasswordVisible = state.isPasswordVisible,
                isError = state.passwordError != null,
                supportingText = state.passwordError?.value,
                imeAction = ImeAction.Done,
                onImeAction = onLoginClick,
                contentType = ContentType.Password,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester),
            )

            Spacer(modifier = Modifier.height(YoloTokens.spacing.elementGap))

            YoloButton(
                text = stringResource(Res.string.forgot_password),
                onClick = onForgotPasswordClick,
                style = YoloButtonStyle.TEXT,
                enabled = !state.isLoggingIn,
                modifier = Modifier.align(Alignment.End),
            )

            Spacer(modifier = Modifier.height(YoloTokens.spacing.itemGap))

            // Never gated on form validity — validation happens on submit.
            YoloButton(
                text = stringResource(Res.string.login),
                onClick = onLoginClick,
                enabled = !state.isLoggingIn,
                isLoading = state.isLoggingIn,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(YoloTokens.spacing.elementGap))

            YoloButton(
                text = stringResource(Res.string.create_account),
                onClick = onSignupClick,
                style = YoloButtonStyle.SECONDARY,
                enabled = !state.isLoggingIn,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    )
}

@Preview
@Composable
fun LoginScreenLightPreview() {
    YoloTheme {
        LoginScreenContent(
            state = LoginState(
                email = "yolo@myhabitshub.com",
                password = "YoloPassword123",
                isEmailFormatValid = true,
            ),
            onTogglePasswordVisibility = {},
            onForgotPasswordClick = {},
            onLoginClick = {},
            onSignupClick = {},
            onBackClick = {},
            onResendVerificationClick = {},
            onPasswordChange = {},
            onEmailChange = {},
            onEmailFocusChanged = {},
        )
    }
}

@Preview
@Composable
fun LoginScreenDarkPreview() {
    YoloTheme(darkTheme = true) {
        LoginScreenContent(
            state = LoginState(
                email = "yolo@myhabitshub.com",
                password = "YoloPassword123",
                isEmailFormatValid = true,
            ),
            onTogglePasswordVisibility = {},
            onForgotPasswordClick = {},
            onLoginClick = {},
            onSignupClick = {},
            onBackClick = {},
            onResendVerificationClick = {},
            onPasswordChange = {},
            onEmailChange = {},
            onEmailFocusChanged = {},
        )
    }
}

@Preview
@Composable
fun LoginScreenFieldErrorsPreview() {
    YoloTheme {
        LoginScreenContent(
            state = LoginState(
                email = "yolo@myhabitshub",
                password = "",
                emailError = UiText.Resource(Res.string.error_invalid_email),
                passwordError = UiText.Resource(Res.string.error_password_required),
            ),
            onTogglePasswordVisibility = {},
            onForgotPasswordClick = {},
            onLoginClick = {},
            onSignupClick = {},
            onBackClick = {},
            onResendVerificationClick = {},
            onPasswordChange = {},
            onEmailChange = {},
            onEmailFocusChanged = {},
        )
    }
}

@Preview
@Composable
fun LoginScreenNotVerifiedBannerPreview() {
    YoloTheme {
        LoginScreenContent(
            state = LoginState(
                email = "yolo@myhabitshub.com",
                password = "YoloPassword123",
                isEmailFormatValid = true,
                errorText = UiText.Resource(Res.string.error_email_not_verified),
                showResendVerification = true,
                resendCooldownSeconds = 24,
            ),
            onTogglePasswordVisibility = {},
            onForgotPasswordClick = {},
            onLoginClick = {},
            onSignupClick = {},
            onBackClick = {},
            onResendVerificationClick = {},
            onPasswordChange = {},
            onEmailChange = {},
            onEmailFocusChanged = {},
        )
    }
}
