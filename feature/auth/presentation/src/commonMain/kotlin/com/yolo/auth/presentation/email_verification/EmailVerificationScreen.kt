package com.yolo.auth.presentation.email_verification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yolo.auth.presentation.email_verification.EmailVerificationViewState
import com.yolo.core.designsystem.components.brand.YoloFailureIcon
import com.yolo.core.designsystem.components.brand.YoloSuccessIcon
import com.yolo.core.designsystem.components.buttons.YoloButton
import com.yolo.core.designsystem.components.buttons.YoloButtonStyle
import com.yolo.core.designsystem.components.layout.YoloAdaptiveResultLayout
import com.yolo.core.designsystem.components.layout.YoloSimpleResultLayout
import com.yolo.core.designsystem.theme.YoloTheme
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.presentation.MviScreen
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.close
import myhabitshub.feature.auth.presentation.generated.resources.email_verified_failed
import myhabitshub.feature.auth.presentation.generated.resources.email_verified_failed_desc
import myhabitshub.feature.auth.presentation.generated.resources.email_verified_successfully
import myhabitshub.feature.auth.presentation.generated.resources.email_verified_successfully_desc
import myhabitshub.feature.auth.presentation.generated.resources.login
import myhabitshub.feature.auth.presentation.generated.resources.verifying_account
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EmailVerificationScreen(
    viewModel: EmailVerificationViewModel = koinViewModel(),
) {
    MviScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {


                else -> {}
            }
        }
    ) { state, onIntent ->
        EmailVerificationScreenContent(
            state = state,
            onLoginClicked = { onIntent(EmailVerificationViewIntent.OnLoginClick) },
            onCloseClicked = { onIntent(EmailVerificationViewIntent.OnCloseClick) }
        )
    }
}

@Composable
fun EmailVerificationScreenContent(
    state: EmailVerificationViewState,
    onLoginClicked: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    YoloAdaptiveResultLayout {
        when {
            state.isVerifying -> {
                VerifyingContent(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            state.isVerified -> {
                YoloSimpleResultLayout(
                    title = stringResource(Res.string.email_verified_successfully),
                    description = stringResource(Res.string.email_verified_successfully_desc),
                    icon = {
                        YoloSuccessIcon()
                    },
                    primaryButton = {
                        YoloButton(
                            text = stringResource(Res.string.login),
                            onClick = onLoginClicked,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                )
            }

            else -> {
                YoloSimpleResultLayout(
                    title = stringResource(Res.string.email_verified_failed),
                    description = stringResource(Res.string.email_verified_failed_desc),
                    icon = {
                        Spacer(modifier = Modifier.height(32.dp))
                        YoloFailureIcon(
                            modifier = Modifier
                                .size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    },
                    primaryButton = {
                        YoloButton(
                            text = stringResource(Res.string.close),
                            onClick = onCloseClicked,
                            modifier = Modifier.fillMaxWidth(),
                            style = YoloButtonStyle.SECONDARY
                        )
                    }
                )
            }
        }

    }
}

@Composable
private fun VerifyingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .heightIn(200.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(Res.string.verifying_account),
            color = MaterialTheme.colorScheme.extended.textSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun EmailVerificationErrorPreview() {
    YoloTheme {
        EmailVerificationScreenContent(
            state = EmailVerificationViewState(),
            onLoginClicked = {},
            onCloseClicked = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationVerifyingPreview() {
    YoloTheme {
        EmailVerificationScreenContent(
            state = EmailVerificationViewState(
                isVerifying = true
            ),
            onLoginClicked = {},
            onCloseClicked = {},
        )
    }
}

@Preview
@Composable
private fun EmailVerificationSuccessPreview() {
    YoloTheme {
        EmailVerificationScreenContent(
            state = EmailVerificationViewState(
                isVerified = true
            ),
            onLoginClicked = {},
            onCloseClicked = {},
        )
    }
}