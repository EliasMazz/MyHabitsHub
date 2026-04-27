package com.yolo.auth.presentation.register_success

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yolo.core.designsystem.components.brand.YoloSuccessIcon
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import com.yolo.core.designsystem.components.layout.YoloAdaptiveResultLayout
import com.yolo.core.designsystem.components.layout.YoloSimpleSuccessLayout
import com.yolo.core.designsystem.components.layout.YoloSnackbarScaffold
import com.yolo.core.presentation.MviScreen
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.account_successfully_created
import myhabitshub.feature.auth.presentation.generated.resources.login
import myhabitshub.feature.auth.presentation.generated.resources.resend_verification_email
import myhabitshub.feature.auth.presentation.generated.resources.resent_verification_email
import myhabitshub.feature.auth.presentation.generated.resources.verification_email_sent_to_x
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterSuccessScreen(
    viewModel: RegisterSuccessViewModel = koinViewModel(),
    onLoginClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    MviScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                RegisterSuccessViewEvent.NavigateToLogin -> onLoginClick()
                RegisterSuccessViewEvent.ResentVerificationEmailSuccess -> {
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
            onResendVerificationEmailClick = { onIntent(RegisterSuccessViewIntent.OnResendVerificationEmailClick) }
        )
    }
}

@Composable
fun RegisterSuccessScreenContent(
    state: RegisterSuccessViewState,
    snackbarHostState: SnackbarHostState,
    onLoginClick: () -> Unit,
    onResendVerificationEmailClick: () -> Unit,
) {
    YoloSnackbarScaffold(snackbarHostState = snackbarHostState){
        YoloAdaptiveResultLayout {
            YoloSimpleSuccessLayout(
                title = stringResource(Res.string.account_successfully_created),
                description = stringResource(
                    Res.string.verification_email_sent_to_x,
                    state.registeredEmail
                ),
                icon = { YoloSuccessIcon() },
                primaryButton = {
                    YoloButton(
                        text = stringResource(Res.string.login),
                        onClick = onLoginClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                secondaryButton = {
                    YoloButton(
                        text = stringResource(Res.string.resend_verification_email),
                        onClick = onResendVerificationEmailClick,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isResendingVerificationEmail,
                        isLoading = state.isResendingVerificationEmail,
                        style = YoloButtonStyle.SECONDARY,
                    )
                },
                secondaryError = state.resendVerificationEmailError?.value
            )
        }
    }
}

@Preview
@Composable
fun RegisterSuccessScreenPreview() {
    RegisterSuccessScreenContent(
        state = RegisterSuccessViewState(
            registeredEmail = "elias.mazzocco@gmail.com",
            isResendingVerificationEmail = false,
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onLoginClick = {},
        onResendVerificationEmailClick = {},
    )
}