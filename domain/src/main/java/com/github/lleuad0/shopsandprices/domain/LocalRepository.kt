package com.github.lleuad0.shopsandprices.domain

interface LocalRepository {
    suspend fun getAllProducts(): Array<Product>
    suspend fun addProduct(product: Product)
}