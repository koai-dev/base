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
import androidx.core.content.edit
import com.koai.base.R

interface SharePreferenceApp {
    fun getIntPref(key: String, default: Int = -1): Int
    fun setIntPref(key: String, value: Int)
    fun getStringPref(key: String, default: String = ""): String?
    fun setStringPref(key: String, value: String)
    fun getBooleanPref(key: String, default: Boolean = false): Boolean
    fun setBooleanPref(key: String, value: Boolean)
    fun removePref(key: String)
    fun contains(key: String): Boolean
}

class SharePreferenceAppImpl(
    private val context: Context,
) : SharePreferenceApp {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.resources.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    override fun getIntPref(
        key: String,
        default: Int,
    ): Int = sharedPreferences.getInt(key, default)

    override fun setIntPref(
        key: String,
        value: Int,
    ) {
        sharedPreferences.edit { putInt(key, value) }
    }

    override fun getStringPref(
        key: String,
        default: String,
    ): String? = sharedPreferences.getString(key, default)

    override fun setStringPref(
        key: String,
        value: String,
    ) {
        sharedPreferences.edit { putString(key, value) }
    }

    override fun getBooleanPref(
        key: String,
        default: Boolean,
    ): Boolean = sharedPreferences.getBoolean(key, default)

    override fun setBooleanPref(
        key: String,
        value: Boolean,
    ) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    override fun removePref(key: String) {
        sharedPreferences.edit { remove(key) }
    }

    override fun contains(key: String): Boolean = sharedPreferences.contains(key)
}
