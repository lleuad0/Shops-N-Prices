package com.github.lleuad0.shopsandprices.data

import com.github.lleuad0.shopsandprices.domain.Product

fun ProductDb.toUi() = Product(productName, price, arrayListOf(shops))

fun Product.toDb() = ProductDb(name, price, shops[0])