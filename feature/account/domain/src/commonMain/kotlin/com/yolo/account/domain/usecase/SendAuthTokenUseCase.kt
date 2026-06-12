package com.yolo.account.domain.usecase

import com.yolo.core.domain.usecase.UseCase
import com.yolo.account.domain.repository.UserRepository
import com.yolo.account.domain.entities.User

class SendAuthTokenUseCase(
    private val userRepository: UserRepository
) : UseCase<Unit, SendAuthResult>() {
    override suspend fun execute(param: Unit): SendAuthResult {
        userRepository.sendAuthToken().fold(
            onSuccess = {
                return SendAuthResult.Success(
                    User(id = it.id,
                        email = it.email,
                        displayName = it.displayName,
                        photoUrl = it.photoUrl,
                        isAnonymous = it.isAnonymous)
                )
            },
            onFailure = {
                return SendAuthResult.Error(it)
            }
        )
    }
}

sealed interface SendAuthResult {
    data class Success(val user: User) : SendAuthResult
    data class Error(val throwable: Throwable) : SendAuthResult
}