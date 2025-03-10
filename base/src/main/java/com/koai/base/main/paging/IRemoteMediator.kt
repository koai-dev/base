@file:OptIn(ExperimentalPagingApi::class)

package com.koai.base.main.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator

abstract class IRemoteMediator<T : Any> : RemoteMediator<Int, T>() {
    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH
}
