package com.koai.base.main.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

internal interface IPagingRepository<T : Any> {
    fun execute(): Flow<PagingData<T>>
}

abstract class PagingRepository<T : Any> : IPagingRepository<T> {
    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun execute(): Flow<PagingData<T>> =
        Pager(
            config = PagingConfig(pageSize = pageSize(), enablePlaceholders = false),
            pagingSourceFactory = { pagingSource() },
            remoteMediator = remoteMediator(),
        ).flow

    abstract fun pagingSource(): IPagingSource<T>

    open fun remoteMediator(): IRemoteMediator<T>? = null

    open fun pageSize() = NETWORK_PAGE_SIZE
}
