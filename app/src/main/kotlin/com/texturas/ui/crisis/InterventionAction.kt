package com.texturas.ui.crisis

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

/**
 * Represents an immediate action the user can take during a crisis.
 */
data class InterventionAction(
    val id: Long,
    val title: String,
    val description: String,
    val icon: ImageVector, // We'll use Icons.Default for now, but can be customized
    val actionType: String // e.g., "BLOCK_CONTACT", "BREATHING_EXERCISE", "ENGAGING_ACTIVITY"
)
