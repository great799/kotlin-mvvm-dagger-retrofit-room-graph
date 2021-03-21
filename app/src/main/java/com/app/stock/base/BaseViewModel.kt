package com.app.stock.base

import androidx.lifecycle.ViewModel
import com.app.stock.network.ApiTag
import com.app.stock.network.ServiceApi
import com.app.stock.utils.AppLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    //Api callback methods
    abstract fun onApiSuccess(apiTag: ApiTag, response: Any)
    abstract fun onApiError(errorMessage: String?, apiTag: ApiTag)
    abstract fun onTimeout(apiTag: ApiTag)
    abstract fun onNetworkError(apiTag: ApiTag)
    abstract fun onAuthError(apiTag: ApiTag)

    @Inject
    lateinit var serviceApi: ServiceApi

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    protected fun callApi(observable: Observable<out Any>, apiTag: ApiTag): Disposable {
        var disposable = observable.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { response: Any? ->
                    @Suppress("UNCHECKED_CAST")
                    var httpResponse = response as Response<Any>
                    AppLog.e(httpResponse.code().toString(), apiTag.name)
                    if (httpResponse.code() == 200) {
                        onApiSuccess(apiTag, httpResponse.body()!!)
                    } else {
                        var errorResponse = httpResponse.errorBody()
                        when (httpResponse.code()) {
                            401 -> {
                                onAuthError(apiTag)
                            }
                            else -> {
                                onApiError(getErrorMessage(errorResponse!!), apiTag)
                            }
                        }
                    }

                },
                { error: Throwable? ->
                    when (error) {
                        is HttpException -> {
                            onApiError(getErrorMessage(error.response()?.errorBody()!!), apiTag)
                        }
                        is SocketTimeoutException -> {
                            onTimeout(apiTag)
                        }
                        is IOException -> {
                            onNetworkError(apiTag)
                        }
                        else -> {
                            onApiError(error?.message, apiTag)
                        }
                    }
                }
            )
        compositeDisposable.add(disposable)
        return disposable
    }

    private fun getErrorMessage(responseBody: ResponseBody): String {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            val errorObject = jsonObject.getJSONObject("error")
            errorObject.getString("message")
        } catch (e: Exception) {
            e.message!!
        }
    }
}