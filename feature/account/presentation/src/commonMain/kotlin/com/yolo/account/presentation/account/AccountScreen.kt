package com.yolo.account.presentation.account

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
import androidx.compose.material3.MaterialTheme
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
import com.yolo.core.presentation.BaseScreen
import com.yolo.account.domain.entities.UserResponse
import myhabitshub.feature.account.presentation.generated.resources.btn_cancel
import myhabitshub.feature.account.presentation.generated.resources.btn_logout_confirm
import myhabitshub.feature.account.presentation.generated.resources.title_screen_help_and_support
import myhabitshub.feature.account.presentation.generated.resources.logout
import myhabitshub.feature.account.presentation.generated.resources.text_logout_confirmation
import myhabitshub.feature.account.presentation.generated.resources.title_screen_sign_in
import com.yolo.core.designsystem.components.legacy.AppCardContainer
import com.yolo.account.presentation.components.SettingItemListContainer
import com.yolo.core.designsystem.components.legacy.SmallTitle
import com.yolo.core.designsystem.components.legacy.modals.AppModalBottomSheet
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.section
import com.yolo.account.presentation.components.SettingsItemViewData
import myhabitshub.core.designsystem.generated.resources.Res as R
import myhabitshub.feature.account.presentation.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_arrow_right
import myhabitshub.core.designsystem.generated.resources.ic_profile_img_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = koinViewModel(),
    onNavigateToSignIn: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHelpAndSupport: () -> Unit
) {
    BaseScreen(
        viewModel = viewModel,
        handleEvent = { event ->
            when (event) {
                AccountViewEvent.NavigateToSignIn -> onNavigateToSignIn()
                AccountViewEvent.NavigateToSettings -> onNavigateToSettings()
                AccountViewEvent.NavigateToHelpAndSupport -> onNavigateToHelpAndSupport()
            }
        }
    ) { state, onIntent ->
        AccountScreenContent(
            modifier = Modifier.fillMaxSize(),
            viewState = state,
            onLogoutDialogConfirmed = { onIntent(AccountViewIntent.OnLogoutDialogConfirmed) },
            onLogoutDialogDismissed = { onIntent(AccountViewIntent.OnLogoutDialogDismissed) },
            onSignInClicked = { onIntent(AccountViewIntent.OnSignInClicked) },
            onProfileClicked = { onIntent(AccountViewIntent.OnProfileClicked) },
            onHelpAndSupportClicked = { onIntent(AccountViewIntent.OnHelpAndSupportClicked) },
            onSettingsItemClicked = { onIntent(AccountViewIntent.OnSettingsItemClicked(it)) }
        )
    }
}

@Composable
fun AccountScreenContent(
    modifier: Modifier = Modifier,
    viewState: AccountViewState,
    onLogoutDialogConfirmed: () -> Unit,
    onLogoutDialogDismissed: () -> Unit,
    onSignInClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onHelpAndSupportClicked: () -> Unit,
    onSettingsItemClicked: (SettingsItemViewData) -> Unit
) {
    if (viewState.isLogoutDialogVisible) {
        LogoutModalBottomSheet(
            onConfirm = onLogoutDialogConfirmed,
            onDismiss = onLogoutDialogDismissed
        )
    }

    AccountContent(
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.section.surfaceTintWash),
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
    onSettingsItemClick: (SettingsItemViewData) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = YoloTokens.spacing.screenEdge)
            .verticalScroll(rememberScrollState())
            .padding(top = YoloTokens.spacing.elementGap, bottom = YoloTokens.spacing.screenEdge),
        verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.sectionGap)
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
            horizontalArrangement = Arrangement.spacedBy(YoloTokens.spacing.itemGap)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(userResponse?.photoUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_profile_img_placeholder),
                error = painterResource(R.drawable.ic_profile_img_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(60.dp).clip(CircleShape),
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(YoloTokens.spacing.stackGapTight)
            ) {
                val displayName =
                    if (userResponse == null) stringResource(Res.string.title_screen_sign_in) else userResponse.displayName

                SmallTitle(text = displayName ?: "User Name")
                userResponse?.email?.let { email ->
                    Text(
                        email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = vectorResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
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
        titleColor = MaterialTheme.colorScheme.error,
        btnDismissText = stringResource(Res.string.btn_cancel),
        btnConfirmText = stringResource(Res.string.btn_logout_confirm),
        onConfirm = { onConfirm() },
        onDismiss = { onDismiss() },
        reverseButtonsOrder = true
    ) {
        Text(
            text = stringResource(Res.string.text_logout_confirmation),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
