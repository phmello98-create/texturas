package com.texturas.ui.repertoire

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texturas.data.model.SubstituteActivity
import com.texturas.data.repository.SubstituteActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepertoireViewModel @Inject constructor(
    private val substituteActivityRepository: SubstituteActivityRepository
) : ViewModel() {

    private val _activities = MutableStateFlow<List<SubstituteActivity>>(emptyList())
    val activities: StateFlow<List<SubstituteActivity>> = _activities.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadActivities()
    }

    private fun loadActivities() {
        viewModelScope.launch {
            _isLoading.value = true
            substituteActivityRepository.allActivities.collect { activities ->
                _activities.value = activities
                _isLoading.value = false
            }
        }
    }

    fun addActivity(activity: SubstituteActivity) {
        viewModelScope.launch {
            substituteActivityRepository.insertActivity(activity)
            // Reload activities
            loadActivities()
        }
    }

    fun updateActivity(activity: SubstituteActivity) {
        viewModelScope.launch {
            substituteActivityRepository.updateActivity(activity)
            loadActivities()
        }
    }

    fun deleteActivity(activity: SubstituteActivity) {
        viewModelScope.launch {
            substituteActivityRepository.deleteActivity(activity)
            loadActivities()
        }
    }

    fun markActivityAsUsed(id: Long) {
        viewModelScope.launch {
            substituteActivityRepository.incrementUsage(id, System.currentTimeMillis())
            // We don't need to reload the list because the timesUsed and lastUsed are not displayed in the list
            // But if we want to show the updated usage, we would need to reload.
            // For now, we'll just update the specific item in the list without reloading everything.
            val currentActivities = _activities.value
            val index = currentActivities.indexOfFirst { it.id == id }
            if (index != -1) {
                val updated = currentActivities[index].copy(
                    timesUsed = currentActivities[index].timesUsed + 1,
                    lastUsed = System.currentTimeMillis()
                )
                val newList = currentActivities.toMutableList()
                newList[index] = updated
                _activities.value = newList
            }
        }
    }
}
