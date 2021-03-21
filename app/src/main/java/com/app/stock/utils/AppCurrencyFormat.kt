package com.app.stock.utils

import android.content.Context
import com.app.stock.R
import com.app.stock.network.model.AppCurrency

object AppCurrencyFormat {

    fun addCurrencySymbolToNumber(context: Context, currency: AppCurrency, number: Double): String {
        when (currency) {
            AppCurrency.RUPEES -> {
                return context.resources.getString(R.string.Rs) + " " + number
            }
            else -> {
                return ""
            }
        }
    }
}