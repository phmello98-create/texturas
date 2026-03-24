package com.texturas.ui.moodjournal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texturas.data.model.TextureEntry
import com.texturas.data.repository.TextureEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextureDiaryViewModel @Inject constructor(
    private val textureEntryRepository: TextureEntryRepository
) : ViewModel() {

    private val _entries = MutableStateFlow<List<TextureEntry>>(emptyList())
    val entries: StateFlow<List<TextureEntry>> = _entries.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            _isLoading.value = true
            textureEntryRepository.allTextureEntries.collect { entries ->
                _entries.value = entries
                _isLoading.value = false
            }
        }
    }

    fun addEntry(entry: TextureEntry) {
        viewModelScope.launch {
            textureEntryRepository.insertTextureEntry(entry)
            loadEntries()
        }
    }
}
