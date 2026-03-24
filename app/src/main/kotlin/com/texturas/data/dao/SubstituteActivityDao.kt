package com.texturas.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.texturas.data.model.SubstituteActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubstituteActivityDao {
    @Query("SELECT * FROM substitute_activities ORDER BY name")
    fun getAllActivities(): Flow<List<SubstituteActivity>>

    @Insert
    suspend fun insertActivity(activity: SubstituteActivity): Long

    @Insert
    suspend fun insertActivities(vararg activities: SubstituteActivity): LongArray

    @Update
    suspend fun updateActivity(activity: SubstituteActivity): Int

    @Delete
    suspend fun deleteActivity(activity: SubstituteActivity): Int

    @Query("DELETE FROM substitute_activities")
    suspend fun deleteAllActivities(): Int

    @Query("UPDATE substitute_activities SET timesUsed = timesUsed + 1, lastUsed = :timestamp WHERE id = :id")
    suspend fun incrementUsage(id: Long, timestamp: Long)
}
