package com.github.lleuad0.shopsandprices.domain.usecase

import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Price
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.model.Shop
import javax.inject.Inject

class EditProductUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Unit>() {
    var productId: Long? = null
    var productName: String? = null
    var productInfo: String? = null
    lateinit var prices: List<Price>

    override suspend fun execute() {
        val product = Product(productName!!, productInfo!!, productId!!)
        localRepository.updateProduct(product)
        prices.forEach {
            if (it.shop.id < 0) {
                val shop = localRepository.addShop(Shop(it.shop.name, it.shop.additionalInfo))
                localRepository.addPrice(product, shop, it.price)
            } else {
                localRepository.updateShop(it.shop)
                localRepository.updatePrice(product, it.shop, it.price)
            }
        }
    }
}