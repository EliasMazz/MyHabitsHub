package com.yolo.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import com.yolo.core.designsystem.components.layout.YoloAdaptiveFormLayout
import com.yolo.core.designsystem.components.textfields.YoloPasswordTextField
import com.yolo.core.designsystem.components.textfields.YoloTextField
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.presentation.BaseScreen
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.create_account
import myhabitshub.feature.auth.presentation.generated.resources.email
import myhabitshub.feature.auth.presentation.generated.resources.email_placeholder
import myhabitshub.feature.auth.presentation.generated.resources.forgot_password
import myhabitshub.feature.auth.presentation.generated.resources.login
import myhabitshub.feature.auth.presentation.generated.resources.password
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    loginSuccessEvent: () -> Unit,
    navigateToForgotPasswordEvent: () -> Unit,
    navigateToRegisterEvent: () -> Unit,
) {
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                LoginEvent.NavigateToForgotPasswordEvent -> navigateToForgotPasswordEvent()
                LoginEvent.NavigateToRegisterEvent -> navigateToRegisterEvent()
                LoginEvent.LoginSuccessEvent -> loginSuccessEvent()
            }
        }
    ) { state, onIntent ->
        LoginScreenContent(
            state = state,
            onTogglePasswordVisibility = { onIntent(LoginIntent.OnTogglePasswordVisibility) },
            onForgotPasswordClick = { onIntent(LoginIntent.OnForgotPasswordClick) },
            onLoginClick = { onIntent(LoginIntent.OnLoginClick) },
            onSignupClick = { onIntent(LoginIntent.OnSignupClick) },
            onPasswordChange = { onIntent(LoginIntent.OnPasswordChange(it)) },
            onEmailChange = { onIntent(LoginIntent.OnEmailChange(it)) },
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
    onPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
) {
    YoloAdaptiveFormLayout(
        logo = { YoloBrandLogo() },
        modifier = Modifier.fillMaxSize(),
        errorText = state.errorText?.value,
        formContent = {
            YoloTextField(
                value = state.email,
                onValueChange = onEmailChange,
                singleLine = true,
                placeholder = stringResource(Res.string.email_placeholder),
                title = stringResource(Res.string.email),
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            YoloPasswordTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                placeholder = stringResource(Res.string.password),
                title = stringResource(Res.string.password),
                onToggleVisibilityClick = onTogglePasswordVisibility,
                isPasswordVisible = state.isPasswordVisible,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.forgot_password),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.align(Alignment.End)
                    .clickable {
                        onForgotPasswordClick.invoke()
                    },
            )

            Spacer(modifier = Modifier.height(24.dp))

            YoloButton(
                text = stringResource(Res.string.login),
                onClick = onLoginClick,
                enabled = !state.isLoggingIn,
                isLoading = state.isLoggingIn,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            YoloButton(
                text = stringResource(Res.string.create_account),
                onClick = onSignupClick,
                style = YoloButtonStyle.SECONDARY,
                modifier = Modifier.fillMaxWidth()
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
            ),
            onTogglePasswordVisibility = {},
            onForgotPasswordClick = {},
            onLoginClick = {},
            onSignupClick = {},
            onPasswordChange = {},
            onEmailChange = {},
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
            ),
            onTogglePasswordVisibility = { },
            onForgotPasswordClick = {},
            onLoginClick = {},
            onSignupClick = {},
            onPasswordChange = {},
            onEmailChange = {},
        )
    }
}