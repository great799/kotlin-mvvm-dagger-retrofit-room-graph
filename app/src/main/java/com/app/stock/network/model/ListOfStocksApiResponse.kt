package com.app.stock.network.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class ListOfStocksApiResponse(
    @field:Json(name = "success") val success: Boolean,
    @field:Json(name = "data") val data: List<StockDetail>
)

@Parcelize
data class StockDetail(
    @field:Json(name = "sid") val sid: String,
    @field:Json(name = "price") val price: Double,
    @field:Json(name = "close") val close: Double,
    @field:Json(name = "change") val change: Double,
    @field:Json(name = "high") val high: Double,
    @field:Json(name = "low") val low: Double,
    @field:Json(name = "volume") val volume: Long,
    @field:Json(name = "date") val date: String
) : Parcelable