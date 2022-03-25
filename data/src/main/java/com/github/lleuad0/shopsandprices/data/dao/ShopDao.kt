package com.github.lleuad0.shopsandprices.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.lleuad0.shopsandprices.data.entities.ShopDb

@Dao
interface ShopDao {
    @Insert
    fun insertShop(shopDb: ShopDb)

    @Delete
    fun removeShop(shopDb: ShopDb)

    @Query("SELECT * FROM shops")
    fun selectAll(): Array<ShopDb>
}