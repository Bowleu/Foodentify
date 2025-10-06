package com.bowleu.foodentify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bowleu.foodentify.ui.common.Screen
import com.bowleu.foodentify.ui.main.MainScreen
import com.bowleu.foodentify.ui.main.MainViewModel
import com.bowleu.foodentify.ui.product.ProductScreen
import com.bowleu.foodentify.ui.product.ProductViewModel
import com.bowleu.foodentify.ui.scanner.ScannerScreen
import com.bowleu.foodentify.ui.scanner.ScannerViewModel
import com.bowleu.foodentify.ui.theme.FoodentifyTheme
import com.journeyapps.barcodescanner.ScanContract
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val scannerViewModel: ScannerViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val barcodeLauncher = registerForActivityResult(ScanContract()) {
        result ->
        if (result.contents.isNullOrEmpty()) {
            Timber.w( "Barcode is null or empty.")
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
                    composable(
                        route = Screen.Product.route,
                        arguments = listOf(navArgument("productId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")?.toLong() ?: 0
                        productViewModel.setProductId(productId)
                        ProductScreen(navController, productViewModel)
                    }
                }
            }
        }
    }
}
