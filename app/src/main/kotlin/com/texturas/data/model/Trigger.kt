package com.texturas.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "triggers")
data class Trigger(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String, // e.g., PERSON, PLACE, TIME, EMOTION, SENSATION, OTHER
    val description: String? = null
)
