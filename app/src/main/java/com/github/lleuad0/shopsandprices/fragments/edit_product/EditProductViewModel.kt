package com.github.lleuad0.shopsandprices.fragments.edit_product

import androidx.lifecycle.ViewModel
import com.github.lleuad0.shopsandprices.domain.usecase.AddProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(private val addProductUseCase: AddProductUseCase) :
    ViewModel() {
    data class EditUiState(val isSaved: Boolean = false)

    val stateFlow = MutableStateFlow(EditUiState())

    fun addProduct(productName: String, productPrice: Double, productShops: String) {
        addProductUseCase.apply {
            this.productName = productName
            this.productPrice = productPrice
            this.productShops = productShops.splitToSequence(", ").toCollection(arrayListOf())
        }.runOnBackground { stateFlow.update { state -> state.copy(isSaved = true) } }
    }

    override fun onCleared() {
        super.onCleared()
        addProductUseCase.cancelJob()
    }
}