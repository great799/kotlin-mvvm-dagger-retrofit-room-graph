package com.app.stock.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StockDao {
    @Query("SELECT * FROM stock")
    fun getAll(): List<Stock>

    @Query("SELECT * FROM stock WHERE sid = :stockId")
    suspend fun loadAllBySIds(stockId: String): List<Stock>

    @Insert
    suspend fun insertListOfStocks(listOfStocks: List<Stock>)

    @Delete
    fun delete(stock: Stock)
}