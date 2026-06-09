package com.yolo.myhabitshub.app

import com.yolo.core.presentation.viewmodel.BaseViewModel.ViewIntent

sealed interface AppViewIntent : ViewIntent {
    data object OnLoginSuccess : AppViewIntent
}