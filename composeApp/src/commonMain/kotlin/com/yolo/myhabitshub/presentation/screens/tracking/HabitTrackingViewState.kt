package com.yolo.myhabitshub.presentation.screens.tracking

import com.yolo.core.presentation.viewmodel.BaseViewModel.*
import com.yolo.myhabitshub.domain.model.HabitTracking

data class HabitTrackingViewState(
    val isLoading: Boolean = false,
    val listHabitTracking: List<HabitTrackingItemViewState> = listOf(
        HabitTrackingItemViewState(
            habitTracking = HabitTracking(
                id = 0,
                title = "GYM"
            ),
        ),
        HabitTrackingItemViewState(
            habitTracking = HabitTracking(
                id = 1,
                title = "Meditate"
            ),
        ),
        HabitTrackingItemViewState(
            habitTracking = HabitTracking(
                id = 2,
                title = "Drink water"
            ),
        )
    ),
    override val viewEvent: HabitTrackingViewEvent? = null
): ViewState<HabitTrackingViewEvent>{
    override fun consumeEvent(): ViewState<HabitTrackingViewEvent> {
       return this.copy(viewEvent = null)
    }
}