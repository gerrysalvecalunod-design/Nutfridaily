package com.example.ui.screens

sealed class AppScreen(val route: String) {
    data object Splash : AppScreen("splash")
    data object Welcome : AppScreen("welcome")
    data object Home : AppScreen("home")
    data object Training : AppScreen("training")
    data object Programs : AppScreen("programs")
    data object History : AppScreen("history")
    data object Goals : AppScreen("goals")
    data object Profile : AppScreen("profile")
}
