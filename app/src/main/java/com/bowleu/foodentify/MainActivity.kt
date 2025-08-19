package com.bowleu.foodentify

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bowleu.foodentify.ui.common.Screen
import com.bowleu.foodentify.ui.main.MainScreen
import com.bowleu.foodentify.ui.main.MainViewModel
import com.bowleu.foodentify.ui.scanner.ScannerScreen
import com.bowleu.foodentify.ui.scanner.ScannerViewModel
import com.bowleu.foodentify.ui.theme.FoodentifyTheme
import com.journeyapps.barcodescanner.ScanContract
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private companion object {
        const val TAG = "MainActivity"
    }
    private val mainViewModel: MainViewModel by viewModels()
    private val scannerViewModel: ScannerViewModel by viewModels()
    private val barcodeLauncher = registerForActivityResult(ScanContract()) {
        result ->
        if (result.contents.isNullOrEmpty()) {
            Log.w(TAG, "Barcode is null or empty.")
        } else {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodentifyTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = Screen.Main.route) {
                    composable(Screen.Main.route) {
                        MainScreen(navController, mainViewModel)
                    }
                    composable(Screen.Scanner.route) {
                        ScannerScreen(navController, scannerViewModel)
                    }
                }
            }
        }
    }
}
