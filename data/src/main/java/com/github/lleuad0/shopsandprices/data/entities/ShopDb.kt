package com.github.lleuad0.shopsandprices.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shops")
data class ShopDb(
    @ColumnInfo(name = "shop_name") val shopName: String,
    @ColumnInfo(name = "shop_info") val shopInfo: String,

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "shop_id") val shopId: Long = 0,
)