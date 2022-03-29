package com.github.lleuad0.shopsandprices.data.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.github.lleuad0.shopsandprices.data.entities.PriceDb
import com.github.lleuad0.shopsandprices.data.entities.ShopAndPrice

@Dao
interface PriceDao {
    @Insert
    fun insertPrice(priceDb: PriceDb)

    @Delete
    fun removePrice(priceDb: PriceDb)

    @Query("SELECT * FROM prices")
    fun selectAll(): PagingSource<Int, PriceDb>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT * FROM prices JOIN shops " +
                "ON shops.shop_name = prices.name_shop " +
                "WHERE name_product = :productName"
    )
    fun selectPrices(productName: String): List<ShopAndPrice>
}