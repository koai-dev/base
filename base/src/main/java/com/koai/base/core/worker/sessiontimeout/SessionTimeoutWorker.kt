package com.koai.base.core.worker.sessiontimeout

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.koai.base.utils.Constants
import kotlinx.coroutines.delay

class SessionTimeoutWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        delay(300000)
        sendSessionTimeoutBroadcast()
        return Result.success()
    }

    private fun sendSessionTimeoutBroadcast() {
        val intent = Intent(applicationContext.packageName.plus(Constants.ACTION_SESSION_TIMEOUT))
        applicationContext.sendBroadcast(intent)
    }
}
