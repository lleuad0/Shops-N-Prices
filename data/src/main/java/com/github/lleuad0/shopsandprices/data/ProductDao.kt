package com.github.lleuad0.shopsandprices.data

import androidx.room.*

@Dao
interface ProductDao {
    @Insert
    fun insertProduct(productDb: ProductDb)

    @Delete
    fun removeProduct(productDb: ProductDb)

    @Query("SELECT * FROM products")
    fun selectAll(): Array<ProductDb>
}