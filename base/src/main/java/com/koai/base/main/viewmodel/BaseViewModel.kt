package com.koai.base.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koai.base.utils.LogUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    val baseCoroutineContext = Dispatchers.IO + exceptionHandler

    fun launchCoroutine(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(context = baseCoroutineContext, block = block)
}
