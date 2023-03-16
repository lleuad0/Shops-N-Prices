package com.github.lleuad0.shopsandprices.ui.new_product

import com.github.lleuad0.shopsandprices.AbstractViewModel
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.model.Shop
import com.github.lleuad0.shopsandprices.domain.usecase.AddPriceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class NewPriceViewModel @Inject constructor(
    private val addPriceUseCase: AddPriceUseCase,
    backgroundContext: CoroutineContext,
) :
    AbstractViewModel(backgroundContext) {
    data class NewPriceState(
        val isPriceAdded: Boolean = false,
    )

    val stateFlow = MutableStateFlow(NewPriceState())
    fun addPrice(product: Product, shop: Shop, price: Double) {
        addPriceUseCase.apply {
            this.product = product
            this.shop = shop
            this.price = price
        }.runOnBackground {
            stateFlow.update { state -> state.copy(isPriceAdded = true) }
        }
    }
}