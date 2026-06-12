package com.yolo.onboarding.presentation

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewIntent

sealed interface OnBoardingViewIntent: ViewIntent {
    data object OnStartClicked : OnBoardingViewIntent
}