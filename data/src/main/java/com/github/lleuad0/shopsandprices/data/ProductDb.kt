package com.github.lleuad0.shopsandprices.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductDb(
    @PrimaryKey val productId: Int = -1,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "shops") val shops: Array<String>,
    ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductDb

        if (productId != other.productId) return false
        if (productName != other.productName) return false
        if (!shops.contentEquals(other.shops)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = productId
        result = 31 * result + productName.hashCode()
        result = 31 * result + shops.contentHashCode()
        return result
    }
}