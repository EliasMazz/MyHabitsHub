package com.yolo.myhabitshub.presentation.screens.account
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot

class AccountScreenRoot(
    private val onNavigateToSignIn: () -> Unit = {},
    private val onNavigateToSettings: () -> Unit = {},
    private val onNavigateToHelpAndSupport: () -> Unit = {},
) : ScreenRoot<AccountViewModel, AccountViewIntent, AccountViewState, AccountViewEvent> {
    @Composable
    override fun provideScreenContract(viewModel: AccountViewModel): ScreenContract<AccountViewState, AccountViewEvent> {
        return object : ScreenContract<AccountViewState, AccountViewEvent> {
            @Composable
            override fun Screen(viewState: AccountViewState) {
                AccountScreen(
                    modifier = Modifier.fillMaxSize(),
                    viewState = viewState,
                    onLogoutDialogConfirmed = { viewModel.handleIntent(AccountViewIntent.OnLogoutDialogConfirmed) },
                    onLogoutDialogDismissed = { viewModel.handleIntent(AccountViewIntent.OnLogoutDialogDismissed) },
                    onSignInClicked = { viewModel.handleIntent(AccountViewIntent.OnSignInClicked) },
                    onProfileClicked = { viewModel.handleIntent(AccountViewIntent.OnProfileClicked) },
                    onHelpAndSupportClicked = { viewModel.handleIntent(AccountViewIntent.OnHelpAndSupportClicked) },
                    onSettingsItemClicked = { viewModel.handleIntent(AccountViewIntent.OnSettingsItemClicked(it)) }
                )
            }
            override suspend fun handleEvent(event: AccountViewEvent) {
                when (event) {
                    AccountViewEvent.NavigateToSignIn -> onNavigateToSignIn()
                    AccountViewEvent.NavigateToSettings -> onNavigateToSettings()
                    AccountViewEvent.NavigateToHelpAndSupport -> onNavigateToHelpAndSupport()
                }
            }
        }
    }
}
