package com.yolo.onboarding.presentation

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewState
import myhabitshub.feature.onboarding.presentation.generated.resources.desc_onboarding_page_1
import myhabitshub.feature.onboarding.presentation.generated.resources.title_onboarding_page_1
import myhabitshub.core.designsystem.generated.resources.Res as R
import myhabitshub.feature.onboarding.presentation.generated.resources.Res
import myhabitshub.core.designsystem.generated.resources.ic_logo
import myhabitshub.core.designsystem.generated.resources.screenshot_example_onboarding_phone_mockup
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class OnBoardingViewState(

    val pages: List<OnBoardingScreenData> = listOf(
        OnBoardingScreenData(
            Res.string.title_onboarding_page_1,
            Res.string.desc_onboarding_page_1,
            R.drawable.screenshot_example_onboarding_phone_mockup
        ),
        OnBoardingScreenData(
            Res.string.title_onboarding_page_1,
            Res.string.desc_onboarding_page_1,
            R.drawable.ic_logo
        ),
        OnBoardingScreenData(
            Res.string.title_onboarding_page_1,
            Res.string.desc_onboarding_page_1,
            R.drawable.ic_logo
        ),
    ),
    val isLoading: Boolean = true,
): ViewState

data class OnBoardingScreenData(
    val title: StringResource,
    val description: StringResource,
    val imageRes: DrawableResource
)

