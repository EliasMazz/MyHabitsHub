package com.yolo.auth.domain

import com.yolo.auth.domain.entities.RegisterAuth
import com.yolo.core.domain.usecase.UseCase
import com.yolo.core.domain.validator.PasswordValidatorUseCase

class RegisterValidatorUseCase(
    private val emailValidatorUseCase: EmailValidatorUseCase,
    private val passwordValidatorUseCase: PasswordValidatorUseCase
) : UseCase<RegisterAuth, RegisterValidatorResult>() {

    override suspend fun execute(param: RegisterAuth): RegisterValidatorResult {
        val isValidEmail = emailValidatorUseCase.invoke(param.email)
        val isValidPassword = passwordValidatorUseCase.invoke(param.password)
        if(!isValidEmail && !isValidPassword) return RegisterValidatorResult.InvalidEmailAndPasswordError
        if(!isValidEmail) return RegisterValidatorResult.InvalidEmailError
        if(!isValidPassword) return RegisterValidatorResult.InvalidPasswordError
        return RegisterValidatorResult.Success
    }
}

sealed interface RegisterValidatorResult{
    data object InvalidEmailAndPasswordError: RegisterValidatorResult
    data object InvalidEmailError: RegisterValidatorResult
    data object InvalidPasswordError: RegisterValidatorResult
    data object Success: RegisterValidatorResult
}