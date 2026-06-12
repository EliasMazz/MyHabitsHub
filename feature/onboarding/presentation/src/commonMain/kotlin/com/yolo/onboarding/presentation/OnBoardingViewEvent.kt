package com.yolo.onboarding.presentation

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface OnBoardingViewEvent: ViewEvent {
    data object OnBoardingComplete: OnBoardingViewEvent
}