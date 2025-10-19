package com.yolo.myhabitshub.presentation.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.yolo.myhabitshub.data.model.UserResponse
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.btn_cancel
import com.yolo.myhabitshub.generated.resources.btn_logout_confirm
import com.yolo.myhabitshub.generated.resources.title_screen_help_and_support
import com.yolo.myhabitshub.generated.resources.ic_arrow_right
import com.yolo.myhabitshub.generated.resources.ic_profile_img_placeholder
import com.yolo.myhabitshub.generated.resources.logout
import com.yolo.myhabitshub.generated.resources.text_logout_confirmation
import com.yolo.myhabitshub.generated.resources.title_screen_sign_in
import com.yolo.myhabitshub.presentation.components.AppCardContainer
import com.yolo.myhabitshub.presentation.components.SettingItemListContainer
import com.yolo.myhabitshub.presentation.components.SmallTitle
import com.yolo.myhabitshub.presentation.components.modals.AppModalBottomSheet
import com.yolo.myhabitshub.core.presentation.theme.AppTheme
import com.yolo.myhabitshub.presentation.components.SettingsItemUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    viewState: AccountViewState,
    onLogoutDialogConfirmed: () -> Unit,
    onLogoutDialogDismissed: () -> Unit,
    onSignInClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onHelpAndSupportClicked: () -> Unit,
    onSettingsItemClicked: (SettingsItemUiState) -> Unit
) {
    if (viewState.isLogoutDialogVisible) {
        LogoutModalBottomSheet(
            onConfirm = onLogoutDialogConfirmed,
            onDismiss = onLogoutDialogDismissed
        )
    }

    AccountContent(
        modifier = modifier.fillMaxSize()
            .background(AppTheme.colors.background),
        uiState = viewState,
        onClickSignIn = onSignInClicked,
        onClickProfile = onProfileClicked,
        onHelpAndSupportClick = onHelpAndSupportClicked,
        onSettingsItemClick = onSettingsItemClicked
    )
}

@Composable
fun AccountContent(
    modifier: Modifier = Modifier,
    uiState: AccountViewState,
    onClickSignIn: () -> Unit,
    onClickProfile: () -> Unit,
    onHelpAndSupportClick: () -> Unit,
    onSettingsItemClick: (SettingsItemUiState) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = AppTheme.spacing.outerSpacing)
            .verticalScroll(rememberScrollState())
            .padding(top = AppTheme.spacing.defaultSpacing, bottom = AppTheme.spacing.outerSpacing),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing)
    ) {
        ProfileInfoBox(userResponse = uiState.userResponse, onClick = {
            if (uiState.userResponse == null) onClickSignIn()
            else onClickProfile()

        })
        SettingItemListContainer(
            itemList = uiState.settingsItemList,
            onClick = {
                when (it.textRes) {
                    Res.string.title_screen_help_and_support -> onHelpAndSupportClick()
                    else -> onSettingsItemClick(it)
                }
            }
        )
    }
}


@Composable
private fun ProfileInfoBox(userResponse: UserResponse?, onClick: () -> Unit) {
    AppCardContainer(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.horizontalItemSpacing)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(userResponse?.photoUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(Res.drawable.ic_profile_img_placeholder),
                error = painterResource(Res.drawable.ic_profile_img_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(60.dp).clip(CircleShape),
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.groupedVerticalElementSpacingSmall)
            ) {
                val displayName =
                    if (userResponse == null) stringResource(Res.string.title_screen_sign_in) else userResponse.displayName

                SmallTitle(text = displayName ?: "User Name")
                userResponse?.email?.let { email ->
                    Text(
                        email,
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.text.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = vectorResource(Res.drawable.ic_arrow_right),
                contentDescription = null,
                tint = AppTheme.colors.text.primary
            )

        }
    }
}

@Composable
private fun LogoutModalBottomSheet(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AppModalBottomSheet(
        title = stringResource(Res.string.logout),
        titleColor = AppTheme.colors.status.error,
        btnDismissText = stringResource(Res.string.btn_cancel),
        btnConfirmText = stringResource(Res.string.btn_logout_confirm),
        onConfirm = { onConfirm() },
        onDismiss = { onDismiss() },
        reverseButtonsOrder = true
    ) {
        Text(
            text = stringResource(Res.string.text_logout_confirmation),
            textAlign = TextAlign.Center,
            color = AppTheme.colors.text.primary,
            style = AppTheme.typography.h5
        )
    }
}
