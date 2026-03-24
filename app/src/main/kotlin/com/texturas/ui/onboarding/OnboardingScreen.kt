package com.texturas.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.VectorAsset
import androidx.compose.ui.input.keyboard.KeyboardType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.texturas.ui.theme.TexturasTheme
import com.texturas.ui.theme.customOutline
import com.texturas.ui.theme.customOnSurface
import com.texturas.ui.theme.customPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onFinished: () -> Unit
) {
    val triggers by viewModel.triggers.collectAsState()
    val currentMood by viewModel.currentMood.collectAsState()
    val moodIntensity by viewModel.moodIntensity.collectAsState()
    val moodNotes by viewModel.moodNotes.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var moodTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var intensitySliderValue by remember { mutableStateOf(5f) }
    var notesTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var showAddTriggerDialog by remember { mutableStateOf(false) }
    var dialogName by remember { mutableStateOf("") }
    var dialogType by remember { mutableStateOf("OUTRO") }

    LaunchedEffect(Unit) {
        moodTextFieldValue = TextFieldValue(currentMood ?: "")
        intensitySliderValue = moodIntensity.toFloat()
        notesTextFieldValue = TextFieldValue(moodNotes ?: "")
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Mapeie sua Vulnerabilidade") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Identifique seus gatilhos",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Trigger list and adder
            Column {
                if (triggers.isEmpty()) {
                    Text(
                        text = "Nenhum gatilho adicionado ainda. Toque no ícone abaixo para adicionar seu primeiro gatilho.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        content = {
                            items(triggers) { trigger ->
                                TriggerChip(
                                    trigger = trigger,
                                    onRemove = { viewModel.removeTrigger(trigger) }
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = { showAddTriggerDialog = true },
                    modifier = Modifier.align(alignment = Alignment.Start)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar gatilho",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Como você está se sentindo agora?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = moodTextFieldValue,
                onValueChange = { moodTextFieldValue = it;
                    viewModel.setCurrentMood(
                        it.text,
                        moodIntensity.toInt(),
                        notesTextFieldValue.text
                    )
                },
                label = { Text("Humor (ex: Eutimico, Hipomaníaco, Depressivo, Ansioso)") },
                placeholder = { Text("Digite seu estado de humor") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Intensidade (1-10)",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            Slider(
                value = intensitySliderValue,
                onValueChange = {
                    intensitySliderValue = it
                    viewModel.setCurrentMood(
                        moodTextFieldValue.text,
                        it.toInt(),
                        notesTextFieldValue.text
                    )
                },
                valueRange = 1f..10f,
                steps = 9,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    thumbColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = notesTextFieldValue,
                onValueChange = {
                    notesTextFieldValue = it;
                    viewModel.setCurrentMood(
                        moodTextFieldValue.text,
                        moodIntensity.toInt(),
                        it.text
                    )
                },
                label = { Text("Observações (opcional)") },
                placeholder = { Text("Alguma observação sobre seu estado?") },
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save and finish button
            Button(
                onClick = {
                    if (viewModel.saveOnboardingData()) {
                        onFinished()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = currentMood != null && moodIntensity >= 1
            ) {
                Text("Salvar e Continuar")
            }
        }

        // Dialog for adding a new trigger
        if (showAddTriggerDialog) {
            AlertDialog(
                onDismissRequest = { showAddTriggerDialog = false },
                title = { Text("Adicionar Gatilho") },
                text = { Text("Descreva o gatilho que você deseja mapear.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (dialogName.isNotBlank()) {
                                val trigger = Trigger(
                                    name = dialogName,
                                    type = dialogType
                                )
                                viewModel.addTrigger(trigger)
                                showAddTriggerDialog = false
                                dialogName = ""
                                dialogType = "OUTRO"
                            }
                        }
                    ) {
                        Text("Adicionar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddTriggerDialog = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                OutlinedTextField(
                    value = dialogName,
                    onValueChange = { dialogName = it },
                    label = { Text("Nome do gatilho (ex: João, Rua XYZ, 22:00)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = dialogType,
                    onValueChange = { dialogType = it },
                    label = { Text("Tipo (ex: PESSOA, LOCAL, HORARIO, EMOCAO, SENSACAO, OUTRO)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriggerChip(
    trigger: Trigger,
    onRemove: (Trigger) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val inactiveBackgroundColor = colors.onSurface.copy(alpha = 0.1f)
    val inactiveContentColor = colors.onSurface

    Chip(
        onDelete = { onRemove(trigger) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Tag,
                contentDescription = null,
                tint = inactiveContentColor
            )
        },
        label = {
            Text(
                text = "${trigger.name} (${trigger.type})",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = inactiveBackgroundColor,
            contentColor = inactiveContentColor
        )
    )
}
