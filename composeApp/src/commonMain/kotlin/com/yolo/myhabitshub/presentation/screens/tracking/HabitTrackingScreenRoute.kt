package com.yolo.myhabitshub.presentation.screens.tracking

import androidx.compose.runtime.Composable
import com.yolo.myhabitshub.core.presentation.ScreenContract
import com.yolo.myhabitshub.core.presentation.ScreenRoot
import kotlinx.serialization.Serializable

@Serializable
class HabitTrackingScreenRoot :
    ScreenRoot<HabitTrackingViewModel, HabitTrackingViewIntent, HabitTrackingViewState, HabitTrackingViewEvent> {

    @Composable
    override fun provideScreenContract(viewModel: HabitTrackingViewModel): ScreenContract<HabitTrackingViewState, HabitTrackingViewEvent> {


        return object : ScreenContract<HabitTrackingViewState, HabitTrackingViewEvent> {

            @Composable
            override fun Screen(viewState: HabitTrackingViewState) {
                HabitTrackingScreen(
                    viewState = viewState,
                    onToggleHabitClicked = {
                        viewModel.handleIntent(HabitTrackingViewIntent.OnToggleHabitClicked(it))
                    },
                    onHabitDetailsClicked = {}
                )
            }

            override fun handleEvent(event: HabitTrackingViewEvent) {}
        }
    }
}