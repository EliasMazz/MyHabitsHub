package com.yolo.myhabitshub.presentation.screens.root

import com.yolo.core.domain.auth.SessionStorage
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.firstOrNull
import androidx.lifecycle.viewModelScope
import com.yolo.core.data.logging.AppLogger
import kotlinx.coroutines.launch

class RootViewModel(
    private val sessionStorage: SessionStorage
) : BaseViewModel<RootViewIntent, RootScreenViewState, RootViewEvent>(
    initialState = RootScreenViewState()
) {

    init {
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

    override fun onViewIntent(intent: RootViewIntent) {
    }

}
