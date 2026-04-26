package com.yolo.myhabitshub.presentation.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot

class SettingsScreenRoot(
    private val onNavigateToSignIn: () -> Unit = {},
) : ScreenRoot<SettingsViewModel, SettingsViewIntent, SettingsViewState, SettingsViewEvent> {

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

            override suspend fun handleEvent(event: SettingsViewEvent) {
                when (event) {
                    SettingsViewEvent.NavigateToSign -> onNavigateToSignIn()
                }
            }
        }
    }
}
