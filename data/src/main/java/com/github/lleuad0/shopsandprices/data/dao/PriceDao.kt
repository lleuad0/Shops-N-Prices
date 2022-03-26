package com.github.lleuad0.shopsandprices.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.lleuad0.shopsandprices.data.entities.PriceDb

@Dao
interface PriceDao {
    @Insert
    fun insertPrice(priceDb: PriceDb)

    @Delete
    fun removePrice(priceDb: PriceDb)

    @Query("SELECT * FROM prices")
    fun selectAll(): PagingSource<Int, PriceDb>
}