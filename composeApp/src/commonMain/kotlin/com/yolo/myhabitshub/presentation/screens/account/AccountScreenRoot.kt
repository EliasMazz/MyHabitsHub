package com.yolo.myhabitshub.presentation.screens.account

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.myhabitshub.core.presentation.ScreenContract
import com.yolo.myhabitshub.root.LocalNavigator
import com.yolo.myhabitshub.core.presentation.ScreenRoot
import com.yolo.myhabitshub.presentation.screens.helpandsupport.HelpAndSupportScreenRoot
import com.yolo.myhabitshub.presentation.screens.settings.SettingsScreenRoot
import com.yolo.myhabitshub.presentation.screens.signin.SignInScreenRoot
import kotlinx.serialization.Serializable

@Serializable
class AccountScreenRoot : ScreenRoot<AccountViewModel, AccountViewIntent, AccountViewState, AccountViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: AccountViewModel): ScreenContract<AccountViewState, AccountViewEvent> {
        val navigator = LocalNavigator.current

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

            override fun handleEvent(event: AccountViewEvent) {
                when (event) {
                    AccountViewEvent.NavigateToHelpAndSupport -> navigator.navigate(HelpAndSupportScreenRoot())
                    AccountViewEvent.NavigateToSettings -> navigator.navigate(SettingsScreenRoot())
                    AccountViewEvent.NavigateToSignIn -> navigator.navigate(SignInScreenRoot())
                }
            }
        }
    }
}
