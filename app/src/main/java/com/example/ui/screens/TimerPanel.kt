package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CircularProgressRing
import com.example.ui.components.SportyCard
import com.example.ui.theme.AmberTimer
import com.example.ui.theme.EmeraldSuccess
import com.example.viewmodel.FootballViewModel
import com.example.viewmodel.TimerPhase
import java.util.Locale

@Composable
fun InteractiveTimerPanel(
    viewModel: FootballViewModel
) {
    val state by viewModel.timerState.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    val ringColor = when (state.phase) {
        TimerPhase.WORK -> MaterialTheme.colorScheme.primary
        TimerPhase.REST -> AmberTimer
        TimerPhase.FINISHED -> EmeraldSuccess
    }
    val progress = if (state.totalPhaseSeconds > 0) {
        state.remainingSeconds.toFloat() / state.totalPhaseSeconds.toFloat()
    } else 0f

    val mins = state.remainingSeconds / 60
    val secs = state.remainingSeconds % 60
    val currentEx = state.currentExercise

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SportyCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (state.queue.isNotEmpty()) {
                                "DRILL ${state.currentIndex + 1} OF ${state.queue.size}"
                            } else "CUSTOM TIMER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.setSoundEnabled(!settings.soundEnabled) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (settings.soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                    contentDescription = "Toggle Sound Cues",
                                    tint = if (settings.soundEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (settings.soundEnabled) "Sound On" else "Muted",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Text(
                        text = currentEx?.name ?: state.customTitle,
                        style = MaterialTheme.typography.titleLarge
                    )
                    if (currentEx != null) {
                        Text(
                            text = "${currentEx.recommendedReps} • Default Rest ${currentEx.restTimeSeconds}s",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            SportyCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ringColor.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = when (state.phase) {
                                TimerPhase.WORK -> "TRAINING COUNTDOWN"
                                TimerPhase.REST -> "REST TIMER"
                                TimerPhase.FINISHED -> "SESSION COMPLETE"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ringColor,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    CircularProgressRing(
                        progress = progress,
                        color = ringColor,
                        strokeWidth = 12.dp,
                        modifier = Modifier.size(190.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = String.format(Locale.US, "%02d:%02d", mins, secs),
                                fontSize = 46.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.testTag("timer_countdown_text")
                            )
                            Text(
                                text = if (state.isRunning) "Running" else if (state.hasStarted) "Paused" else "Ready",
                                fontSize = 12.sp,
                                color = ringColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (state.lastNotificationText != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NotificationsActive, null, tint = ringColor, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = state.lastNotificationText!!,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (state.isRunning) viewModel.pauseTimer()
                                else viewModel.startOrResumeTimer()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.isRunning) AmberTimer else MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(50.dp)
                                .testTag("timer_start_pause_resume")
                        ) {
                            Icon(
                                imageVector = if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (state.isRunning) Color.Black else MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when {
                                    state.isRunning -> "Pause"
                                    state.hasStarted && state.phase != TimerPhase.FINISHED -> "Resume"
                                    else -> "Start"
                                },
                                fontWeight = FontWeight.Bold,
                                color = if (state.isRunning) Color.Black else MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.resetTimer() },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(0.9f)
                                .height(50.dp)
                                .testTag("timer_reset_button")
                        ) {
                            Icon(Icons.Default.Refresh, "Reset", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.startRestTimerNow(currentEx?.restTimeSeconds ?: 30) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("timer_rest_button")
                        ) {
                            Icon(Icons.Default.Timer, null, tint = AmberTimer, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("Rest Timer", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.nextExerciseInTimer() },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("timer_next_exercise_button")
                        ) {
                            Icon(Icons.Default.SkipNext, null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(19.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (state.hasNextExercise) "Next Drill" else "Finish & Log",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            SportyCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Quick Countdown Duration", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf(30 to "30s", 45 to "45s", 60 to "1m", 90 to "90s", 180 to "3m").forEach { (sec, lbl) ->
                            val active = state.totalPhaseSeconds == sec && state.phase == TimerPhase.WORK
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { viewModel.setCustomCountdownSeconds(sec) }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = lbl,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
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
