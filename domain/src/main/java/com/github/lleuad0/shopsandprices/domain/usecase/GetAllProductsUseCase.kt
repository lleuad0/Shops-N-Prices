package com.github.lleuad0.shopsandprices.domain.usecase

import androidx.paging.PagingData
import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Flow<PagingData<Product>>>() {

    override suspend fun execute(): Flow<PagingData<Product>> {
        return localRepository.getAllProducts()
    }
}