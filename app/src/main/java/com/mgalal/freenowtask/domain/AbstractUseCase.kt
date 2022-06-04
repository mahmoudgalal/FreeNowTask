package com.mgalal.freenowtask.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class AbstractUseCase<in Input, Output>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(param: Input): DomainResult<Output> {
        return withContext(coroutineDispatcher) {
            execute(param)
        }
    }

    abstract suspend fun execute(param: Input): DomainResult<Output>
}