package com.app.stock.view_model

import com.app.stock.base.BaseViewModel
import com.app.stock.network.ApiTag

class MainActivityViewModel : BaseViewModel() {
    override fun onApiSuccess(apiTag: ApiTag, response: Any) {
        TODO("Not yet implemented")
    }

    override fun onApiError(errorMessage: String?, apiTag: ApiTag) {
        TODO("Not yet implemented")
    }

    override fun onTimeout(apiTag: ApiTag) {
        TODO("Not yet implemented")
    }

    override fun onNetworkError(apiTag: ApiTag) {
        TODO("Not yet implemented")
    }

    override fun onAuthError(apiTag: ApiTag) {
        TODO("Not yet implemented")
    }
}