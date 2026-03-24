package com.texturas.data.repository

import com.texturas.data.dao.SubstituteActivityDao
import com.texturas.data.model.SubstituteActivity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class SubstituteActivityRepository @Inject constructor(
    private val substituteActivityDao: SubstituteActivityDao
) {
    val allActivities: Flow<List<SubstituteActivity>> = substituteActivityDao.getAllActivities()

    suspend fun insertActivity(activity: SubstituteActivity): Long = substituteActivityDao.insertActivity(activity)
    suspend fun insertActivities(vararg activities: SubstituteActivity): LongArray = substituteActivityDao.insertActivities(*activities)
    suspend fun updateActivity(activity: SubstituteActivity): Int = substituteActivityDao.updateActivity(activity)
    suspend fun deleteActivity(activity: SubstituteActivity): Int = substituteActivityDao.deleteActivity(activity)
    suspend fun deleteAllActivities(): Int = substituteActivityDao.deleteAllActivities()
    suspend fun incrementUsage(id: Long, timestamp: Long) = substituteActivityDao.incrementUsage(id, timestamp)
}
