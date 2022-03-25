package com.github.lleuad0.shopsandprices.data

import com.github.lleuad0.shopsandprices.data.dao.ProductDao
import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.Product
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val productDao: ProductDao): LocalRepository {
    override suspend fun getAllProducts(): Array<Product> {
        return productDao.selectAll().map { it.toUi() }.toTypedArray()
    }

    override suspend fun addProduct(product: Product) {
        productDao.insertProduct(product.toDb())
    }
}