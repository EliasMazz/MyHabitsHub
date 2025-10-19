package com.yolo.myhabitshub.presentation.screens.account

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.myhabitshub.core.presentation.ScreenContract
import com.yolo.myhabitshub.root.LocalNavigator
import com.yolo.myhabitshub.core.presentation.ScreenRoute
import com.yolo.myhabitshub.presentation.screens.helpandsupport.HelpAndSupportScreenRoute
import com.yolo.myhabitshub.presentation.screens.settings.SettingsScreenRoute
import com.yolo.myhabitshub.presentation.screens.signin.SignInScreenRoute
import kotlinx.serialization.Serializable

@Serializable
class AccountScreenRoute : ScreenRoute<AccountViewModel, AccountViewIntent, AccountViewState, AccountViewEvent> {

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
                    AccountViewEvent.NavigateToHelpAndSupport -> navigator.navigate(HelpAndSupportScreenRoute())
                    AccountViewEvent.NavigateToSettings -> navigator.navigate(SettingsScreenRoute())
                    AccountViewEvent.NavigateToSignIn -> navigator.navigate(SignInScreenRoute())
                }
            }
        }
    }
}
