package com.github.lleuad0.shopsandprices.domain.usecase

import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Price
import javax.inject.Inject

class GetPricesByProductIdUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<List<Price>>() {
    var productId: Long = 0

    override suspend fun execute(): List<Price> {
        return localRepository.getShopsAndPricesByProductId(productId)
    }
}