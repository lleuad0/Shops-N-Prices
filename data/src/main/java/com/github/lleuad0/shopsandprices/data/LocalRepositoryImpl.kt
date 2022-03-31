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
import com.github.lleuad0.shopsandprices.domain.model.Price
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

    override suspend fun addProduct(product: Product): Product {
        return productDao.getProductById(productDao.insertProduct(product.toDb())).toUi()
    }

    override suspend fun updateProduct(product: Product) {
        return productDao.updateProduct(product.toDb())
    }

    override suspend fun getProductById(productId: Long): Product {
        return productDao.getProductById(productId).toUi()
    }

    override suspend fun removeProduct(product: Product) {
        return productDao.deleteProduct(product.toDb())
    }

    override suspend fun getAllShops(): Flow<PagingData<Shop>> {
        return Pager(PagingConfig(10)) { shopDao.selectAll() }
            .flow.map { data -> data.map { it.toUi() } }
    }

    override suspend fun addShop(shop: Shop): Shop {
        return shopDao.getShopById(shopDao.insertShop(shop.toDb())).toUi()
    }

    override suspend fun updateShop(shop: Shop) {
        return shopDao.updateShop(shop.toDb())
    }

    override suspend fun removeShop(shop: Shop) {
        return shopDao.deleteShop(shop.toDb())
    }

    override suspend fun getShopById(shopId: Long): Shop {
        return shopDao.getShopById(shopId).toUi()
    }

    override suspend fun addPrice(product: Product, shop: Shop, price: Double) {
        return priceDao.insertPrice(PriceDb(product.id, shop.id, price))
    }

    override suspend fun updatePrice(product: Product, shop: Shop, price: Double) {
        return priceDao.updatePrice(PriceDb(product.id, shop.id, price))
    }

    override suspend fun getShopsAndPricesByProductId(productId: Long): List<Price> {
        return priceDao.selectPrices(productId).map { it.toUi() }
    }

    override suspend fun removePrice(product: Product, shop: Shop) {
        return priceDao.deletePrice(PriceDb(product.id, shop.id))
    }
}