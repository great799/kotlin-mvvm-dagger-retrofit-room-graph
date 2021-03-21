package com.app.stock.utils

import android.os.Bundle
import android.util.Log
import com.app.stock.BuildConfig
import java.util.*

object AppLog {

    private val isDebug = BuildConfig.DEBUG
    private val isInfo = BuildConfig.DEBUG
    private val isWarn = BuildConfig.DEBUG
    private val isError = BuildConfig.DEBUG
    private val isVerb = BuildConfig.DEBUG

    private const val DEFAULT_TAG = "SmallCaseLog"
    private const val DEFAULT_ERROR = "Errored"

    //----------- Verbose -----------

    @JvmStatic
    fun v(tag: String, msg: String) {
        if (isVerb)
            Log.v(tag, "$msg ")
    }

    @JvmStatic
    fun v(msg: String) {
        v(DEFAULT_TAG, msg)
    }

    //----------- Info -----------

    @JvmStatic
    fun i(tag: String, msg: String) {
        if (isInfo)
            Log.i(tag, "$msg ")
    }

    @JvmStatic
    fun i(msg: String) {
        i(DEFAULT_TAG, msg)
    }

    //----------- Warning -----------

    @JvmStatic
    fun w(tag: String, msg: String) {
        if (isWarn)
            Log.w(tag, "$msg ")
    }

    @JvmStatic
    fun w(msg: String) {
        w(DEFAULT_TAG, msg)
    }

    //----------- Debug -----------

    @JvmStatic
    fun d(tag: String, msg: String) {
        if (isDebug)
            Log.d(tag, "$msg ")
    }

    @JvmStatic
    fun d(tag: String, msg: String, e: Throwable) {
        if (isDebug)
            Log.d(tag, "$msg ", e)
    }

    @JvmStatic
    fun d(msg: String) {
        d(DEFAULT_TAG, msg)
    }

    @JvmStatic
    fun d(e: Throwable) {
        d(DEFAULT_TAG, DEFAULT_ERROR, e)
    }

    @JvmStatic
    fun d(l: List<*>) {
        for (o in l)
            d(DEFAULT_TAG, o.toString())
    }

    @JvmStatic
    fun d(b: Bundle) {
        for (key in b.keySet()) {
            when {
                b.get(key) is List<*> -> d(b.get(key) as List<*>)
                isArray(b.get(key)) -> d(b.get(key))
                else -> d(DEFAULT_TAG, b.get(key)!!.toString())
            }
        }
    }

    @JvmStatic
    fun d(a: Any?) {
        try {
            if (isArray(a))
                Arrays.deepToString(a as Array<Any>?)
            d(a!!.toString() + "")
        } catch (e: Exception) {
            d(e)
        }
    }

    @JvmStatic
    fun d(s: String, vararg args: Any) {
        d(formatString(s, *args))
    }

    //----------- Error -----------

    @JvmStatic
    fun e(tag: String, msg: String) {
        if (isError)
            Log.e(tag, "$msg ")
    }

    @JvmStatic
    fun e(msg: String, e: Throwable) {
        if (isError)
            Log.e(DEFAULT_ERROR, "$msg ", e)
    }

    @JvmStatic
    fun e(tag: String, msg: String, e: Throwable) {
        if (isError)
            Log.e(tag, "$msg ", e)
    }

    @JvmStatic
    fun e(msg: String) {
        e(DEFAULT_TAG, msg)
    }

    @JvmStatic
    fun e(e: Throwable) {
        e(DEFAULT_TAG, DEFAULT_ERROR, e)
    }

    //------ Other methods -------
    private fun isArray(obj: Any?): Boolean {
        return obj != null && obj.javaClass.isArray
    }

    private fun formatString(str: String, vararg args: Any): String {
        return try {
            String.format(Locale.ENGLISH, str, *args)
        } catch (e: Exception) {
            ""
        }
    }
}