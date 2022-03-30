package com.github.lleuad0.shopsandprices.data.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.github.lleuad0.shopsandprices.data.entities.ProductDb

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(productDb: ProductDb)

    @Update
    fun updateProduct(productDb: ProductDb)

    @Query("SELECT * from products WHERE product_name=:productName LIMIT 1")
    fun getProductByName(productName: String): ProductDb?

    @Query("SELECT * from products WHERE product_id = :productId LIMIT 1")
    fun getProductById(productId: Long): ProductDb?

    @Delete
    fun deleteProduct(productDb: ProductDb)

    @Query("SELECT * FROM products")
    fun selectAll(): PagingSource<Int, ProductDb>
}