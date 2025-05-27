package com.koai.base.utils

class SessionTimeoutUtil {
    companion object{
        const val SESSION_TIMEOUT = "session_timeout"
    }

    fun start(){
        val workRequest = OneTimeWorkRequestBuilder<SessionTimeoutWorker>()
            .setInitialDelay(30, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "session_timeout",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )

    }
}