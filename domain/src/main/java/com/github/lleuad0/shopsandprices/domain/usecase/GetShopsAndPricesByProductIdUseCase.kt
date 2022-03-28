package com.github.lleuad0.shopsandprices.domain.usecase

import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Price
import javax.inject.Inject

class GetShopsAndPricesByProductIdUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<List<Price>>() {
    var productId: Int = 0

    override suspend fun execute(): List<Price> {
        return localRepository.getShopsAndPricesByProductId(productId)
    }
}