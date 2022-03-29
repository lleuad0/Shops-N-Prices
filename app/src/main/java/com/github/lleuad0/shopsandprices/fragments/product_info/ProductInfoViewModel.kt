package com.github.lleuad0.shopsandprices.fragments.product_info

import androidx.lifecycle.ViewModel
import com.github.lleuad0.shopsandprices.domain.model.Price
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.usecase.GetProductByNameUseCase
import com.github.lleuad0.shopsandprices.domain.usecase.GetShopsAndPricesByProductNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductInfoViewModel @Inject constructor(
    private val getProductByNameUseCase: GetProductByNameUseCase,
    private val getShopsAndPricesByProductNameUseCase: GetShopsAndPricesByProductNameUseCase,
) : ViewModel() {
    data class InfoUiState(
        val product: Product? = null,
        val shopsAndPrices: List<Price> = listOf(),
    )

    val stateFlow = MutableStateFlow(InfoUiState())

    fun getProduct(productName: String) {
        getProductByNameUseCase.apply {
            this.productName = productName
        }.runOnBackground {
            stateFlow.update { state -> state.copy(product = it) }
        }
    }

    fun getDataForProduct(productName: String) {
        getShopsAndPricesByProductNameUseCase.apply {
            this.productName = productName
        }.runOnBackground { stateFlow.update { state -> state.copy(shopsAndPrices = it) } }
    }

    override fun onCleared() {
        super.onCleared()
        getProductByNameUseCase.cancelJob()
        getShopsAndPricesByProductNameUseCase.cancelJob()
    }
}