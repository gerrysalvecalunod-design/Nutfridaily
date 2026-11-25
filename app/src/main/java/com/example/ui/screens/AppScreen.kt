package com.example.ui.screens

sealed class AppScreen(val route: String) {
    data object Splash : AppScreen("splash")
    data object Welcome : AppScreen("welcome")
    data object Home : AppScreen("home")
    data object Training : AppScreen("training")
    data object DailyPlan : AppScreen("daily_plan")
    data object Timer : AppScreen("timer")
    data object History : AppScreen("history")
    data object Progress : AppScreen("progress")
    data object Achievements : AppScreen("achievements")
    data object Profile : AppScreen("profile")
    data object Settings : AppScreen("settings")
}
