package com.texturas.ui.moodjournal

import androidx.compose.foundation.Dialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.texturas.ui.theme.TexturasTheme
import com.texturas.ui.theme.customOutline
import com.texturas.ui.theme.customOnSurface
import com.texturas.ui.theme.customPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    viewModel: JournalViewModel = hiltViewModel()
) {
    val journalItems by viewModel.journalItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showAddTextureDialog by remember { mutableStateOf(false) }
    var textureDescription by remember { mutableStateOf("") }
    var textureRating by remember { mutableStateOf(3) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Diário de Texturas") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTextureDialog = true },
                backgroundColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar nova entrada"
                )
            }
        }
    ) { padding ->
        if (isLoading) {
            CenteredCircularProgressModifier(modifier = Modifier.fillMaxSize().padding(padding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (journalItems.isEmpty()) {
                    Text(
                        text = "Nenhuma entrada ainda. Suas experiências aparecerão aqui conforme você registrar seus momentos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(top = 24.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(journalItems) { item ->
                            when (item) {
                                is JournalItem.MoodEntry -> MoodEntryItem(entry = item.entry)
                                is JournalItem.TextureEntry -> TextureEntryItem(entry = item.entry)
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog for adding a texture entry
    if (showAddTextureDialog) {
        AlertDialog(
            onDismissRequest = { showAddTextureDialog = false },
            title = { Text("Registrar sua textura") },
            text = {
                Column {
                    OutlinedTextField(
                        value = textureDescription,
                        onValueChange = { textureDescription = it },
                        label = { Text("Como você descreveria a textura do momento agora?") },
                        placeholder = { Text("Ex: Leve, pesado, áspero, suave, denso, vazio...") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Como você avalia essa experiência? (1 = muito negativa, 5 = muito positiva)",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )
                    Slider(
                        value = textureRating.toFloat(),
                        onValueChange = { textureRating = it.toInt() },
                        valueRange = 1f..5f,
                        steps = 4,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Avaliação: $textureRating") },
                        colors = SliderDefaults.colors(
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            thumbColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (textureDescription.isNotBlank()) {
                            viewModel.addTextureEntry(textureDescription, textureRating)
                            showAddTextureDialog = false
                            // Reset form
                            textureDescription = ""
                            textureRating = 3
                        }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTextureDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodEntryItem(entry: MoodEntry) {
    val colors = MaterialTheme.colorScheme
    val timestamp = entry.timestamp
    val date = java.util.Date(timestamp)
    val dateString = android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", date).toString()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = MaterialShape.CornerFamily.Rounded,
        colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Registro de Humor",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = "Humor: ${entry.mood} (Intensidade: ${entry.intensity}/10)",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                entry.notes?.let { note ->
                    Text(
                        text = note,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .maxLines(2)
                    )
                }
                Text(
                    text = "Registrado em: $dateString",
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextureEntryItem(entry: TextureEntry) {
    val colors = MaterialTheme.colorScheme
    val timestamp = entry.timestamp
    val date = java.util.Date(timestamp)
    val dateString = android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", date).toString()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = MaterialShape.CornerFamily.Rounded,
        colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Entrada de Textura",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = entry.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                        .maxLines(2)
                )
                entry.rating?.let { rating ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "Avaliação: ",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.onSurfaceVariant
                        )
                        // Display rating as stars
                        val starRating = remember { mutableStateOf(rating) }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            repeat(5) { index ->
                                val isFilled = index < rating
                                Icon(
                                    imageVector = if (isFilled) Icons.Default.FavoriteBorder else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (isFilled) Color(0xFFFF9900) else Color(0xFFCCCCCC),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
                Text(
                    text = "Registrado em: $dateString",
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
