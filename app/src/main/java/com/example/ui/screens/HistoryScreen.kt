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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.SkillCategory
import com.example.ui.components.StadiumCard
import com.example.ui.theme.CrimsonOpponent
import com.example.ui.theme.GoldTrophy
import com.example.ui.theme.LocalAppStrings
import com.example.viewmodel.FootballViewModel

private enum class HistoryFilter {
    ALL,
    SKILLS,
    FITNESS,
    PROGRAMS
}

@Composable
fun HistoryScreen(
    viewModel: FootballViewModel,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val summary by viewModel.dashboardSummary.collectAsStateWithLifecycle()
    val completedLessons by viewModel.completedLessons.collectAsStateWithLifecycle()
    val personalRecords by viewModel.personalRecords.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Workout Log, 1 = Personal Records
    var activeFilter by remember { mutableStateOf(HistoryFilter.ALL) }
    var showAddRecordDialog by remember { mutableStateOf(false) }
    var editingRecord by remember { mutableStateOf<PersonalRecordEntity?>(null) }

    val skillKeys = remember { SkillCategory.entries.map { it.name }.toSet() }
    val filteredHistory = remember(completedLessons, activeFilter) {
        when (activeFilter) {
            HistoryFilter.ALL -> completedLessons
            HistoryFilter.SKILLS -> completedLessons.filter { it.categoryKey in skillKeys }
            HistoryFilter.FITNESS -> completedLessons.filter { it.categoryKey.startsWith("FITNESS_") || it.categoryKey == "TIMER_SESSION" }
            HistoryFilter.PROGRAMS -> completedLessons.filter { it.categoryKey == "PROGRAM" || it.categoryKey == "PLANNER" }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(strings.historyTitle, style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = strings.historySubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Top Summary Strip
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HistoryKpiCard(strings.sessionsKpi, "${summary.completedSessionsTotal}", Icons.Default.CheckCircle, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                HistoryKpiCard(strings.minutesKpi, "${summary.totalMinutes}m", Icons.Default.Schedule, GoldTrophy, Modifier.weight(1f))
                HistoryKpiCard(strings.streakKpi, "${summary.streakDays}d", Icons.Default.LocalFireDepartment, CrimsonOpponent, Modifier.weight(1f))
            }
        }

        // Tab Switcher: Workout Log | Personal Records
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(strings.historyWorkoutsTab, strings.historyRecordsTab).forEachIndexed { idx, label ->
                    val selected = selectedTab == idx
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedTab = idx }
                            .testTag("history_tab_$idx")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = label,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        if (selectedTab == 0) {
            // Category Filter Chips
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val filters = listOf(
                        HistoryFilter.ALL to strings.historyFilterAll,
                        HistoryFilter.SKILLS to strings.historyFilterSkills,
                        HistoryFilter.FITNESS to strings.historyFilterFitness,
                        HistoryFilter.PROGRAMS to strings.historyFilterPrograms
                    )
                    items(filters) { (filter, label) ->
                        val selected = activeFilter == filter
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { activeFilter = filter }
                                .testTag("history_filter_${filter.name.lowercase()}")
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("${strings.completedSessionsHeader} (${filteredHistory.size})", style = MaterialTheme.typography.titleLarge)
                }
            }

            if (filteredHistory.isEmpty()) {
                item {
                    StadiumCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = strings.noSessionsYet,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(18.dp)
                        )
                    }
                }
            } else {
                items(filteredHistory, key = { it.id }) { log ->
                    StadiumCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("history_item_${log.id}")
                    ) {
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
                                Text(
                                    text = "${log.categoryLabel} • ${DateUtils.formatDisplayDate(log.dateKey)}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "${log.durationMinutes} min",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            IconButton(onClick = { viewModel.deleteCompletedLesson(log.id) }) {
                                Icon(Icons.Default.DeleteOutline, "Delete history item", tint = CrimsonOpponent, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        } else {
            // Personal Records Tab
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EmojiEvents, null, tint = GoldTrophy, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.personalRecordsTitle, style = MaterialTheme.typography.titleLarge)
                    }
                    TextButton(
                        onClick = { showAddRecordDialog = true },
                        modifier = Modifier.testTag("add_record_button")
                    ) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(strings.newPrButton, fontWeight = FontWeight.Bold)
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
                            Icon(Icons.Default.DeleteOutline, "Delete record", tint = CrimsonOpponent, modifier = Modifier.size(18.dp))
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
private fun HistoryKpiCard(
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
