package com.yolo.myhabitshub.presentation.screens.tracking

import com.yolo.core.domain.auth.SessionStorage
import com.yolo.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class HabitTrackingViewModel(
    private val sessionStorage: SessionStorage
) :
    BaseViewModel<HabitTrackingViewIntent, HabitTrackingViewState, HabitTrackingViewEvent>(
        initialState = HabitTrackingViewState()
    ) {

    init {
        viewModelScope.launch {
            delay(5000)
            sessionStorage.set(null)
        }
    }

    override fun onViewIntent(intent: HabitTrackingViewIntent) {
        when (intent) {
            is HabitTrackingViewIntent.OnToggleHabitClicked -> handleOnClickToggleHabitCheck(intent.habitTrackingViewState)
        }
    }

    private fun handleOnClickToggleHabitCheck(habitTrackingViewState: HabitTrackingItemViewState) {
        updateState {
            val updatedInternalList = this.listHabitTracking.map { item ->
                item.copy(
                    isChecked = if (item.habitTracking.id == habitTrackingViewState.habitTracking.id)
                        !item.isChecked else item.isChecked
                )
            }
            this.copy(listHabitTracking = updatedInternalList)
        }
    }
}