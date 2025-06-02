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
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.koai.base.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SharePreference {
    suspend fun set(key: String, value: Any)
    fun getInt(key: String, default: Int = -1): Flow<Int>
    fun getBoolean(key: String, default: Boolean = false): Flow<Boolean>
    fun getString(key: String, default: String = ""): Flow<String?>
    suspend fun remove(key: String)
    fun contains(key: String): Boolean
    suspend fun clear()
    fun getLong(key: String, default: Long = 0L): Flow<Long>
}
internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
internal class SharePreferenceImpl(
    private val context: Context,
) : SharePreference {

    override suspend fun set(key: String, value: Any) {
        val encrypted = EncryptionHelper.encrypt(context, value.toString())
        context.dataStore.edit { preferences ->
            val secretKey = stringPreferencesKey(key.toEncryptedString(context))
            preferences[secretKey] = encrypted
        }
    }

    override fun getInt(key: String, default: Int): Flow<Int>  = context.dataStore.data.map { preferences ->
        val secretKey = stringPreferencesKey(key.toEncryptedString(context))
        val encrypted = preferences[secretKey] ?: ""
        val decrypted = EncryptionHelper.decrypt(context, encrypted)
        decrypted.toIntOrNull() ?: default
    }

    override fun getBoolean(key: String, default: Boolean): Flow<Boolean> = context.dataStore.data.map { preferences ->
        val secretKey = stringPreferencesKey(key.toEncryptedString(context))
        val encrypted = preferences[secretKey] ?: ""
        val decrypted = EncryptionHelper.decrypt(context, encrypted)
        decrypted.toBooleanStrictOrNull() ?: default
    }

    override fun getString(key: String, default: String): Flow<String?> = context.dataStore.data.map { preferences ->
        val secretKey = stringPreferencesKey(key.toEncryptedString(context))
        val encrypted = preferences[secretKey]
        encrypted?.let {
            EncryptionHelper.decrypt(context, it)
        } ?: default
    }

    override suspend fun remove(key: String){
        context.dataStore.edit { preferences ->
            val secretKey = stringPreferencesKey(key.toEncryptedString(context))
            preferences.remove(secretKey)
        }
    }

    override fun contains(key: String): Boolean {
        context.dataStore.data.map { preferences ->
            val secretKey = stringPreferencesKey(key.toEncryptedString(context))
            return@map preferences.contains(secretKey)
        }
        return false
    }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override fun getLong(key: String, default: Long): Flow<Long>  = context.dataStore.data.map { preferences ->
        val secretKey = stringPreferencesKey(key.toEncryptedString(context))
        val encrypted = preferences[secretKey] ?: ""
        val decrypted = EncryptionHelper.decrypt(context, encrypted)
        decrypted.toLongOrNull() ?: default
    }

}
