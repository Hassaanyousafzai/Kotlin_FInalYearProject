package com.example.finalyearproject.data.repository

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.util.UUID

class BluetoothRepository(private val bluetoothAdapter: BluetoothAdapter?) {
    private var bluetoothSocket: BluetoothSocket? = null
    private val UUID_SERIAL_PORT = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    fun scanForDevices(): Flow<List<BluetoothDevice>> = flow {
        bluetoothAdapter?.startDiscovery()
        val bondedDevices = bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
        emit(bondedDevices)
    }.flowOn(Dispatchers.IO)

    suspend fun connectToDevice(device: BluetoothDevice): Result<Unit> {
        return try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID_SERIAL_PORT)
            bluetoothSocket?.connect()
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    fun readData(): Flow<List<Float>> = flow {
        val buffer = StringBuilder()
        var isCollecting = false

        while (true) {
            try {
                val byte = bluetoothSocket?.inputStream?.read() ?: break
                val char = byte.toChar()

                when {
                    char == '<' -> {
                        // Start of new data packet
                        buffer.clear()
                        isCollecting = true
                    }
                    char == '>' && isCollecting -> {
                        // End of data packet, process it
                        isCollecting = false
                        val data = buffer.toString()
                            .split(',')
                            .mapNotNull { it.toFloatOrNull() }

                        if (data.size == 6) {
                            emit(data)
                        }
                        buffer.clear()
                    }
                    isCollecting -> {
                        // Collect characters between < and >
                        buffer.append(char)
                    }
                }
            } catch (e: IOException) {
                break
            }
        }
    }.flowOn(Dispatchers.IO)

    fun disconnect() {
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}