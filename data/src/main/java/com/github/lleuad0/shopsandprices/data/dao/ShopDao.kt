package com.github.lleuad0.shopsandprices.data.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.github.lleuad0.shopsandprices.data.entities.ShopDb

@Dao
interface ShopDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertShop(shopDb: ShopDb): Long

    @Update
    fun updateShop(shopDb: ShopDb)

    @Delete
    fun deleteShop(shopDb: ShopDb)

    @Query("SELECT * from shops WHERE shop_id=:shopId LIMIT 1")
    fun getShopById(shopId: Long): ShopDb

    @Query("SELECT * FROM shops")
    fun selectAll(): PagingSource<Int, ShopDb>
}