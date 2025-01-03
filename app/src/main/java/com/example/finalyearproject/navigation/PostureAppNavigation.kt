package com.example.finalyearproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalyearproject.ui.screens.*
import com.example.finalyearproject.viewmodel.AuthViewModel
import com.example.finalyearproject.viewmodel.BluetoothViewModel
import com.example.finalyearproject.viewmodel.PostureViewModel

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object BluetoothDevices : Screen("bluetooth_devices")
    object Home : Screen("home")
}

@Composable
fun PostureAppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Welcome.route,
    authViewModel: AuthViewModel,
    bluetoothViewModel: BluetoothViewModel,
    postureViewModel: PostureViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.BluetoothDevices.route) {
                        // Clear the back stack up to the login screen
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                viewModel = authViewModel
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.BluetoothDevices.route) {
                        // Clear the back stack up to the signup screen
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                viewModel = authViewModel
            )
        }

        composable(Screen.BluetoothDevices.route) {
            BluetoothDevicesScreen(
                onDeviceConnected = {
                    navController.navigate(Screen.Home.route) {
                        // Clear the back stack up to the bluetooth devices screen
                        popUpTo(Screen.BluetoothDevices.route) { inclusive = true }
                    }
                },
                viewModel = bluetoothViewModel
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = postureViewModel
            )
        }
    }
}