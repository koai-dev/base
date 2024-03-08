package com.koai.base.network

sealed class ResponseStatus<out T> {
    data class Success<out T>(val data: T) : ResponseStatus<T>()

    data class Error(val message: String = "", val errorCode: Int = 500) : ResponseStatus<Nothing>()

    data object Loading : ResponseStatus<Nothing>()
}
