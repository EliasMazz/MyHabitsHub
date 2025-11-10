package com.yolo.auth.presentation

import com.yolo.core.presentation.viewmodel.BaseViewModel

class RegisterViewModel : BaseViewModel<RegisterViewIntent, RegisterViewState, RegisterViewEvent>(
    RegisterViewState()
){
    override fun onViewIntent(intent: RegisterViewIntent) {
    }
}