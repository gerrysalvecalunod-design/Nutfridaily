package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.local.DateUtils
import com.example.data.model.PlayerPosition
import com.example.data.model.SkillLevel
import com.example.data.util.SoundAndReminderHelper
import com.example.ui.components.SportyCard
import com.example.ui.theme.AlertCoral
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.StadiumForestDark
import com.example.viewmodel.FootballViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: FootballViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.playerProfile.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val lastAutoSavedAt by viewModel.lastAutoSavedAt.collectAsStateWithLifecycle()
    val resetVersion by viewModel.profileResetVersion.collectAsStateWithLifecycle()

    var initialized by remember(resetVersion) { mutableStateOf(false) }
    var name by remember(resetVersion) { mutableStateOf("") }
    var selectedLevel by remember(resetVersion) { mutableStateOf(SkillLevel.BEGINNER) }
    var selectedPosition by remember(resetVersion) { mutableStateOf(PlayerPosition.ALL_ROUNDER) }
    var trainingGoals by remember(resetVersion) { mutableStateOf("") }
    var preferredDuration by remember(resetVersion) { mutableIntStateOf(30) }
    var showResetDialog by remember { mutableStateOf(false) }

    LaunchedEffect(profile, resetVersion) {
        val p = profile ?: return@LaunchedEffect
        if (!initialized) {
            name = p.playerName
            selectedLevel = SkillLevel.entries.find { it.name == p.skillLevel } ?: SkillLevel.BEGINNER
            selectedPosition = PlayerPosition.entries.find { it.name == p.playerPosition } ?: PlayerPosition.ALL_ROUNDER
            trainingGoals = p.trainingGoals
            preferredDuration = p.preferredDurationMinutes
            initialized = true
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = StadiumForestDark,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(52.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_football_app_icon),
                        contentDescription = "Football Pro Crest",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Player Profile & Settings", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = "Every change auto-saves offline to device SQLite",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Player Profile & Goals Form (Automatic Saving — No Save Button Required!)
        item {
            SportyCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Player Profile & Goals", style = MaterialTheme.typography.titleLarge)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = EmeraldSuccess.copy(alpha = 0.14f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, "Auto-saved", tint = EmeraldSuccess, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (lastAutoSavedAt != null) {
                                        "Saved ${DateUtils.formatTimestampTime(lastAutoSavedAt!!)}"
                                    } else {
                                        "Auto-saved"
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldSuccess
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            viewModel.autoUpdatePlayerName(it)
                        },
                        label = { Text("Player Name") },
                        placeholder = { Text("Enter your name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_name_input")
                    )

                    Text("Skill Level", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        SkillLevel.entries.forEach { lvl ->
                            val selected = lvl == selectedLevel
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedLevel = lvl
                                        viewModel.autoUpdateSkillLevel(lvl)
                                    }
                                    .testTag("profile_level_${lvl.name.lowercase()}")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = lvl.displayName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Text("Preferred Position", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PlayerPosition.entries.forEach { pos ->
                            val selected = pos == selectedPosition
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedPosition = pos
                                        viewModel.autoUpdatePosition(pos)
                                    }
                            ) {
                                Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = pos.displayName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Text("Preferred Training Duration", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf(15, 20, 30, 45, 60).forEach { mins ->
                            val selected = preferredDuration == mins
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        preferredDuration = mins
                                        viewModel.autoUpdatePreferredDuration(mins)
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${mins}m",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Text("Weekly Session Target", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf(3, 4, 5, 6, 7).forEach { target ->
                            val selected = settings.weeklySessionsGoal == target
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { viewModel.setWeeklySessionsGoal(target) }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${target}x",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = trainingGoals,
                        onValueChange = {
                            trainingGoals = it
                            viewModel.autoUpdateTrainingGoals(it)
                        },
                        label = { Text("Personal Training Goals") },
                        minLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_goals_input")
                    )
                }
            }
        }

        // Sound Notifications & App Appearance
        item {
            SportyCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("Sound & Appearance", style = MaterialTheme.typography.titleLarge)

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VolumeUp, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Timer Sound Notifications", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Play audio cues on timer start, rest & finish", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.soundEnabled,
                            onCheckedChange = { viewModel.setSoundEnabled(it) },
                            modifier = Modifier.testTag("profile_sound_switch")
                        )
                    }

                    if (settings.soundEnabled) {
                        Text("Sound Cue Style (Tap to Preview)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            SoundAndReminderHelper.soundStyleNames.forEachIndexed { idx, label ->
                                val selected = settings.soundToneIndex == idx
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { viewModel.setSoundToneIndex(idx) }
                                ) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 4.dp)) {
                                        Text(
                                            text = label,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DarkMode, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Stadium Night Dark Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Switch between Chalk White & Floodlight Turf themes", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.isDarkTheme,
                            onCheckedChange = { viewModel.setDarkTheme(it) },
                            modifier = Modifier.testTag("profile_dark_switch")
                        )
                    }
                }
            }
        }

        // Reset All Saved Data with Confirmation
        item {
            SportyCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Offline SQLite Storage & Reset", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "Your player name, position, skill profile, completed drills, achievements, streaks, and schedules stay saved on your phone across restarts — 100% offline.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = { showResetDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AlertCoral),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("profile_reset_progress_button")
                    ) {
                        Icon(Icons.Default.DeleteForever, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset All Saved Data", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset All Saved Information?", fontWeight = FontWeight.Bold) },
            text = {
                Text("This permanently clears your player name, profile, training history, achievements, personal records, active timer checkpoint, and planned workouts from local storage.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResetDialog = false
                        viewModel.resetAllTrainingProgress()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AlertCoral),
                    modifier = Modifier.testTag("confirm_reset_progress_button")
                ) {
                    Text("Reset Everything", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }
}
