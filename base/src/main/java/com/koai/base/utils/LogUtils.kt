package com.koai.base.utils
import android.util.Log
import com.koai.base.BuildConfig

object LogUtils {
    fun log(
        tag: String,
        msg: String,
        type: LogType = LogType.DEBUG,
    ) {
        if (BuildConfig.DEBUG) {
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