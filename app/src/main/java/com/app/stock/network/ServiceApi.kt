package com.app.stock.network

import com.app.stock.network.model.ListOfStocksApiResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {
    @GET("stocks/quotes")
    fun fetchListOfStocks(@Query("sids") sids: String): Observable<Response<ListOfStocksApiResponse>>
}