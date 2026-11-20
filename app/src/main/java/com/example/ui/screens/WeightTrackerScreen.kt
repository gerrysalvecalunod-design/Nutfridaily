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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.DateUtils
import com.example.ui.components.BotanicalCard
import com.example.ui.components.WeightTrendChart
import com.example.ui.theme.CitrusAmber
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MintContainerLight
import com.example.ui.theme.VioletLight
import com.example.ui.theme.VioletWeight
import com.example.viewmodel.NutriFitViewModel

@Composable
fun WeightTrackerScreen(
    viewModel: NutriFitViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val weightLogs by viewModel.weightLogs.collectAsStateWithLifecycle()

    var showRecordDialog by remember { mutableStateOf(false) }
    var showTargetDialog by remember { mutableStateOf(false) }

    val latestWeight = weightLogs.firstOrNull()?.weightKg ?: (profile?.startWeightKg ?: 70.0f)
    val startWeight = profile?.startWeightKg ?: (weightLogs.lastOrNull()?.weightKg ?: latestWeight)
    val targetWeight = profile?.targetWeightKg ?: 66.0f
    val totalDelta = latestWeight - startWeight
    val heightMeters = (profile?.heightCm ?: 170f) / 100f
    val bmi = if (heightMeters > 0f) latestWeight / (heightMeters * heightMeters) else 22f

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Weight Tracker",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "Record weight & monitor progress toward your target",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Summary Stats Card
            item {
                BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Current Weight", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = "${String.format("%.1f", latestWeight)} kg",
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VioletWeight
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (totalDelta <= 0f) MintContainerLight else Color(0xFFFEF3C7)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (totalDelta <= 0f) Icons.Default.TrendingDown else Icons.Default.TrendingUp,
                                        contentDescription = null,
                                        tint = if (totalDelta <= 0f) EmeraldPrimary else CitrusAmber,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    val sign = if (totalDelta > 0) "+" else ""
                                    Text(
                                        text = "${sign}${String.format("%.1f", totalDelta)} kg",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (totalDelta <= 0f) EmeraldPrimary else CitrusAmber
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            WeightStatColumn("Start", "${String.format("%.1f", startWeight)} kg")
                            WeightStatColumn("Target", "${String.format("%.1f", targetWeight)} kg", onEdit = { showTargetDialog = true })
                            WeightStatColumn("BMI Est.", String.format("%.1f", bmi))
                        }
                    }
                }
            }

            // Trend Chart Card
            item {
                BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Progress Trend",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        WeightTrendChart(
                            logs = weightLogs,
                            targetWeightKg = targetWeight,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // History Header
            item {
                Text(
                    text = "Weight History (${weightLogs.size})",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (weightLogs.isEmpty()) {
                item {
                    BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.MonitorWeight, null, tint = VioletWeight.copy(alpha = 0.4f), modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No weight records yet", fontWeight = FontWeight.Bold)
                            Text("Tap + Record Weight to start tracking changes.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                itemsIndexed(weightLogs, key = { _, item -> item.id }) { index, log ->
                    val prev = weightLogs.getOrNull(index + 1)
                    val diff = if (prev != null) log.weightKg - prev.weightKg else 0f
                    BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(VioletLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.MonitorWeight, null, tint = VioletWeight, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${String.format("%.1f", log.weightKg)} kg",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = DateUtils.formatDisplayDate(log.dateKey),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (log.note.isNotBlank()) {
                                    Text(
                                        text = log.note,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            if (prev != null) {
                                val diffSign = if (diff > 0) "+" else ""
                                Text(
                                    text = "${diffSign}${String.format("%.1f", diff)} kg",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (diff <= 0f) EmeraldPrimary else CitrusAmber
                                )
                            }
                            IconButton(onClick = { viewModel.deleteWeightLog(log) }) {
                                Icon(Icons.Default.DeleteOutline, "Delete", tint = DangerRed.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showRecordDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("record_weight_fab"),
            containerColor = VioletWeight,
            contentColor = Color.White
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Record Weight", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showRecordDialog) {
        var weightInput by remember { mutableStateOf(String.format("%.1f", latestWeight)) }
        var noteInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showRecordDialog = false },
            title = { Text("Record Today's Weight", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = { Text("Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("weight_input_field")
                    )
                    OutlinedTextField(
                        value = noteInput,
                        onValueChange = { noteInput = it },
                        label = { Text("Note (optional)") },
                        placeholder = { Text("E.g. Morning weigh-in before breakfast") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val kg = weightInput.toFloatOrNull()
                        if (kg != null && kg > 20f) {
                            viewModel.addWeight(kg, noteInput)
                            showRecordDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VioletWeight),
                    modifier = Modifier.testTag("save_weight_button")
                ) {
                    Text("Save Weight", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRecordDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showTargetDialog) {
        var targetInput by remember { mutableStateOf(String.format("%.1f", targetWeight)) }
        AlertDialog(
            onDismissRequest = { showTargetDialog = false },
            title = { Text("Update Target Weight", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = targetInput,
                    onValueChange = { targetInput = it },
                    label = { Text("Target Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val t = targetInput.toFloatOrNull()
                        if (t != null && t > 25f) {
                            viewModel.updateTargetWeight(t)
                            showTargetDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Save Target", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTargetDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun WeightStatColumn(
    label: String,
    value: String,
    onEdit: (() -> Unit)? = null
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (onEdit != null) {
                IconButton(onClick = onEdit, modifier = Modifier.size(20.dp)) {
                    Icon(Icons.Default.Edit, "Edit target", tint = EmeraldPrimary, modifier = Modifier.size(13.dp))
                }
            }
        }
        Text(value, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}
