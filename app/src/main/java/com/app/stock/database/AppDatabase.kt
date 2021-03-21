package com.app.stock.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Stock::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
}