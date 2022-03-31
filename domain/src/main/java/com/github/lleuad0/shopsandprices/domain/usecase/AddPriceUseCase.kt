package com.github.lleuad0.shopsandprices.domain.usecase

import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.model.Shop
import javax.inject.Inject

class AddPriceUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Unit>() {
    lateinit var product: Product
    lateinit var shop: Shop
    var price: Double = 0.0

    override suspend fun execute() {
        val product = localRepository.addProduct(this.product)
        val shop = localRepository.addShop(this.shop)
        return localRepository.addPrice(product, shop, price)
    }
}