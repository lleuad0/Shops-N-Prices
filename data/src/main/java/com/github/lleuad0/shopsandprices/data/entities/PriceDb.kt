package com.github.lleuad0.shopsandprices.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["product_id", "shop_id"],
    tableName = "prices",
    foreignKeys = [ForeignKey(
        entity = ProductDb::class,
        parentColumns = ["product_id"],
        childColumns = ["product_id"]
    ),
        ForeignKey(entity = ShopDb::class, parentColumns = ["shop_id"], childColumns = ["shop_id"])]
)
data class PriceDb(
    @ColumnInfo(name = "product_id") val productId: Int,
    @ColumnInfo(name = "shop_id") val shopId: Int,
    @ColumnInfo(name = "price") val price: Double,
)
