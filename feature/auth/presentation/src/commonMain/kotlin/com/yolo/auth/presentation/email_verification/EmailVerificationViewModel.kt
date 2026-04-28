package com.yolo.auth.presentation.email_verification

import androidx.lifecycle.SavedStateHandle
import com.yolo.core.presentation.viewmodel.BaseViewModel

class EmailVerificationViewModel(
) : BaseViewModel<EmailVerificationViewIntent, EmailVerificationViewState, EmailVerificationViewEvent>(
    EmailVerificationViewState()
) {


    override fun onViewIntent(intent: EmailVerificationViewIntent) {
        when (intent) {
            else -> {
                // TODO
            }
        }
    }

}