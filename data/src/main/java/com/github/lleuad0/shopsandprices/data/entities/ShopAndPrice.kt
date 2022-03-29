package com.github.lleuad0.shopsandprices.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ShopAndPrice(
    @Embedded val shop: ShopDb,
    @Relation(
        parentColumn = "shop_name",
        entityColumn = "name_shop"
    ) val price: PriceDb
)