package com.texturas.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "substitute_activities")
data class SubstituteActivity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // e.g., LEARNING, CREATION, MOVEMENT, SOCIAL, WONDER, ACTIVE_REST
    val energyRequired: Int, // 1-10 scale, where 1 is low energy, 10 is high energy
    val description: String? = null,
    val timesUsed: Int = 0,
    val lastUsed: Long? = null // Unix timestamp in milliseconds
)
