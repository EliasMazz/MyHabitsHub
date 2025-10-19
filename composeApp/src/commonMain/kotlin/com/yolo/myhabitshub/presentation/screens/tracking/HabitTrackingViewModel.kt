package com.yolo.myhabitshub.presentation.screens.tracking

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel

class HabitTrackingViewModel :
    BaseViewModel<HabitTrackingViewIntent, HabitTrackingViewState, HabitTrackingViewEvent>(
        initialState = HabitTrackingViewState()
    ) {

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