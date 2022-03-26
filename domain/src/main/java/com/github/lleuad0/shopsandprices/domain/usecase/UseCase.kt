package com.github.lleuad0.shopsandprices.domain.usecase

import kotlinx.coroutines.*

abstract class UseCase<T> {
    private val job = Job()
    private val foregroundContext = Dispatchers.Main
    private val backgroundContext = Dispatchers.IO

    abstract suspend fun execute(): T

    fun runOnBackground(
        doOnCancel: (CancellationException) -> Unit = {},
        doOnError: (Exception) -> Unit = {},
        doOnComplete: (T) -> Unit = {}
    ) {
        CoroutineScope(foregroundContext + job).launch {
            try {
                val result = withContext(backgroundContext) { execute() }
                doOnComplete(result)
            } catch (cancellationException: CancellationException) {
                doOnCancel(cancellationException)
            } catch (e: Exception) {
                doOnError(e)
            }
        }
    }

    fun cancelJob() {
        job.apply {
            cancelChildren()
            cancel()
        }
    }
}