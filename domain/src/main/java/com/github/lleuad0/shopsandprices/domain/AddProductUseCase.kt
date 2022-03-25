package com.github.lleuad0.shopsandprices.domain

import javax.inject.Inject

class AddProductUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Unit>() {
    lateinit var productName: String
    var productPrice: Double = -1.0
    lateinit var productShops: ArrayList<String>

    override suspend fun execute() {
        return localRepository.addProduct(Product(productName, productPrice, productShops))
    }
}