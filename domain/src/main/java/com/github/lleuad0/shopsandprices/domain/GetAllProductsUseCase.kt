package com.github.lleuad0.shopsandprices.domain

import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Array<Product>>() {

    override suspend fun execute(): Array<Product> {
        return localRepository.getAllProducts()
    }
}