package com.github.lleuad0.shopsandprices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lleuad0.shopsandprices.domain.usecase.UseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class AbstractViewModel(private val backgroundContext: CoroutineContext) : ViewModel() {
    data class AbstractState(
        val throwable: Throwable? = null,
    )

    val abstractStateFlow = MutableStateFlow(AbstractState())

    private fun throwError(throwable: Throwable) {
        abstractStateFlow.update { it.copy(throwable = throwable) }
    }

    fun onErrorThrown() {
        abstractStateFlow.update { it.copy(throwable = null) }
    }

    fun <T> UseCase<T>.runOnBackground(
        doOnCancel: (CancellationException) -> Unit = { Timber.d("${this.javaClass.simpleName} \n ${it.stackTraceToString()}") },
        doOnError: (Throwable) -> Unit = { throwError(it) },
        doOnComplete: (T) -> Unit,
    ) {
        runOnBackground(viewModelScope, backgroundContext, doOnCancel, doOnError, doOnComplete)
    }
}