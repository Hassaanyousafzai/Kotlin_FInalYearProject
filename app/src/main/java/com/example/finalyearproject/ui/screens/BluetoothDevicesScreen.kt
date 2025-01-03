package com.example.finalyearproject.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finalyearproject.ui.components.*
import com.example.finalyearproject.viewmodel.BluetoothViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothDevicesScreen(
    onDeviceConnected: () -> Unit,
    viewModel: BluetoothViewModel
) {
    val devices by viewModel.devices.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()

    LaunchedEffect(connectionState) {
        if (connectionState is BluetoothViewModel.ConnectionState.Connected) {
            onDeviceConnected()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Available Devices") },
            actions = {
                IconButton(onClick = { viewModel.scanForDevices() }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Refresh,
                        contentDescription = "Scan"
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(devices) { device ->
                BluetoothDeviceItem(
                    device = device,
                    onConnect = { viewModel.connectToDevice(it) }
                )
            }
        }

        if (connectionState is BluetoothViewModel.ConnectionState.Connecting) {
            LoadingIndicator()
        }
    }
}