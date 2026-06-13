package com.yolo.auth.presentation.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.yolo.auth.presentation.components.AuthCompactHeader
import com.yolo.auth.presentation.components.PasswordRuleChecklist
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
import myhabitshub.feature.auth.presentation.generated.resources.email
import myhabitshub.feature.auth.presentation.generated.resources.email_placeholder
import myhabitshub.feature.auth.presentation.generated.resources.error_account_exists
import myhabitshub.feature.auth.presentation.generated.resources.error_email_required
import myhabitshub.feature.auth.presentation.generated.resources.error_password_required
import myhabitshub.feature.auth.presentation.generated.resources.login
import myhabitshub.feature.auth.presentation.generated.resources.login_instead
import myhabitshub.feature.auth.presentation.generated.resources.password
import myhabitshub.feature.auth.presentation.generated.resources.register
import myhabitshub.feature.auth.presentation.generated.resources.register_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import myhabitshub.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    navigateToRegisterSuccessEvent: (String) -> Unit,
    navigateToLoginEvent: (String) -> Unit,
    onBack: () -> Unit,
) {
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                is RegisterViewEvent.NavigateToRegisterSuccessEvent ->
                    navigateToRegisterSuccessEvent(event.email)

                is RegisterViewEvent.NavigateToLoginEvent -> navigateToLoginEvent(event.email)

                RegisterViewEvent.NavigateBackEvent -> onBack()

                is RegisterViewEvent.FocusFirstInvalidFieldEvent -> when (event.field) {
                    RegisterFormField.EMAIL -> emailFocusRequester.requestFocus()
                    RegisterFormField.PASSWORD -> passwordFocusRequester.requestFocus()
                }
            }
        }
    ) { state, onIntent ->
        RegisterScreenContent(
            state = state,
            emailFocusRequester = emailFocusRequester,
            passwordFocusRequester = passwordFocusRequester,
            onEmailChange = { onIntent(RegisterViewIntent.OnEmailChange(it)) },
            onEmailFocusChanged = { onIntent(RegisterViewIntent.OnEmailFocusChanged(it)) },
            onPasswordChange = { onIntent(RegisterViewIntent.OnPasswordChange(it)) },
            onTogglePasswordVisibility = { onIntent(RegisterViewIntent.OnTogglePasswordVisibility) },
            onRegisterClick = { onIntent(RegisterViewIntent.OnRegisterClick) },
            onLoginClick = { onIntent(RegisterViewIntent.OnLoginClick) },
            onLoginInsteadClick = { onIntent(RegisterViewIntent.OnLoginInsteadClick) },
            onBackClick = { onIntent(RegisterViewIntent.OnBackClick) },
        )
    }
}

@Composable
fun RegisterScreenContent(
    state: RegisterViewState,
    onEmailChange: (String) -> Unit,
    onEmailFocusChanged: (Boolean) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLoginInsteadClick: () -> Unit,
    onBackClick: () -> Unit,
    emailFocusRequester: FocusRequester = remember { FocusRequester() },
    passwordFocusRequester: FocusRequester = remember { FocusRequester() },
) {
    YoloAdaptiveFormLayout(
        aura = MaterialTheme.colorScheme.extended.auraMint,
        auraCenterFraction = Offset(0.9f, 0f),
        errorText = state.registrationError?.value,
        errorAction = if (state.isAccountConflict) {
            {
                YoloButton(
                    text = stringResource(Res.string.login_instead),
                    onClick = onLoginInsteadClick,
                    style = YoloButtonStyle.TEXT,
                    enabled = !state.isLoading,
                )
            }
        } else {
            null
        },
        logo = null,
        navigationIcon = {
            YoloIconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(DesignSystemRes.drawable.ic_back),
                    contentDescription = stringResource(Res.string.navigate_back),
                )
            }
        },
        formContent = {
            AuthCompactHeader(
                title = stringResource(Res.string.register_title),
            )

            Spacer(modifier = Modifier.height(YoloTokens.spacing.sectionGap))

            YoloTextField(
                value = state.email,
                onValueChange = onEmailChange,
                singleLine = true,
                placeholder = stringResource(Res.string.email_placeholder),
                title = stringResource(Res.string.email),
                supportingText = state.emailError?.value,
                isError = state.emailError != null,
                onFocusChanged = onEmailFocusChanged,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = { passwordFocusRequester.requestFocus() },
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
                supportingText = state.passwordError?.value,
                isError = state.passwordError != null,
                onToggleVisibilityClick = onTogglePasswordVisibility,
                isPasswordVisible = state.isPasswordVisible,
                imeAction = ImeAction.Done,
                onImeAction = onRegisterClick,
                contentType = ContentType.NewPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester),
            )

            PasswordRuleChecklist(password = state.password)

            Spacer(modifier = Modifier.height(YoloTokens.spacing.itemGap))

            YoloButton(
                text = stringResource(Res.string.register),
                onClick = onRegisterClick,
                // Never gated on form validity — validation happens on submit.
                enabled = !state.isLoading,
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(YoloTokens.spacing.elementGap))

            YoloButton(
                text = stringResource(Res.string.login),
                onClick = onLoginClick,
                style = YoloButtonStyle.SECONDARY,
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(YoloTokens.spacing.sectionGap))
        }
    )
}

@Preview
@Composable
private fun RegisterScreenContentPreview() {
    YoloTheme {
        RegisterScreenContent(
            state = RegisterViewState(),
            onEmailChange = {},
            onEmailFocusChanged = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onRegisterClick = {},
            onLoginClick = {},
            onLoginInsteadClick = {},
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun RegisterScreenContentValidEmailPreview() {
    YoloTheme {
        RegisterScreenContent(
            state = RegisterViewState(
                email = "test@yolo.com",
                isEmailValid = true,
                password = "Yolo1",
            ),
            onEmailChange = {},
            onEmailFocusChanged = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onRegisterClick = {},
            onLoginClick = {},
            onLoginInsteadClick = {},
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun RegisterScreenContentEmptySubmitPreview() {
    YoloTheme {
        RegisterScreenContent(
            state = RegisterViewState(
                emailError = UiText.Resource(Res.string.error_email_required),
                passwordError = UiText.Resource(Res.string.error_password_required),
            ),
            onEmailChange = {},
            onEmailFocusChanged = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onRegisterClick = {},
            onLoginClick = {},
            onLoginInsteadClick = {},
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun RegisterScreenContentAccountExistsPreview() {
    YoloTheme {
        RegisterScreenContent(
            state = RegisterViewState(
                email = "test@yolo.com",
                isEmailValid = true,
                password = "YoloPassword1",
                registrationError = UiText.Resource(Res.string.error_account_exists),
                isAccountConflict = true,
            ),
            onEmailChange = {},
            onEmailFocusChanged = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onRegisterClick = {},
            onLoginClick = {},
            onLoginInsteadClick = {},
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun RegisterScreenContentLoadingPreview() {
    YoloTheme {
        RegisterScreenContent(
            state = RegisterViewState(
                email = "test@yolo.com",
                isEmailValid = true,
                password = "YoloPassword1",
                isLoading = true,
            ),
            onEmailChange = {},
            onEmailFocusChanged = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onRegisterClick = {},
            onLoginClick = {},
            onLoginInsteadClick = {},
            onBackClick = {},
        )
    }
}
