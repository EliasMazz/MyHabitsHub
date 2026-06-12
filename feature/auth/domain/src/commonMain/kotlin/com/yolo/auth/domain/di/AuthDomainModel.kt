package com.yolo.auth.domain.di

import com.yolo.auth.domain.AuthValidatorUseCase
import com.yolo.auth.domain.EmailValidatorUseCase
import com.yolo.auth.domain.EmailVerificationUseCase
import com.yolo.auth.domain.ForgotPasswordUseCase
import com.yolo.auth.domain.LoginAuthUseCase
import com.yolo.auth.domain.RegisterAuthUseCase
import com.yolo.auth.domain.ResendVerificationEmailUseCase
import org.koin.dsl.module

val authDomainModule = module{
    factory { EmailValidatorUseCase() }
    factory { AuthValidatorUseCase(emailValidatorUseCase = get(), passwordValidatorUseCase = get()) }
    factory { RegisterAuthUseCase(authRepository = get()) }
    factory { ResendVerificationEmailUseCase(authRepository = get()) }
    factory { EmailVerificationUseCase(authRepository = get()) }
    factory { ForgotPasswordUseCase(authRepository = get()) }
    factory { LoginAuthUseCase(authRepository = get()) }
}