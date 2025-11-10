package com.yolo.myhabitshub.presentation.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot
import com.yolo.myhabitshub.presentation.screens.signin.SignInScreenRoot
import kotlinx.serialization.Serializable

@Serializable
class SettingsScreenRoot :
    ScreenRoot<SettingsViewModel, SettingsViewIntent, SettingsViewState, SettingsViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: SettingsViewModel): ScreenContract<SettingsViewState, SettingsViewEvent> {
        return object : ScreenContract<SettingsViewState, SettingsViewEvent> {

            @Composable
            override fun Screen(viewState: SettingsViewState) {
                SettingsScreen(
                    modifier = Modifier.fillMaxSize(),
                    viewState = viewState,
                    onDeleteAccountDialogConfirmed = { viewModel.handleIntent(SettingsViewIntent.OnDeleteAccountDialogConfirmed) },
                    onDeleteAccountDialogDismissed = {
                        viewModel.handleIntent(
                            SettingsViewIntent.OnDeleteAccountDialogDismissed
                        )
                    },
                    onErrorDialogConfirmed = { viewModel.handleIntent(SettingsViewIntent.OnErrorDialogConfirmed) },
                    onDeleteAccountClicked = { viewModel.handleIntent(SettingsViewIntent.OnDeleteAccountClicked) }
                )
            }

            override fun handleEvent(event: SettingsViewEvent, navigator: NavHostController) {
                when (event) {
                    SettingsViewEvent.NavigateToSign -> {
                        navigator.navigate(SignInScreenRoot()) {
                            popUpTo(SettingsScreenRoot()) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    }
}
