package com.github.lleuad0.shopsandprices.ui.edit_product

import com.github.lleuad0.shopsandprices.AbstractViewModel
import com.github.lleuad0.shopsandprices.domain.model.Price
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.model.Shop
import com.github.lleuad0.shopsandprices.domain.usecase.EditProductUseCase
import com.github.lleuad0.shopsandprices.domain.usecase.GetPricesByProductIdUseCase
import com.github.lleuad0.shopsandprices.domain.usecase.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getPricesByProductIdUseCase: GetPricesByProductIdUseCase,
    private val editProductUseCase: EditProductUseCase,
    backgroundContext: CoroutineContext,
) : AbstractViewModel(backgroundContext) {
    data class EditUiState(
        val product: Product? = null,
        val shopsAndPrices: List<Price> = listOf(),
        val isSaved: Boolean = false,
    )

    val stateFlow = MutableStateFlow(EditUiState())

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

    fun addNewName(name: String, price: Price) {
        val index = stateFlow.value.shopsAndPrices.indexOf(price)
        val newPrice = Price(price.price, Shop(name, price.shop.additionalInfo, price.shop.id))
        with(stateFlow.value.shopsAndPrices.toMutableList()) {
            remove(price)
            add(index, newPrice)
            stateFlow.update { it.copy(shopsAndPrices = this.toList()) }
        }
    }

    fun addNewPrice(value: String, price: Price) {
        val index = stateFlow.value.shopsAndPrices.indexOf(price)
        val newPrice = Price(value.toDouble(), price.shop)
        with(stateFlow.value.shopsAndPrices.toMutableList()) {
            remove(price)
            add(index, newPrice)
            stateFlow.update { it.copy(shopsAndPrices = this.toList()) }
        }
    }

    fun removeItem(item: Price) {
        with(stateFlow.value.shopsAndPrices.toMutableList()) {
            remove(item)
            stateFlow.update { it.copy(shopsAndPrices = this.toList()) }
        }
    }

    fun addItem() {
        with(stateFlow.value.shopsAndPrices.toMutableList()) {
            add(Price(0.0, Shop("")))
            stateFlow.update { it.copy(shopsAndPrices = this.toList()) }
        }
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
}