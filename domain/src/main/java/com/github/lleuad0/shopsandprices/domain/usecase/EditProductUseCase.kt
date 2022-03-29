package com.github.lleuad0.shopsandprices.domain.usecase

import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Price
import com.github.lleuad0.shopsandprices.domain.model.Product
import javax.inject.Inject

class EditProductUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Unit>() {
    var productId: Int? = null
    var productName: String? = null
    var productInfo: String? = null
    lateinit var prices: List<Price>

    override suspend fun execute() {
        val product = Product(productName!!, productInfo!!, productId!!)
        localRepository.updateProduct(product)
        prices.forEach {
            localRepository.updateShop(it.shop)
            localRepository.updatePrice(product, it.shop, it.price)
        }
    }
}