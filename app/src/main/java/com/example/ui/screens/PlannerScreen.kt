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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BadmintonExercise
import com.example.data.model.LessonSection
import com.example.ui.components.SportyCard
import com.example.ui.theme.AlertCoral
import com.example.viewmodel.BadmintonViewModel

@Composable
fun PlannerScreen(
    viewModel: BadmintonViewModel,
    modifier: Modifier = Modifier
) {
    val plannedWorkouts by viewModel.plannedWorkouts.collectAsStateWithLifecycle()
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    var selectedDay by remember { mutableStateOf("Mon") }
    var showCreateDialog by remember { mutableStateOf(false) }

    val dayWorkouts = plannedWorkouts.filter { it.dayOfWeek == selectedDay }
    val totalDayMinutes = dayWorkouts.sumOf { it.durationMinutes }
    val completedDayCount = dayWorkouts.count { it.isCompleted }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Daily Workout Planner", style = MaterialTheme.typography.headlineLarge)
                    Text(
                        text = "Schedule drills, set durations & track daily completion",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = { showCreateDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("planner_add_workout_button")
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Day Selector (Mon..Sun)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                daysOfWeek.forEach { day ->
                    val count = plannedWorkouts.count { it.dayOfWeek == day }
                    val done = plannedWorkouts.count { it.dayOfWeek == day && it.isCompleted }
                    val selected = day == selectedDay
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedDay = day }
                            .testTag("planner_day_$day")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = day,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$done/$count",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // Day Summary Strip
        item {
            SportyCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.30f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("$selectedDay Schedule", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(
                            text = "${dayWorkouts.size} workout(s) • $totalDayMinutes min planned • $completedDayCount completed",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (dayWorkouts.isEmpty()) {
            item {
                SportyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("No workouts planned for $selectedDay", fontWeight = FontWeight.Bold)
                        Text(
                            text = "Tap + New to create a custom schedule with badminton exercises.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(onClick = { showCreateDialog = true }) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Create $selectedDay Workout")
                        }
                    }
                }
            }
        } else {
            items(dayWorkouts, key = { it.id }) { workout ->
                SportyCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("planner_workout_card_${workout.id}")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.togglePlannedWorkoutCompleted(workout) }
                            ) {
                                Icon(
                                    imageVector = if (workout.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = "Mark workout completed",
                                    tint = if (workout.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(workout.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(
                                    text = "${workout.categoryLabel} • ${workout.durationMinutes} min",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { viewModel.deletePlannedWorkout(workout) }) {
                                Icon(Icons.Default.DeleteOutline, "Remove workout", tint = AlertCoral)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.startPlannedWorkoutInTimer(workout) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Run in Timer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        val selectableExercises = remember(viewModel.allExercises) {
            viewModel.allExercises.filter { it.section != LessonSection.RULES }
        }
        var workoutTitle by remember { mutableStateOf("") }
        var durationInput by remember { mutableStateOf("30") }
        val pickedExercises = remember { mutableStateListOf<BadmintonExercise>() }

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create $selectedDay Workout", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = workoutTitle,
                        onValueChange = { workoutTitle = it },
                        label = { Text("Workout Title") },
                        placeholder = { Text("E.g. Smash & Footwork Combo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("planner_title_input")
                    )
                    OutlinedTextField(
                        value = durationInput,
                        onValueChange = { durationInput = it },
                        label = { Text("Duration (minutes)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Add Exercises (${pickedExercises.size} selected)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(selectableExercises, key = { it.id }) { ex ->
                            val isSelected = pickedExercises.any { it.id == ex.id }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        if (isSelected) pickedExercises.removeAll { it.id == ex.id }
                                        else {
                                            pickedExercises.add(ex)
                                            durationInput = pickedExercises.sumOf { it.durationMinutes }.toString()
                                        }
                                    }
                                    .padding(vertical = 6.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(ex.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                                    Text("${ex.categoryLabel} • ${ex.durationMinutes}m", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addPlannedWorkout(
                            dayOfWeek = selectedDay,
                            title = workoutTitle,
                            categoryLabel = pickedExercises.firstOrNull()?.categoryLabel ?: "Custom Schedule",
                            selectedExercises = pickedExercises.toList(),
                            durationMinutes = durationInput.toIntOrNull() ?: 30
                        )
                        showCreateDialog = false
                    },
                    modifier = Modifier.testTag("planner_save_button")
                ) {
                    Text("Save Plan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) { Text("Cancel") }
            }
        )
    }
}
