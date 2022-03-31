package com.github.lleuad0.shopsandprices.domain

import androidx.paging.PagingData
import com.github.lleuad0.shopsandprices.domain.model.Price
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.model.Shop
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun getAllProducts(): Flow<PagingData<Product>>
    suspend fun addProduct(product: Product): Product
    suspend fun updateProduct(product: Product)
    suspend fun getProductById(productId: Long): Product
    suspend fun removeProduct(product: Product)

    suspend fun getAllShops(): Flow<PagingData<Shop>>
    suspend fun addShop(shop: Shop): Shop
    suspend fun updateShop(shop: Shop)
    suspend fun getShopById(shopId: Long): Shop
    suspend fun removeShop(shop: Shop)

    suspend fun addPrice(product: Product, shop: Shop, price: Double)
    suspend fun updatePrice(product: Product, shop: Shop, price: Double)
    suspend fun getShopsAndPricesByProductId(productId: Long): List<Price>
    suspend fun removePrice(product: Product, shop: Shop)
}