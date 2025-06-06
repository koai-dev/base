package com.koai.base.core.worker.sessiontimeout

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class SessionTimeoutWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        delay(300000)
        return Result.success()
    }
}
