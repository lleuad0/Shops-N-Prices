package com.github.lleuad0.shopsandprices.domain.usecase

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class UseCase<T> {
    abstract suspend fun execute(): T

    @OptIn(ExperimentalCoroutinesApi::class)
    fun runOnBackground(
        foregroundScope: CoroutineScope,
        backgroundContext: CoroutineContext,
        doOnCancel: (CancellationException) -> Unit,
        doOnError: (Throwable) -> Unit,
        doOnComplete: (T) -> Unit,
    ) {
        val deferred = foregroundScope.async {
            withContext(backgroundContext) {
                execute()
            }
        }
        deferred.invokeOnCompletion {
            foregroundScope.launch {
                when (it) {
                    is CancellationException -> doOnCancel(it)
                    null -> doOnComplete(deferred.getCompleted())
                    else -> doOnError(it)
                }
            }
        }
    }
}