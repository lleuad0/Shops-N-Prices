package com.github.lleuad0.shopsandprices.domain

import androidx.paging.PagingData
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.model.Shop
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun getAllProducts(): Flow<PagingData<Product>>
    suspend fun addProduct(product: Product)
    suspend fun getProductByName(productName: String): Product?
    suspend fun removeProduct(product: Product)

    suspend fun addShop(shop: Shop)
    suspend fun getShopByName(shopName: String): Shop

    suspend fun addPrice(product: Product, shop: Shop, price: Double)
    suspend fun getShopsAndPricesByProductId(productId: Int): Map<String, Double>
}