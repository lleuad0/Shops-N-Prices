package com.github.lleuad0.shopsandprices.domain.usecase

import com.github.lleuad0.shopsandprices.domain.LocalRepository
import javax.inject.Inject

class GetShopsAndPricesByProductIdUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Map<String, Double>>() {
    var productId: Int = 0

    override suspend fun execute(): Map<String, Double> {
        return localRepository.getShopsAndPricesByProductId(productId)
    }
}