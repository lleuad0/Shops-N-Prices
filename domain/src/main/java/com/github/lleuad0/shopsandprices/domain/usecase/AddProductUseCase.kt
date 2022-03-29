package com.github.lleuad0.shopsandprices.domain.usecase

import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Product
import com.github.lleuad0.shopsandprices.domain.model.Shop
import javax.inject.Inject

class AddProductUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Unit>() {
    lateinit var productName: String
    var productPrice: Double = -1.0
    lateinit var productShops: ArrayList<String>

    override suspend fun execute() {
        val savedProduct = localRepository.getProductByName(productName)

        val product = savedProduct
            ?: localRepository.addProduct(Product(productName))
                .run { localRepository.getProductByName(productName) }

        productShops.map {
            localRepository.addShop(Shop(it))
                .run { localRepository.getShopByName(it) }
        }.forEach {
            product?.let { product -> localRepository.addPrice(product, it, productPrice) }
        }
    }
}