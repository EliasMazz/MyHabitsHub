package com.yolo.account.presentation.account

import androidx.lifecycle.viewModelScope
import com.yolo.core.domain.usecase.invoke
import com.yolo.account.domain.usecase.GetUserStream
import com.yolo.account.domain.usecase.LogOutUseCase
import com.yolo.account.presentation.components.SettingsAction
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userStream: GetUserStream,
    private val logOutUseCase: LogOutUseCase
) : BaseViewModel<AccountViewIntent, AccountViewState, AccountViewEvent>(
    initialState = AccountViewState()
) {
    init {
        collectCurrentUserFlow()
    }

    private fun collectCurrentUserFlow() {
        viewModelScope.launch {
            userStream.invoke()
                .collect { result ->
                    result.onSuccess { user ->
                        updateState {
                            copy(userResponse = if (user.isAnonymous) null else user)
                        }
                    }.onFailure { error ->
                        updateState {
                            copy(userResponse = null)
                        }
                    }
                }
        }
    }

    override fun onViewIntent(intent: AccountViewIntent) {
        when (intent) {
            AccountViewIntent.OnLogoutDialogDismissed ->
                updateState { copy(isLogoutDialogVisible = false) }

            AccountViewIntent.OnProfileClicked ->
                sendEvent(AccountViewEvent.NavigateToSettings)

            AccountViewIntent.OnSignInClicked ->
                sendEvent(AccountViewEvent.NavigateToSignIn)

            AccountViewIntent.OnHelpAndSupportClicked ->
                sendEvent(AccountViewEvent.NavigateToHelpAndSupport)

            AccountViewIntent.OnLogoutDialogConfirmed ->
                viewModelScope.launch {
                    updateState { copy(isLogoutDialogVisible = false) }
                    logOutUseCase.invoke()
                }

            is AccountViewIntent.OnSettingsItemClicked -> {
                when (intent.item.action) {
                    SettingsAction.HELP_AND_SUPPORT ->
                        sendEvent(AccountViewEvent.NavigateToHelpAndSupport)
                    SettingsAction.LOGOUT ->
                        updateState { copy(isLogoutDialogVisible = true) }
                    else -> Unit
                }
            }
        }
    }
}