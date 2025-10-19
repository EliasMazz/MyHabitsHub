package com.yolo.myhabitshub.presentation.screens.tracking

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewIntent

interface HabitTrackingViewIntent: ViewIntent {

    data class OnToggleHabitClicked(val habitTrackingViewState: HabitTrackingItemViewState): HabitTrackingViewIntent
}