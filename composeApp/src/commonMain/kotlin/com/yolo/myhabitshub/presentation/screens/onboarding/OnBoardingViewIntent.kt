package com.yolo.myhabitshub.presentation.screens.onboarding

import com.yolo.myhabitshub.core.presentation.viewmodel.BaseViewModel.ViewIntent

sealed interface OnBoardingViewIntent: ViewIntent {
    data object OnStartClicked : OnBoardingViewIntent
}