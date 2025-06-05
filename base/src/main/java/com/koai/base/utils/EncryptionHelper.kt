package com.koai.base.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

interface EncryptionHelper {
    fun initialize(alias: String? = null)

    fun encrypt(
        plainText: String,
        secretKey: SecretKey? = null,
    ): String?

    fun decrypt(
        data: String,
        secretKey: SecretKey? = null,
    ): String?
}

internal class EncryptionHelperImpl : EncryptionHelper {
    companion object {
        private const val KEY_ALIAS = "my_alias_key"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    }

    private var alias: String? = null

    override fun initialize(alias: String?) {
        try {
            this.alias = alias
            generateAESKeyIfNeeded(alias ?: KEY_ALIAS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateAESKeyIfNeeded(alias: String? = this.alias) {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        if (!keyStore.containsAlias(alias)) {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            val spec =
                KeyGenParameterSpec
                    .Builder(
                        alias ?: this.alias ?: KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                    ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build()
            keyGenerator.init(spec)
            keyGenerator.generateKey()
        }
    }

    private fun getAESKey(): SecretKey? {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return keyStore.getKey(alias ?: KEY_ALIAS, null) as? SecretKey
    }

    override fun encrypt(
        plainText: String,
        secretKey: SecretKey?,
    ): String? {
        try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val iv = ByteArray(12).apply { SecureRandom().nextBytes(this) }
            val gcmSpec = GCMParameterSpec(128, iv)

            cipher.init(Cipher.ENCRYPT_MODE, secretKey ?: getAESKey(), gcmSpec)
            val ciphertext = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

            return Base64.encodeToString(iv + ciphertext, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun decrypt(
        data: String,
        secretKey: SecretKey?,
    ): String? {
        try {
            val encryptedData = Base64.decode(data, Base64.DEFAULT)
            val iv = encryptedData.copyOfRange(0, 12)
            val ciphertext = encryptedData.copyOfRange(12, encryptedData.size)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val gcmSpec = GCMParameterSpec(128, iv)

            cipher.init(Cipher.DECRYPT_MODE, secretKey ?: getAESKey(), gcmSpec)
            val plainBytes = cipher.doFinal(ciphertext)

            return String(plainBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
