package com.github.lleuad0.shopsandprices.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductDb(
    @PrimaryKey val productId: Int = -1,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "shops") val shops: String,
    )