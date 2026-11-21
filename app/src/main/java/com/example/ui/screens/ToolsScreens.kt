package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TacticMarker
import com.example.ui.components.StadiumCard
import com.example.ui.theme.AmberTimer
import com.example.ui.theme.CrimsonOpponent
import com.example.ui.theme.GoldTrophy
import com.example.ui.theme.NeonPitchGreen
import com.example.ui.theme.TacticalCyan
import com.example.viewmodel.FootballViewModel
import com.example.viewmodel.TimerToolTab
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun TimersToolPanel(
    viewModel: FootballViewModel
) {
    val state by viewModel.timers.collectAsStateWithLifecycle()
    var savedMsg by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(TimerToolTab.entries) { tab ->
                    val selected = state.selectedTab == tab
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.selectTimerTab(tab) }
                            .testTag("timer_tab_${tab.name.lowercase()}")
                    ) {
                        Box(modifier = Modifier.padding(horizontal = 14.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = tab.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        if (savedMsg != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = savedMsg!!,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            }
        }

        when (state.selectedTab) {
            TimerToolTab.STOPWATCH -> {
                item {
                    val totalSec = state.stopwatchMillis / 1000
                    val mins = totalSec / 60
                    val secs = totalSec % 60
                    val tenths = (state.stopwatchMillis % 1000) / 100
                    StadiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("TRAINING STOPWATCH", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = String.format(Locale.US, "%02d:%02d.%d", mins, secs, tenths),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.testTag("stopwatch_display")
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                Button(
                                    onClick = { viewModel.toggleStopwatch() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (state.stopwatchRunning) AmberTimer else MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("stopwatch_start_pause")
                                ) {
                                    Icon(if (state.stopwatchRunning) Icons.Default.Pause else Icons.Default.PlayArrow, null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (state.stopwatchRunning) "Pause" else "Start", fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.addStopwatchLap() },
                                    enabled = state.stopwatchMillis > 0L,
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Icon(Icons.Default.Flag, "Lap", modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Lap")
                                }
                                OutlinedButton(
                                    onClick = { viewModel.resetStopwatch() },
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, "Reset", modifier = Modifier.size(18.dp))
                                }
                            }
                            if (state.stopwatchMillis >= 5000L) {
                                Spacer(modifier = Modifier.height(10.dp))
                                TextButton(onClick = {
                                    val logMins = (totalSec / 60).toInt().coerceAtLeast(1)
                                    viewModel.logCustomTimerWorkout("Stopwatch Drill Session", "Stopwatch", logMins)
                                    savedMsg = "Logged ${logMins}m Stopwatch session to History!"
                                }) {
                                    Text("Log Session to History", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                items(state.laps) { lap ->
                    StadiumCard(modifier = Modifier.fillMaxWidth()) {
                        Text(lap, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(14.dp))
                    }
                }
            }

            TimerToolTab.COUNTDOWN -> {
                item {
                    val m = state.countdownRemainingSec / 60
                    val s = state.countdownRemainingSec % 60
                    StadiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("COUNTDOWN TIMER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TacticalCyan)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = String.format(Locale.US, "%02d:%02d", m, s),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.countdownRemainingSec == 0) AmberTimer else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(60 to "1m", 180 to "3m", 300 to "5m", 600 to "10m", 900 to "15m").forEach { (sec, label) ->
                                    val active = state.countdownInitialSec == sec
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.clickable { viewModel.setCountdownPreset(sec) }
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                Button(
                                    onClick = { viewModel.toggleCountdown() },
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Text(if (state.countdownRunning) "Pause" else "Start Countdown", fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.resetCountdown() },
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, "Reset")
                                }
                            }
                        }
                    }
                }
            }

            TimerToolTab.INTERVAL -> {
                item {
                    val phaseColor = if (state.isWorkPhase) NeonPitchGreen else AmberTimer
                    StadiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(shape = RoundedCornerShape(10.dp), color = phaseColor.copy(alpha = 0.18f)) {
                                Text(
                                    text = "${if (state.isWorkPhase) "WORK SPRINT" else "ACTIVE REST"} • ROUND ${state.currentRound}/${state.totalRounds}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = phaseColor,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = String.format(Locale.US, "00:%02d", state.intervalRemainingSec),
                                fontSize = 50.sp,
                                fontWeight = FontWeight.Bold,
                                color = phaseColor
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(
                                    Triple(20, 10, 8) to "Tabata 20/10",
                                    Triple(30, 15, 8) to "Match 30/15",
                                    Triple(45, 30, 6) to "Stamina 45/30"
                                ).forEach { (cfg, lbl) ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.clickable { viewModel.configureInterval(cfg.first, cfg.second, cfg.third) }
                                    ) {
                                        Text(lbl, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                Button(
                                    onClick = { viewModel.toggleIntervalTimer() },
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Text(if (state.intervalRunning) "Pause HIIT" else "Start HIIT", fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.resetIntervalTimer() },
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, "Reset")
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = {
                                val mins = (((state.workSeconds + state.restSeconds) * state.totalRounds) / 60).coerceAtLeast(1)
                                viewModel.logCustomTimerWorkout("HIIT Interval Session (${state.workSeconds}/${state.restSeconds}s)", "Fitness HIIT", mins)
                                savedMsg = "Logged ${mins}m HIIT Interval workout!"
                            }) {
                                Text("Log Completed HIIT Workout", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            TimerToolTab.REST -> {
                item {
                    StadiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("SET RECOVERY / REST TIMER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmberTimer)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = String.format(Locale.US, "%02d:%02d", state.restRemainingSec / 60, state.restRemainingSec % 60),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberTimer
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(30, 45, 60, 90, 120).forEach { sec ->
                                    val selected = state.restPresetSec == sec
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (selected) AmberTimer else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.clickable { viewModel.setRestPreset(sec) }
                                    ) {
                                        Text(
                                            text = "${sec}s",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { viewModel.toggleRestTimer() },
                                colors = ButtonDefaults.buttonColors(containerColor = AmberTimer),
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            ) {
                                Text(if (state.restRunning) "Pause Rest" else "Start Recovery Timer", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlannerAndCalendarPanel(
    viewModel: FootballViewModel
) {
    val planned by viewModel.plannedWorkouts.collectAsStateWithLifecycle()
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    var selectedDay by remember { mutableStateOf("Mon") }
    var showAddDialog by remember { mutableStateOf(false) }

    val dayWorkouts = planned.filter { it.dayOfWeek == selectedDay }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Training Calendar & Planner", style = MaterialTheme.typography.titleLarge)
                    Text("Tap a weekday to view or schedule sessions", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("add_planner_workout_button")
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Calendar Strip
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                daysOfWeek.forEach { day ->
                    val count = planned.count { it.dayOfWeek == day }
                    val done = planned.count { it.dayOfWeek == day && it.isCompleted }
                    val selected = day == selectedDay
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedDay = day }
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
                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        if (dayWorkouts.isEmpty()) {
            item {
                StadiumCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No sessions planned for $selectedDay", fontWeight = FontWeight.Bold)
                        Text("Tap + Add to schedule a drill or rest workout.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            items(dayWorkouts, key = { it.id }) { item ->
                StadiumCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.togglePlannedWorkout(item) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (item.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Toggle completion",
                            tint = if (item.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("${item.categoryLabel} • ${item.durationMinutes} min", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { viewModel.deletePlannedWorkout(item) }) {
                            Icon(Icons.Default.DeleteOutline, "Delete", tint = CrimsonOpponent.copy(alpha = 0.8f))
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var titleInput by remember { mutableStateOf("") }
        var catInput by remember { mutableStateOf("Skill Drills") }
        var minsInput by remember { mutableStateOf("30") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Schedule $selectedDay Workout", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Workout Name") },
                        placeholder = { Text("E.g. Finishing & Volleys") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = catInput,
                        onValueChange = { catInput = it },
                        label = { Text("Category") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = minsInput,
                        onValueChange = { minsInput = it },
                        label = { Text("Duration (minutes)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (titleInput.isNotBlank()) {
                        viewModel.addPlannedWorkout(selectedDay, titleInput, catInput, minsInput.toIntOrNull() ?: 30)
                        showAddDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun TacticsBoardPanel(
    viewModel: FootballViewModel
) {
    val formation by viewModel.tacticFormation.collectAsStateWithLifecycle()
    val markers by viewModel.tacticMarkers.collectAsStateWithLifecycle()
    val arrows by viewModel.tacticArrows.collectAsStateWithLifecycle()
    val savedTactics by viewModel.savedTactics.collectAsStateWithLifecycle()

    var drawArrowMode by remember { mutableStateOf(false) }
    var dragArrowStart by remember { mutableStateOf<Offset?>(null) }
    var dragArrowEnd by remember { mutableStateOf<Offset?>(null) }
    var activeMarkerId by remember { mutableStateOf<String?>(null) }
    var boardSize by remember { mutableStateOf(IntSize(1, 1)) }

    var showSaveDialog by remember { mutableStateOf(false) }
    var showLoadDialog by remember { mutableStateOf(false) }
    val textMeasurer = rememberTextMeasurer()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Formation Selector
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Football Tactics Board", style = MaterialTheme.typography.titleLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { showLoadDialog = true }
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FolderOpen, "Load", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Load", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { showSaveDialog = true }
                            .testTag("save_tactic_button")
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Save, "Save", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("4-3-3", "4-4-2", "3-5-2", "4-2-3-1")) { fmt ->
                    val selected = formation == fmt
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { viewModel.applyFormation(fmt) }
                    ) {
                        Text(
                            text = fmt,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Tool Controls (Move vs Draw Arrow + Add Markers)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (!drawArrowMode) NeonPitchGreen else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { drawArrowMode = false }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.OpenWith, null, tint = if (!drawArrowMode) Color.Black else MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Move Players", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (!drawArrowMode) Color.Black else MaterialTheme.colorScheme.onSurface)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (drawArrowMode) GoldTrophy else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { drawArrowMode = true }
                        .testTag("tactics_draw_arrow_toggle")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Edit, null, tint = if (drawArrowMode) Color.Black else MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Draw Arrow", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (drawArrowMode) Color.Black else MaterialTheme.colorScheme.onSurface)
                    }
                }
                IconButton(onClick = { viewModel.undoLastArrow() }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.AutoMirrored.Filled.Undo, "Undo arrow")
                }
            }
        }

        // Interactive Pitch Canvas
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.74f)
                    .clip(RoundedCornerShape(18.dp))
                    .onSizeChanged { boardSize = it }
                    .pointerInput(drawArrowMode, markers, boardSize) {
                        detectDragGestures(
                            onDragStart = { startOffset ->
                                val w = boardSize.width.toFloat().coerceAtLeast(1f)
                                val h = boardSize.height.toFloat().coerceAtLeast(1f)
                                if (drawArrowMode) {
                                    dragArrowStart = startOffset
                                    dragArrowEnd = startOffset
                                } else {
                                    // Find nearest marker within 55px
                                    val hit = markers.minByOrNull { m ->
                                        hypot((m.xFraction * w) - startOffset.x, (m.yFraction * h) - startOffset.y)
                                    }
                                    if (hit != null && hypot((hit.xFraction * w) - startOffset.x, (hit.yFraction * h) - startOffset.y) < 75f) {
                                        activeMarkerId = hit.id
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val w = boardSize.width.toFloat().coerceAtLeast(1f)
                                val h = boardSize.height.toFloat().coerceAtLeast(1f)
                                if (drawArrowMode) {
                                    dragArrowEnd = change.position
                                } else if (activeMarkerId != null) {
                                    val m = markers.find { it.id == activeMarkerId }
                                    if (m != null) {
                                        viewModel.moveTacticMarker(
                                            markerId = m.id,
                                            newX = ((m.xFraction * w) + dragAmount.x) / w,
                                            newY = ((m.yFraction * h) + dragAmount.y) / h
                                        )
                                    }
                                }
                            },
                            onDragEnd = {
                                val w = boardSize.width.toFloat().coerceAtLeast(1f)
                                val h = boardSize.height.toFloat().coerceAtLeast(1f)
                                if (drawArrowMode && dragArrowStart != null && dragArrowEnd != null) {
                                    viewModel.addTacticArrow(
                                        startX = dragArrowStart!!.x / w,
                                        startY = dragArrowStart!!.y / h,
                                        endX = dragArrowEnd!!.x / w,
                                        endY = dragArrowEnd!!.y / h
                                    )
                                }
                                dragArrowStart = null
                                dragArrowEnd = null
                                activeMarkerId = null
                            }
                        )
                    }
                    .testTag("tactics_pitch_canvas")
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Striped turf pitch
                    drawRect(Brush.verticalGradient(listOf(Color(0xFF0A361C), Color(0xFF072514))))
                    val bandH = h / 8f
                    for (i in 0 until 8 step 2) {
                        drawRect(Color(0xFF104927).copy(alpha = 0.45f), Offset(0f, i * bandH), Size(w, bandH))
                    }

                    val lineColor = Color.White.copy(alpha = 0.65f)
                    val lineStroke = Stroke(width = 2.dp.toPx())
                    // Outer boundary
                    drawRect(lineColor, Offset(14.dp.toPx(), 14.dp.toPx()), Size(w - 28.dp.toPx(), h - 28.dp.toPx()), style = lineStroke)
                    // Halfway line & center circle
                    drawLine(lineColor, Offset(14.dp.toPx(), h / 2f), Offset(w - 14.dp.toPx(), h / 2f), strokeWidth = 2.dp.toPx())
                    drawCircle(lineColor, radius = 44.dp.toPx(), center = Offset(w / 2f, h / 2f), style = lineStroke)
                    drawCircle(lineColor, radius = 3.dp.toPx(), center = Offset(w / 2f, h / 2f))
                    // Top & Bottom Penalty Boxes
                    drawRect(lineColor, Offset(w * 0.22f, 14.dp.toPx()), Size(w * 0.56f, h * 0.16f), style = lineStroke)
                    drawRect(lineColor, Offset(w * 0.22f, h - 14.dp.toPx() - h * 0.16f), Size(w * 0.56f, h * 0.16f), style = lineStroke)

                    // Saved Tactical Arrows
                    arrows.forEach { a ->
                        drawTacticArrow(Offset(a.startX * w, a.startY * h), Offset(a.endX * w, a.endY * h), GoldTrophy)
                    }
                    if (dragArrowStart != null && dragArrowEnd != null) {
                        drawTacticArrow(dragArrowStart!!, dragArrowEnd!!, GoldTrophy)
                    }

                    // Player & Ball Markers
                    markers.forEach { m ->
                        drawBoardMarker(m, Offset(m.xFraction * w, m.yFraction * h), textMeasurer)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { viewModel.addTacticMarker("H", "HOME") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+ Home", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { viewModel.addTacticMarker("OPP", "AWAY") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+ Opponent", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { viewModel.clearTacticArrows() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear Arrows", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showSaveDialog) {
        var nameInput by remember { mutableStateOf("$formation Tactic") }
        var notesInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Tactic Locally", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = nameInput, onValueChange = { nameInput = it }, label = { Text("Tactic Name") }, singleLine = true)
                    OutlinedTextField(value = notesInput, onValueChange = { notesInput = it }, label = { Text("Coaching Notes") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.saveCurrentTactic(nameInput, notesInput)
                    showSaveDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showLoadDialog) {
        AlertDialog(
            onDismissRequest = { showLoadDialog = false },
            title = { Text("Saved Tactics (${savedTactics.size})", fontWeight = FontWeight.Bold) },
            text = {
                if (savedTactics.isEmpty()) {
                    Text("No saved tactics yet.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        savedTactics.forEach { t ->
                            StadiumCard(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    viewModel.loadSavedTactic(t)
                                    showLoadDialog = false
                                }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(t.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("${t.formation}${if (t.notes.isNotBlank()) " • ${t.notes}" else ""}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    IconButton(onClick = { viewModel.deleteSavedTactic(t) }) {
                                        Icon(Icons.Default.DeleteOutline, "Delete", tint = CrimsonOpponent)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLoadDialog = false }) { Text("Close") }
            }
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTacticArrow(
    start: Offset,
    end: Offset,
    color: Color
) {
    drawLine(color = color, start = start, end = end, strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
    val angle = atan2((end.y - start.y).toDouble(), (end.x - start.x).toDouble())
    val headLen = 11.dp.toPx()
    val a1 = angle + Math.toRadians(148.0)
    val a2 = angle - Math.toRadians(148.0)
    drawLine(color, end, Offset(end.x + (cos(a1) * headLen).toFloat(), end.y + (sin(a1) * headLen).toFloat()), 3.dp.toPx(), StrokeCap.Round)
    drawLine(color, end, Offset(end.x + (cos(a2) * headLen).toFloat(), end.y + (sin(a2) * headLen).toFloat()), 3.dp.toPx(), StrokeCap.Round)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBoardMarker(
    marker: TacticMarker,
    center: Offset,
    textMeasurer: TextMeasurer
) {
    if (marker.team == "BALL") {
        drawCircle(color = Color.White, radius = 9.dp.toPx(), center = center)
        drawCircle(color = Color.Black, radius = 4.dp.toPx(), center = center)
        return
    }
    val fillColor = if (marker.team == "AWAY") CrimsonOpponent else NeonPitchGreen
    val textColor = if (marker.team == "AWAY") Color.White else Color.Black
    drawCircle(color = Color.Black.copy(alpha = 0.45f), radius = 16.dp.toPx(), center = center)
    drawCircle(color = fillColor, radius = 14.dp.toPx(), center = center)
    drawCircle(color = Color.White, radius = 14.dp.toPx(), center = center, style = Stroke(width = 1.5.dp.toPx()))

    val measured = textMeasurer.measure(
        text = marker.label.take(3),
        style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = textColor)
    )
    drawText(
        textLayoutResult = measured,
        topLeft = Offset(center.x - measured.size.width / 2f, center.y - measured.size.height / 2f)
    )
}
