package com.yolo.auth.presentation

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed class RegisterViewIntent: ViewIntent {
    data object OnLogicClick: ViewIntent
    data object OnInputTextFocusGain: ViewIntent
    data object OnRegisterClick: ViewIntent
    data object OnTogglePasswordVisibility: ViewIntent
}