package com.yolo.myhabitshub.presentation.screens.progress

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.*

data class HabitProgressViewState(
    override val viewEvent: HabitProgressViewEvent? = null
) : ViewState<HabitProgressViewEvent>{
    override fun consumeEvent(): ViewState<HabitProgressViewEvent> {
        return this.copy(viewEvent = null)
    }
}