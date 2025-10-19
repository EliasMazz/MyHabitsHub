package com.yolo.myhabitshub.presentation.screens.onboarding

import androidx.lifecycle.viewModelScope
import com.yolo.myhabitshub.data.source.preferences.UserPreferences
import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch


class OnBoardingViewModel(
    private val userPreferences: UserPreferences
) : BaseViewModel<OnBoardingViewIntent, OnBoardingViewState, OnBoardingViewEvent>(
    initialState = OnBoardingViewState(isLoading = true)
) {
    init {
        checkIfOnBoardIsShown()
    }

    private fun checkIfOnBoardIsShown() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            if (userPreferences.getBoolean(UserPreferences.KEY_IS_ONBOARD_SHOWN)) {
                updateState { copy(isLoading = false, viewEvent = OnBoardingViewEvent.OnBoardingComplete) }
            } else{
                updateState{ copy( isLoading = false) }
            }
        }
    }

    override fun onViewIntent(intent: OnBoardingViewIntent) {
        when (intent) {
            OnBoardingViewIntent.OnStartClicked -> { viewModelScope.launch { onBoardShown() } }
        }
    }

    private suspend fun onBoardShown() {
        userPreferences.putBoolean(UserPreferences.KEY_IS_ONBOARD_SHOWN, true)
        updateState { copy( isLoading = false, viewEvent = OnBoardingViewEvent.OnBoardingComplete) }
    }
}

