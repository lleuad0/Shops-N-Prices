package com.github.lleuad0.shopsandprices.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.github.lleuad0.shopsandprices.data.dao.PriceDao
import com.github.lleuad0.shopsandprices.data.dao.ProductDao
import com.github.lleuad0.shopsandprices.data.dao.ShopDao
import com.github.lleuad0.shopsandprices.data.entities.PriceDb
import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.model.Shop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val shopDao: ShopDao,
    private val priceDao: PriceDao,
) : LocalRepository {
    override suspend fun getAllProducts(): Flow<PagingData<Product>> {
        return Pager(PagingConfig(10)) { productDao.selectAll() }
            .flow.map { data -> data.map { it.toUi() } }
    }

    override suspend fun addProduct(product: Product) {
        return productDao.insertProduct(product.toDb())
    }

    override suspend fun getProductById(productId: Int): Product? {
        return productDao.getProductById(productId)?.toUi()
    }

    override suspend fun getProductByName(productName: String): Product? {
        return productDao.getProductByName(productName)?.toUi()
    }

    override suspend fun removeProduct(product: Product) {
        return productDao.removeProduct(product.toDb())
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

    override suspend fun getShopsAndPricesByProductId(productId: Int): Map<String, Double> {
        return priceDao.selectPrices(productId)
    }
}