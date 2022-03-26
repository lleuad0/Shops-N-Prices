package com.github.lleuad0.shopsandprices.data.dao

import androidx.room.*
import com.github.lleuad0.shopsandprices.data.entities.ShopDb

@Dao
interface ShopDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertShop(shopDb: ShopDb)

    @Delete
    fun removeShop(shopDb: ShopDb)

    @Query("SELECT * from shops WHERE shop_name=:shopName LIMIT 1")
    fun getShopByName(shopName: String): ShopDb

    @Query("SELECT * FROM shops")
    fun selectAll(): Array<ShopDb>
}