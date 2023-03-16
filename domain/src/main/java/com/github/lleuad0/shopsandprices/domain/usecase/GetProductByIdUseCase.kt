package com.github.lleuad0.shopsandprices.domain.usecase

import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Product
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Product?>() {
    var productId: Long = 0

    override suspend fun execute(): Product? {
        return localRepository.getProductById(productId)
    }
}