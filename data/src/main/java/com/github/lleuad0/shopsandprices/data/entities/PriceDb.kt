package com.github.lleuad0.shopsandprices.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["id_product", "id_shop"],
    tableName = "prices",
    foreignKeys = [ForeignKey(
        entity = ProductDb::class,
        parentColumns = ["product_id"],
        childColumns = ["id_product"]
    ),
        ForeignKey(entity = ShopDb::class, parentColumns = ["shop_id"], childColumns = ["id_shop"])]
)
data class PriceDb(
    @ColumnInfo(name = "id_product") val productId: Int,
    @ColumnInfo(name = "id_shop", index = true) val shopId: Int,
    @ColumnInfo(name = "price") val price: Double,
)
