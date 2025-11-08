package com.yolo.myhabitshub.presentation.screens.progress

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.myhabitshub.core.presentation.ScreenContract
import com.yolo.myhabitshub.core.presentation.ScreenRoot
import kotlinx.serialization.Serializable

@Serializable
class HabitProgressScreenRoot : ScreenRoot<HabitProgressViewModel, HabitProgressViewIntent, HabitProgressViewState, HabitProgressViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: HabitProgressViewModel): ScreenContract<HabitProgressViewState, HabitProgressViewEvent> {
        return object : ScreenContract<HabitProgressViewState, HabitProgressViewEvent>{

            @Composable
            override fun ScreenView(viewState: HabitProgressViewState) {
                HabitProgressScreenView(Modifier.fillMaxSize())
            }

            override fun handleEvent(event: HabitProgressViewEvent) {
                TODO("Not yet implemented")
            }
        }
    }
}