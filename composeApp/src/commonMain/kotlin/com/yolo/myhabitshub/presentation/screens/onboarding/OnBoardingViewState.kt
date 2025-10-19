package com.yolo.myhabitshub.presentation.screens.onboarding

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewState
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.desc_onboarding_page_1
import com.yolo.myhabitshub.generated.resources.ic_logo
import com.yolo.myhabitshub.generated.resources.screenshot_example_onboarding_phone_mockup
import com.yolo.myhabitshub.generated.resources.title_onboarding_page_1
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class OnBoardingViewState(

    val pages: List<OnBoardingScreenData> = listOf(
        OnBoardingScreenData(
            Res.string.title_onboarding_page_1,
            Res.string.desc_onboarding_page_1,
            Res.drawable.screenshot_example_onboarding_phone_mockup
        ),
        OnBoardingScreenData(
            Res.string.title_onboarding_page_1,
            Res.string.desc_onboarding_page_1,
            Res.drawable.ic_logo
        ),
        OnBoardingScreenData(
            Res.string.title_onboarding_page_1,
            Res.string.desc_onboarding_page_1,
            Res.drawable.ic_logo
        ),
    ),
    val isLoading: Boolean = true,
    override val viewEvent: OnBoardingViewEvent? = null
): ViewState<OnBoardingViewEvent>{
    override fun consumeEvent(): ViewState<OnBoardingViewEvent> {
      return this.copy(viewEvent = null)
    }
}

data class OnBoardingScreenData(
    val title: StringResource,
    val description: StringResource,
    val imageRes: DrawableResource
)

