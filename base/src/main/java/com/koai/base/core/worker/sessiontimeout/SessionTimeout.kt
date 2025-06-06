package com.koai.base.core.worker.sessiontimeout

import java.util.UUID

interface SessionTimeout {
    fun start(): UUID
}
