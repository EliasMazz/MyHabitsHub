package com.yolo.myhabitshub.domain.usecase

import com.yolo.core.domain.usecase.FireAndForgetUseCase
import com.yolo.myhabitshub.data.repository.UserRepository

class DeleteAccountUseCase(
    private val userRepository: UserRepository
): FireAndForgetUseCase<Unit>() {
    override suspend fun execute(param: Unit) {
        userRepository.deleteAccount()
    }
}