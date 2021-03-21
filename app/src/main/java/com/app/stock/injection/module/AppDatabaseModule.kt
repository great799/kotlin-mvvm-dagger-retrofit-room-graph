package com.app.stock.injection.module

import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.stock.SmallCaseApp
import com.app.stock.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module which provides all required dependencies about database
 */
@Module
class AppDatabaseModule(private val application: SmallCaseApp) {
    /**
     * Provides the database object.
     * @return the database object
     */
    @Singleton
    @Provides
    internal fun provideDatabaseObject(): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java, "small-case-db"
        ).build()
    }
}