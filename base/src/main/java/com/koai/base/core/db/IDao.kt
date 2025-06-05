package com.koai.base.core.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface IDao<I : Any> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg model: I)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(model: I)

    @Delete
    suspend fun delete(vararg model: I)

    fun getAll(): Flow<List<I>>

    fun getById(id: String): Flow<I?>
}
