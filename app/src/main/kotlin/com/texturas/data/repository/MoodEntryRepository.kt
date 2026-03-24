package com.texturas.data.repository

import com.texturas.data.dao.MoodEntryDao
import com.texturas.data.model.MoodEntry
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class MoodEntryRepository @Inject constructor(
    private val moodEntryDao: MoodEntryDao
) {
    val allMoodEntries: Flow<List<MoodEntry>> = moodEntryDao.getAllMoodEntries()

    suspend fun insertMoodEntry(entry: MoodEntry): Long = moodEntryDao.insertMoodEntry(entry)
    suspend fun insertMoodEntries(vararg entries: MoodEntry): LongArray = moodEntryDao.insertMoodEntries(*entries)
    suspend fun updateMoodEntry(entry: MoodEntry): Int = moodEntryDao.updateMoodEntry(entry)
    suspend fun deleteMoodEntry(entry: MoodEntry): Int = moodEntryDao.deleteMoodEntry(entry)
    suspend fun deleteAllMoodEntries(): Int = moodEntryDao.deleteAllMoodEntries()
}
