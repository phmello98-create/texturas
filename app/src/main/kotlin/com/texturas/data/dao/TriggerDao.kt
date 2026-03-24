package com.texturas.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.texturas.data.model.Trigger
import kotlinx.coroutines.flow.Flow

@Dao
interface TriggerDao {
    @Query("SELECT * FROM triggers ORDER BY name")
    fun getAllTriggers(): Flow<List<Trigger>>

    @Insert
    suspend fun insertTrigger(trigger: Trigger): Long

    @Insert
    suspend fun insertTriggers(vararg triggers: Trigger): LongArray

    @Update
    suspend fun updateTrigger(trigger: Trigger): Int

    @Delete
    suspend fun deleteTrigger(trigger: Trigger): Int

    @Query("DELETE FROM triggers")
    suspend fun deleteAllTriggers(): Int
}
