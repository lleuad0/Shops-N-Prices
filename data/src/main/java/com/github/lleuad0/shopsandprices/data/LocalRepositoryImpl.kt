package com.github.lleuad0.shopsandprices.data

import com.github.lleuad0.shopsandprices.data.dao.PriceDao
import com.github.lleuad0.shopsandprices.data.dao.ProductDao
import com.github.lleuad0.shopsandprices.data.dao.ShopDao
import com.github.lleuad0.shopsandprices.data.entities.PriceDb
import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.Product
import com.github.lleuad0.shopsandprices.domain.Shop
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val shopDao: ShopDao,
    private val priceDao: PriceDao,
) : LocalRepository {
    override suspend fun getAllProducts(): Array<Product> {
        return productDao.selectAll().map { it.toUi() }.toTypedArray()
    }

    override suspend fun addProduct(product: Product) {
        return productDao.insertProduct(product.toDb())
    }

    override suspend fun getProductByName(productName: String): Product {
        return productDao.getProductByName(productName).toUi()
    }

    override suspend fun addShop(shop: Shop) {
        return shopDao.insertShop(shop.toDb())
    }

    override suspend fun getShopByName(shopName: String): Shop {
        return shopDao.getShopByName(shopName).toUi()
    }

    override suspend fun addPrice(product: Product, shop: Shop, price: Double) {
        return priceDao.insertPrice(PriceDb(product.id, shop.id, price))
    }
}