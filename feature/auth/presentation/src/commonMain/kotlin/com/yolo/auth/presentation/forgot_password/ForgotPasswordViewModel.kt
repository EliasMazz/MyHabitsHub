package com.yolo.auth.presentation.forgot_password

import com.yolo.core.presentation.viewmodel.BaseViewModel

class ForgotPasswordViewModel(
) : BaseViewModel<ForgotPasswordViewIntent, ForgotPasswordViewState, ForgotPasswordViewEvent>(
    initialState = ForgotPasswordViewState()
) {
    override fun onViewIntent(intent: ForgotPasswordViewIntent) {
        TODO("Not yet implemented")
    }
}
