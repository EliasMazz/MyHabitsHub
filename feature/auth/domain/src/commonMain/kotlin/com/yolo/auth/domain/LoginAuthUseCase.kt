package com.yolo.auth.domain

import com.yolo.auth.domain.entities.LoginAuth
import com.yolo.core.domain.auth.AuthRepository
import com.yolo.core.domain.usecase.UseCase
import com.yolo.core.domain.util.DataError
import com.yolo.core.domain.util.fold

class LoginAuthUseCase(
    private val authRepository: AuthRepository,
) : UseCase<LoginAuth, LoginAuthResult>() {
    override suspend fun execute(param: LoginAuth): LoginAuthResult {
        val result = authRepository.login(param.email, param.password)
        return result.fold(
            onSuccess = {
                LoginAuthResult.Success
            },
            onFailure = { error ->
                LoginAuthResult.Error(error)
            }
        )
    }
}

sealed interface LoginAuthResult {
    object Success : LoginAuthResult
    data class Error(val dataError: DataError) : LoginAuthResult
}