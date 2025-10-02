package com.bowleu.foodentify.ui.common

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Login : Screen("login")
    object Settings : Screen("settings")
    object Profile : Screen("profile")
    object Scanner : Screen("scan")
    object Product : Screen("product/{productId}")
}