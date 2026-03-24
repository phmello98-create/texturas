package com.texturas.data.repository

import com.texturas.data.dao.TextureEntryDao
import com.texturas.data.model.TextureEntry
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class TextureEntryRepository @Inject constructor(
    private val textureEntryDao: TextureEntryDao
) {
    val allTextureEntries: Flow<List<TextureEntry>> = textureEntryDao.getAllTextureEntries()

    suspend fun insertTextureEntry(entry: TextureEntry): Long = textureEntryDao.insertTextureEntry(entry)
    suspend fun insertTextureEntries(vararg entries: TextureEntry): LongArray = textureEntryDao.insertTextureEntries(*entries)
    suspend fun updateTextureEntry(entry: TextureEntry): Int = textureEntryDao.updateTextureEntry(entry)
    suspend fun deleteTextureEntry(entry: TextureEntry): Int = textureEntryDao.deleteTextureEntry(entry)
    suspend fun deleteAllTextureEntries(): Int = textureEntryDao.deleteAllTextureEntries()
}
