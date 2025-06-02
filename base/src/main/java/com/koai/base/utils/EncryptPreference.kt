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
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.koai.base.R

interface EncryptPreferenceApp : SharePreferenceApp {
    fun clear()
    fun getAll(): Map<String, *>
    fun edit(): SharedPreferences.Editor
    fun getLongPref(key: String, default: Long = 0): Long
    fun setLongPref(key: String, value: Long)
    fun getStringSetPref(key: String, default: Set<String> = emptySet()): Set<String>?
}

class EncryptPreferenceAppImpl(
    private val context: Context,
) : EncryptPreferenceApp {
    private val sharedPreferences: SharedPreferences by lazy {
        val masterKey = MasterKey
            .Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "secret_" + context.resources.getString(R.string.app_name),
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
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

    override fun clear() {
        sharedPreferences.edit { clear() }
    }

    override fun getAll(): Map<String, *> = sharedPreferences.all

    override fun edit(): SharedPreferences.Editor = sharedPreferences.edit()

    override fun getLongPref(
        key: String,
        default: Long,
    ): Long = sharedPreferences.getLong(key, default)

    override fun setLongPref(
        key: String,
        value: Long,
    ) {
        sharedPreferences.edit { putLong(key, value) }
    }

    override fun getStringSetPref(
        key: String,
        default: Set<String>,
    ): Set<String>? = sharedPreferences.getStringSet(key, default)
}
