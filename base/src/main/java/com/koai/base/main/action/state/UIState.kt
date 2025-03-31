package com.koai.base.main.action.state

sealed class UIState<out T> {
    data class Success<out T>(val data: T) : UIState<T>()

    data class Error(val message: String? = null, val code: Int? = null) : UIState<Nothing>()

    data object Loading : UIState<Nothing>()
}
