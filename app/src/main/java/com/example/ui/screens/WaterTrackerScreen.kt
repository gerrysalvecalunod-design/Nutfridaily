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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.data.local.DateUtils
import com.example.data.util.ReminderHelper
import com.example.ui.components.BotanicalCard
import com.example.ui.components.DateSelectorStrip
import com.example.ui.components.HydrationWaveCup
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.HydrationBlue
import com.example.ui.theme.HydrationLight
import com.example.viewmodel.NutriFitViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WaterTrackerScreen(
    viewModel: NutriFitViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val selectedDateKey by viewModel.selectedDateKey.collectAsStateWithLifecycle()
    val waterLogs by viewModel.waterLogsForSelectedDate.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showCustomDialog by remember { mutableStateOf(false) }
    var reminderFeedback by remember { mutableStateOf<String?>(null) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            ReminderHelper.showHealthReminderNotification(
                context,
                "Hydration Reminder Active",
                "Remember to drink 250 ml of water to stay on track with your daily goal!"
            )
            reminderFeedback = "Notification reminder sent!"
        } else {
            reminderFeedback = "In-app hydration reminders remain active."
        }
    }

    val totalMl = waterLogs.sumOf { it.amountMl }
    val goalMl = profile?.dailyWaterGoalMl ?: 2250
    val reminderEnabled = profile?.waterReminderEnabled ?: true
    val reminderInterval = profile?.waterReminderIntervalHours ?: 2

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Water Tracker",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Stay hydrated with daily intake logging & gentle reminders",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            DateSelectorStrip(
                selectedDateKey = selectedDateKey,
                onPreviousDay = { viewModel.stepDate(-1) },
                onNextDay = { viewModel.stepDate(1) },
                onJumpToday = { viewModel.selectDate(DateUtils.todayKey()) }
            )
        }

        // Hydration Animated Wave Card
        item {
            BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HydrationWaveCup(
                        progress = totalMl.toFloat() / goalMl,
                        currentMl = totalMl,
                        goalMl = goalMl,
                        modifier = Modifier.size(190.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val remaining = (goalMl - totalMl).coerceAtLeast(0)
                    Text(
                        text = if (remaining == 0)
                            "Daily Hydration Goal Achieved!"
                        else
                            "$remaining ml remaining (~${(remaining + 249) / 250} glasses)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (remaining == 0) EmeraldPrimary else HydrationBlue
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Add Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QuickWaterButton("+150 ml", "Cup", Modifier.weight(1f).testTag("water_add_150")) {
                            viewModel.addWater(150, "Small Cup (150 ml)")
                        }
                        QuickWaterButton("+250 ml", "Glass", Modifier.weight(1f).testTag("water_add_250")) {
                            viewModel.addWater(250, "Glass (250 ml)")
                        }
                        QuickWaterButton("+500 ml", "Bottle", Modifier.weight(1f).testTag("water_add_500")) {
                            viewModel.addWater(500, "Water Bottle (500 ml)")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { showCustomDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, null, tint = HydrationBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Log Custom Water Amount", color = HydrationBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Water Reminders Settings Card
        item {
            BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(HydrationLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.NotificationsActive, null, tint = HydrationBlue, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Water Reminders", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Every $reminderInterval hours (offline schedule)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Switch(
                            checked = reminderEnabled,
                            onCheckedChange = { viewModel.toggleWaterReminder(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = HydrationBlue)
                        )
                    }

                    if (reminderEnabled) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(1 to "Every 1h", 2 to "Every 2h", 3 to "Every 3h").forEach { (hrs, label) ->
                                val selected = reminderInterval == hrs
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.setWaterReminderInterval(hrs) },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) HydrationBlue else HydrationLight
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) Color.White else HydrationBlue,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val sent = ReminderHelper.showHealthReminderNotification(
                                        context,
                                        "Time to Hydrate!",
                                        "Drink a 250 ml glass of water to keep your energy high."
                                    )
                                    if (!sent) {
                                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        reminderFeedback = "Hydration reminder notification sent!"
                                    }
                                } else {
                                    ReminderHelper.showHealthReminderNotification(
                                        context,
                                        "Time to Hydrate!",
                                        "Drink a 250 ml glass of water to keep your energy high."
                                    )
                                    reminderFeedback = "Hydration reminder notification sent!"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = HydrationBlue)
                        ) {
                            Text("Send Water Reminder Alert", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        if (reminderFeedback != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = reminderFeedback!!,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldPrimary
                            )
                        }
                    }
                }
            }
        }

        // Daily Water History
        item {
            Text(
                text = "Today's Water Entries (${waterLogs.size})",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (waterLogs.isEmpty()) {
            item {
                BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.WaterDrop, null, tint = HydrationBlue.copy(alpha = 0.4f), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No water logged yet", fontWeight = FontWeight.Bold)
                        Text("Tap +250 ml above to log your first glass.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            items(waterLogs, key = { it.id }) { log ->
                val timeStr = remember(log.timestamp) {
                    SimpleDateFormat("h:mm a", Locale.US).format(Date(log.timestamp))
                }
                BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(HydrationLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocalDrink, null, tint = HydrationBlue, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(log.label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(timeStr, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = "+${log.amountMl} ml",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = HydrationBlue
                        )
                        IconButton(onClick = { viewModel.deleteWaterLog(log) }) {
                            Icon(Icons.Default.DeleteOutline, "Remove", tint = DangerRed.copy(alpha = 0.7f))
                        }
                    }
                }
            }
        }
    }

    if (showCustomDialog) {
        var amountStr by remember { mutableStateOf("350") }
        var labelStr by remember { mutableStateOf("Reusable Flask") }
        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            title = { Text("Add Custom Water", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = amountStr,
                        onValueChange = { amountStr = it },
                        label = { Text("Amount (ml)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = labelStr,
                        onValueChange = { labelStr = it },
                        label = { Text("Label (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val ml = amountStr.toIntOrNull() ?: 250
                        viewModel.addWater(ml, labelStr.ifBlank { "Custom ($ml ml)" })
                        showCustomDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HydrationBlue)
                ) {
                    Text("Add Water", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun QuickWaterButton(
    amount: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        color = HydrationLight,
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(amount, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = HydrationBlue)
            Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
