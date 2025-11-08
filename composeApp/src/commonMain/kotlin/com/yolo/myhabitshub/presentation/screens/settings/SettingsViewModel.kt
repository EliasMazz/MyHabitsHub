package com.yolo.myhabitshub.presentation.screens.settings

import androidx.lifecycle.viewModelScope
import com.yolo.myhabitshub.core.domain.usecase.invoke
import com.yolo.myhabitshub.domain.exceptions.UnAuthorizedException
import com.yolo.myhabitshub.domain.usecase.DeleteAccountUseCase
import com.yolo.myhabitshub.domain.usecase.GetUserStream
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getUserStream: GetUserStream,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : BaseViewModel<SettingsViewIntent, SettingsViewState, SettingsViewEvent>(
    initialState = SettingsViewState(isLoading = true)
) {

    init {
        collectCurrentUserFlow()
    }

    private fun collectCurrentUserFlow() {
        viewModelScope.launch {
            getUserStream.invoke()
                .collect { result ->
                    result.onSuccess { user ->
                        updateState { copy(isLoading = false, userResponse = user) }
                    }.onFailure { error ->
                        if (error is UnAuthorizedException) {
                            updateState { copy(isLoading = false, userResponse = null, viewEvent = SettingsViewEvent.NavigateToSign) }
                        } else {
                            updateState { copy(isLoading = false, errorMessage = error.message) }
                        }
                    }
                }
        }
    }

    override fun onViewIntent(intent: SettingsViewIntent) {
        when (intent) {
            SettingsViewIntent.OnDeleteAccountClicked -> updateState { copy(deleteUserDialogShown = true) }
            SettingsViewIntent.OnDeleteAccountDialogDismissed -> updateState { copy(deleteUserDialogShown = false) }
            SettingsViewIntent.OnDeleteAccountDialogConfirmed -> handleConfirmDeleteAccount()
            SettingsViewIntent.OnErrorDialogConfirmed -> updateState { copy(errorMessage = null) }
        }
    }

    private fun handleConfirmDeleteAccount() = viewModelScope.launch {
        updateState { copy(deleteUserDialogShown = false, isLoading = true) }
        viewModelScope.launch {
            deleteAccountUseCase.invoke()
        }
    }

    /*
  //TODO Handle error  when deleting account
  .onSuccess {
              AppLogger.d("Account is deleted successfully")
              updateState { copy(isLoading = false, user = null) }
          }
          .onFailure { error ->
              AppLogger.d("Account deletion failed ${error.message}")
              if (error is FirebaseAuthRecentLoginRequiredException) {
                  updateState { copy(isLoading = false, user = null) }
              } else
                  updateState { copy(isLoading = false, errorMessage = error.message) }
          }
   */

}