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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.AppScreen
import com.example.ui.screens.GoalsScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.ProfileAndSettingsScreen
import com.example.ui.screens.ProgramsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TrainingScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.FootballProTheme
import com.example.ui.theme.LocalAppStrings
import com.example.ui.theme.stringsFor
import com.example.viewmodel.FootballViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: FootballViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by viewModel.settings.collectAsStateWithLifecycle()
            val appStrings = stringsFor(settings.appLanguage)
            CompositionLocalProvider(LocalAppStrings provides appStrings) {
                FootballProTheme(darkTheme = settings.isDarkTheme) {
                    FootballProApp(viewModel = viewModel)
                }
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
                is AppScreen.Splash -> {
                    SplashScreen(onFinished = { viewModel.onSplashComplete() })
                }

                is AppScreen.Welcome -> {
                    WelcomeScreen(viewModel = viewModel)
                }

                is AppScreen.Home -> {
                    HomeDashboardScreen(viewModel = viewModel)
                }

                is AppScreen.Training -> {
                    TrainingScreen(viewModel = viewModel)
                }

                is AppScreen.Programs -> {
                    ProgramsScreen(viewModel = viewModel)
                }

                is AppScreen.History -> {
                    HistoryScreen(viewModel = viewModel)
                }

                is AppScreen.Goals -> {
                    GoalsScreen(viewModel = viewModel)
                }

                is AppScreen.Profile -> {
                    ProfileAndSettingsScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun FootballBottomNavigationBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    val strings = LocalAppStrings.current
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
                icon = { Icon(Icons.Default.Home, contentDescription = strings.navHome) },
                label = { Text(strings.navHome, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) },
                colors = colors,
                modifier = Modifier.testTag("nav_home")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Training,
                onClick = { onNavigate(AppScreen.Training) },
                icon = { Icon(Icons.Default.SportsSoccer, contentDescription = strings.navTraining) },
                label = { Text(strings.navTraining, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) },
                colors = colors,
                modifier = Modifier.testTag("nav_training")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Programs,
                onClick = { onNavigate(AppScreen.Programs) },
                icon = { Icon(Icons.Default.CalendarMonth, contentDescription = strings.navPrograms) },
                label = { Text(strings.navPrograms, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) },
                colors = colors,
                modifier = Modifier.testTag("nav_programs")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.History,
                onClick = { onNavigate(AppScreen.History) },
                icon = { Icon(Icons.Default.History, contentDescription = strings.navHistory) },
                label = { Text(strings.navHistory, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) },
                colors = colors,
                modifier = Modifier.testTag("nav_history")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Profile,
                onClick = { onNavigate(AppScreen.Profile) },
                icon = { Icon(Icons.Default.Settings, contentDescription = strings.navSettings) },
                label = { Text(strings.navSettings, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) },
                colors = colors,
                modifier = Modifier.testTag("nav_settings")
            )
        }
    }
}
