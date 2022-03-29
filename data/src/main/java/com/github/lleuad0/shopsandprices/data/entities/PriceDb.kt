package com.github.lleuad0.shopsandprices.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    primaryKeys = ["name_product", "name_shop"],
    tableName = "prices",
    foreignKeys = [ForeignKey(
        entity = ProductDb::class,
        parentColumns = ["product_name"],
        childColumns = ["name_product"],
        onDelete = CASCADE
    ),
        ForeignKey(
            entity = ShopDb::class,
            parentColumns = ["shop_name"],
            childColumns = ["name_shop"],
            onDelete = CASCADE
        )]
)
data class PriceDb(
    @ColumnInfo(name = "name_product") val productName: String,
    @ColumnInfo(name = "name_shop", index = true) val shopName: String,
    @ColumnInfo(name = "price") val price: Double,
)
