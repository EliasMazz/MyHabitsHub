package com.yolo.habits.presentation.progress

import com.yolo.core.presentation.viewmodel.BaseViewModel

class HabitProgressViewModel : BaseViewModel<HabitProgressViewIntent, HabitProgressViewState, HabitProgressViewEvent>(
    initialState = HabitProgressViewState()
) {
    override fun onViewIntent(intent: HabitProgressViewIntent) {
        TODO("Not yet implemented")
    }
}