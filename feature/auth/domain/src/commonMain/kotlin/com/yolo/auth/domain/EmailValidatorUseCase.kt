package com.yolo.auth.domain

import com.yolo.core.domain.usecase.UseCase

private const val EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"

class EmailValidatorUseCase: UseCase<String, Boolean>() {

    override suspend fun execute(email: String): Boolean {
        return EMAIL_PATTERN.toRegex().matches(email)
    }
}