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
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.DateUtils
import com.example.data.model.ActivityType
import com.example.ui.components.BotanicalCard
import com.example.ui.components.DateSelectorStrip
import com.example.ui.theme.CitrusAmber
import com.example.ui.theme.CitrusGold
import com.example.ui.theme.CitrusLight
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ForestDeep
import com.example.ui.theme.MintContainerLight
import com.example.viewmodel.NutriFitViewModel
import java.util.Locale

@Composable
fun ActivityScreen(
    viewModel: NutriFitViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val selectedDateKey by viewModel.selectedDateKey.collectAsStateWithLifecycle()
    val activities by viewModel.activitiesForSelectedDate.collectAsStateWithLifecycle()
    val activeType by viewModel.activeWorkoutType.collectAsStateWithLifecycle()
    val stopwatchSeconds by viewModel.stopwatchSeconds.collectAsStateWithLifecycle()
    val isRunning by viewModel.isStopwatchRunning.collectAsStateWithLifecycle()

    var showManualDialog by remember { mutableStateOf(false) }

    val totalMinutes = activities.sumOf { it.durationMinutes }
    val totalDistance = activities.sumOf { it.distanceKm.toDouble() }.toFloat()
    val totalSteps = activities.sumOf { it.steps }
    val totalCalories = activities.sumOf { it.caloriesBurned }

    val goalMinutes = profile?.dailyActivityMinutesGoal ?: 30
    val goalDistance = profile?.dailyDistanceKmGoal ?: 3.0f
    val minutesProgress = (totalMinutes.toFloat() / goalMinutes.coerceAtLeast(1)).coerceIn(0f, 1f)

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Walking & Jogging",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "Track daily walks, jogs, steps & active time offline",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                DateSelectorStrip(
                    selectedDateKey = selectedDateKey,
                    onPreviousDay = { viewModel.stepDate(-1) },
                    onNextDay = { viewModel.stepDate(1) },
                    onJumpToday = { viewModel.selectDate(DateUtils.todayKey()) }
                )
            }

            // Live Workout Stopwatch Card
            item {
                BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        CitrusLight.copy(alpha = 0.7f),
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Timer, null, tint = CitrusAmber, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Live Activity Timer",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isRunning) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = if (isRunning) "ACTIVE" else "READY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isRunning) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Walking vs Jogging Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ActivityType.entries.forEach { type ->
                                val selected = activeType == type
                                val icon = if (type == ActivityType.WALKING) {
                                    Icons.AutoMirrored.Filled.DirectionsWalk
                                } else {
                                    Icons.AutoMirrored.Filled.DirectionsRun
                                }
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (selected) CitrusAmber else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable { viewModel.setWorkoutType(type) }
                                        .testTag("toggle_type_${type.name.lowercase()}")
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = type.displayName,
                                            tint = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = type.displayName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Formatted Timer Readout
                        val hrs = stopwatchSeconds / 3600
                        val mins = (stopwatchSeconds % 3600) / 60
                        val secs = stopwatchSeconds % 60
                        val timeText = if (hrs > 0) {
                            String.format(Locale.US, "%02d:%02d:%02d", hrs, mins, secs)
                        } else {
                            String.format(Locale.US, "%02d:%02d", mins, secs)
                        }
                        val estMinutes = (stopwatchSeconds / 60f)
                        val estDist = estMinutes * (if (activeType == ActivityType.JOGGING) 0.135f else 0.082f)
                        val estKcal = (estMinutes * (if (activeType == ActivityType.JOGGING) 9 else 4)).toInt()

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = timeText,
                                fontSize = 44.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ForestDeep,
                                modifier = Modifier.testTag("workout_timer_text")
                            )
                            Text(
                                text = "Est. ${String.format(Locale.US, "%.2f", estDist)} km • $estKcal kcal",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (isRunning) viewModel.pauseWorkout() else viewModel.startWorkout()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isRunning) CitrusAmber else EmeraldPrimary
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("workout_start_pause_button")
                            ) {
                                Icon(
                                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isRunning) "Pause" else "Start",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isRunning) "Pause" else if (stopwatchSeconds > 0) "Resume" else "Start",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (stopwatchSeconds > 0L) {
                                OutlinedButton(
                                    onClick = { viewModel.resetWorkoutTimer() },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("workout_reset_button")
                                ) {
                                    Icon(Icons.Default.Refresh, "Reset timer", modifier = Modifier.size(18.dp))
                                }

                                Button(
                                    onClick = { viewModel.finishAndSaveWorkout() },
                                    colors = ButtonDefaults.buttonColors(containerColor = ForestDeep),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("workout_save_button")
                                ) {
                                    Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Daily Activity Goal Progress Card
            item {
                BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Daily Activity Goal",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "$totalMinutes / $goalMinutes min",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = CitrusAmber
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { minutesProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(CircleShape),
                            color = CitrusAmber,
                            trackColor = CitrusLight
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ActivityStatBadge(
                                label = "Distance",
                                value = "${String.format(Locale.US, "%.1f", totalDistance)} km",
                                sub = "Goal ${String.format(Locale.US, "%.1f", goalDistance)} km"
                            )
                            ActivityStatBadge(
                                label = "Steps",
                                value = "$totalSteps",
                                sub = "Estimated"
                            )
                            ActivityStatBadge(
                                label = "Burned",
                                value = "$totalCalories kcal",
                                sub = "${activities.size} sessions"
                            )
                        }
                    }
                }
            }

            // Activity History Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Activity Records (${activities.size})",
                        style = MaterialTheme.typography.titleLarge
                    )
                    TextButton(
                        onClick = { showManualDialog = true },
                        modifier = Modifier.testTag("quick_manual_activity_button")
                    ) {
                        Text("+ Quick Log", fontWeight = FontWeight.Bold, color = CitrusAmber)
                    }
                }
            }

            if (activities.isEmpty()) {
                item {
                    BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                                contentDescription = null,
                                tint = CitrusAmber.copy(alpha = 0.45f),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No walking or jogging logged", fontWeight = FontWeight.Bold)
                            Text(
                                text = "Use the live timer above or tap + Log Activity to record a session.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(activities, key = { it.id }) { log ->
                    val isJog = log.activityType == ActivityType.JOGGING.name
                    val icon = if (isJog) Icons.AutoMirrored.Filled.DirectionsRun else Icons.AutoMirrored.Filled.DirectionsWalk
                    val tint = if (isJog) CitrusAmber else EmeraldPrimary
                    val bg = if (isJog) CitrusLight else MintContainerLight

                    BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(bg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, contentDescription = log.activityType, tint = tint)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isJog) "Jogging Session" else "Walking Session",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "${log.durationMinutes} min • ${String.format(Locale.US, "%.1f", log.distanceKm)} km • ${log.steps} steps",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (log.note.isNotBlank()) {
                                    Text(
                                        text = log.note,
                                        fontSize = 11.sp,
                                        color = tint,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = CitrusGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${log.caloriesBurned} kcal",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = tint
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.deleteActivity(log) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        Icons.Default.DeleteOutline,
                                        contentDescription = "Delete activity",
                                        tint = DangerRed.copy(alpha = 0.7f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showManualDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("log_activity_fab"),
            containerColor = CitrusAmber,
            contentColor = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Log Activity", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showManualDialog) {
        var selectedType by remember { mutableStateOf(activeType) }
        var durationInput by remember { mutableStateOf("30") }
        var distanceInput by remember { mutableStateOf(if (activeType == ActivityType.JOGGING) "3.5" else "2.4") }
        var notesInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showManualDialog = false },
            title = { Text("Record Walking or Jogging", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActivityType.entries.forEach { type ->
                            val selected = selectedType == type
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (selected) CitrusAmber else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        selectedType = type
                                        val mins = durationInput.toIntOrNull() ?: 30
                                        val estDist = mins * (if (type == ActivityType.JOGGING) 0.135f else 0.082f)
                                        distanceInput = String.format(Locale.US, "%.1f", estDist)
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = type.displayName,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = durationInput,
                            onValueChange = { durationInput = it },
                            label = { Text("Duration (min)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("activity_duration_input")
                        )
                        OutlinedTextField(
                            value = distanceInput,
                            onValueChange = { distanceInput = it },
                            label = { Text("Distance (km)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("activity_distance_input")
                        )
                    }

                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Notes (optional)") },
                        placeholder = { Text("E.g. Morning neighborhood walk") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val mins = durationInput.toIntOrNull() ?: 0
                        val dist = distanceInput.toFloatOrNull() ?: 0f
                        if (mins > 0 && dist > 0f) {
                            viewModel.addActivity(
                                type = selectedType,
                                durationMinutes = mins,
                                distanceKm = dist,
                                note = notesInput
                            )
                            showManualDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CitrusAmber),
                    modifier = Modifier.testTag("save_manual_activity_button")
                ) {
                    Text("Save Activity", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showManualDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ActivityStatBadge(
    label: String,
    value: String,
    sub: String
) {
    Column {
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
        Text(sub, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
