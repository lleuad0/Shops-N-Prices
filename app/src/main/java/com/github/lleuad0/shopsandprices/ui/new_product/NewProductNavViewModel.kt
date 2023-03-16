package com.github.lleuad0.shopsandprices.ui.new_product

import androidx.lifecycle.ViewModel
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewProductNavViewModel @Inject constructor() :
    ViewModel() {
    data class NewProductUiState(
        val isProductAdded: Boolean = false,
        val product: Product? = null,
        val isShopAdded: Boolean = false,
        val shop: Shop? = null,
    )

    val stateFlow = MutableStateFlow(NewProductUiState())

    fun addProduct(productName: String, productInfo: String) {
        stateFlow.update { state ->
            state.copy(
                product = Product(productName, productInfo),
                isProductAdded = true
            )
        }
    }

    fun addShop(shopName: String, shopInfo: String) {
        stateFlow.update { state ->
            state.copy(
                shop = Shop(shopName, shopInfo),
                isShopAdded = true
            )
        }
    }

    fun onRedirectedProduct() {
        stateFlow.update { state -> state.copy(isProductAdded = false) }
    }

    fun onRedirectedShop() {
        stateFlow.update { state -> state.copy(isShopAdded = false) }
    }
}