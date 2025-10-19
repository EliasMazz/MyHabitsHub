package com.yolo.myhabitshub.domain.usecase

import com.yolo.myhabitshub.core.domain.usecase.StreamUseCase
import com.yolo.myhabitshub.data.repository.UserRepository
import com.yolo.myhabitshub.data.model.UserResponse
import kotlinx.coroutines.flow.Flow

class GetUserStream(
    private val userRepository: UserRepository
): StreamUseCase<Unit, Result<UserResponse>>() {

    override fun run(param: Unit): Flow<Result<UserResponse>> {
        return userRepository.getCurrentUser()
    }
}