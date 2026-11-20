package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.ActivityScreen
import com.example.ui.screens.AppScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.NutritionScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WaterTrackerScreen
import com.example.ui.screens.WeightTrackerScreen
import com.example.ui.screens.WelcomeLoginScreen
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ForestDeep
import com.example.ui.theme.MintContainerLight
import com.example.ui.theme.NutriFitTheme
import com.example.viewmodel.NutriFitViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: NutriFitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriFitTheme {
                NutriFitApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NutriFitApp(viewModel: NutriFitViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    // Return to Home Dashboard from secondary screens on system Back press
    val isSecondaryScreen = currentScreen !is AppScreen.Splash &&
        currentScreen !is AppScreen.WelcomeLogin &&
        currentScreen !is AppScreen.Home

    BackHandler(enabled = isSecondaryScreen) {
        viewModel.navigateTo(AppScreen.Home)
    }

    val showBottomBar = currentScreen !is AppScreen.Splash &&
        currentScreen !is AppScreen.WelcomeLogin

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NutriFitBottomBar(
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
                    SplashScreen(
                        onFinished = { viewModel.onSplashComplete() }
                    )
                }

                is AppScreen.WelcomeLogin -> {
                    WelcomeLoginScreen(viewModel = viewModel)
                }

                is AppScreen.Home -> {
                    HomeDashboardScreen(viewModel = viewModel)
                }

                is AppScreen.Nutrition -> {
                    NutritionScreen(viewModel = viewModel)
                }

                is AppScreen.Water -> {
                    WaterTrackerScreen(viewModel = viewModel)
                }

                is AppScreen.Weight -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Surface(
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { viewModel.navigateTo(AppScreen.Home) },
                                    modifier = Modifier.testTag("weight_back_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back to Home",
                                        tint = EmeraldPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Back to Dashboard",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = EmeraldPrimary
                                )
                            }
                        }
                        WeightTrackerScreen(
                            viewModel = viewModel,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                is AppScreen.Activity -> {
                    ActivityScreen(viewModel = viewModel)
                }

                is AppScreen.Progress -> {
                    ProgressScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun NutriFitBottomBar(
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
            val itemColors = NavigationBarItemDefaults.colors(
                selectedIconColor = ForestDeep,
                indicatorColor = MintContainerLight,
                selectedTextColor = EmeraldPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Home,
                onClick = { onNavigate(AppScreen.Home) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_home")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Nutrition,
                onClick = { onNavigate(AppScreen.Nutrition) },
                icon = { Icon(Icons.Default.Restaurant, contentDescription = "Nutrition") },
                label = { Text("Meals", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_nutrition")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Water,
                onClick = { onNavigate(AppScreen.Water) },
                icon = { Icon(Icons.Default.WaterDrop, contentDescription = "Water") },
                label = { Text("Water", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_water")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Activity,
                onClick = { onNavigate(AppScreen.Activity) },
                icon = { Icon(Icons.AutoMirrored.Filled.DirectionsRun, contentDescription = "Walk & Jog") },
                label = { Text("Walk/Jog", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_activity")
            )

            NavigationBarItem(
                selected = currentScreen is AppScreen.Progress || currentScreen is AppScreen.Weight,
                onClick = { onNavigate(AppScreen.Progress) },
                icon = { Icon(Icons.Default.BarChart, contentDescription = "Progress") },
                label = { Text("Progress", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                colors = itemColors,
                modifier = Modifier.testTag("nav_progress")
            )
        }
    }
}
