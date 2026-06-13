package com.yolo.auth.domain

import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.usecase.UseCase
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.fold

class ResetPasswordUseCase(
    private val authRepository: AuthRepository,
) : UseCase<ResetPasswordParams, ResetPasswordResult>() {
    override suspend fun execute(param: ResetPasswordParams): ResetPasswordResult {
        return authRepository.resetPassword(
            newPassword = param.newPassword,
            token = param.token,
        ).fold(
            onSuccess = { ResetPasswordResult.Success },
            onFailure = { error -> ResetPasswordResult.Error(error) }
        )
    }
}

data class ResetPasswordParams(
    val newPassword: String,
    val token: String,
)

sealed interface ResetPasswordResult {
    data object Success : ResetPasswordResult
    data class Error(val dataError: DataError) : ResetPasswordResult
}
