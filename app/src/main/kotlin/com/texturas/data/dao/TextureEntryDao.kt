package com.texturas.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.texturas.data.model.TextureEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface TextureEntryDao {
    @Query("SELECT * FROM texture_entries ORDER BY timestamp DESC")
    fun getAllTextureEntries(): Flow<List<TextureEntry>>

    @Insert
    suspend fun insertTextureEntry(entry: TextureEntry): Long

    @Insert
    suspend fun insertTextureEntries(vararg entries: TextureEntry): LongArray

    @Update
    suspend fun updateTextureEntry(entry: TextureEntry): Int

    @Delete
    suspend fun deleteTextureEntry(entry: TextureEntry): Int

    @Query("DELETE FROM texture_entries")
    suspend fun deleteAllTextureEntries(): Int
}
