package com.yolo.auth.domain

import com.yolo.core.domain.usecase.StreamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext

/**
 * Resend-cooldown ticker: emits the remaining seconds once per second, finishing with 0.
 * Cold stream — cancelling the collecting job cancels the ticker.
 */
class ResendCooldownStreamUseCase(
    coroutineContext: CoroutineContext = Dispatchers.IO,
) : StreamUseCase<Int, Int>(coroutineContext) {

    override fun run(param: Int): Flow<Int> = flow {
        var remaining = param
        while (remaining > 0) {
            emit(remaining)
            delay(1_000)
            remaining--
        }
        emit(0)
    }
}
