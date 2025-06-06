package com.koai.base.core.worker.sessiontimeout

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.UUID

class SessionTimeoutImpl(
    private val context: Context,
) : SessionTimeout {
    companion object {
        const val SESSION_TIMEOUT = "session_timeout"
    }

    override fun start(): UUID {
        cancel()
        val workRequest =
            OneTimeWorkRequestBuilder<SessionTimeoutWorker>()
                .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            SESSION_TIMEOUT,
            ExistingWorkPolicy.REPLACE,
            workRequest,
        )
        return workRequest.id
    }

    private fun cancel() {
        WorkManager.getInstance(context).cancelUniqueWork(SESSION_TIMEOUT)
    }
}
