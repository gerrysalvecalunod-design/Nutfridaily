package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppLanguage
import com.example.data.model.DifficultyLevel
import com.example.data.model.PlayerPosition
import com.example.data.model.PreferredFoot
import com.example.data.util.SoundAndReminderHelper
import com.example.ui.components.StadiumCard
import com.example.ui.theme.CrimsonOpponent
import com.example.ui.theme.LocalAppStrings
import com.example.viewmodel.FootballViewModel

@Composable
fun ProfileAndSettingsScreen(
    viewModel: FootballViewModel,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val profile by viewModel.playerProfile.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var name by remember(profile) { mutableStateOf(profile?.playerName ?: "Jordan Silva") }
    var age by remember(profile) { mutableStateOf((profile?.age ?: 19).toString()) }
    var heightCm by remember(profile) { mutableStateOf((profile?.heightCm ?: 176).toString()) }
    var weightKg by remember(profile) { mutableStateOf(String.format("%.1f", profile?.weightKg ?: 70.0f)) }
    var position by remember(profile) {
        mutableStateOf(PlayerPosition.entries.find { it.name == profile?.playingPosition } ?: PlayerPosition.CENTRAL_MID)
    }
    var foot by remember(profile) {
        mutableStateOf(PreferredFoot.entries.find { it.name == profile?.preferredFoot } ?: PreferredFoot.RIGHT)
    }
    var level by remember(profile) {
        mutableStateOf(DifficultyLevel.entries.find { it.name == profile?.experienceLevel } ?: DifficultyLevel.INTERMEDIATE)
    }
    var goals by remember(profile) { mutableStateOf(profile?.personalGoals ?: "") }

    var profileSavedToast by remember { mutableStateOf(false) }
    var notifStatusMsg by remember { mutableStateOf<String?>(null) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        notifStatusMsg = if (granted) {
            val sent = SoundAndReminderHelper.showTrainingReminderNotification(
                context = context,
                title = "Football Training",
                message = "Time for your daily football training session!"
            )
            if (sent) "Training reminder notification sent!" else "Permission granted."
        } else {
            "Notification permission denied."
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
                Text(strings.settingsScreenTitle, style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = strings.settingsScreenSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Application Settings Card (Language, Goals, Theme, Sound, Reminders, Data)
        item {
            StadiumCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(strings.appSettingsCardTitle, style = MaterialTheme.typography.titleLarge)

                    // Language Setting (English / Tagalog)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(strings.languageSettingTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(strings.languageSettingSubtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AppLanguage.entries.forEach { lang ->
                                val selected = settings.appLanguage == lang.code
                                val label = if (lang == AppLanguage.ENGLISH) strings.englishOption else strings.tagalogOption
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { viewModel.setAppLanguage(lang.code) }
                                        .testTag("lang_option_${lang.code}")
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

                    // Training Goals Quick Access
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.navigateTo(AppScreen.Goals) }
                            .padding(vertical = 4.dp)
                            .testTag("settings_open_goals"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Flag, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(strings.goalTargetsSettingTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = "${settings.dailyMinutesGoal} min/day • ${settings.weeklySessionsGoal}x/week",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = strings.openGoalsButton,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }
                    }

                    // Dark / Light Theme
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.DarkMode, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(strings.darkThemeTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(strings.darkThemeSubtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.isDarkTheme,
                            onCheckedChange = { viewModel.setDarkTheme(it) },
                            modifier = Modifier.testTag("switch_dark_theme")
                        )
                    }

                    // Sound Settings
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VolumeUp, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(strings.soundTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(strings.soundSubtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.soundEnabled,
                            onCheckedChange = { viewModel.setSoundEnabled(it) },
                            modifier = Modifier.testTag("switch_sound")
                        )
                    }

                    // Reminder Settings
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.NotificationsActive, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(strings.remindersTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("${strings.remindersSubtitlePrefix} ${settings.reminderTimeLabel}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = settings.reminderEnabled,
                            onCheckedChange = { viewModel.setReminderEnabled(it) },
                            modifier = Modifier.testTag("switch_reminders")
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                val sent = SoundAndReminderHelper.showTrainingReminderNotification(
                                    context = context,
                                    title = "Football Training",
                                    message = "Time for your daily football training session!"
                                )
                                notifStatusMsg = if (sent) "Training reminder notification sent!" else "Enable notifications in system settings."
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Text(strings.testReminderButton, fontWeight = FontWeight.SemiBold)
                    }

                    if (notifStatusMsg != null) {
                        Text(notifStatusMsg!!, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    }

                    // About & Clear Local Data
                    OutlinedButton(
                        onClick = { showAboutDialog = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Icon(Icons.Default.Info, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.aboutAppButton, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = { showClearConfirmDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonOpponent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("clear_all_data_button")
                    ) {
                        Icon(Icons.Default.DeleteForever, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.clearDataButton, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Player Profile Form
        item {
            StadiumCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(strings.playerBioTitle, style = MaterialTheme.typography.titleLarge)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(strings.playerNameLabel) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text(strings.ageLabel) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = heightCm,
                            onValueChange = { heightCm = it },
                            label = { Text(strings.heightLabel) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = weightKg,
                            onValueChange = { weightKg = it },
                            label = { Text(strings.weightLabel) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Text(strings.playingPositionLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(PlayerPosition.entries) { pos ->
                            val selected = pos == position
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { position = pos }
                            ) {
                                Text(
                                    text = "${pos.shortCode} • ${pos.displayName}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                                )
                            }
                        }
                    }

                    Text(strings.preferredFootLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PreferredFoot.entries.forEach { f ->
                            val selected = f == foot
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f).clickable { foot = f }
                            ) {
                                Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = f.displayName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Text(strings.experienceLevelLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DifficultyLevel.entries.forEach { lvl ->
                            val selected = lvl == level
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f).clickable { level = lvl }
                            ) {
                                Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = lvl.displayName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = goals,
                        onValueChange = { goals = it },
                        label = { Text(strings.personalGoalsFieldLabel) },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (profileSavedToast) {
                        Text(
                            text = strings.profileSavedMsg,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.savePlayerProfile(
                                name = name,
                                age = age.toIntOrNull() ?: 19,
                                heightCm = heightCm.toIntOrNull() ?: 176,
                                weightKg = weightKg.toFloatOrNull() ?: 70.0f,
                                position = position,
                                foot = foot,
                                level = level,
                                goals = goals
                            )
                            profileSavedToast = true
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_profile_button")
                    ) {
                        Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.saveProfileButton, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = { Text("Clear All Local Data?", fontWeight = FontWeight.Bold) },
            text = {
                Text("This permanently erases your player profile, completed drills, personal records, saved tactics, planner workouts, and program progress from this device.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showClearConfirmDialog = false
                        viewModel.clearAllLocalData()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonOpponent),
                    modifier = Modifier.testTag("confirm_clear_data_button")
                ) {
                    Text("Erase Everything", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("Football Training", fontWeight = FontWeight.Bold) },
            text = {
                Text("Version 1.0 • 100% Offline Football Training Companion.\n\nIncludes 30 skill lessons, 24 tiered fitness drills, 16 position-specific drills, 7/30/90-day programs, timers, planner, interactive tactics board, and local Room/DataStore persistence.")
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) { Text("Close") }
            }
        )
    }
}
