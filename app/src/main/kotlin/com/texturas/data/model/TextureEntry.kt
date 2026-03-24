package com.texturas.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "texture_entries")
data class TextureEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long, // Unix timestamp in milliseconds
    val description: String, // Free-form text about the quality of the experience
    val rating: Int? = null // Optional rating 1-5 for how positive/negative the texture was
)
