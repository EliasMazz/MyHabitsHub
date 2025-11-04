package com.yolo.myhabitshub.presentation.screens.settings

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
import com.yolo.myhabitshub.data.model.UserResponse
import com.yolo.myhabitshub.generated.resources.btn_delete_account
import com.yolo.myhabitshub.presentation.components.LoadingProgress
import com.yolo.myhabitshub.presentation.components.LoadingProgressMode
import com.yolo.myhabitshub.presentation.components.SettingItemListContainer
import com.yolo.myhabitshub.presentation.components.SettingsItemViewData
import com.yolo.myhabitshub.presentation.components.UserInput
import com.yolo.myhabitshub.presentation.components.modals.AppDialog
import com.yolo.myhabitshub.presentation.components.modals.DeleteUserConfirmation
import com.yolo.myhabitshub.presentation.components.modals.DialogType
import com.yolo.myhabitshub.core.presentation.theme.AppTheme
import myhabitshub.core.designsystem.generated.resources.Res as R
import com.yolo.myhabitshub.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_delete
import myhabitshub.core.designsystem.generated.resources.ic_profile_img_placeholder
import org.jetbrains.compose.resources.painterResource

@Composable
fun SettingsScreen(
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
                    .background(AppTheme.colors.background),
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
            .padding(horizontal = AppTheme.spacing.outerSpacing)
            .verticalScroll(rememberScrollState())
            .padding(top = AppTheme.spacing.defaultSpacing, bottom = AppTheme.spacing.outerSpacing),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing),
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
            itemTextStyle = AppTheme.typography.h5.copy(fontWeight = FontWeight.SemiBold),
            itemList = listOf(
                SettingsItemViewData(
                    textRes = Res.string.btn_delete_account,
                    startIcon = R.drawable.ic_delete,
                    showEndIcon = false,
                    textIconColor = AppTheme.colors.status.error
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
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.defaultSpacing)
    ) {
        Text(
            text = label,
            style = AppTheme.typography.bodyExtraLarge,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colors.text.primary
        )
        userInput()
    }
}




