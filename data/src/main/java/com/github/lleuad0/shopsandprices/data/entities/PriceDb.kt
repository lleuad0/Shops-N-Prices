package com.github.lleuad0.shopsandprices.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    primaryKeys = ["id_product", "id_shop"],
    tableName = "prices",
    foreignKeys = [ForeignKey(
        entity = ProductDb::class,
        parentColumns = ["product_id"],
        childColumns = ["id_product"],
        onDelete = CASCADE
    ),
        ForeignKey(
            entity = ShopDb::class,
            parentColumns = ["shop_id"],
            childColumns = ["id_shop"],
            onDelete = CASCADE
        )]
)
data class PriceDb(
    @ColumnInfo(name = "id_product") val productId: Long,
    @ColumnInfo(name = "id_shop", index = true) val shopId: Long,
    @ColumnInfo(name = "price") val price: Double = -1.0,
)
