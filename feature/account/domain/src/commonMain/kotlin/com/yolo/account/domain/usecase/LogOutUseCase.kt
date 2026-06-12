package com.yolo.account.domain.usecase

import com.yolo.core.domain.usecase.FireAndForgetUseCase
import com.yolo.account.domain.repository.UserRepository

class LogOutUseCase(
    private val userRepository: UserRepository
): FireAndForgetUseCase<Unit>() {
    override suspend fun execute(param: Unit) {
        userRepository.logOut()
    }
}