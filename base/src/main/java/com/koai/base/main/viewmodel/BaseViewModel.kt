package com.koai.base.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.koai.base.utils.LogUtils
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel : ViewModel() {
    protected val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            LogUtils.log(
                "ERROR VM",
                throwable.message ?: "Unknown error",
                type = LogUtils.LogType.ERROR,
            )
            _msgException.postValue(throwable.message ?: "Unknown error")
        }
    private val _msgException = MutableLiveData<String>()
    val msgException: LiveData<String> = _msgException
}
