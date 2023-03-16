package com.github.lleuad0.shopsandprices.ui.product_info

import com.github.lleuad0.shopsandprices.AbstractViewModel
import com.github.lleuad0.shopsandprices.domain.model.Price
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.usecase.GetPricesByProductIdUseCase
import com.github.lleuad0.shopsandprices.domain.usecase.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class ProductInfoViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getPricesByProductIdUseCase: GetPricesByProductIdUseCase,
    backgroundContext: CoroutineContext,
) : AbstractViewModel(backgroundContext) {
    data class InfoUiState(
        val product: Product? = null,
        val shopsAndPrices: List<Price> = listOf(),
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
}