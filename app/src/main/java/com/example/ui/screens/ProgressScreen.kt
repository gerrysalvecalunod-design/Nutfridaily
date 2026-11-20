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
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.ui.components.BotanicalCard
import com.example.ui.components.WeeklyHabitBarChart
import com.example.ui.theme.CitrusAmber
import com.example.ui.theme.CitrusLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ForestDeep
import com.example.ui.theme.HydrationBlue
import com.example.ui.theme.HydrationLight
import com.example.ui.theme.MintContainerLight
import com.example.ui.theme.VioletLight
import com.example.ui.theme.VioletWeight
import com.example.viewmodel.NutriFitViewModel
import java.util.Locale

private enum class TimelineFilter(val label: String) {
    ALL("All"),
    MEALS("Meals"),
    WATER("Water"),
    WEIGHT("Weight"),
    ACTIVITY("Walk & Jog")
}

private data class ProgressTimelineItem(
    val id: String,
    val category: TimelineFilter,
    val title: String,
    val subtitle: String,
    val badge: String,
    val dateKey: String,
    val timestamp: Long
)

@Composable
fun ProgressScreen(
    viewModel: NutriFitViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val weeklyProgress by viewModel.weeklyProgress.collectAsStateWithLifecycle()
    val allMeals by viewModel.allMeals.collectAsStateWithLifecycle()
    val allWaterLogs by viewModel.allWaterLogs.collectAsStateWithLifecycle()
    val weightLogs by viewModel.weightLogs.collectAsStateWithLifecycle()
    val allActivities by viewModel.allActivities.collectAsStateWithLifecycle()

    var selectedFilter by remember { mutableStateOf(TimelineFilter.ALL) }

    val totalMeals = allMeals.size
    val healthyMealsPct = if (totalMeals > 0) (allMeals.count { it.isHealthyChoice } * 100) / totalMeals else 0
    val avgDailyWater = if (weeklyProgress.isNotEmpty()) {
        weeklyProgress.sumOf { it.waterMl } / weeklyProgress.size
    } else 0
    val totalWorkouts = allActivities.size
    val totalDistance = allActivities.sumOf { it.distanceKm.toDouble() }.toFloat()

    val latestWeight = weightLogs.firstOrNull()?.weightKg ?: (profile?.startWeightKg ?: 70.0f)
    val startWeight = profile?.startWeightKg ?: (weightLogs.lastOrNull()?.weightKg ?: latestWeight)
    val weightDelta = latestWeight - startWeight
    val weightDeltaSign = if (weightDelta > 0f) "+" else ""
    val streakDays = weeklyProgress.count { it.habitsMetCount >= 2 }

    val timelineItems = remember(allMeals, allWaterLogs, weightLogs, allActivities, selectedFilter) {
        val combined = buildList {
            if (selectedFilter == TimelineFilter.ALL || selectedFilter == TimelineFilter.MEALS) {
                allMeals.forEach { meal ->
                    add(
                        ProgressTimelineItem(
                            id = "meal_${meal.id}",
                            category = TimelineFilter.MEALS,
                            title = meal.foodName,
                            subtitle = "${meal.mealType.lowercase().replaceFirstChar { it.uppercase() }} • ${DateUtils.formatDisplayDate(meal.dateKey)}",
                            badge = "${meal.calories} kcal",
                            dateKey = meal.dateKey,
                            timestamp = meal.timestamp
                        )
                    )
                }
            }
            if (selectedFilter == TimelineFilter.ALL || selectedFilter == TimelineFilter.WATER) {
                allWaterLogs.forEach { water ->
                    add(
                        ProgressTimelineItem(
                            id = "water_${water.id}",
                            category = TimelineFilter.WATER,
                            title = water.label,
                            subtitle = DateUtils.formatDisplayDate(water.dateKey),
                            badge = "+${water.amountMl} ml",
                            dateKey = water.dateKey,
                            timestamp = water.timestamp
                        )
                    )
                }
            }
            if (selectedFilter == TimelineFilter.ALL || selectedFilter == TimelineFilter.WEIGHT) {
                weightLogs.forEach { w ->
                    add(
                        ProgressTimelineItem(
                            id = "weight_${w.id}",
                            category = TimelineFilter.WEIGHT,
                            title = "Weight Check-in: ${String.format(Locale.US, "%.1f", w.weightKg)} kg",
                            subtitle = "${DateUtils.formatDisplayDate(w.dateKey)}${if (w.note.isNotBlank()) " • ${w.note}" else ""}",
                            badge = "${String.format(Locale.US, "%.1f", w.weightKg)} kg",
                            dateKey = w.dateKey,
                            timestamp = w.timestamp
                        )
                    )
                }
            }
            if (selectedFilter == TimelineFilter.ALL || selectedFilter == TimelineFilter.ACTIVITY) {
                allActivities.forEach { act ->
                    val label = if (act.activityType == "JOGGING") "Jogging" else "Walking"
                    add(
                        ProgressTimelineItem(
                            id = "act_${act.id}",
                            category = TimelineFilter.ACTIVITY,
                            title = "$label • ${act.durationMinutes} min",
                            subtitle = "${DateUtils.formatDisplayDate(act.dateKey)} • ${String.format(Locale.US, "%.1f", act.distanceKm)} km • ${act.steps} steps",
                            badge = "${act.caloriesBurned} kcal",
                            dateKey = act.dateKey,
                            timestamp = act.timestamp
                        )
                    )
                }
            }
        }
        combined.sortedWith(compareByDescending<ProgressTimelineItem> { it.dateKey }.thenByDescending { it.timestamp })
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
                Text(
                    text = "Progress & Habits",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Review your healthy consistency and activity history",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 7-Day Weekly Consistency Bar Chart Card
        item {
            BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "7-Day Habit Consistency",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Meals, hydration & activity goals met each day",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MintContainerLight
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.EmojiEvents, null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$streakDays/7 active",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestDeep
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    WeeklyHabitBarChart(
                        days = weeklyProgress,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Cumulative Summary Cards (2x2)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProgressSummaryCard(
                    title = "Nutrition",
                    value = "$totalMeals meals",
                    detail = "$healthyMealsPct% healthy choices",
                    icon = Icons.Default.Restaurant,
                    accent = EmeraldPrimary,
                    tint = MintContainerLight,
                    modifier = Modifier.weight(1f)
                )
                ProgressSummaryCard(
                    title = "Hydration",
                    value = "$avgDailyWater ml",
                    detail = "7-day daily average",
                    icon = Icons.Default.WaterDrop,
                    accent = HydrationBlue,
                    tint = HydrationLight,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProgressSummaryCard(
                    title = "Walk & Jog",
                    value = "${String.format(Locale.US, "%.1f", totalDistance)} km",
                    detail = "$totalWorkouts recorded sessions",
                    icon = Icons.AutoMirrored.Filled.DirectionsRun,
                    accent = CitrusAmber,
                    tint = CitrusLight,
                    modifier = Modifier.weight(1f)
                )
                ProgressSummaryCard(
                    title = "Weight Shift",
                    value = "$weightDeltaSign${String.format(Locale.US, "%.1f", weightDelta)} kg",
                    detail = "Now ${String.format(Locale.US, "%.1f", latestWeight)} kg",
                    icon = Icons.Default.MonitorWeight,
                    accent = VioletWeight,
                    tint = VioletLight,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("progress_weight_card"),
                    onClick = { viewModel.navigateTo(AppScreen.Weight) }
                )
            }
        }

        // Quick Weight Tracker Access Banner
        item {
            BotanicalCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("open_weight_from_progress"),
                onClick = { viewModel.navigateTo(AppScreen.Weight) }
            ) {
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
                        Icon(Icons.Default.MonitorWeight, null, tint = VioletWeight)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Open Weight Progress & Chart",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "View BMI, trend chart & log new weigh-ins",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Open Weight Tracker",
                        tint = VioletWeight
                    )
                }
            }
        }

        // Filter Chips for Timeline
        item {
            Text(
                text = "Activity & Habit History",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(TimelineFilter.entries) { filter ->
                    val selected = selectedFilter == filter
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (selected) EmeraldPrimary else MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .height(40.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedFilter = filter }
                            .testTag("filter_${filter.name.lowercase()}")
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = filter.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        if (timelineItems.isEmpty()) {
            item {
                BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, null, tint = EmeraldPrimary.copy(alpha = 0.4f), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No records for this category yet", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            items(timelineItems, key = { it.id }) { item ->
                val (icon, tint, bg) = when (item.category) {
                    TimelineFilter.MEALS -> Triple(Icons.Default.Restaurant, EmeraldPrimary, MintContainerLight)
                    TimelineFilter.WATER -> Triple(Icons.Default.WaterDrop, HydrationBlue, HydrationLight)
                    TimelineFilter.WEIGHT -> Triple(Icons.Default.MonitorWeight, VioletWeight, VioletLight)
                    else -> Triple(Icons.AutoMirrored.Filled.DirectionsRun, CitrusAmber, CitrusLight)
                }

                BotanicalCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(bg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = item.category.label, tint = tint, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = item.subtitle,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = bg
                        ) {
                            Text(
                                text = item.badge,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = tint,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressSummaryCard(
    title: String,
    value: String,
    detail: String,
    icon: ImageVector,
    accent: Color,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    BotanicalCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(tint),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = accent, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
            Text(detail, fontSize = 11.sp, color = accent, fontWeight = FontWeight.Medium)
        }
    }
}
