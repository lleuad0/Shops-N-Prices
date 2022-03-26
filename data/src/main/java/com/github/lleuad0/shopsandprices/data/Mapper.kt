package com.github.lleuad0.shopsandprices.data

import com.github.lleuad0.shopsandprices.data.entities.ProductDb
import com.github.lleuad0.shopsandprices.data.entities.ShopDb
import com.github.lleuad0.shopsandprices.domain.Product
import com.github.lleuad0.shopsandprices.domain.Shop

fun ProductDb.toUi() = Product(productName, productId)

fun Product.toDb() = ProductDb(name, id)

fun ShopDb.toUi() = Shop(shopName, shopId)

fun Shop.toDb() = ShopDb(name, id)