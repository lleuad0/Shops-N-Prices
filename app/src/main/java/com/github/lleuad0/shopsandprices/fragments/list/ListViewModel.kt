package com.github.lleuad0.shopsandprices.fragments.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.usecase.GetAllProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
) : ViewModel() {
    data class ListUiState(val products: Flow<PagingData<Product>> = MutableStateFlow(PagingData.empty()))

    val stateFlow = MutableStateFlow(ListUiState())

    fun getAllData() {
        getAllProductsUseCase.runOnBackground {
            stateFlow.update { state ->
                state.copy(products = it.cachedIn(viewModelScope))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        getAllProductsUseCase.cancelJob()
    }
}