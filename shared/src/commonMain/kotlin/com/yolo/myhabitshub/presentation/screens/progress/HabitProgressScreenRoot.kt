package com.yolo.myhabitshub.presentation.screens.progress

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot

class HabitProgressScreenRoot : ScreenRoot<HabitProgressViewModel, HabitProgressViewIntent, HabitProgressViewState, HabitProgressViewEvent> {
    @Composable
    override fun provideScreenContract(viewModel: HabitProgressViewModel): ScreenContract<HabitProgressViewState, HabitProgressViewEvent> {
        return object : ScreenContract<HabitProgressViewState, HabitProgressViewEvent>{
            @Composable
            override fun Screen(viewState: HabitProgressViewState) {
                HabitProgressScreen(Modifier.fillMaxSize())
            }

            override fun handleEvent(event: HabitProgressViewEvent) {
            }
        }
    }
}
