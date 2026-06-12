package com.yolo.onboarding.presentation.di

import com.yolo.onboarding.presentation.OnBoardingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onBoardingPresentationModule = module {
    viewModelOf(::OnBoardingViewModel)
}
