package com.texturas.ui.crisis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texturas.data.model.MoodEntry
import com.texturas.data.model.SubstituteActivity
import com.texturas.data.repository.MoodEntryRepository
import com.texturas.data.repository.SubstituteActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrisisButtonViewModel @Inject constructor(
    private val moodEntryRepository: MoodEntryRepository,
    private val substituteActivityRepository: SubstituteActivityRepository
) : ViewModel() {

    // We'll expose a flow of intervention steps for the current mood
    private val _interventionSteps = MutableStateFlow<List<InterventionStep>>(emptyList())
    val interventionSteps: StateFlow<List<InterventionStep>> = _interventionSteps.asStateFlow()

    init {
        // Collect the latest mood entry and update the intervention steps accordingly
        viewModelScope.launch {
            moodEntryRepository.allMoodEntries
                .map { entries -> entries.firstOrNull() } // Get the most recent
                .collect { latestMood ->
                    updateInterventionStepsBasedOnMood(latestMood)
                }
        }
    }

    private fun updateInterventionStepsBasedOnMood(moodEntry: MoodEntry?) {
        if (moodEntry == null) {
            // No mood entry yet, provide default steps
            _interventionSteps.value = getDefaultInterventionSteps()
            return
        }

        when (moodEntry.mood.toUpperCase()) {
            "ANSIOSO", "ANSIOUS", "ANSIEDADE", "STRESS" -> {
                _interventionSteps.value = getAnxiousInterventionSteps()
            }
            "DEPRIMIDO", "DEPRESSÃO", "DEPRESSED" -> {
                _interventionSteps.value = getDepressedInterventionSteps()
            }
            "HIPOMANÍACO", "HIPOMANIC", "EUPÓRICO", "EUPHORIC" -> {
                _interventionSteps.value = getHypomanicInterventionSteps()
            }
            "EUTÍMICO", "EUTHYMIC", "NEUTRO", "NEUTRAL" -> {
                _interventionSteps.value = getEuthymicInterventionSteps()
            }
            else -> {
                _interventionSteps.value = getDefaultInterventionSteps()
            }
        }
    }

    private fun getDefaultInterventionSteps(): List<InterventionStep> {
        return listOf(
            InterventionStep(
                id = 1,
                title = "Faça uma pausa consciente",
                description = "Observe sua respiração por 30 segundos.",
                actionType = "MINDFUL_PAUSE",
                icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
            )
        )
    }

    private fun getAnxiousInterventionSteps(): List<InterventionStep> {
        // We need to get activities asynchronously
        viewModelScope.launch {
            // Get high engagement activities (energy >= 6) for the third step
            substituteActivityRepository.allActivities
                .map { it.filter { it.energyRequired >= 6 } }
                .map { activities ->
                    val steps = mutableListOf<InterventionStep>()
                    
                    // Step 1: Block contact temporarily
                    steps.add(InterventionStep(
                        id = 1,
                        title = "Bloquear contato temporariamente",
                        description = "Toque para bloquear contatos de risco por 30 minutos.",
                        actionType = "BLOCK_CONTACT",
                        icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
                    ))
                    
                    // Step 2: 4-minute nervous system regulation
                    steps.add(InterventionStep(
                        id = 2,
                        title = "Respiração 4-7-8",
                        description = "Inspire por 4s, segure por 7s, expire por 8s. Repita 4 vezes (cerca de 4 minutos).",
                        actionType = "BREATHING_EXERCISE",
                        icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
                    ))
                    
                    // Step 3: High engagement activity from repertoire
                    if (activities.isNotEmpty()) {
                        val randomActivity = activities.random()
                        steps.add(InterventionStep(
                            id = 3,
                            title = randomActivity.name,
                            description = randomActivity.description ?: "Realize esta atividade para mudar seu estado.",
                            actionType = "DO_ACTIVITY",
                            icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0),
                            activityId = randomActivity.id
                        ))
                    } else {
                        // Fallback
                        steps.add(InterventionStep(
                            id = 3,
                            title = "Dança livre",
                            description = "Coloque uma música que você gosta e dance livremente.",
                            actionType = "DO_ACTIVITY",
                            icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
                        ))
                    }
                    
                    _interventionSteps.value = steps
                }
        }
        // Return loading state while we wait for async data
        return listOf(
            InterventionStep(
                id = 0,
                title = "Carregando intervenção para ansiedade...",
                description = "Aguarde um momento.",
                actionType = "LOADING",
                icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
            )
        )
    }

    private fun getDepressedInterventionSteps(): List<InterventionStep> {
        viewModelScope.launch {
            // Get low energy activities (energy <= 3) for the repertoire access
            substituteActivityRepository.allActivities
                .map { it.filter { it.energyRequired <= 3 } }
                .map { activities ->
                    val steps = mutableListOf<InterventionStep>()
                    
                    // Step 1: Direct acknowledgment of the state
                    steps.add(InterventionStep(
                        id = 1,
                        title = "Reconhecimento do estado",
                        description = "Noites difíceis são reais. Lembre-se: isso vai passar.",
                        actionType = "ACKNOWLEDGMENT",
                        icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
                    ))
                    
                    // Step 2: Access to low-energy repertoire
                    if (activities.isNotEmpty()) {
                        val randomActivity = activities.random()
                        steps.add(InterventionStep(
                            id = 2,
                            title = randomActivity.name,
                            description = randomActivity.description ?: "Realize esta atividade suave.",
                            actionType = "DO_ACTIVITY",
                            icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0),
                            activityId = randomActivity.id
                        ))
                    } else {
                        // Fallback
                        steps.add(InterventionStep(
                            id = 2,
                            title = "Escuta de música calma",
                            description = "Escolha uma faixa que te acolha sem exigir energia.",
                            actionType = "DO_ACTIVITY",
                            icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
                        ))
                    }
                    
                    // Step 3: Reminder of why they want to change
                    // In a real app, we would get this from the user's purpose statement
                    // For now, we'll use a generic reminder
                    steps.add(InterventionStep(
                        id = 3,
                        title = "Lembre-se do seu porquê",
                        description = "Pense em uma razão pela qual você quer mudar seus padrões.",
                        actionType = "PURPOSE_REMINDER",
                        icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
                    ))
                    
                    _interventionSteps.value = steps
                }
        }
        // Return loading state
        return listOf(
            InterventionStep(
                id = 0,
                title = "Carregando intervenção para depressão...",
                description = "Aguarde um momento.",
                actionType = "LOADING",
                icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
            )
        )
    }

    private fun getHypomanicInterventionSteps(): List<InterventionStep> {
        // For hypomanic state, we want grounding and calming activities
        viewModelScope.launch {
            substituteActivityRepository.allActivities
                .map { it.filter { it.energyRequired <= 4 } } // Moderate to low energy
                .map { activities ->
                    val steps = mutableListOf<InterventionStep>()
                    
                    // Step 1: Grounding exercise
                    steps.add(InterventionStep(
                        id = 1,
                        title = "Exercício de aterramento",
                        description = "Nomeie 5 coisas que você vê, 4 que você toca, 3 que você ouve, 2 que você cheira, 1 que você prova.",
                        actionType = "GROUNDING_EXERCISE",
                        icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
                    ))
                    
                    // Step 2: Calming activity from repertoire
                    if (activities.isNotEmpty()) {
                        val randomActivity = activities.random()
                        steps.add(InterventionStep(
                            id = 2,
                            title = randomActivity.name,
                            description = randomActivity.description ?: "Realize esta atividade para trazer equilíbrio.",
                            actionType = "DO_ACTIVITY",
                            icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0),
                            activityId = randomActivity.id
                        ))
                    } else {
                        // Fallback
                        steps.add(InterventionStep(
                            id = 2,
                            title = "Meditação guiada",
                            description = "Use um aplicativo de meditação por 10 minutos.",
                            actionType = "DO_ACTIVITY",
                            icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
                        ))
                    }
                    
                    // Step 3: Boundary setting reminder
                    steps.add(InterventionStep(
                        id = 3,
                        title = "Defina um limite",
                        description = "Escolha uma ação concreta para limitar excessos (ex: desligar redes sociais por 1h).",
                        actionType = "BOUNDARY_SETTING",
                        icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
                    ))
                    
                    _interventionSteps.value = steps
                }
        }
        // Return loading state
        return listOf(
            InterventionStep(
                id = 0,
                title = "Carregando intervenção para hipomania...",
                description = "Aguarde um momento.",
                actionType = "LOADING",
                icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
            )
        )
    }

    private fun getEuthymicInterventionSteps(): List<InterventionStep> {
        return listOf(
            InterventionStep(
                id = 1,
                title = "Registre sua textura",
                description = "Como você está se sentindo agora? Anote em uma frase.",
                actionType = "JOURNAL",
                icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
            ),
            InterventionStep(
                id = 2,
                title = "Explore seu repertório",
                description = "Escolha uma atividade que lhe traga alegria ou significado.",
                actionType = "EXPLORE_REPERTOIRE",
                icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
            ),
            InterventionStep(
                id = 3,
                title = "Conexão saudável",
                description = "Entre em contato com alguém que lhe faz bem, mesmo que seja uma mensagem curta.",
                actionType = "HEALTHY_CONNECTION",
                icon = androidx.compose.ui.graphics.vector.ImageVector.vectorResource(id = 0x0)
            )
        )
    }
}

/**
 * Represents a single step in an intervention sequence
 */
data class InterventionStep(
    val id: Long,
    val title: String,
    val description: String,
    val actionType: String, // e.g., "BLOCK_CONTACT", "BREATHING_EXERCISE", "DO_ACTIVITY", "JOURNAL"
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val activityId: Long? = null // For DO_ACTIVITY steps that reference a specific repertoire activity
)
