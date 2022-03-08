package com.github.lleuad0.shopsandprices.fragments.list

import androidx.lifecycle.ViewModel
import com.github.lleuad0.shopsandprices.data.Database
import com.github.lleuad0.shopsandprices.data.ProductDao
import com.github.lleuad0.shopsandprices.domain.AddProductUseCase
import com.github.lleuad0.shopsandprices.domain.GetAllProductsUseCase
import com.github.lleuad0.shopsandprices.domain.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
) : ViewModel() {
    data class ListUiState(val products: Array<Product> = emptyArray()) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ListUiState

            if (!products.contentEquals(other.products)) return false

            return true
        }

        override fun hashCode(): Int {
            return products.contentHashCode()
        }
    }

    val stateFlow = MutableStateFlow(ListUiState())

    fun getAllData() {
        getAllProductsUseCase.runOnBackground { stateFlow.update { state -> state.copy(products = it) } }
    }

    fun addProduct(product: Product) {
        addProductUseCase.apply {
            this.product = product
        }.runOnBackground{getAllData()}
    }

    override fun onCleared() {
        super.onCleared()
        getAllProductsUseCase.cancelJob()
        addProductUseCase.cancelJob()
    }
}