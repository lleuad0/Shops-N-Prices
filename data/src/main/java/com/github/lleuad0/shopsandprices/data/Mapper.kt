package com.github.lleuad0.shopsandprices.data

import com.github.lleuad0.shopsandprices.data.entities.ProductDb
import com.github.lleuad0.shopsandprices.data.entities.ShopAndPrice
import com.github.lleuad0.shopsandprices.data.entities.ShopDb
import com.github.lleuad0.shopsandprices.domain.model.Price
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.model.Shop

fun ProductDb.toUi() = Product(productName, productInfo, productId)

fun Product.toDb() = ProductDb(name, additionalInfo, id)

fun ShopDb.toUi() = Shop(shopName, shopInfo, shopId)

fun Shop.toDb() = ShopDb(name, additionalInfo, id)

fun ShopAndPrice.toUi() = Price(price.price, shop.toUi())