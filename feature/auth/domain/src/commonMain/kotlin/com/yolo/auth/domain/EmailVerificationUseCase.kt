package com.yolo.auth.domain

import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.usecase.UseCase
import com.yolo.core.domain.util.fold

class EmailVerificationUseCase(
    private val authRepository: AuthRepository,
) : UseCase<String, EmailVerificationUseCase.EmailVerificationResult>() {
    override suspend fun execute(param: String): EmailVerificationResult {
        return authRepository.verifyEmail(param).fold(
            onSuccess = {
                EmailVerificationResult.Success
            },
            onFailure = {
                EmailVerificationResult.Failure
            }
        )
    }

    sealed interface EmailVerificationResult {
        object Success : EmailVerificationResult
        object Failure : EmailVerificationResult
    }
}