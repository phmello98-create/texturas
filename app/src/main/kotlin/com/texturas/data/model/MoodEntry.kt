package com.texturas.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long, // Unix timestamp in milliseconds
    val mood: String, // e.g., EUTHYMIC, HYPOMANIC, DEPRESSED, ANXIOUS, etc.
    val intensity: Int, // 1-10 scale
    val notes: String? = null
)
