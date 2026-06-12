package com.yolo.auth.domain

import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.usecase.UseCase
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.fold

class ForgotPasswordUseCase(
    private val authRepository: AuthRepository,
) : UseCase<String, ForgotPasswordResult>() {
    override suspend fun execute(param: String): ForgotPasswordResult {
        val result = authRepository.forgotPassword(param)
        return result.fold(
            onSuccess = {
                ForgotPasswordResult.Success
            },
            onFailure = { error ->
                ForgotPasswordResult.Error(error)
            }
        )
    }
}

sealed interface ForgotPasswordResult {
    data class Error(val dataError: DataError) : ForgotPasswordResult
    data object Success : ForgotPasswordResult
}
