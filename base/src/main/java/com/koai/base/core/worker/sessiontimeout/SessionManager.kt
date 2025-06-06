package com.koai.base.core.worker.sessiontimeout

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.koai.base.utils.Constants

object SessionManager {
    private var onSessionTimeout: (() -> Unit)? = null
    private val sessionTimeoutReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == context.packageName.plus(Constants.ACTION_SESSION_TIMEOUT)) {
                onSessionTimeout?.invoke()
            }
        }
    }

    fun register(activity: AppCompatActivity, onSessionTimeout: () -> Unit) {
        this.onSessionTimeout = onSessionTimeout
        ContextCompat.registerReceiver(
            activity,
            sessionTimeoutReceiver,
            IntentFilter(Constants.ACTION_SESSION_TIMEOUT),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    fun unregister(activity: AppCompatActivity) {
        this.onSessionTimeout = null
        activity.unregisterReceiver(sessionTimeoutReceiver)
    }
}