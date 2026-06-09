package com.yolo.myhabitshub.presentation.screens.root

import com.yolo.core.domain.auth.SessionStorage
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.firstOrNull
import androidx.lifecycle.viewModelScope
import com.yolo.core.data.logging.AppLogger
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RootViewModel(
    private val sessionStorage: SessionStorage
) : BaseViewModel<RootViewIntent, RootViewState, RootViewEvent>(
    initialState = RootViewState()
) {

    private var previousRefreshToken: String? = null

    init {
        observeSession()
        viewModelScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            AppLogger.d("authInfo: $authInfo")
            updateState {
                copy(
                    isCheckingAuth = false,
                    isLoggedIn = authInfo != null
                )
            }
        }
    }

    private fun observeSession() {
        sessionStorage.observeAuthInfo().onEach { authInfo ->
            val currentRefreshToken = authInfo?.refreshToken
            val isSessionExpired = previousRefreshToken != null && currentRefreshToken == null
            if (isSessionExpired) {
                sessionStorage.set(null)
                updateState {
                    copy(
                        isLoggedIn = false,
                        viewEvent = RootViewEvent.SessionExpired
                    )
                }
            }
            previousRefreshToken = currentRefreshToken
        }.launchIn(viewModelScope)
    }


    override fun onViewIntent(intent: RootViewIntent) {}

}
