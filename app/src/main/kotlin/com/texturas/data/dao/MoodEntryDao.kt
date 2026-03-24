package com.texturas.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.texturas.data.model.MoodEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodEntryDao {
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoodEntries(): Flow<List<MoodEntry>>

    @Insert
    suspend fun insertMoodEntry(entry: MoodEntry): Long

    @Insert
    suspend fun insertMoodEntries(vararg entries: MoodEntry): LongArray

    @Update
    suspend fun updateMoodEntry(entry: MoodEntry): Int

    @Delete
    suspend fun deleteMoodEntry(entry: MoodEntry): Int

    @Query("DELETE FROM mood_entries")
    suspend fun deleteAllMoodEntries(): Int
}
