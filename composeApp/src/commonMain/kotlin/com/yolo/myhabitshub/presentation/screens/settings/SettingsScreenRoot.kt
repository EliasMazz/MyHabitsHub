package com.yolo.myhabitshub.presentation.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.myhabitshub.core.presentation.ScreenContract
import com.yolo.myhabitshub.core.presentation.ScreenRoot
import com.yolo.myhabitshub.presentation.screens.signin.SignInScreenRoot
import com.yolo.myhabitshub.root.LocalNavigator
import kotlinx.serialization.Serializable

@Serializable
class SettingsScreenRoot :
    ScreenRoot<SettingsViewModel, SettingsViewIntent, SettingsViewState, SettingsViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: SettingsViewModel): ScreenContract<SettingsViewState, SettingsViewEvent> {
        val navigator = LocalNavigator.current

        return object : ScreenContract<SettingsViewState, SettingsViewEvent> {

            @Composable
            override fun ScreenView(viewState: SettingsViewState) {
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

            override fun handleEvent(event: SettingsViewEvent) {
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
