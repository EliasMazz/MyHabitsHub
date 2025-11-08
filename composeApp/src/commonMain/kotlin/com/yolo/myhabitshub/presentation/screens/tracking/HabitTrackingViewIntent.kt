package com.yolo.myhabitshub.presentation.screens.tracking

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewIntent

interface HabitTrackingViewIntent: ViewIntent {

    data class OnToggleHabitClicked(val habitTrackingViewState: HabitTrackingItemViewState): HabitTrackingViewIntent
}