package com.github.lleuad0.shopsandprices.fragments.edit_product

import androidx.lifecycle.ViewModel
import com.github.lleuad0.shopsandprices.domain.model.Price
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.usecase.EditProductUseCase
import com.github.lleuad0.shopsandprices.domain.usecase.GetPricesByProductIdUseCase
import com.github.lleuad0.shopsandprices.domain.usecase.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getPricesByProductIdUseCase: GetPricesByProductIdUseCase,
    private val editProductUseCase: EditProductUseCase,
) : ViewModel() {
    data class InfoUiState(
        val product: Product? = null,
        val shopsAndPrices: List<Price> = listOf(),
        val isSaved: Boolean = false,
    )

    val stateFlow = MutableStateFlow(InfoUiState())

    fun getProduct(productId: Long) {
        getProductByIdUseCase.apply {
            this.productId = productId
        }.runOnBackground {
            stateFlow.update { state -> state.copy(product = it) }
        }
    }

    fun getDataForProduct(productId: Long) {
        getPricesByProductIdUseCase.apply {
            this.productId = productId
        }.runOnBackground { stateFlow.update { state -> state.copy(shopsAndPrices = it) } }
    }

    fun saveProductData(newName: String?, newInfo: String?, newPrices: List<Price>) {
        editProductUseCase.apply {
            productId = stateFlow.value.product?.id
            productName = newName
            productInfo = newInfo
            prices = newPrices
        }.runOnBackground {
            stateFlow.update { state -> state.copy(isSaved = true) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        getProductByIdUseCase.cancelJob()
        getPricesByProductIdUseCase.cancelJob()
        editProductUseCase.cancelJob()
    }
}