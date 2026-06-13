package com.yolo.auth.domain

import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.usecase.UseCase
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.fold

class EmailVerificationUseCase(
    private val authRepository: AuthRepository,
) : UseCase<String, EmailVerificationUseCase.EmailVerificationResult>() {
    override suspend fun execute(param: String): EmailVerificationResult {
        return authRepository.verifyEmail(param).fold(
            onSuccess = {
                EmailVerificationResult.Success
            },
            onFailure = { error ->
                // Pass the error through (auth-screens-improvement-spec §4.4#2): the caller
                // must distinguish a network blip (retryable with the same token) from a
                // dead/expired token (resend-recovery state).
                EmailVerificationResult.Failure(error)
            }
        )
    }

    sealed interface EmailVerificationResult {
        data object Success : EmailVerificationResult
        data class Failure(val error: DataError.Remote) : EmailVerificationResult
    }
}
