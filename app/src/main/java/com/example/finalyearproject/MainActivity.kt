package com.example.finalyearproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.finalyearproject.ui.theme.PostureMonitorTheme
import com.example.finalyearproject.viewmodel.AuthViewModel
import com.example.finalyearproject.viewmodel.BluetoothViewModel
import com.example.finalyearproject.viewmodel.PostureViewModel
import com.example.finalyearproject.data.repository.AuthRepository
import com.example.finalyearproject.data.repository.BluetoothRepository
import com.example.finalyearproject.utils.LabelEncoder
import com.example.finalyearproject.utils.PostureClassifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Bluetooth
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        // Initialize components
        val labelEncoder = LabelEncoder(this)
        val postureClassifier = PostureClassifier(this, labelEncoder)

        // Initialize repositories
        val authRepository = AuthRepository()
        val bluetoothRepository = BluetoothRepository(bluetoothAdapter)

        // Initialize ViewModels
        val authViewModel = AuthViewModel(authRepository)
        val postureViewModel = PostureViewModel(postureClassifier)
        // Pass postureViewModel to bluetoothViewModel
        val bluetoothViewModel = BluetoothViewModel(bluetoothRepository, postureViewModel)

        setContent {
            PostureMonitorTheme {
                PostureApp(
                    authViewModel = authViewModel,
                    bluetoothViewModel = bluetoothViewModel,
                    postureViewModel = postureViewModel
                )
            }
        }
    }
}