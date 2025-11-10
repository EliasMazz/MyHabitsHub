package com.yolo.myhabitshub.presentation.screens.tracking

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.yolo.core.presentation.ScreenContract
import com.yolo.core.presentation.ScreenRoot
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

            override fun handleEvent(event: HabitTrackingViewEvent, navigator: NavHostController) {}
        }
    }
}