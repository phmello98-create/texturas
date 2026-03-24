package com.texturas.data.repository

import com.texturas.data.dao.TriggerDao
import com.texturas.data.model.Trigger
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class TriggerRepository @Inject constructor(
    private val triggerDao: TriggerDao
) {
    val allTriggers: Flow<List<Trigger>> = triggerDao.getAllTriggers()

    suspend fun insertTrigger(trigger: Trigger): Long = triggerDao.insertTrigger(trigger)
    suspend fun insertTriggers(vararg triggers: Trigger): LongArray = triggerDao.insertTriggers(*triggers)
    suspend fun updateTrigger(trigger: Trigger): Int = triggerDao.updateTrigger(trigger)
    suspend fun deleteTrigger(trigger: Trigger): Int = triggerDao.deleteTrigger(trigger)
    suspend fun deleteAllTriggers(): Int = triggerDao.deleteAllTriggers()
}
