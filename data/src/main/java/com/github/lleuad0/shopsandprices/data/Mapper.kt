package com.github.lleuad0.shopsandprices.data

import com.github.lleuad0.shopsandprices.data.entities.ProductDb
import com.github.lleuad0.shopsandprices.domain.Product

fun ProductDb.toUi() = Product(productName, 0.0, arrayListOf(""))

fun Product.toDb() = ProductDb(name)