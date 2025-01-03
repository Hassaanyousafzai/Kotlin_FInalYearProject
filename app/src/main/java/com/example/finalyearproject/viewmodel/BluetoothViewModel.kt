package com.example.finalyearproject.viewmodel

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalyearproject.data.repository.BluetoothRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BluetoothViewModel(
    private val repository: BluetoothRepository,
    private val postureViewModel: PostureViewModel
) : ViewModel() {
    private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val devices: StateFlow<List<BluetoothDevice>> = _devices

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    sealed class ConnectionState {
        object Disconnected : ConnectionState()
        object Connecting : ConnectionState()
        object Connected : ConnectionState()
        data class Error(val message: String) : ConnectionState()
    }

    fun scanForDevices() {
        viewModelScope.launch {
            repository.scanForDevices().collect { deviceList ->
                _devices.value = deviceList
            }
        }
    }

    fun connectToDevice(device: BluetoothDevice) {
        viewModelScope.launch {
            _connectionState.value = ConnectionState.Connecting
            repository.connectToDevice(device)
                .onSuccess {
                    _connectionState.value = ConnectionState.Connected
                    startReadingData()
                }
                .onFailure {
                    _connectionState.value = ConnectionState.Error(it.message ?: "Connection failed")
                }
        }
    }

    private fun startReadingData() {
        viewModelScope.launch {
            repository.readData().collect { sensorValues ->
                // Since BluetoothRepository now returns parsed float values,
                // we can directly pass them to PostureViewModel
                postureViewModel.processPostureData(sensorValues)
            }
        }
    }

    fun disconnect() {
        repository.disconnect()
        _connectionState.value = ConnectionState.Disconnected
    }
}