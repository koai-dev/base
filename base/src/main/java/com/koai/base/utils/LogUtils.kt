package com.koai.base.utils

import android.util.Log

object LogUtils {
    private var debugMode = false

    fun setDebugMode(debugMode: Boolean) {
        this.debugMode = debugMode
    }

    fun getDebugMode(): Boolean = this.debugMode

    fun log(
        tag: String,
        msg: String,
        type: LogType = LogType.DEBUG,
    ) {
        if (debugMode) {
            try {
                when (type) {
                    LogType.DEBUG -> Log.d(tag, msg)
                    LogType.ERROR -> Log.e(tag, msg)
                    LogType.WARNING -> Log.w(tag, msg)
                    LogType.VERBOSE -> Log.v(tag, msg)
                    LogType.INFO -> Log.i(tag, msg)
                    LogType.ASSETS -> Log.wtf(tag, msg)
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    enum class LogType {
        DEBUG,
        ERROR,
        WARNING,
        VERBOSE,
        INFO,
        ASSETS,
    }
}
