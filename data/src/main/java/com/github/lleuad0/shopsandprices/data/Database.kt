package com.github.lleuad0.shopsandprices.data

import androidx.room.Database

@Database(entities = [ProductDb::class], version = 1)
abstract class Database {
    abstract fun productDao(): ProductDao
}