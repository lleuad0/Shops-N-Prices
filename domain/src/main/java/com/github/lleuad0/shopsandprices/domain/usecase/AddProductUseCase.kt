package com.github.lleuad0.shopsandprices.domain.usecase

import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Product
import javax.inject.Inject

class AddProductUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Product>() {
    lateinit var productName: String
    lateinit var productInfo: String

    override suspend fun execute(): Product {
        return localRepository.addProduct(Product(productName, productInfo))
    }
}