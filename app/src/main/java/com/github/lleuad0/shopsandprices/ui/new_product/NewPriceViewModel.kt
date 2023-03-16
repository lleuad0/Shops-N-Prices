package com.github.lleuad0.shopsandprices.ui.new_product

import androidx.lifecycle.ViewModel
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.model.Shop
import com.github.lleuad0.shopsandprices.domain.usecase.AddPriceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewPriceViewModel @Inject constructor(private val addPriceUseCase: AddPriceUseCase) :
    ViewModel() {
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

    override fun onCleared() {
        super.onCleared()
        addPriceUseCase.cancelJob()
    }
}