package com.yolo.myhabitshub.presentation.screens.progress

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot
import com.yolo.core.presentation.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
class HabitProgressScreenRoot : ScreenRoot<HabitProgressViewModel, HabitProgressViewIntent, HabitProgressViewState, HabitProgressViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: HabitProgressViewModel): ScreenContract<HabitProgressViewState, HabitProgressViewEvent> {
        return object : ScreenContract<HabitProgressViewState, HabitProgressViewEvent>{

            @Composable
            override fun Screen(viewState: HabitProgressViewState) {
                HabitProgressScreen(Modifier.fillMaxSize())
            }

            override fun handleEvent(event: HabitProgressViewEvent, navigator: Navigator) {
                TODO("Not yet implemented")
            }
        }
    }
}