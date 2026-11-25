package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.AppScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.InteractiveTimerPanel
import com.example.ui.screens.PlannerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TrainingHistoryScreen
import com.example.ui.screens.TrainingScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.FootballProTheme
import com.example.viewmodel.FootballViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: FootballViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by viewModel.settings.collectAsStateWithLifecycle()
            FootballProTheme(darkTheme = settings.isDarkTheme) {
                FootballProApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FootballProApp(viewModel: FootballViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    val isSecondaryScreen = currentScreen !is AppScreen.Splash &&
        currentScreen !is AppScreen.Welcome &&
        currentScreen !is AppScreen.Home

    BackHandler(enabled = isSecondaryScreen) {
        viewModel.navigateTo(AppScreen.Home)
    }

    val showBottomBar = currentScreen !is AppScreen.Splash &&
        currentScreen !is AppScreen.Welcome

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                FootballBottomNavigationBar(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                is AppScreen.Splash -> SplashScreen(onFinished = { viewModel.onSplashComplete() })
                is AppScreen.Welcome -> WelcomeScreen(viewModel = viewModel)
                is AppScreen.Home -> HomeDashboardScreen(viewModel = viewModel)
                is AppScreen.Training -> TrainingScreen(viewModel = viewModel)
                is AppScreen.DailyPlan -> PlannerScreen(viewModel = viewModel)
                is AppScreen.Timer -> InteractiveTimerPanel(viewModel = viewModel, showBackHeader = true)
                is AppScreen.History -> TrainingHistoryScreen(viewModel = viewModel)
                is AppScreen.Progress -> ProgressScreen(viewModel = viewModel)
                is AppScreen.Achievements -> AchievementsScreen(viewModel = viewModel)
                is AppScreen.Profile -> ProfileScreen(viewModel = viewModel)
                is AppScreen.Settings -> SettingsScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun FootballBottomNavigationBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 10.dp,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            val colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Home,
                onClick = { onNavigate(AppScreen.Home) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) },
                colors = colors,
                modifier = Modifier.testTag("nav_home")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Training || currentScreen is AppScreen.Timer,
                onClick = { onNavigate(AppScreen.Training) },
                icon = { Icon(Icons.Default.SportsSoccer, contentDescription = "Training") },
                label = { Text("Training", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) },
                colors = colors,
                modifier = Modifier.testTag("nav_training")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.DailyPlan,
                onClick = { onNavigate(AppScreen.DailyPlan) },
                icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Daily Plan") },
                label = { Text("Daily Plan", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) },
                colors = colors,
                modifier = Modifier.testTag("nav_daily_plan")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Progress || currentScreen is AppScreen.History || currentScreen is AppScreen.Achievements,
                onClick = { onNavigate(AppScreen.Progress) },
                icon = { Icon(Icons.Default.BarChart, contentDescription = "Progress") },
                label = { Text("Progress", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) },
                colors = colors,
                modifier = Modifier.testTag("nav_progress")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Profile || currentScreen is AppScreen.Settings,
                onClick = { onNavigate(AppScreen.Profile) },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) },
                colors = colors,
                modifier = Modifier.testTag("nav_profile")
            )
        }
    }
}
