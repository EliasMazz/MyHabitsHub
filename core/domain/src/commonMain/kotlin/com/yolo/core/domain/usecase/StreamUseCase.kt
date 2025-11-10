package com.yolo.core.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

suspend operator fun <RESULT> UseCase<Unit, RESULT>.invoke(): RESULT = invoke(Unit)

abstract class StreamUseCase<in PARAM, out RESULT>(
    private val  coroutineContext: CoroutineContext = Dispatchers.IO
) {

    protected abstract fun run(param: PARAM): Flow<RESULT>

    operator fun invoke(param: PARAM): Flow<RESULT> = run(param).flowOn(coroutineContext)
}

operator fun <RESULT> StreamUseCase<Unit, RESULT>.invoke(): Flow<RESULT> = invoke(Unit)

