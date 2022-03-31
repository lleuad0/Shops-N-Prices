package com.github.lleuad0.shopsandprices.domain.usecase

import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Shop
import javax.inject.Inject

class AddShopUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Shop>() {
    lateinit var shopName: String
    lateinit var shopInfo: String

    override suspend fun execute(): Shop {
        return localRepository.addShop(Shop(shopName, shopInfo))
    }
}