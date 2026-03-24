package com.texturas.ui.repertoire

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
fun RepertoireScreen(
    viewModel: RepertoireViewModel = hiltViewModel(),
    onAddActivity: (SubstituteActivity) -> Unit = { /* Do nothing */ }
) {
    val activities by viewModel.activities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf("") }
    var editedDescription by remember { mutableStateOf("") }
    var editedCategory by remember { mutableStateOf("APRENDIZADO") }
    var editedEnergy by remember { mutableStateOf(5) }
    val categories = listOf("APRENDIZADO", "CRIAÇÃO", "MOVIMENTO", "CONEXÃO", "ESPLENDOR", "REPOUSO_ATIVO")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Seu Repertório") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                backgroundColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar nova atividade"
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
                if (activities.isEmpty()) {
                    Text(
                        text = "Nenhuma atividade adicionada ainda. Toque no ícone + acima para adicionar sua primeira atividade.",
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
                        items(activities) { activity ->
                            RepertoireItem(
                                activity = activity,
                                onMarkAsUsed = { viewModel.markActivityAsUsed(it.id) },
                                onEdit = { /* TODO: Implement edit */ },
                                onDelete = { viewModel.deleteActivity(it) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog for adding/editing an activity
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Adicionar Atividade") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Nome da atividade") },
                        placeholder = { Text("Ex: Caminhar na praia, Ler um livro") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = editedDescription,
                        onValueChange = { editedDescription = it },
                        label = { Text("Descrição (opcional)") },
                        placeholder = { Text("O que essa atividade envolve?") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedDropdownMenu(
                        expanded = editedCategory.isNotEmpty(),
                        onDismissRequest = { /* TODO */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = editedCategory,
                            modifier = Modifier
                                .clickable { /* TODO: Open dropdown */ }
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                    // For simplicity, we'll use a text field for category as well in this minimal version
                    OutlinedTextField(
                        value = editedCategory,
                        onValueChange = { editedCategory = it },
                        label = { Text("Categoria (ex: APRENDIZADO, CRIAÇÃO, MOVIMENTO, CONEXÃO, ESPLENDOR, REPOUSO_ATIVO)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Slider(
                        value = editedEnergy.toFloat(),
                        onValueChange = { editedEnergy = it.toInt() },
                        valueRange = 1f..10f,
                        steps = 9,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Energia necessária: $editedEnergy") },
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
                        if (editedName.isNotBlank()) {
                            val newActivity = SubstituteActivity(
                                name = editedName,
                                description = if (editedDescription.isBlank()) null else editedDescription,
                                category = editedCategory,
                                energyRequired = editedEnergy
                            )
                            viewModel.addActivity(newActivity)
                            showAddDialog = false
                            // Reset form
                            editedName = ""
                            editedDescription = ""
                            editedCategory = "APRENDIZADO"
                            editedEnergy = 5
                        }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepertoireItem(
    activity: SubstituteActivity,
    onMarkAsUsed: (Long) -> Unit,
    onEdit: (SubstituteActivity) -> Unit,
    onDelete: (SubstituteActivity) -> Unit
) {
    val colors = MaterialTheme.colorScheme
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
                    text = activity.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = activity.description ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .maxLines(2)
                )
                Text(
                    text = "${activity.category} • Energia: ${activity.energyRequired}/10",
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            // Action icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { onMarkAsUsed(activity.id) },
                    tooltip = { Text("Marcar como usada") }
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = colors.primary
                    )
                }
                IconButton(
                    onClick = { onEdit(activity) },
                    tooltip = { Text("Editar") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = { onDelete(activity) },
                    tooltip = { Text("Excluir") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = colors.error
                    )
                }
            }
        }
    }
}

// Placeholder for a circular progress indicator
@Composable
fun CenteredCircularProgressModifier(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .contentBackground(MaterialTheme.colorScheme.background)
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}
