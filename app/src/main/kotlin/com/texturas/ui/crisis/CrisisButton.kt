package com.texturas.ui.crisis

import androidx.compose.foundation.Clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.texturas.ui.theme.TexturasTheme
import com.texturas.ui.theme.customOnSurface
import com.texturas.ui.theme.customPrimary
import com.texturas.ui.theme.customOutline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrisisButton(
    onStepCompleted: (InterventionStep) -> Unit = { /* Do nothing */ },
    modifier: Modifier = Modifier
) {
    val viewModel: CrisisButtonViewModel = hiltViewModel()
    val interventionSteps by viewModel.interventionSteps.collectAsState()

    // We want the button to be always accessible, so we'll use a Scaffold or just place it.
    // For the MVP, let's assume it's a floating action button that's always on screen.
    // We'll use a simple ElevatedButton for now that can be placed anywhere.
    // In a real app, this would be a persistent floating action button or a quick settings tile.

    // For the purpose of this task, we'll create a button that shows the intervention.
    // The user taps it to get the suggestion, and then taps the suggestion to perform the action.
    // This two-tap approach is safer than executing immediately on long press, which could be accidental.

    var showDetails by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterX,
        modifier = modifier
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { showDetails = true },
                    onTap = { /* On tap, we could execute the default action, but we'll show details first */ }
                )
            }
    ) {
        // The button itself
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .clickable(
                    onClick = {
                        // On click, we toggle the details view
                        showDetails = !showDetails
                    },
                    // Indicate it's clickable
                    indication = rememberRipple()
                )
                .drawWithContent {
                    drawContent()
                    // Add a plus icon in the center
                    val iconSize = 24.dp
                    drawIntoCanvas { canvas ->
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            strokeWidth = 3f
                            style = android.graphics.Paint.Style.STROKE
                        }
                        val center = size.center()
                        // Vertical line
                        canvas.drawLine(
                            center.x,
                            center.y - iconSize / 2,
                            center.x,
                            center.y + iconSize / 2,
                            paint
                        )
                        // Horizontal line
                        canvas.drawLine(
                            center.x - iconSize / 2,
                            center.y,
                            center.x + iconSize / 2,
                            center.y,
                            paint
                        )
                    }
                }
        )

        // If details are shown, display the intervention steps
        if (showDetails) {
            InterventionStepsCard(
                steps = interventionSteps,
                onStepCompleted = { step ->
                    showDetails = false
                    onStepCompleted(step)
                },
                onDismiss = { showDetails = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterventionStepsCard(
    steps: List<InterventionStep>,
    onStepCompleted: (InterventionStep) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    // If we're in loading state, show a loading indicator
    if (steps.isNotEmpty() && steps.first().actionType == "LOADING") {
        LoadingCard(
            message = steps.first().description,
            onDismiss = onDismiss
        )
        return
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialShape.CornerFamily.Rounded,
        colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Sugestão de intervenção",
                style = MaterialTheme.typography.titleMedium,
                color = colors.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Display each step
            StepsList(
                steps = steps,
                onStepCompleted = onStepCompleted
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.onSurfaceVariant.copy(alpha = 0.1f)
                    )
                ) {
                    Text("Cancelar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                // We won't have a "Do All" button since each step should be done individually
                // The user will tap on each step to mark it as completed
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingCard(
    message: String,
    onDismiss: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialShape.CornerFamily.Rounded,
        colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.onSurfaceVariant.copy(alpha = 0.1f)
                )
            ) {
                Text("Cancelar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepsList(
    steps: List<InterventionStep>,
    onStepCompleted: (InterventionStep) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(steps) { step ->
            InterventionStepItem(
                step = step,
                onStepCompleted = onStepCompleted
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterventionStepItem(
    step: InterventionStep,
    onStepCompleted: (InterventionStep) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val isCompleted = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = MaterialShape.CornerFamily.Rounded,
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) colors.primary.copy(alpha = 0.1f) else colors.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        ToggleRow(
            checked = isCompleted,
            onCheckedChange = { isChecked ->
                isCompleted.value = isChecked
                if (isChecked) {
                    onStepCompleted(step)
                }
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Icon placeholder
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (isCompleted) colors.primary else colors.onSurfaceVariant.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                        .content {
                            Icon(
                                imageVector = step.icon,
                                contentDescription = null,
                                tint = if (isCompleted) colors.onPrimary else colors.primary
                            )
                        }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isCompleted) colors.primary else colors.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )
                    Text(
                        text = step.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isCompleted) colors.primary else colors.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
