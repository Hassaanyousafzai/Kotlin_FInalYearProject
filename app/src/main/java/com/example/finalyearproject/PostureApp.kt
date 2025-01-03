package com.example.finalyearproject

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.finalyearproject.navigation.PostureAppNavigation
import com.example.finalyearproject.viewmodel.AuthViewModel
import com.example.finalyearproject.viewmodel.BluetoothViewModel
import com.example.finalyearproject.viewmodel.PostureViewModel

@Composable
fun PostureApp(
    authViewModel: AuthViewModel,
    bluetoothViewModel: BluetoothViewModel,
    postureViewModel: PostureViewModel
) {
    val navController = rememberNavController()
    PostureAppNavigation(
        navController = navController,
        authViewModel = authViewModel,
        bluetoothViewModel = bluetoothViewModel,
        postureViewModel = postureViewModel
    )
}