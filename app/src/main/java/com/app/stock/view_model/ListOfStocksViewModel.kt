package com.app.stock.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.stock.SmallCaseApp
import com.app.stock.base.BaseViewModel
import com.app.stock.database.AppDatabase
import com.app.stock.database.Stock
import com.app.stock.network.ApiTag
import com.app.stock.network.ErrorType
import com.app.stock.network.model.ListOfStocksApiResponse
import com.app.stock.network.model.StockDetail
import kotlinx.coroutines.*
import javax.inject.Inject

class ListOfStocksViewModel : BaseViewModel() {

    val apiErrorLiveData: MutableLiveData<String> = MutableLiveData()
    val otherApiErrorsLiveData: MutableLiveData<ErrorType> = MutableLiveData()
    val loadingVisibilityLiveData = MutableLiveData<Boolean>()
    val listOfStocksLiveData = MutableLiveData<List<StockDetail>>()
    var isPolling = false
    private var pollingJob: Job? = null
    var highestPriceStockDetail: StockDetail? = null

    @Inject
    lateinit var database: AppDatabase

    init {
        SmallCaseApp.getAppComponent().inject(this)
    }

    fun fetchListOfStocks(sids: List<String>) {
        loadingVisibilityLiveData.value = true
        callApi(serviceApi.fetchListOfStocks(sids.joinToString(",")), ApiTag.STOCKS_LIST)
    }

    override fun onApiSuccess(apiTag: ApiTag, response: Any) {
        when (apiTag) {
            ApiTag.STOCKS_LIST -> {
                loadingVisibilityLiveData.value = false
                val stocksListResponse = response as ListOfStocksApiResponse
                listOfStocksLiveData.value = stocksListResponse.data
                calculateHighestPriceStock(stocksListResponse.data)
                if (isPolling) {
                    insertPollingStocksData(stocksListResponse.data)
                }
            }
        }
    }

    override fun onApiError(errorMessage: String?, apiTag: ApiTag) {
        loadingVisibilityLiveData.value = false
        apiErrorLiveData.value = errorMessage
    }

    override fun onTimeout(apiTag: ApiTag) {
        loadingVisibilityLiveData.value = false
        otherApiErrorsLiveData.value = ErrorType.TIMEOUT_ERROR
    }

    override fun onNetworkError(apiTag: ApiTag) {
        loadingVisibilityLiveData.value = false
        otherApiErrorsLiveData.value = ErrorType.NETWORK_ERROR
    }

    override fun onAuthError(apiTag: ApiTag) {
        loadingVisibilityLiveData.value = false
        otherApiErrorsLiveData.value = ErrorType.AUTH_ERROR
    }

    fun pollLatestStockDetails(sids: List<String>) {
        isPolling = true;
        pollingJob = viewModelScope.launch {
            while (true) {
                delay(5000)
                /*
                * no need to dispatch on worker thread as retrofit already handles it
                * */
                fetchListOfStocks(sids)
            }
        }
    }

    fun cancelStocksPollingCoroutine() {
        isPolling = false
        pollingJob?.cancel()
    }

    private fun calculateHighestPriceStock(stocks: List<StockDetail>) {
        for (stockDetail in stocks) {
            if (highestPriceStockDetail == null) {
                highestPriceStockDetail = stockDetail
            } else {
                if (stockDetail.price > highestPriceStockDetail?.price!!) {
                    highestPriceStockDetail = stockDetail
                }
            }
        }
    }

    private fun insertPollingStocksData(stocks: List<StockDetail>) {
        val dbInsertionJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val dbStocks = mutableListOf<Stock>()
                for (stockDetail in stocks) {
                    dbStocks.add(Stock(0, stockDetail.sid, stockDetail.price))
                }
                database.stockDao().insertListOfStocks(dbStocks)
            }
        }
    }
}