package com.github.lleuad0.shopsandprices.data

import android.content.Context
import androidx.room.Room
import com.github.lleuad0.shopsandprices.data.dao.PriceDao
import com.github.lleuad0.shopsandprices.data.dao.ProductDao
import com.github.lleuad0.shopsandprices.data.dao.ShopDao
import com.github.lleuad0.shopsandprices.domain.LocalRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
abstract class HiltModule {

    @Binds
    abstract fun bindsLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    companion object {
        @Provides
        @Singleton
        fun providesBackgroundContext(): CoroutineContext = Dispatchers.Default

        @Provides
        @Singleton
        fun providesDatabase(@ApplicationContext context: Context): Database {
            return Room.databaseBuilder(context, Database::class.java, "database").build()
        }

        @Provides
        @Singleton
        fun providesProductDao(database: Database): ProductDao {
            return database.productDao()
        }

        @Provides
        @Singleton
        fun providesShopDao(database: Database): ShopDao {
            return database.shopDao()
        }

        @Provides
        @Singleton
        fun providesPriceDao(database: Database): PriceDao {
            return database.priceDao()
        }
    }
}