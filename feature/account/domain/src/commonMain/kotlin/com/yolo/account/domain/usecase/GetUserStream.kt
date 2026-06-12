package com.yolo.account.domain.usecase

import com.yolo.core.domain.usecase.StreamUseCase
import com.yolo.account.domain.repository.UserRepository
import com.yolo.account.domain.entities.UserResponse
import kotlinx.coroutines.flow.Flow

class GetUserStream(
    private val userRepository: UserRepository
): StreamUseCase<Unit, Result<UserResponse>>() {

    override fun run(param: Unit): Flow<Result<UserResponse>> {
        return userRepository.getCurrentUser()
    }
}