package com.yolo.core.domain.validator

import com.yolo.core.domain.usecase.UseCase

private const val MIN_PASSWORD_LENGTH = 9

class PasswordValidatorUseCase : UseCase<String, Boolean>() {
    override suspend fun execute(password: String): Boolean {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasUppercase = password.any { it.isUpperCase() }
        return hasMinLength && hasDigit && hasUppercase
    }
}