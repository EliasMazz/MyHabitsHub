package com.yolo.auth.presentation.forgot_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.yolo.core.designsystem.components.brand.YoloBrandLogo
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.layout.YoloAdaptiveFormLayout
import com.yolo.core.designsystem.components.textfields.YoloTextField
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.presentation.BaseScreen
import com.yolo.core.presentation.util.UiText
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.email
import myhabitshub.feature.auth.presentation.generated.resources.email_placeholder
import myhabitshub.feature.auth.presentation.generated.resources.forgot_password
import myhabitshub.feature.auth.presentation.generated.resources.submit
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                ForgotPasswordViewEvent.NavigateBack -> onBack()
            }
        }
    ) { state, onIntent ->
        ForgotPasswordScreenContent(
            state = state,
            onEmailChange = { onIntent(ForgotPasswordViewIntent.OnEmailChange(it)) },
            onSubmitClick = { onIntent(ForgotPasswordViewIntent.OnSubmitClick) }
        )
    }
}

@Composable
fun ForgotPasswordScreenContent(
    state: ForgotPasswordViewState,
    onEmailChange: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    YoloAdaptiveFormLayout(
        headerText = stringResource(Res.string.forgot_password),
        errorText = state.errorText?.value,
        logo = {
            YoloBrandLogo()
        },
        formContent = {
            YoloTextField(
                value = state.email,
                onValueChange = onEmailChange,
                placeholder = stringResource(Res.string.email_placeholder),
                title = stringResource(Res.string.email),
                isError = state.emailError != null,
                supportingText = state.emailError?.value,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            YoloButton(
                text = stringResource(Res.string.submit),
                onClick = onSubmitClick,
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.canSubmit && !state.isLoading,
            )
        }
    )

}

@Preview
@Composable
private fun ForgotPasswordScreenContentPreview() {
    YoloTheme {
        ForgotPasswordScreenContent(
            state = ForgotPasswordViewState(),
            onEmailChange = {},
            onSubmitClick = {}
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenContentFilledPreview() {
    YoloTheme {
        ForgotPasswordScreenContent(
            state = ForgotPasswordViewState(
                email = "test@yolo.com",
                canSubmit = true
            ),
            onEmailChange = {},
            onSubmitClick = {}
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
                emailError = UiText.Message("Invalid email address"),
                canSubmit = false
            ),
            onEmailChange = {},
            onSubmitClick = {}
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
                isLoading = true,
                canSubmit = true
            ),
            onEmailChange = {},
            onSubmitClick = {}
        )
    }
}
