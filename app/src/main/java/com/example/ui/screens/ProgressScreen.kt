package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.DateUtils
import com.example.data.model.PersonalRecordEntity
import com.example.ui.components.MonthlyActivityGrid
import com.example.ui.components.StadiumCard
import com.example.ui.components.WeeklyActivityBarChart
import com.example.ui.theme.CrimsonOpponent
import com.example.ui.theme.GoldTrophy
import com.example.viewmodel.FootballViewModel

@Composable
fun ProgressScreen(
    viewModel: FootballViewModel,
    modifier: Modifier = Modifier
) {
    val summary by viewModel.dashboardSummary.collectAsStateWithLifecycle()
    val completedLessons by viewModel.completedLessons.collectAsStateWithLifecycle()
    val personalRecords by viewModel.personalRecords.collectAsStateWithLifecycle()

    var showAddRecordDialog by remember { mutableStateOf(false) }
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
                Text("Progress Analytics", style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = "Local performance metrics, streaks & personal records",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Top KPI Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProgressKpiCard("Sessions", "${summary.completedSessionsTotal}", Icons.Default.CheckCircle, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                ProgressKpiCard("Minutes", "${summary.totalMinutes}m", Icons.Default.Schedule, GoldTrophy, Modifier.weight(1f))
                ProgressKpiCard("Streak", "${summary.streakDays}d", Icons.Default.LocalFireDepartment, CrimsonOpponent, Modifier.weight(1f))
            }
        }

        // Weekly Activity Chart
        item {
            StadiumCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Weekly Training Activity", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    WeeklyActivityBarChart(points = summary.weeklyPoints, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        // Monthly Activity Grid
        item {
            StadiumCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Monthly Training Activity", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(10.dp))
                    MonthlyActivityGrid(points = summary.monthlyPoints, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        // Skill Improvement Ratings
        item {
            StadiumCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Skill Improvement Ratings", style = MaterialTheme.typography.titleLarge)
                    summary.skillRatings.forEach { rating ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(rating.category.displayName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = "${rating.scorePercent}/100 (${rating.completedCount} drills)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { rating.scorePercent / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(CircleShape),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Personal Records Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EmojiEvents, null, tint = GoldTrophy, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Personal Records", style = MaterialTheme.typography.titleLarge)
                }
                TextButton(
                    onClick = { showAddRecordDialog = true },
                    modifier = Modifier.testTag("add_record_button")
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New PR", fontWeight = FontWeight.Bold)
                }
            }
        }

        items(personalRecords, key = { it.id }) { rec ->
            StadiumCard(modifier = Modifier.fillMaxWidth()) {
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
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                    IconButton(onClick = { editingRecord = rec }) {
                        Icon(Icons.Default.Edit, "Edit record", modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = { viewModel.deletePersonalRecord(rec) }) {
                        Icon(Icons.Default.DeleteOutline, "Delete record", modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Recent Completed History
        item {
            Text("Recent Completed Sessions (${completedLessons.size})", style = MaterialTheme.typography.titleLarge)
        }

        items(completedLessons.take(15), key = { it.id }) { log ->
            StadiumCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(log.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${log.categoryLabel} • ${DateUtils.formatDisplayDate(log.dateKey)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("${log.durationMinutes} min", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
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
            title = { Text("Add Personal Record", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Record Title") }, placeholder = { Text("E.g. Plank Hold") }, singleLine = true)
                    OutlinedTextField(value = value, onValueChange = { value = it }, label = { Text("Best Value") }, placeholder = { Text("E.g. 120") }, singleLine = true)
                    OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("Unit") }, placeholder = { Text("E.g. sec / touches") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (title.isNotBlank() && value.isNotBlank()) {
                        viewModel.addPersonalRecord(title, value, unit)
                        showAddRecordDialog = false
                    }
                }) { Text("Save PR") }
            },
            dismissButton = {
                TextButton(onClick = { showAddRecordDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (editingRecord != null) {
        var newValue by remember { mutableStateOf(editingRecord!!.valueText) }
        AlertDialog(
            onDismissRequest = { editingRecord = null },
            title = { Text("Update ${editingRecord!!.title}", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newValue,
                    onValueChange = { newValue = it },
                    label = { Text("New Best (${editingRecord!!.unit})") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.updatePersonalRecord(editingRecord!!, newValue)
                    editingRecord = null
                }) { Text("Update") }
            },
            dismissButton = {
                TextButton(onClick = { editingRecord = null }) { Text("Cancel") }
            }
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
    StadiumCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
