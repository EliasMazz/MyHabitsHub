package com.yolo.auth.presentation.register_success

import com.yolo.core.presentation.viewmodel.BaseViewModel.*

sealed interface RegisterSuccessViewIntent : ViewIntent {
    data object OnLoginClick : RegisterSuccessViewIntent
    data object OnResendVerificationEmailClick : RegisterSuccessViewIntent

    /** Wrong-email escape (spec §5.2): back to Register with the typed address prefilled. */
    data object OnEditEmailClick : RegisterSuccessViewIntent
}
