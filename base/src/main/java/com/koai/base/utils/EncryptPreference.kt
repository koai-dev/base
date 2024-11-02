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
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.koai.base.R

class EncryptPreference(context: Context) {
    private val pref: SharedPreferences

    init {
        val masterKey =
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

        pref =
            EncryptedSharedPreferences.create(
                context,
                "secret_" + context.resources.getString(R.string.app_name),
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
    }

    fun getIntPref(key: String, default: Int = -1): Int {
        return pref.getInt(key, default)
    }

    fun setIntPref(
        key: String,
        value: Int,
    ) {
        pref.edit().putInt(key, value).apply()
    }

    fun getStringPref(key: String, default: String = ""): String? {
        return pref.getString(key, default)
    }

    fun setStringPref(
        key: String,
        value: String,
    ) {
        pref.edit().putString(key, value).apply()
    }

    fun getBooleanPref(key: String, default: Boolean = false): Boolean {
        return pref.getBoolean(key, default)
    }

    fun setBooleanPref(
        key: String,
        value: Boolean,
    ) {
        pref.edit().putBoolean(key, value).apply()
    }

    fun removePref(key: String) {
        pref.edit().remove(key).apply()
    }

    fun contains(key: String): Boolean {
        return pref.contains(key)
    }

    fun clear() {
        pref.edit().clear().apply()
    }

    fun getAll(): Map<String, *> {
        return pref.all
    }

    fun edit(): SharedPreferences.Editor {
        return pref.edit()
    }

    fun getLongPref(key: String, default: Long = 0): Long {
        return pref.getLong(key, default)
    }

    fun setLongPref(key: String, value: Long) {
        pref.edit().putLong(key, value).apply()
    }
}
