package com.github.lleuad0.shopsandprices.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.lleuad0.shopsandprices.domain.LocalRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HiltModule {

    @Binds
    abstract fun bindsLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    companion object {

        @Provides
        @Singleton
        fun providesProductDao(@ApplicationContext context: Context): ProductDao {
            return Room.databaseBuilder(context, Database::class.java, "database").build()
                .productDao()
        }
    }
}