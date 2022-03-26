package com.github.lleuad0.shopsandprices.fragments.product_info

import androidx.lifecycle.ViewModel
import com.github.lleuad0.shopsandprices.domain.usecase.GetShopsAndPricesByProductIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductInfoViewModel @Inject constructor(
    private val getShopsAndPricesByProductIdUseCase: GetShopsAndPricesByProductIdUseCase
) : ViewModel() {
    data class InfoUiState(val shopsAndPricesMap: Map<String, Double> = mapOf())

    val stateFlow = MutableStateFlow(InfoUiState())

    fun getDataForProduct(productId: Int) {
        getShopsAndPricesByProductIdUseCase.apply {
            this.productId = productId
        }.runOnBackground { stateFlow.update { state -> state.copy(shopsAndPricesMap = it) } }
    }

    override fun onCleared() {
        super.onCleared()
        getShopsAndPricesByProductIdUseCase.cancelJob()
    }
}