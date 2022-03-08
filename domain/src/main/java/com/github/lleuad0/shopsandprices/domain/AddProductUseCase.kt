package com.github.lleuad0.shopsandprices.domain

import javax.inject.Inject

class AddProductUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Unit>() {
    lateinit var product: Product

    override suspend fun execute() {
        return localRepository.addProduct(product)
    }
}