package com.app.stock.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Stock(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "sid") val sid: String,
    @ColumnInfo(name = "price") val price: Double
)