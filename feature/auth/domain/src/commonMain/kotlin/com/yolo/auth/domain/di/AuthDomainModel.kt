package com.yolo.auth.domain.di

import com.yolo.auth.domain.AuthValidatorUseCase
import com.yolo.auth.domain.EmailValidatorUseCase
import com.yolo.auth.domain.RegisterAuthUseCase
import org.koin.dsl.module

val authDomainModule = module{
    factory { EmailValidatorUseCase() }
    factory { AuthValidatorUseCase(emailValidatorUseCase = get(), passwordValidatorUseCase = get()) }
    factory { RegisterAuthUseCase(authRepository = get()) }
}