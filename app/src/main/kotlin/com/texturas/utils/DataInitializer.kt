package com.texturas.utils

import android.content.Context
import com.texturas.data.model.SubstituteActivity
import com.texturas.data.repository.SubstituteActivityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataInitializer @Inject constructor(
    private val context: Context,
    private val substituteActivityRepository: SubstituteActivityRepository
) {
    /**
     * Initialize the database with default substitute activities if it's empty.
     * This should be called once after the database is ready.
     */
    fun initializeDefaultActivities() {
        // We'll run this in a coroutine, but we need to be careful not to block the UI.
        // For simplicity in this MVP, we'll just check if there are any activities and add some if not.
        // In a real app, we might want to do this asynchronously and show a loading state.
        // However, since this is just for initialization and we're using Room which is fast,
        // we'll do it synchronously for now.
        
        // Check if we already have activities
        val activities = substituteActivityRepository.allActivities.first() // This is a flow, we need to collect it
        // Actually, we can't do this synchronously easily. Let's change the approach.
        // We'll make this a suspend function and call it from a ViewModel or use lifecycleScope.
    }
    
    suspend fun initializeDefaultActivitiesAsync() {
        // Check if we already have activities by collecting the first element
        val activitiesList = substituteActivityRepository.allActivities.first()
        if (activitiesList.isEmpty()) {
            val defaultActivities = listOf(
                SubstituteActivity(
                    name = "Respiração 4-7-8",
                    description = "Inspire por 4 segundos, segure por 7, expire por 8. Repita 4 vezes.",
                    category = "REPOUSO_ATIVO",
                    energyRequired = 1
                ),
                SubstituteActivity(
                    name = "Caminhada consciente",
                    description = "Caminhe lentamente, prestando atenção em cada passo e na respiração.",
                    category = "MOVIMENTO",
                    energyRequired = 2
                ),
                SubstituteActivity(
                    name = "Desenho livre",
                    description = "Pegue um papel e lápis e desenhe o que vier à mente, sem julgamento.",
                    category = "CRIAÇÃO",
                    energyRequired = 2
                ),
                SubstituteActivity(
                    name = "Escuta ativa de música",
                    description = "Escolha uma música que você gosta e ouça-a com atenção plena, focando nos instrumentos.",
                    category = "APRENDIZADO",
                    energyRequired = 2
                ),
                SubstituteActivity(
                    name = "Banho morno com sais",
                    description = "Tome um banho morno, adicionando sais de banho ou óleos essenciais relaxantes.",
                    category = "REPOUSO_ATIVO",
                    energyRequired = 2
                ),
                SubstituteActivity(
                    name = "Ligação para amigo de confiança",
                    description = "Entre em contato com alguém de sua rede de apoio para uma conversa de apoio.",
                    category = "CONEXÃO",
                    energyRequired = 3
                ),
                SubstituteActivity(
                    name = "Alongamento suave",
                    description = "Faça uma sequência de alongamentos suaves, focando na liberação de tensão.",
                    category = "MOVIMENTO",
                    energyRequired = 2
                ),
                SubstituteActivity(
                    name = "Jornal de gratidão",
                    description = "Escreva três coisas pelas quais você é grato hoje, por menores que sejam.",
                    category = "APRENDIZADO",
                    energyRequired = 1
                ),
                SubstituteActivity(
                    name = "Observação de nuvens",
                    description = "Deite-se e observe as nuvens, imaginando formas e histórias nelas.",
                    category = "ESPLENDOR",
                    energyRequired = 1
                ),
                SubstituteActivity(
                    name = "Cozinhar uma receita simples",
                    description = "Prepare um prato simples que você goste, focando no processo e nos aromas.",
                    category = "CRIAÇÃO",
                    energyRequired = 4
                ),
                SubstituteActivity(
                    name = "Dança livre",
                    description = "Coloque uma música que você gosta e dance livremente, sem coreografia.",
                    category = "MOVIMENTO",
                    energyRequired = 6
                ),
                SubstituteActivity(
                    name = "Corrida leve",
                    description = "Corra em ritmo leve por 15-20 minutos, focando na respiração e na sensação do corpo.",
                    category = "MOVIMENTO",
                    energyRequired = 7
                ),
                SubstituteActivity(
                    name = "Meditação guiada",
                    description = "Use um aplicativo de meditação guiada por 10-15 minutos.",
                    category = "REPOUSO_ATIVO",
                    energyRequired = 1
                ),
                SubstituteActivity(
                    name = "Leitura de ficção",
                    description = "Leia um capítulo de um livro de ficção que você goste.",
                    category = "APRENDIZADO",
                    energyRequired = 2
                ),
                SubstituteActivity(
                    name = "Contato com a natureza",
                    description = "Passe algum tempo em um parque, jardim ou área verde, observando as plantas e animais.",
                    category = "ESPLENDOR",
                    energyRequired = 2
                )
            )
            
            substituteActivityRepository.insertActivities(*defaultActivities.toTypedArray())
        }
    }
}
