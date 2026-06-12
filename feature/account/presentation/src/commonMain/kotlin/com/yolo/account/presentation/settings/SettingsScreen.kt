package com.yolo.account.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.yolo.core.presentation.BaseScreen
import com.yolo.account.domain.entities.UserResponse
import myhabitshub.feature.account.presentation.generated.resources.btn_delete_account
import com.yolo.core.designsystem.components.legacy.LoadingProgress
import com.yolo.core.designsystem.components.legacy.LoadingProgressMode
import com.yolo.account.presentation.components.SettingItemListContainer
import com.yolo.account.presentation.components.SettingsAction
import com.yolo.account.presentation.components.SettingsItemViewData
import com.yolo.core.designsystem.components.legacy.UserInput
import com.yolo.core.designsystem.components.legacy.modals.AppDialog
import com.yolo.account.presentation.components.DeleteUserConfirmation
import com.yolo.core.designsystem.components.legacy.modals.DialogType
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.section
import myhabitshub.core.designsystem.generated.resources.Res as R
import myhabitshub.feature.account.presentation.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_delete
import myhabitshub.core.designsystem.generated.resources.ic_profile_img_placeholder
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateToSignIn: () -> Unit,
) {
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                SettingsViewEvent.NavigateToSign -> onNavigateToSignIn()
            }
        }
    ) { state, onIntent ->
        SettingsScreenContent(
            modifier = Modifier.fillMaxSize(),
            viewState = state,
            onDeleteAccountDialogConfirmed = { onIntent(SettingsViewIntent.OnDeleteAccountDialogConfirmed) },
            onDeleteAccountDialogDismissed = {
                onIntent(SettingsViewIntent.OnDeleteAccountDialogDismissed)
            },
            onErrorDialogConfirmed = { onIntent(SettingsViewIntent.OnErrorDialogConfirmed) },
            onDeleteAccountClicked = { onIntent(SettingsViewIntent.OnDeleteAccountClicked) }
        )
    }
}

@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    viewState: SettingsViewState,
    onDeleteAccountDialogConfirmed: () -> Unit,
    onDeleteAccountDialogDismissed: () -> Unit,
    onErrorDialogConfirmed: () -> Unit,
    onDeleteAccountClicked: (SettingsItemViewData) -> Unit,
) {
    if (viewState.deleteUserDialogShown) {
        DeleteUserConfirmation(
            onConfirm = onDeleteAccountDialogConfirmed ,
            onDismiss = onDeleteAccountDialogDismissed
        )
    }
    AppDialog(
        type = DialogType.ERROR,
        text = viewState.errorMessage,
        onConfirm = onErrorDialogConfirmed
    )
    if (viewState.isLoading) {
        LoadingProgress(mode = LoadingProgressMode.FULLSCREEN)
    } else {
        val currentUser = viewState.userResponse
        currentUser?.let {
            SettingsScreenContent(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.section.surfaceTintWash),
                currentUserResponse = it,
                onClickDeleteAccount = onDeleteAccountClicked
            )
        }
    }

}

@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    currentUserResponse: UserResponse,
    onClickDeleteAccount: (SettingsItemViewData) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = YoloTokens.spacing.screenEdge)
            .verticalScroll(rememberScrollState())
            .padding(top = YoloTokens.spacing.elementGap, bottom = YoloTokens.spacing.screenEdge),
        verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.sectionGap),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(currentUserResponse.photoUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_profile_img_placeholder),
            error = painterResource(R.drawable.ic_profile_img_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(100.dp).clip(CircleShape),
        )
        UserInputWithLabel(label = "Display Name"){
            UserInput(
                value = currentUserResponse.displayName ?: "",
                readOnly = true,
                onValueChange = {}
            )
        }
        UserInputWithLabel(label = "Email"){
            UserInput(
                value = currentUserResponse.email ?: "",
                readOnly = true,
                onValueChange = {}
            )
        }

        SettingItemListContainer(
            onClick = onClickDeleteAccount,
            itemTextStyle = MaterialTheme.typography.titleLarge,
            itemList = listOf(
                SettingsItemViewData(
                    action = SettingsAction.DELETE_ACCOUNT,
                    textRes = Res.string.btn_delete_account,
                    startIcon = R.drawable.ic_delete,
                    showEndIcon = false,
                    textIconColor = MaterialTheme.colorScheme.error
                )
            )
        )
    }
}

@Composable
fun UserInputWithLabel(
    label: String,
    userInput: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.elementGap)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        userInput()
    }
}




