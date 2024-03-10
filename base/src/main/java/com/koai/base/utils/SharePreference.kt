/*
 * *
 *  * Created by Nguyễn Kim Khánh on 7/18/23, 10:10 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/18/23, 10:10 AM
 *
 */

package com.koai.base.utils

import android.content.Context
import android.content.SharedPreferences
import com.koai.base.R

class SharePreference(private val context: Context) {
    fun getIntPref(key: String): Int {
        val pref: SharedPreferences =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        return pref.getInt(key, -1)
    }

    fun setIntPref(
        key: String,
        value: Int,
    ) {
        val pref: SharedPreferences =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        pref.edit().putInt(key, value).apply()
    }

    fun getStringPref(key: String): String? {
        val pref: SharedPreferences? =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        return pref!!.getString(key, "")
    }

    fun setStringPref(
        key: String,
        value: String,
    ) {
        val pref: SharedPreferences =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        pref.edit().putString(key, value).apply()
    }

    fun getBooleanPref(key: String): Boolean {
        val pref: SharedPreferences? =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)

        return pref!!.getBoolean(key, false)
    }

    fun setBooleanPref(
        key: String,
        value: Boolean,
    ) {
        val pref: SharedPreferences =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        pref.edit().putBoolean(key, value).apply()
    }
}
