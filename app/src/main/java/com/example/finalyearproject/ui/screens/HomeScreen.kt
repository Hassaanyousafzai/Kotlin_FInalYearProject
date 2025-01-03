package com.example.finalyearproject.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finalyearproject.ui.components.PostureIndicator
import com.example.finalyearproject.viewmodel.PostureViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PostureViewModel
) {
    val postureState by viewModel.postureState.collectAsState()
    val sittingDuration by viewModel.sittingDuration.collectAsState()
    val modelState by viewModel.modelState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Posture Monitor") }
        )

        when (modelState) {
            is PostureViewModel.ModelState.NotLoaded -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Model is not loaded. Please check the application setup.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            is PostureViewModel.ModelState.Error -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Error loading model: ${(modelState as PostureViewModel.ModelState.Error).message}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            is PostureViewModel.ModelState.Ready -> {
                if (postureState?.isOccupied == true) {
                    PostureIndicator(
                        posture = postureState?.predictedPosture ?: "unknown",
                        sittingDuration = sittingDuration
                    )
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Please sit on the cushion to start monitoring",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}