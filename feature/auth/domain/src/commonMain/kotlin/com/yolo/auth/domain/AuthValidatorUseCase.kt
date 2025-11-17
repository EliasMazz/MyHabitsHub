package com.yolo.auth.domain

import com.yolo.auth.domain.entities.RegisterAuth
import com.yolo.core.domain.usecase.UseCase
import com.yolo.core.domain.validator.PasswordValidatorUseCase

class AuthValidatorUseCase(
    private val emailValidatorUseCase: EmailValidatorUseCase,
    private val passwordValidatorUseCase: PasswordValidatorUseCase
) : UseCase<RegisterAuth, AuthValidatorResult>() {

    override suspend fun execute(param: RegisterAuth): AuthValidatorResult {
        val isValidEmail = emailValidatorUseCase.invoke(param.email)
        val isValidPassword = passwordValidatorUseCase.invoke(param.password)
        if(!isValidEmail && !isValidPassword) return AuthValidatorResult.InvalidEmailAndPasswordError
        if(!isValidEmail) return AuthValidatorResult.InvalidEmailError
        if(!isValidPassword) return AuthValidatorResult.InvalidPasswordError
        return AuthValidatorResult.Success
    }
}

sealed interface AuthValidatorResult{
    data object InvalidEmailAndPasswordError: AuthValidatorResult
    data object InvalidEmailError: AuthValidatorResult
    data object InvalidPasswordError: AuthValidatorResult
    data object Success: AuthValidatorResult
}