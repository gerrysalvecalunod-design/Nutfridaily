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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
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
import com.example.ui.theme.CobaltAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.VictoryGold
import com.example.viewmodel.BadmintonViewModel

@Composable
fun ProgressScreen(
    viewModel: BadmintonViewModel,
    modifier: Modifier = Modifier
) {
    val summary by viewModel.dashboardSummary.collectAsStateWithLifecycle()
    val completedSessions by viewModel.completedSessions.collectAsStateWithLifecycle()
    val personalRecords by viewModel.personalRecords.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Charts & Skills, 1 = History, 2 = Achievements, 3 = Records
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
                Text("Progress Tracker", style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = "Saved training history, skill improvements, charts & achievements",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Top 3 KPI Summary Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProgressKpiCard("Sessions", "${summary.totalSessions}", Icons.Default.CheckCircle, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                ProgressKpiCard("Hours", "${summary.totalHoursFormatted}h", Icons.Default.Schedule, CobaltAccent, Modifier.weight(1f))
                ProgressKpiCard("Streak", "${summary.streakDays}d", Icons.Default.LocalFireDepartment, AmberTimer, Modifier.weight(1f))
            }
        }

        // Sub-Tab Switcher
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Charts", "History", "Badges", "Records").forEachIndexed { idx, label ->
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
                                fontSize = 11.sp,
                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        when (selectedTab) {
            0 -> {
                // Weekly Performance Bar Chart
                item {
                    SportyCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Weekly Performance", style = MaterialTheme.typography.titleLarge)
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

                // Monthly Performance 28-Day Grid
                item {
                    SportyCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Monthly Performance", style = MaterialTheme.typography.titleLarge)
                            MonthlyPerformanceGrid(points = summary.monthlyPoints, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }

                // Skill Improvements Across 9 Categories
                item {
                    SportyCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Skill Improvements", style = MaterialTheme.typography.titleLarge)
                                Text(
                                    text = "${summary.activeProgramTitle}: ${summary.activeProgramPercent}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            summary.categoryMastery.forEach { stat ->
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(stat.category.displayName, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
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
            }

            1 -> {
                item {
                    Text(
                        text = "Training History (${completedSessions.size})",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                if (completedSessions.isEmpty()) {
                    item {
                        SportyCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "No completed training sessions yet. Complete any drill, program module, or timer workout to permanently record date, duration, drills, and results offline!",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(18.dp)
                            )
                        }
                    }
                } else {
                    items(completedSessions, key = { it.id }) { session ->
                        val timeText = DateUtils.formatTimestampTime(session.timestamp)
                        SportyCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(session.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(
                                            text = "${session.categoryLabel} • ${DateUtils.formatDisplayDate(session.dateKey)}" +
                                                if (timeText.isNotBlank()) " at $timeText" else "",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                                        Text(
                                            text = "${session.durationMinutes} min",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    IconButton(onClick = { viewModel.deleteCompletedSession(session.id) }) {
                                        Icon(Icons.Default.DeleteOutline, "Delete log", tint = AlertCoral, modifier = Modifier.size(18.dp))
                                    }
                                }
                                if (session.drillsSummary.isNotBlank() || session.resultSummary.isNotBlank()) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                            if (session.drillsSummary.isNotBlank()) {
                                                Text(
                                                    text = "Drills: ${session.drillsSummary}",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                            if (session.resultSummary.isNotBlank()) {
                                                Text(
                                                    text = "Result: ${session.resultSummary}",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Offline Achievements", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = "${summary.unlockedAchievementsCount}/${summary.totalAchievementsCount} Unlocked",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                items(achievements, key = { it.id }) { ach ->
                    val pct = if (ach.progressTarget > 0) {
                        (ach.progressCurrent.toFloat() / ach.progressTarget.toFloat()).coerceIn(0f, 1f)
                    } else 0f
                    SportyCard(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = if (ach.unlocked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.38f)
                        else MaterialTheme.colorScheme.surface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (ach.unlocked) VictoryGold.copy(alpha = 0.22f)
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (ach.unlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                                    contentDescription = ach.title,
                                    tint = if (ach.unlocked) AmberTimer else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(ach.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(
                                        text = if (ach.unlocked) "Unlocked" else "${ach.progressCurrent}/${ach.progressTarget}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (ach.unlocked) EmeraldSuccess else MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(ach.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                LinearProgressIndicator(
                                    progress = { pct },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .clip(CircleShape),
                                    color = if (ach.unlocked) EmeraldSuccess else MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            else -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EmojiEvents, null, tint = AmberTimer, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Personal Bests", style = MaterialTheme.typography.titleLarge)
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
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Metric Name") }, placeholder = { Text("E.g. Plank Hold") }, singleLine = true)
                    OutlinedTextField(value = value, onValueChange = { value = it }, label = { Text("Value") }, placeholder = { Text("E.g. 95") }, singleLine = true)
                    OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("Unit") }, placeholder = { Text("E.g. sec / hits") }, singleLine = true)
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
            dismissButton = {
                TextButton(onClick = { showAddRecordDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (editingRecord != null) {
        var newVal by remember { mutableStateOf(editingRecord!!.valueText) }
        AlertDialog(
            onDismissRequest = { editingRecord = null },
            title = { Text("Update ${editingRecord!!.title}", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(value = newVal, onValueChange = { newVal = it }, label = { Text("New Value (${editingRecord!!.unit})") }, singleLine = true)
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.updatePersonalRecord(editingRecord!!, newVal)
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
    SportyCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, label, tint = tint, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
