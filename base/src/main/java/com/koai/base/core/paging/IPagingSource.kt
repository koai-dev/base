package com.koai.base.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlin.math.max

abstract class IPagingSource<T : Any> : PagingSource<Int, T>() {
    companion object {
        const val STARTING_KEY = 0
        const val LOAD_DELAY_MILLIS = 3_000L
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val startKey = params.key ?: startKey()
        val rangeKey = startKey.until(startKey + params.loadSize)

        return loadData(
            startKey = startKey,
            rangeKey = rangeKey,
            prevKey =
                when (startKey) {
                    startKey() -> null
                    else -> ensureValidKey(key = rangeKey.first - params.loadSize)
                },
            nextKey = rangeKey.last + 1,
        )
    }

    /**
     * Makes sure the paging key is never less than [STARTING_KEY]
     */
    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)

    open fun startKey() = STARTING_KEY

    abstract suspend fun loadData(
        startKey: Int,
        rangeKey: IntRange,
        prevKey: Int? = null,
        nextKey: Int? = null,
    ): LoadResult<Int, T>
}
