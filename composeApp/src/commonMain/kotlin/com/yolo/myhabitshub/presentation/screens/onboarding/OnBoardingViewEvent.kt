package com.yolo.myhabitshub.presentation.screens.onboarding

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewEvent

sealed interface OnBoardingViewEvent: ViewEvent {
    data object OnBoardingComplete: OnBoardingViewEvent
}