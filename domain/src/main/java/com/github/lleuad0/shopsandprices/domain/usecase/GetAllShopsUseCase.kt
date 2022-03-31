package com.github.lleuad0.shopsandprices.domain.usecase

import androidx.paging.PagingData
import com.github.lleuad0.shopsandprices.domain.LocalRepository
import com.github.lleuad0.shopsandprices.domain.model.Shop
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllShopsUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<Flow<PagingData<Shop>>>() {

    override suspend fun execute(): Flow<PagingData<Shop>> {
        return localRepository.getAllShops()
    }
}