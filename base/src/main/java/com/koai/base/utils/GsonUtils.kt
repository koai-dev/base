package com.koai.base.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object GsonUtils {
    fun <T> fromJson(json: String?, classOfT: Class<T>?): T? {
        return try {
            Gson().fromJson(json, classOfT)
        } catch (e: Exception) {
            null
        }
    }

    fun toJson(src: Any?): String? {
        return try {
            Gson().toJson(src)
        } catch (e: Exception) {
            null
        }
    }

    fun <T> fromJson(json: String?, typeOfT: Type): T? {
        return try {
            Gson().fromJson(json, typeOfT)
        } catch (e: Exception) {
            null
        }
    }

    fun <T> fromJson(json: String?, typeOfT: TypeToken<T>): T? {
        return try {
            Gson().fromJson(json, typeOfT.type)
        } catch (e: Exception) {
            null
        }
    }
}