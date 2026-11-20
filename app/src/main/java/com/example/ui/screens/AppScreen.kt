package com.example.ui.screens

sealed class AppScreen(val route: String) {
    data object Splash : AppScreen("splash")
    data object WelcomeLogin : AppScreen("welcome_login")
    data object Home : AppScreen("home")
    data object Nutrition : AppScreen("nutrition")
    data object Water : AppScreen("water")
    data object Weight : AppScreen("weight")
    data object Activity : AppScreen("activity")
    data object Progress : AppScreen("progress")
}
