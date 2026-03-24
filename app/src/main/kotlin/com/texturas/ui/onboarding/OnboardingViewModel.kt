package com.texturas.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texturas.data.model.Trigger
import com.texturas.data.model.MoodEntry
import com.texturas.data.repository.MoodEntryRepository
import com.texturas.data.repository.TriggerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val triggerRepository: TriggerRepository,
    private val moodEntryRepository: MoodEntryRepository
) : ViewModel() {

    private val _triggers = MutableStateFlow<List<Trigger>>(emptyList())
    val triggers: StateFlow<List<Trigger>> = _triggers

    private val _currentMood = MutableStateFlow<String?>(null)
    val currentMood: StateFlow<String?> = _currentMood

    private val _moodIntensity = MutableStateFlow(5)
    val moodIntensity: StateFlow<Int> = _moodIntensity

    private val _moodNotes = MutableStateFlow<String?>(null)
    val moodNotes: StateFlow<String?> = _moodNotes

    init {
        loadTriggers()
    }

    private fun loadTriggers() {
        viewModelScope.launch {
            // Collect the flow and update the state
            // For simplicity, we'll just get the current list (not collecting for updates in this VM)
            // In a real app, we might want to collect and update as the DB changes.
            // But for onboarding, we just need the initial list and then we add to it.
            triggerRepository.allTriggers.collect { triggers ->
                _triggers.value = triggers
            }
        }
    }

    fun addTrigger(trigger: Trigger) {
        viewModelScope.launch {
            triggerRepository.insertTrigger(trigger)
            // Reload triggers
            loadTriggers()
        }
    }

    fun removeTrigger(trigger: Trigger) {
        viewModelScope.launch {
            triggerRepository.deleteTrigger(trigger)
            loadTriggers()
        }
    }

    fun setCurrentMood(mood: String, intensity: Int, notes: String? = null) {
        _currentMood.value = mood
        _moodIntensity.value = intensity
        _moodNotes.value = notes
    }

    fun saveOnboardingData(): Boolean {
        val mood = _currentMood.value
        val intensity = _moodIntensity.value
        if (mood == null || intensity == 0) {
            return false
        }
        viewModelScope.launch {
            val moodEntry = MoodEntry(
                timestamp = System.currentTimeMillis(),
                mood = mood,
                intensity = intensity,
                notes = _moodNotes.value
            )
            moodEntryRepository.insertMoodEntry(moodEntry)
        }
        return true
    }
}
