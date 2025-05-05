package com.koai.base.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koai.base.main.action.state.UIState
import com.koai.base.utils.LogUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    protected val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            LogUtils.log(
                "ERROR VM",
                throwable.message ?: "Unknown error",
                type = LogUtils.LogType.ERROR,
            )
            _uiErrorState.update { UIState.Error(message = throwable.message ?: "Unknown error") }
        }
    private val _uiErrorState = MutableStateFlow(UIState.Error())
    val uiErrorState = _uiErrorState.asStateFlow()
    val baseCoroutineContext = Dispatchers.IO + exceptionHandler

    protected val _uiState: MutableStateFlow<UIState<*>> = MutableStateFlow(UIState.Init)
    val uiState = _uiState.asStateFlow()

    val currentJobs = mutableListOf<Job>()

    fun launchCoroutine(block: suspend CoroutineScope.() -> Unit): Job {
        val job = viewModelScope.launch(context = baseCoroutineContext, block = block)
        currentJobs.add(job)
        job.invokeOnCompletion { currentJobs.remove(job) }
        return job
    }

    fun cancelAll() {
        val currentJob = currentJobs.toList()
        currentJob.forEach { job ->
            if (job.isActive) {
                currentJobs.remove(job)
                job.cancel()
            }
        }
    }

    override fun onCleared() {
        cancelAll()
        super.onCleared()
    }
}
