package com.example.finalyearproject.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PostureIndicator(
    posture: String,
    sittingDuration: Long
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current Posture",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = posture.replace("_", " ").uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                color = when (posture) {
                    "correct" -> Color.Green
                    else -> Color.Red
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sitting Duration: ${formatDuration(sittingDuration)}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun formatDuration(minutes: Long): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return if (hours > 0) {
        "$hours h $remainingMinutes min"
    } else {
        "$remainingMinutes min"
    }
}