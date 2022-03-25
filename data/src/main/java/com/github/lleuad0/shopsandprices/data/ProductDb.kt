package com.github.lleuad0.shopsandprices.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductDb(
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "shops") val shops: String,

    @PrimaryKey(autoGenerate = true) val productId: Int = 0,
)