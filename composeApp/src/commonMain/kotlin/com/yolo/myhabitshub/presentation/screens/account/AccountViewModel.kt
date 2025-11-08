package com.yolo.myhabitshub.presentation.screens.account

import androidx.lifecycle.viewModelScope
import com.yolo.myhabitshub.core.domain.usecase.invoke
import com.yolo.myhabitshub.domain.usecase.GetUserStream
import com.yolo.myhabitshub.domain.usecase.LogOutUseCase
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.logout
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
                updateState { copy(viewEvent = AccountViewEvent.NavigateToSettings) }

            AccountViewIntent.OnSignInClicked ->
                updateState { copy(viewEvent = AccountViewEvent.NavigateToSignIn) }

            AccountViewIntent.OnHelpAndSupportClicked ->
                updateState { copy(viewEvent = AccountViewEvent.NavigateToHelpAndSupport) }

            AccountViewIntent.OnLogoutDialogConfirmed ->
                viewModelScope.launch {
                    updateState { copy(isLogoutDialogVisible = false) }
                    logOutUseCase.invoke()
                }

            is AccountViewIntent.OnSettingsItemClicked -> {
                when (intent.item.textRes) {
                    Res.string.logout -> {
                        updateState { copy(isLogoutDialogVisible = true) }
                    }

                    else -> {
                        //TODO
                    }
                }
            }
        }
    }
}