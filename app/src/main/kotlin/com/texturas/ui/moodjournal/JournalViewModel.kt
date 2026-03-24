package com.texturas.ui.moodjournal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texturas.data.model.MoodEntry
import com.texturas.data.model.TextureEntry
import com.texturas.data.repository.MoodEntryRepository
import com.texturas.data.repository.TextureEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val moodEntryRepository: MoodEntryRepository,
    private val textureEntryRepository: TextureEntryRepository
) : ViewModel() {

    private val _journalItems = MutableStateFlow<List<JournalItem>>(emptyList())
    val journalItems: StateFlow<List<JournalItem>> = _journalItems.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadJournalItems()
    }

    private fun loadJournalItems() {
        viewModelScope.launch {
            _isLoading.value = true
            // Combine the two flows
            val moodEntries = moodEntryRepository.allMoodEntries
            val textureEntries = textureEntryRepository.allTextureEntries

            combine(moodEntries, textureEntries) { moodList, textureList ->
                val journalItems = mutableListOf<JournalItem>()
                moodList.forEach { entry -> journalItems.add(JournalItem.MoodEntry(entry)) }
                textureList.forEach { entry -> journalItems.add(JournalItem.TextureEntry(entry)) }
                journalItems.sortByDescending { when (it) {
                    is JournalItem.MoodEntry -> it.entry.timestamp
                    is JournalItem.TextureEntry -> it.entry.timestamp
                } }
                journalItems
            }
            .collect { items ->
                _journalItems.value = items
                _isLoading.value = false
            }
        }
    }

    /** Add a new texture entry to the diary */
    fun addTextureEntry(description: String, rating: Int) {
        viewModelScope.launch {
            val entry = TextureEntry(
                timestamp = System.currentTimeMillis(),
                description = description,
                rating = rating
            )
            textureEntryRepository.insertTextureEntry(entry)
            // The reload will be triggered by the flow in init
        }
    }
}
