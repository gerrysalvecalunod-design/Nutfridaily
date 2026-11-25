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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.DateUtils
import com.example.data.model.PersonalRecordEntity
import com.example.ui.components.MonthlyPerformanceGrid
import com.example.ui.components.SportyCard
import com.example.ui.components.WeeklyPerformanceChart
import com.example.ui.theme.AlertCoral
import com.example.ui.theme.AmberTimer
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.TrophyGold
import com.example.ui.theme.TurfAccentTeal
import com.example.viewmodel.FootballViewModel

@Composable
fun ProgressScreen(
    viewModel: FootballViewModel,
    modifier: Modifier = Modifier
) {
    val summary by viewModel.dashboardSummary.collectAsStateWithLifecycle()
    val personalRecords by viewModel.personalRecords.collectAsStateWithLifecycle()
    val trainingGoals by viewModel.trainingGoals.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Analytics, 1 = Records & Goals
    var showAddRecordDialog by remember { mutableStateOf(false) }
    var showAddGoalDialog by remember { mutableStateOf(false) }
    var editingRecord by remember { mutableStateOf<PersonalRecordEntity?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text("Progress Dashboard", style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = "Sessions, hours, minutes, completed drills, streak & category mastery",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // KPI Row 1: Sessions, Hours, Minutes
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProgressKpiCard("Sessions", "${summary.totalSessions}", Icons.Default.CheckCircle, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                ProgressKpiCard("Hours", "${summary.totalHoursFormatted}h", Icons.Default.Schedule, TurfAccentTeal, Modifier.weight(1f))
                ProgressKpiCard("Minutes", "${summary.totalMinutes}m", Icons.Default.Schedule, MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
            }
        }

        // KPI Row 2: Completed Drills, Current Streak, Badges
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProgressKpiCard("Drills Done", "${summary.totalCompletedDrills}", Icons.Default.FitnessCenter, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                ProgressKpiCard("Streak", "${summary.streakDays}d", Icons.Default.LocalFireDepartment, AmberTimer, Modifier.weight(1f))
                ProgressKpiCard("Badges", "${summary.unlockedAchievementsCount}/9", Icons.Default.EmojiEvents, TrophyGold, Modifier.weight(1f))
            }
        }

        // Quick Links to Dedicated Training History & Achievements Screens
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.navigateTo(AppScreen.History) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("progress_open_history_button")
                ) {
                    Icon(Icons.Default.History, null, modifier = Modifier.size(17.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("View History", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = { viewModel.navigateTo(AppScreen.Achievements) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("progress_open_achievements_button")
                ) {
                    Icon(Icons.Default.EmojiEvents, null, tint = TrophyGold, modifier = Modifier.size(17.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Achievements (9)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Analytics vs Records & Goals Toggle
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Charts & Mastery", "Records & Goals").forEachIndexed { idx, label ->
                    val selected = selectedTab == idx
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedTab = idx }
                            .testTag("progress_tab_$idx")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = label,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        if (selectedTab == 0) {
            item {
                SportyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Weekly Training Chart", style = MaterialTheme.typography.titleLarge)
                            Text(
                                text = "${summary.weeklySessionsCount}/${summary.weeklyGoalSessions} goal",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        WeeklyPerformanceChart(points = summary.weeklyPoints, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            item {
                SportyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Monthly Training Chart", style = MaterialTheme.typography.titleLarge)
                        MonthlyPerformanceGrid(points = summary.monthlyPoints, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            item {
                SportyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("8-Category Skill Mastery", style = MaterialTheme.typography.titleLarge)
                        summary.categoryMastery.forEach { stat ->
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${stat.category.letter}. ${stat.category.displayName}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text("${stat.percent}% (${stat.completedCount} done)", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                }
                                LinearProgressIndicator(
                                    progress = { stat.percent / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        } else {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EmojiEvents, null, tint = AmberTimer, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Personal Records", style = MaterialTheme.typography.titleLarge)
                    }
                    TextButton(onClick = { showAddRecordDialog = true }) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add PR", fontWeight = FontWeight.Bold)
                    }
                }
            }
            items(personalRecords, key = { it.id }) { rec ->
                SportyCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(rec.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Updated ${DateUtils.formatDisplayDate(rec.updatedDateKey)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                            Text(
                                text = "${rec.valueText} ${rec.unit}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                        IconButton(onClick = { editingRecord = rec }) {
                            Icon(Icons.Default.Edit, "Edit PR", modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { viewModel.deletePersonalRecord(rec) }) {
                            Icon(Icons.Default.DeleteOutline, "Delete PR", tint = AlertCoral, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Flag, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Custom Training Goals", style = MaterialTheme.typography.titleLarge)
                    }
                    TextButton(onClick = { showAddGoalDialog = true }) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Goal", fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(trainingGoals, key = { it.id }) { goal ->
                val pct = (goal.currentValue.toFloat() / goal.targetValue.coerceAtLeast(1)).coerceIn(0f, 1f)
                SportyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(goal.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${goal.category} • ${goal.currentValue}/${goal.targetValue} ${goal.unit}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { viewModel.deleteTrainingGoal(goal) }) {
                                Icon(Icons.Default.DeleteOutline, "Delete goal", tint = AlertCoral, modifier = Modifier.size(18.dp))
                            }
                        }
                        LinearProgressIndicator(
                            progress = { pct },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                            color = if (goal.isCompleted) EmeraldSuccess else MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { viewModel.incrementTrainingGoal(goal, 1) },
                                modifier = Modifier.height(34.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                            ) {
                                Text("+1 ${goal.unit}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { viewModel.incrementTrainingGoal(goal, 10) },
                                modifier = Modifier.height(34.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                            ) {
                                Text("+10", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddRecordDialog) {
        var title by remember { mutableStateOf("") }
        var value by remember { mutableStateOf("") }
        var unit by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddRecordDialog = false },
            title = { Text("New Personal Record", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Metric Name") }, singleLine = true)
                    OutlinedTextField(value = value, onValueChange = { value = it }, label = { Text("Value") }, singleLine = true)
                    OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("Unit") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (title.isNotBlank() && value.isNotBlank()) {
                        viewModel.addPersonalRecord(title, value, unit)
                        showAddRecordDialog = false
                    }
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showAddRecordDialog = false }) { Text("Cancel") } }
        )
    }

    if (showAddGoalDialog) {
        var title by remember { mutableStateOf("") }
        var target by remember { mutableStateOf("100") }
        var unit by remember { mutableStateOf("reps") }
        AlertDialog(
            onDismissRequest = { showAddGoalDialog = false },
            title = { Text("Add Training Goal", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Goal Title") }, singleLine = true)
                    OutlinedTextField(value = target, onValueChange = { target = it }, label = { Text("Target") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("Unit") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (title.isNotBlank()) {
                        viewModel.addTrainingGoal(title, "Football", target.toIntOrNull() ?: 50, unit)
                        showAddGoalDialog = false
                    }
                }) { Text("Add") }
            },
            dismissButton = { TextButton(onClick = { showAddGoalDialog = false }) { Text("Cancel") } }
        )
    }

    if (editingRecord != null) {
        var newVal by remember { mutableStateOf(editingRecord!!.valueText) }
        AlertDialog(
            onDismissRequest = { editingRecord = null },
            title = { Text("Update ${editingRecord!!.title}", fontWeight = FontWeight.Bold) },
            text = { OutlinedTextField(value = newVal, onValueChange = { newVal = it }, label = { Text("New Value (${editingRecord!!.unit})") }, singleLine = true) },
            confirmButton = {
                Button(onClick = {
                    viewModel.updatePersonalRecord(editingRecord!!, newVal)
                    editingRecord = null
                }) { Text("Update") }
            },
            dismissButton = { TextButton(onClick = { editingRecord = null }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun ProgressKpiCard(
    label: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    SportyCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, label, tint = tint, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
    }
}
