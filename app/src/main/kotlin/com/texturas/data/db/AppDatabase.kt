package com.texturas.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.texturas.data.dao.TextureEntryDao
import com.texturas.data.dao.TriggerDao
import com.texturas.data.dao.MoodEntryDao
import com.texturas.data.dao.SubstituteActivityDao
import com.texturas.data.model.TextureEntry
import com.texturas.data.model.Trigger
import com.texturas.data.model.MoodEntry
import com.texturas.data.model.SubstituteActivity

@Database(
    entities = [Trigger::class, MoodEntry::class, SubstituteActivity::class, TextureEntry::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun triggerDao(): TriggerDao
    abstract fun moodEntryDao(): MoodEntryDao
    abstract fun substituteActivityDao(): SubstituteActivityDao
    abstract fun textureEntryDao(): TextureEntryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "texturas_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
