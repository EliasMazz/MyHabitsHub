package com.yolo.myhabitshub.core.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

abstract class UseCase<in PARAM, out RESULT> (
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
){
    protected abstract suspend fun execute(param:PARAM): RESULT

    suspend operator fun invoke(param: PARAM): RESULT = withContext(dispatcher){
        execute(param)
    }
}

typealias FireAndForgetUseCase<PARAM> = UseCase<PARAM, Unit>

