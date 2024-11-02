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
    fun getIntPref(key: String, default: Int = -1): Int {
        val pref: SharedPreferences =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        return pref.getInt(key, default)
    }

    fun setIntPref(
        key: String,
        value: Int,
    ) {
        val pref: SharedPreferences =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        pref.edit().putInt(key, value).apply()
    }

    fun getStringPref(key: String, default: String = ""): String? {
        val pref: SharedPreferences? =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        return pref!!.getString(key, default)
    }

    fun setStringPref(
        key: String,
        value: String,
    ) {
        val pref: SharedPreferences =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        pref.edit().putString(key, value).apply()
    }

    fun getBooleanPref(key: String, default: Boolean = false): Boolean {
        val pref: SharedPreferences? =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)

        return pref!!.getBoolean(key, default)
    }

    fun setBooleanPref(
        key: String,
        value: Boolean,
    ) {
        val pref: SharedPreferences =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        pref.edit().putBoolean(key, value).apply()
    }

    fun removePref(key: String){
        val pref: SharedPreferences =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        pref.edit().remove(key).apply()
    }

    fun contains(key: String): Boolean{
        val pref: SharedPreferences =
            context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        return pref.contains(key)
    }
}
