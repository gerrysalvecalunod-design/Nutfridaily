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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.StadiumCard
import com.example.ui.theme.AmberTimer
import com.example.ui.theme.TacticalCyan
import com.example.viewmodel.FootballViewModel

@Composable
fun ProgramsScreen(
    viewModel: FootballViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val completions by viewModel.programCompletions.collectAsStateWithLifecycle()

    var viewingProgramId by remember(settings.activeProgramId) { mutableStateOf(settings.activeProgramId) }
    val program = viewModel.programs.find { it.id == viewingProgramId } ?: viewModel.programs.first()

    val completedDaySet = remember(completions, program.id) {
        completions.filter { it.programId == program.id && it.isCompleted }.map { it.dayNumber }.toSet()
    }
    val completionPct = ((completedDaySet.size.toFloat() / program.totalDays.coerceAtLeast(1)) * 100).toInt().coerceIn(0, 100)

    // Phase/Week filter for 30 & 90 day programs so scrolling stays fast
    val blockCount = ((program.totalDays - 1) / 7) + 1
    var selectedWeekBlock by remember(program.id) { mutableIntStateOf(1) }
    val visibleDays = remember(program, selectedWeekBlock) {
        if (program.totalDays <= 7) program.days
        else {
            val start = (selectedWeekBlock - 1) * 7 + 1
            val end = (start + 6).coerceAtMost(program.totalDays)
            program.days.filter { it.dayNumber in start..end }
        }
    }

    val isActiveProgram = settings.activeProgramId == program.id

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text("Training Programs", style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = "Structured 7-day, 30-day & 90-day offline training schedules",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Program Selector Strip
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.programs.forEach { p ->
                    val selected = p.id == program.id
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                viewingProgramId = p.id
                                selectedWeekBlock = 1
                            }
                            .testTag("select_${p.id}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${p.totalDays}-Day",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Program Overview & Tracker Card
        item {
            StadiumCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                            Text(
                                text = program.badge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = "${completedDaySet.size} / ${program.totalDays} days ($completionPct%)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(program.title, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(program.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(program.weeklyStructureSummary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TacticalCyan)

                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { completionPct / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(9.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { viewModel.selectActiveProgram(program.id) },
                            enabled = !isActiveProgram,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (isActiveProgram) "Active Program" else "Set as Active Program",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (completedDaySet.isNotEmpty()) {
                            OutlinedButton(onClick = { viewModel.resetProgramProgress(program.id) }) {
                                Text("Reset", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Week Switcher for 30 & 90 day programs
        if (blockCount > 1) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items((1..blockCount).toList()) { wk ->
                        val selected = selectedWeekBlock == wk
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            modifier = Modifier.clickable { selectedWeekBlock = wk }
                        ) {
                            Text(
                                text = "Week $wk",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Daily Schedule Cards
        items(visibleDays, key = { "${program.id}_${it.dayNumber}" }) { day ->
            val isDone = completedDaySet.contains(day.dayNumber)
            StadiumCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("program_day_${day.dayNumber}"),
                onClick = { viewModel.toggleProgramDayCompleted(program.id, day.dayNumber, isDone) }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (day.isRestDay) AmberTimer.copy(alpha = 0.18f)
                                    else MaterialTheme.colorScheme.primaryContainer
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (day.isRestDay) Icons.Default.Hotel else Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = if (day.isRestDay) AmberTimer else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Day ${day.dayNumber} • ${day.focusArea}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (day.isRestDay) AmberTimer else MaterialTheme.colorScheme.primary
                            )
                            Text(day.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("${day.durationMinutes} min", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(
                            imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = if (isDone) "Completed" else "Incomplete",
                            tint = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    day.exercises.forEach { ex ->
                        Text(
                            text = "• $ex",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
