package com.yolo.auth.domain

import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.usecase.UseCase
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.fold

class ResendVerificationEmailUseCase(
    private val authRepository: AuthRepository,
) : UseCase<String, ResendVerificationEmailResult>() {
    override suspend fun execute(param: String): ResendVerificationEmailResult {
        val result = authRepository.resendVerificationEmail(param)
        return result.fold(
            onSuccess = {
                ResendVerificationEmailResult.Success
            },
            onFailure = { error ->
                ResendVerificationEmailResult.Error(error)
            }
        )
    }
}

sealed interface ResendVerificationEmailResult {
    data class Error(val dataError: DataError) : ResendVerificationEmailResult
    data object Success : ResendVerificationEmailResult
}