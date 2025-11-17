package com.yolo.auth.domain

import com.yolo.auth.domain.entities.RegisterAuth
import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.usecase.UseCase
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.fold

class RegisterAuthUseCase(
    private val authRepository: AuthRepository,
) : UseCase<RegisterAuth, RegisterAuthResult>() {
    override suspend fun execute(param: RegisterAuth): RegisterAuthResult {
        val result = authRepository.register(
            email = param.email,
            password = param.password
        )
        return result.fold(
            onSuccess = {
                RegisterAuthResult.Success
            },
            onFailure = { error ->
                RegisterAuthResult.Error(error)
            }
        )
    }
}

sealed interface RegisterAuthResult {
    data class Error(val dataError: DataError) : RegisterAuthResult
    data object Success : RegisterAuthResult
}