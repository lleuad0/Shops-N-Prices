package com.github.lleuad0.shopsandprices.domain

interface LocalRepository {
    suspend fun getAllProducts(): Array<Product>
    suspend fun addProduct(product: Product)
    suspend fun getProductByName(productName: String): Product

    suspend fun addShop(shop: Shop)
    suspend fun getShopByName(shopName: String): Shop

    suspend fun addPrice(product: Product, shop: Shop, price: Double)
}