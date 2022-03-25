package com.github.lleuad0.shopsandprices.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.lleuad0.shopsandprices.data.dao.ProductDao
import com.github.lleuad0.shopsandprices.data.dao.ShopDao
import com.github.lleuad0.shopsandprices.data.entities.ProductDb
import com.github.lleuad0.shopsandprices.data.entities.ShopDb

@Database(entities = [ProductDb::class, ShopDb::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun shopDao(): ShopDao
}