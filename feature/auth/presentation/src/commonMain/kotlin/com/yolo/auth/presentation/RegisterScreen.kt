package com.yolo.auth.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import com.yolo.core.designsystem.components.layout.YoloAdaptiveFormLayout
import com.yolo.core.designsystem.components.layout.YoloSnackbarScaffold
import com.yolo.core.designsystem.components.textfields.YoloPasswordTextField
import com.yolo.core.designsystem.components.textfields.YoloTextField
import com.yolo.core.designsystem.theme.YoloTheme
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.email
import myhabitshub.feature.auth.presentation.generated.resources.email_placeholder
import myhabitshub.feature.auth.presentation.generated.resources.login
import myhabitshub.feature.auth.presentation.generated.resources.password
import myhabitshub.feature.auth.presentation.generated.resources.password_hint
import myhabitshub.feature.auth.presentation.generated.resources.register
import myhabitshub.feature.auth.presentation.generated.resources.welcome
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RegisterScreen(
    state: RegisterViewState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onRegisterClick: () -> Unit,
    onInputTextFocusGain: () -> Unit,
    onTogglePasswordVisibility: () -> Unit,
) {
    YoloSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        YoloAdaptiveFormLayout(
            headerText = stringResource(Res.string.welcome),
            errorText = state.registrationError?.value,
            logo = { YoloBrandLogo() },
        ) {
            YoloTextField(
                state = state.emailTextState,
                singleLine = true,
                placeholder = stringResource(Res.string.email_placeholder),
                title = stringResource(Res.string.email),
                supportingText = state.emailError?.value,
                isError = state.emailError != null,
                onFocusChanged = { isFocused ->
                    onInputTextFocusGain.invoke()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            YoloPasswordTextField(
                state = state.passwordTextState,
                placeholder = stringResource(Res.string.password),
                title = stringResource(Res.string.password),
                supportingText = state.passwordError?.value
                    ?: stringResource(Res.string.password_hint),
                isError = state.emailError != null,
                onFocusChanged = { isFocused ->
                    onInputTextFocusGain.invoke()
                },
                onToggleVisibilityClick = onTogglePasswordVisibility,
                isPasswordVisible = state.isPasswordVisible
            )

            Spacer(modifier = Modifier.height(16.dp))
            YoloButton(
                text = stringResource(Res.string.register),
                enabled = state.canRegister,
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth(),
                onClick = onRegisterClick
            )

            Spacer(modifier = Modifier.height(8.dp))
            YoloButton(
                text = stringResource(Res.string.login),
                isLoading = state.isLoading,
                style = YoloButtonStyle.SECONDARY,
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
@Preview
fun RegisterScreenPreview() {
    YoloTheme {
        RegisterScreen(
            state = RegisterViewState(),
            onRegisterClick = { },
            onInputTextFocusGain = { },
            onTogglePasswordVisibility = { }
        )
    }
}