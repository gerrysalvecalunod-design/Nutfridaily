package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.DateUtils
import com.example.data.model.ReminderCategory
import com.example.ui.components.BotanicalCard
import com.example.ui.components.CircularProgressRing
import com.example.ui.theme.CitrusAmber
import com.example.ui.theme.CitrusLight
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ForestDeep
import com.example.ui.theme.HydrationBlue
import com.example.ui.theme.HydrationLight
import com.example.ui.theme.MintContainerLight
import com.example.ui.theme.MintVibrant
import com.example.ui.theme.VioletLight
import com.example.ui.theme.VioletWeight
import com.example.viewmodel.NutriFitViewModel

@Composable
fun HomeDashboardScreen(
    viewModel: NutriFitViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val overview by viewModel.todayOverview.collectAsStateWithLifecycle()
    val reminders by viewModel.reminders.collectAsStateWithLifecycle()

    var showSettingsDialog by remember { mutableStateOf(false) }
    var showAddReminderDialog by remember { mutableStateOf(false) }
    var suggestedLoggedToast by remember { mutableStateOf<String?>(null) }

    val todayKey = DateUtils.todayKey()
    // Rotate suggestion based on day of month
    val foodTip = remember(viewModel.foodSuggestions) {
        val idx = (System.currentTimeMillis() / 86_400_000L).toInt() % viewModel.foodSuggestions.size
        viewModel.foodSuggestions[idx]
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // 1. Emerald Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(ForestDeep, EmeraldPrimary))
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = DateUtils.formatDisplayDate(todayKey),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MintContainerLight
                            )
                            Text(
                                text = "Hi, ${profile?.name?.split(" ")?.firstOrNull() ?: "Friend"}!",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.18f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            IconButton(
                                onClick = { showSettingsDialog = true },
                                modifier = Modifier.testTag("open_settings_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Profile & Goal Settings",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Daily Habit Score Card inside Header
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(AppScreen.Progress) }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressRing(
                                progress = overview.habitsCompleted / 4f,
                                color = Color.White,
                                trackColor = Color.White.copy(alpha = 0.25f),
                                strokeWidth = 8.dp,
                                modifier = Modifier.size(66.dp)
                            ) {
                                Text(
                                    text = "${overview.habitsCompleted}/4",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Daily Healthy Habits",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = if (overview.habitsCompleted == 4)
                                        "All 4 daily goals achieved! Keep it up!"
                                    else
                                        "Meals, hydration, weight & active minutes today.",
                                    fontSize = 12.sp,
                                    color = MintContainerLight,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. 2x2 Today's Trackers Grid
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Text(
                    text = "Today's Overview",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardMetricTile(
                        title = "Daily Nutrition",
                        mainValue = "${overview.caloriesConsumed} kcal",
                        subText = "${overview.mealsCount} meals • Goal ${overview.calorieGoal}",
                        progress = (overview.caloriesConsumed.toFloat() / overview.calorieGoal).coerceIn(0f, 1f),
                        accentColor = EmeraldPrimary,
                        bgTint = MintContainerLight,
                        icon = Icons.Default.Restaurant,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("tile_nutrition"),
                        onClick = { viewModel.navigateTo(AppScreen.Nutrition) }
                    )

                    DashboardMetricTile(
                        title = "Water Intake",
                        mainValue = "${overview.waterMl} ml",
                        subText = "Goal ${overview.waterGoalMl} ml",
                        progress = (overview.waterMl.toFloat() / overview.waterGoalMl).coerceIn(0f, 1f),
                        accentColor = HydrationBlue,
                        bgTint = HydrationLight,
                        icon = Icons.Default.WaterDrop,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("tile_water"),
                        onClick = { viewModel.navigateTo(AppScreen.Water) },
                        quickActionLabel = "+250 ml",
                        onQuickAction = { viewModel.addWater(250, "Quick Glass (250 ml)") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val delta = overview.latestWeightKg - overview.startWeightKg
                    val deltaSign = if (delta > 0) "+" else ""
                    DashboardMetricTile(
                        title = "Weight Tracker",
                        mainValue = String.format("%.1f kg", overview.latestWeightKg),
                        subText = "${deltaSign}${String.format("%.1f", delta)} kg • Goal ${String.format("%.1f", overview.targetWeightKg)}",
                        progress = if (overview.startWeightKg > overview.targetWeightKg) {
                            ((overview.startWeightKg - overview.latestWeightKg) / (overview.startWeightKg - overview.targetWeightKg)).coerceIn(0.1f, 1f)
                        } else 0.7f,
                        accentColor = VioletWeight,
                        bgTint = VioletLight,
                        icon = Icons.Default.MonitorWeight,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("tile_weight"),
                        onClick = { viewModel.navigateTo(AppScreen.Weight) }
                    )

                    DashboardMetricTile(
                        title = "Walk & Jog",
                        mainValue = "${overview.activityMinutes} min",
                        subText = "${String.format("%.1f", overview.distanceKm)} km • ${overview.caloriesBurned} kcal",
                        progress = (overview.activityMinutes.toFloat() / overview.activityGoalMinutes).coerceIn(0f, 1f),
                        accentColor = CitrusAmber,
                        bgTint = CitrusLight,
                        icon = Icons.Default.DirectionsRun,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("tile_activity"),
                        onClick = { viewModel.navigateTo(AppScreen.Activity) }
                    )
                }
            }
        }

        // 3. Healthy Food Suggestion Card
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Healthy Food Suggestion",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "See all",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.Nutrition) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                BotanicalCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MintContainerLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Eco, null, tint = EmeraldPrimary)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = foodTip.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "${foodTip.mealCategory.displayName} • ${foodTip.calories} kcal • ${foodTip.benefitTag}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = EmeraldPrimary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = foodTip.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (suggestedLoggedToast != null) {
                                Text(
                                    text = suggestedLoggedToast!!,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                            } else {
                                Text(
                                    text = "P: ${foodTip.proteinGrams}g • C: ${foodTip.carbsGrams}g • F: ${foodTip.fatGrams}g",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.logSuggestedFood(foodTip)
                                    suggestedLoggedToast = "Added to Today's Meals!"
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                modifier = Modifier.testTag("log_suggested_food_button")
                            ) {
                                Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Log Meal", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 4. Daily Health Reminders Section
        item {
            Spacer(modifier = Modifier.height(22.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NotificationsActive, null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Daily Health Reminders",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                TextButton(onClick = { showAddReminderDialog = true }) {
                    Text("+ Add", fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                }
            }
        }

        items(reminders) { reminder ->
            val isDoneToday = reminder.lastCompletedDateKey == todayKey
            BotanicalCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 5.dp),
                onClick = { viewModel.toggleReminderCompletedToday(reminder) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isDoneToday) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = if (isDoneToday) "Completed" else "Pending",
                        tint = if (isDoneToday) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = reminder.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDoneToday) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = reminder.description,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MintContainerLight
                    ) {
                        Text(
                            text = reminder.timeLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestDeep,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }

    // Settings & Profile Modal
    if (showSettingsDialog) {
        var editName by remember { mutableStateOf(profile?.name ?: "Alex Morgan") }
        var editCal by remember { mutableStateOf((profile?.dailyCalorieGoal ?: 2000).toString()) }
        var editWater by remember { mutableStateOf((profile?.dailyWaterGoalMl ?: 2250).toString()) }
        var editActivity by remember { mutableStateOf((profile?.dailyActivityMinutesGoal ?: 30).toString()) }
        var editTargetW by remember { mutableStateOf(String.format("%.1f", profile?.targetWeightKg ?: 66.0f)) }

        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Profile & Daily Goals", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Profile Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editCal,
                            onValueChange = { editCal = it },
                            label = { Text("Calorie Goal") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editWater,
                            onValueChange = { editWater = it },
                            label = { Text("Water (ml)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editActivity,
                            onValueChange = { editActivity = it },
                            label = { Text("Activity (min)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editTargetW,
                            onValueChange = { editTargetW = it },
                            label = { Text("Target kg") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "NutriFit Daily v1.0 • Offline Local Storage",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedButton(
                        onClick = {
                            showSettingsDialog = false
                            viewModel.signOutLocalProfile()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed)
                    ) {
                        Text("Switch / Exit Profile", fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateGoals(
                            name = editName,
                            calorieGoal = editCal.toIntOrNull() ?: 2000,
                            waterGoalMl = editWater.toIntOrNull() ?: 2250,
                            activityMinutesGoal = editActivity.toIntOrNull() ?: 30,
                            targetWeightKg = editTargetW.toFloatOrNull() ?: 66.0f
                        )
                        showSettingsDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Save Goals", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Custom Reminder Dialog
    if (showAddReminderDialog) {
        var remTitle by remember { mutableStateOf("") }
        var remDesc by remember { mutableStateOf("") }
        var remTime by remember { mutableStateOf("06:00 PM") }
        var remCat by remember { mutableStateOf(ReminderCategory.ACTIVITY) }

        AlertDialog(
            onDismissRequest = { showAddReminderDialog = false },
            title = { Text("New Health Reminder", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = remTitle,
                        onValueChange = { remTitle = it },
                        label = { Text("Reminder Title") },
                        placeholder = { Text("E.g. Drink Green Tea") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = remDesc,
                        onValueChange = { remDesc = it },
                        label = { Text("Details") },
                        placeholder = { Text("Optional helpful note") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = remTime,
                        onValueChange = { remTime = it },
                        label = { Text("Time (e.g. 08:30 AM)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ReminderCategory.entries.forEach { cat ->
                            val selected = remCat == cat
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (selected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable { remCat = cat }
                            ) {
                                Text(
                                    text = cat.displayName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (remTitle.isNotBlank()) {
                            viewModel.addCustomReminder(remTitle, remDesc, remTime, remCat)
                            showAddReminderDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddReminderDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun DashboardMetricTile(
    title: String,
    mainValue: String,
    subText: String,
    progress: Float,
    accentColor: Color,
    bgTint: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    quickActionLabel: String? = null,
    onQuickAction: (() -> Unit)? = null
) {
    BotanicalCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(bgTint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = title, tint = accentColor, modifier = Modifier.size(20.dp))
                }

                if (quickActionLabel != null && onQuickAction != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = accentColor,
                        modifier = Modifier
                            .clickable { onQuickAction() }
                            .testTag("quick_add_water_tile")
                    ) {
                        Text(
                            text = quickActionLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = mainValue,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subText,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = accentColor,
                trackColor = bgTint
            )
        }
    }
}
