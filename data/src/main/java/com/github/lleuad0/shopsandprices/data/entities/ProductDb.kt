package com.github.lleuad0.shopsandprices.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductDb(
    @PrimaryKey @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "product_info") val productInfo: String,
)