package com.yolo.auth.presentation.di

import com.yolo.auth.presentation.email_verification.EmailVerificationViewModel
import com.yolo.auth.presentation.forgot_password.ForgotPasswordViewModel
import com.yolo.auth.presentation.login.LoginViewModel
import com.yolo.auth.presentation.register.RegisterViewModel
import com.yolo.auth.presentation.register_success.RegisterSuccessViewModel
import com.yolo.auth.presentation.reset_password.ResetPasswordViewModel
import com.yolo.auth.presentation.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::ResetPasswordViewModel)
    viewModelOf(::WelcomeViewModel)
}