package com.app.stock.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.stock.SmallCaseApp
import com.app.stock.base.BaseViewModel
import com.app.stock.database.AppDatabase
import com.app.stock.database.Stock
import com.app.stock.network.ApiTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryViewModel : BaseViewModel() {
    @Inject
    lateinit var database: AppDatabase

    val listOfStocksLiveData = MutableLiveData<List<Stock>>()
    val loadingVisibilityLiveData = MutableLiveData<Boolean>()

    init {
        SmallCaseApp.getAppComponent().inject(this)
    }

    fun getHistoryOfSID(sid: String) {
        loadingVisibilityLiveData.value = true
        val fetchHistoryJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val list = database.stockDao().loadAllBySIds(sid)
                /*
                * must dispatch on main thread
                * */
                withContext(Dispatchers.Main) {
                    loadingVisibilityLiveData.value = false
                    listOfStocksLiveData.value = list
                }
            }
        }
    }

    override fun onApiSuccess(apiTag: ApiTag, response: Any) {
    }

    override fun onApiError(errorMessage: String?, apiTag: ApiTag) {
    }

    override fun onTimeout(apiTag: ApiTag) {
    }

    override fun onNetworkError(apiTag: ApiTag) {
    }

    override fun onAuthError(apiTag: ApiTag) {
    }
}