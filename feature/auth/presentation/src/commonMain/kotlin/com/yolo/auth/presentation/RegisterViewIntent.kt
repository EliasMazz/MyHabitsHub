package com.yolo.auth.presentation

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface RegisterViewIntent : ViewIntent {
    data object OnLogicClick : RegisterViewIntent
    data object OnInputTextFocusGain : RegisterViewIntent
    data object OnRegisterClick : RegisterViewIntent
    data object OnTogglePasswordVisibility : RegisterViewIntent
}