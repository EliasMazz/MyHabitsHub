package com.yolo.myhabitshub.presentation.screens.onboarding

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface OnBoardingViewEvent: ViewEvent {
    data object OnBoardingComplete: OnBoardingViewEvent
}