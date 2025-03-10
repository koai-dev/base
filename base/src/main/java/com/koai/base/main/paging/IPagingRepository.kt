package com.koai.base.main.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface IPagingRepository<T: Any> {
    fun execute(): Flow<PagingData<T>>
}

abstract class PagingRepository<T: Any>: IPagingRepository<T>{
    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }
    override fun execute(): Flow<PagingData<T>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = pagingSource(),

    )

    abstract fun pagingSource(): IPagingSource<T>
}