package com.yolo.myhabitshub.app

import com.yolo.core.presentation.viewmodel.BaseViewModel

sealed interface AppViewEvent : BaseViewModel.ViewEvent{
    data object SessionExpired : AppViewEvent
}