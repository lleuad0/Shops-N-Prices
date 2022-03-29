package com.github.lleuad0.shopsandprices.domain.usecase

import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Price
import javax.inject.Inject

class GetShopsAndPricesByProductNameUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<List<Price>>() {
    lateinit var productName: String

    override suspend fun execute(): List<Price> {
        return localRepository.getShopsAndPricesByProductName(productName)
    }
}