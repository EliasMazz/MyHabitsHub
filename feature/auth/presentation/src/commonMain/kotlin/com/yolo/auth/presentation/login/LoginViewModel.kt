package com.yolo.auth.presentation.login

import com.yolo.core.presentation.viewmodel.BaseViewModel

class LoginViewModel : BaseViewModel<LoginIntent, LoginState, LoginEvent>(
    initialState = LoginState(email = "", password = "")
) {
    override fun onViewIntent(intent: LoginIntent) {
      when(intent) {
          LoginIntent.OnForgotPasswordClick -> TODO()
          LoginIntent.OnLoginClick -> TODO()
          LoginIntent.OnSignupClick -> updateState { copy(viewEvent = LoginEvent.OnRegister) }
          LoginIntent.OnTogglePasswordVisibility -> TODO()
      }
    }
}