package com.github.lleuad0.shopsandprices.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductDb::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun productDao(): ProductDao
}