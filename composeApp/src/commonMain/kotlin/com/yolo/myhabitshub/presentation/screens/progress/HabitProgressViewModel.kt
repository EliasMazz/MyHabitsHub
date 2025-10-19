package com.yolo.myhabitshub.presentation.screens.progress

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel

class HabitProgressViewModel : BaseViewModel<HabitProgressViewIntent, HabitProgressViewState, HabitProgressViewEvent>(
    initialState = HabitProgressViewState()
) {
    override fun onViewIntent(intent: HabitProgressViewIntent) {
        TODO("Not yet implemented")
    }
}