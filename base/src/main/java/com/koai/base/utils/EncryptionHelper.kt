package com.koai.base.utils

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

internal object EncryptionHelper {
    private const val KEYSET_NAME = "my_encryption_key"
    private const val PREF_FILE = "my_encryption_prefs"

    fun initialize() {
        try {
            TinkConfig.register()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAead(context: Context): Aead {
        val keysetHandle: KeysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, PREF_FILE)
            .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
            .withMasterKeyUri("android-keystore://my_master_key")
            .build()
            .keysetHandle

        return keysetHandle.getPrimitive(Aead::class.java)
    }

    fun encrypt(context: Context, plaintext: String): String {
        val aead = getAead(context)
        val ciphertext = aead.encrypt(plaintext.trim().toByteArray(), null)
        return android.util.Base64.encodeToString(ciphertext, android.util.Base64.NO_WRAP)
    }

    fun decrypt(context: Context, encrypted: String): String {
        val aead = getAead(context)
        val ciphertext = android.util.Base64.decode(encrypted, android.util.Base64.NO_WRAP)
        val decrypted = aead.decrypt(ciphertext, null)
        return String(decrypted)
    }
}

fun String.toEncryptedString(context: Context): String  = EncryptionHelper.encrypt(context, this.trim())