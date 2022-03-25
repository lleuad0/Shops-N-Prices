package com.github.lleuad0.shopsandprices.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.lleuad0.shopsandprices.data.entities.ProductDb

@Dao
interface ProductDao {
    @Insert
    fun insertProduct(productDb: ProductDb)

    @Delete
    fun removeProduct(productDb: ProductDb)

    @Query("SELECT * FROM products")
    fun selectAll(): Array<ProductDb>
}