package com.yolo.auth.presentation.welcome

import com.yolo.core.presentation.viewmodel.BaseViewModel

data class WelcomeViewState(
    val showGoogleButton: Boolean = false,
    val showAppleButton: Boolean = false,
    /** OAuth Web client ID for Credential Manager (Android). Empty in previews. */
    val googleServerClientId: String = "",
    /** True while exchanging the social credential with the backend. */
    val isAuthenticating: Boolean = false,
) : BaseViewModel.ViewState
