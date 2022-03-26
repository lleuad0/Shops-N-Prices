package com.github.lleuad0.shopsandprices.domain

import javax.inject.Inject

class AddProductUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Unit>() {
    lateinit var productName: String
    var productPrice: Double = -1.0
    lateinit var productShops: ArrayList<String>

    override suspend fun execute() {
        val product = localRepository.addProduct(Product(productName))
            .run { localRepository.getProductByName(productName) }

        productShops.map {
            localRepository.addShop(Shop(it))
                .run { localRepository.getShopByName(it) }
        }.forEach {
            localRepository.addPrice(product, it, productPrice)
        }
    }
}