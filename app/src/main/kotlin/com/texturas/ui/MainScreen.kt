package com.texturas.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.texturas.ui.crisis.CrisisButton
import com.texturas.ui.theme.TexturasTheme

@Composable
fun MainScreen(
    onActionPerformed: (String) -> Unit = { /* Do nothing */ }
) {
    val context = LocalContext.current
    TexturasTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Texturas",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Toque no botão abaixo quando sentir um impulso.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .width(240.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))

            // The crisis button will be at the bottom center
            CrisisButton(
                onActionClicked = { action ->
                    // Show a toast with the action title
                    Toast.makeText(context, "Realizando: ${action.title}", Toast.LENGTH_SHORT).show()
                    // Also call the passed onActionPerformed for any other handling
                    onActionPerformed(action.title)
                },
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
            )
        }
    }
}
