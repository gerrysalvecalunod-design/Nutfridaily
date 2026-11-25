package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.data.model.TrainingCategory
import com.example.ui.components.CircularProgressRing
import com.example.ui.components.SportyCard
import com.example.ui.theme.AmberTimer
import com.example.ui.theme.MintEmeraldPrimary
import com.example.ui.theme.PitchMintHighlight
import com.example.ui.theme.StadiumDeepBlack
import com.example.ui.theme.TrophyGold
import com.example.ui.theme.TurfAccentTeal
import com.example.viewmodel.FootballViewModel
import com.example.viewmodel.TrainingSubTab
import java.util.Locale

@Composable
fun HomeDashboardScreen(
    viewModel: FootballViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.playerProfile.collectAsStateWithLifecycle()
    val summary by viewModel.dashboardSummary.collectAsStateWithLifecycle()
    val checkpoint by viewModel.savedCheckpoint.collectAsStateWithLifecycle()
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val dailyPlanItems by viewModel.dailyPlanItems.collectAsStateWithLifecycle()

    val recommended = viewModel.recommendedExerciseForToday()
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val skillLevel = SkillLevel.entries.find { it.name == profile?.skillLevel } ?: SkillLevel.BEGINNER
    val position = PlayerPosition.entries.find { it.name == profile?.playerPosition } ?: PlayerPosition.STRIKER
    val displayPlayerName = profile?.playerName?.trim()?.ifBlank { "Player" } ?: "Player"

    val dailyDrills = dailyPlanItems.filter { !it.isRestSlot }
    val dailyCompletedCount = dailyDrills.count { it.isCompleted }
    val hasActiveCheckpoint = checkpoint != null && checkpoint!!.isActive
    val hasUnfinishedPlan = dailyCompletedCount in 1 until dailyDrills.size
    val showContinue = hasActiveCheckpoint || hasUnfinishedPlan

    val cardAlpha = if (isDark) 0.84f else 0.92f
    val cardBg = MaterialTheme.colorScheme.surface.copy(alpha = cardAlpha)

    Box(modifier = modifier.fillMaxSize()) {
        // Full-Screen Ronaldo #7 Motivational Backdrop
        Image(
            painter = painterResource(id = R.drawable.bg_dashboard_ronaldo),
            contentDescription = "Ronaldo #7 Football Dashboard Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contrast-safe dark pitch gradient scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to StadiumDeepBlack.copy(alpha = if (isDark) 0.42f else 0.30f),
                        0.38f to StadiumDeepBlack.copy(alpha = if (isDark) 0.72f else 0.58f),
                        0.75f to MaterialTheme.colorScheme.background.copy(alpha = if (isDark) 0.88f else 0.85f),
                        1.0f to MaterialTheme.colorScheme.background.copy(alpha = if (isDark) 0.96f else 0.94f)
                    )
                )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 14.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Top Bar: Custom Logo Crest, Player Greeting, Offline Pill & Settings Shortcut
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFFCF3E4),
                                shadowElevation = 8.dp,
                                border = BorderStroke(2.dp, PitchMintHighlight),
                                modifier = Modifier.size(54.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_football_app_icon),
                                    contentDescription = "FOOTBALL PRO Crest",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${DateUtils.formatDisplayDate(DateUtils.todayKey())} • ${position.displayName}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PitchMintHighlight
                                )
                                Text(
                                    text = "Ready, $displayPlayerName!",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Black.copy(alpha = 0.65f),
                                border = BorderStroke(1.dp, PitchMintHighlight.copy(alpha = 0.45f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.WifiOff, "Offline Mode", tint = PitchMintHighlight, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("OFFLINE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.65f),
                                border = BorderStroke(1.dp, PitchMintHighlight.copy(alpha = 0.45f)),
                                modifier = Modifier.size(40.dp)
                            ) {
                                IconButton(
                                    onClick = { viewModel.navigateTo(AppScreen.Settings) },
                                    modifier = Modifier.testTag("home_settings_button")
                                ) {
                                    Icon(Icons.Default.Settings, "Settings", tint = PitchMintHighlight, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }

                    // 2. Spacer letting Ronaldo #7 shine cleanly in upper hero zone
                    Spacer(modifier = Modifier.height(64.dp))

                    // 3. Continue Training OR Start Today's Training Primary Hero Action
                    SportyCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_continue_training_card"),
                        containerColor = Color(0xFF06160D).copy(alpha = 0.88f),
                        borderColor = MintEmeraldPrimary.copy(alpha = 0.75f),
                        onClick = { viewModel.continueOrStartTodaysTraining() }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MintEmeraldPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (showContinue) Icons.Default.Restore else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color(0xFF021409),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (showContinue) "CONTINUE TRAINING" else "TODAY'S PERSONALIZED PLAN",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = PitchMintHighlight,
                                        letterSpacing = 1.sp
                                    )
                                    val sub = when {
                                        hasActiveCheckpoint -> {
                                            val mins = timerState.remainingSeconds / 60
                                            val secs = timerState.remainingSeconds % 60
                                            val fmt = String.format(Locale.US, "%02d:%02d", mins, secs)
                                            "${timerState.customTitle} • Drill ${timerState.currentIndex + 1}/${timerState.queue.size.coerceAtLeast(1)} ($fmt left)"
                                        }
                                        hasUnfinishedPlan -> "$dailyCompletedCount/${dailyDrills.size} drills done today • Resume remaining drills"
                                        else -> "${skillLevel.displayName} • ${position.displayName} • ${profile?.preferredDurationMinutes ?: 30} min plan ready"
                                    }
                                    Text(
                                        text = sub,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        maxLines = 2
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.continueOrStartTodaysTraining() },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MintEmeraldPrimary,
                                        contentColor = Color(0xFF021409)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("home_quick_start_button")
                                ) {
                                    Icon(
                                        imageVector = if (showContinue) Icons.Default.Restore else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (showContinue) "Continue Training" else "Start Today's Training",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFF0F2418),
                                    border = BorderStroke(1.dp, PitchMintHighlight.copy(alpha = 0.5f)),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable { viewModel.navigateTo(AppScreen.Timer) }
                                        .testTag("home_timer_shortcut")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Timer, "Timer", tint = PitchMintHighlight, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text("Timer", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. KPI Ring + Stats Card
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SportyCard(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = cardBg,
                        onClick = { viewModel.navigateTo(AppScreen.Progress) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressRing(
                                progress = summary.weeklyProgressPercent / 100f,
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 8.dp,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("${summary.weeklyProgressPercent}%", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                                    Text("Week", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                DashboardKpiItem("${summary.totalSessions}", "Sessions", Icons.Default.CheckCircle, MaterialTheme.colorScheme.primary)
                                DashboardKpiItem("${summary.totalMinutes}m", "Minutes", Icons.Default.Schedule, TurfAccentTeal)
                                DashboardKpiItem("${summary.streakDays}d", "Streak", Icons.Default.LocalFireDepartment, AmberTimer)
                                DashboardKpiItem("${summary.unlockedAchievementsCount}/${summary.totalAchievementsCount}", "Badges", Icons.Default.EmojiEvents, TrophyGold)
                            }
                        }
                    }
                }
            }

            // 5. Quick Access Tiles for Dedicated Screens (Daily Plan, Timer, History, Achievements)
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Training Studio", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        HomeToolTile(
                            title = "Daily Plan",
                            subtitle = "$dailyCompletedCount/${dailyDrills.size} drills done",
                            icon = Icons.Default.CalendarMonth,
                            tint = MaterialTheme.colorScheme.primary,
                            containerColor = cardBg,
                            modifier = Modifier.weight(1f).testTag("home_tile_daily_plan"),
                            onClick = { viewModel.navigateTo(AppScreen.DailyPlan) }
                        )
                        HomeToolTile(
                            title = "Training Timer",
                            subtitle = "Countdown + Rest",
                            icon = Icons.Default.Timer,
                            tint = AmberTimer,
                            containerColor = cardBg,
                            modifier = Modifier.weight(1f).testTag("home_tile_timer"),
                            onClick = { viewModel.navigateTo(AppScreen.Timer) }
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        HomeToolTile(
                            title = "History",
                            subtitle = "${summary.totalSessions} saved logs",
                            icon = Icons.Default.History,
                            tint = TurfAccentTeal,
                            containerColor = cardBg,
                            modifier = Modifier.weight(1f).testTag("home_tile_history"),
                            onClick = { viewModel.navigateTo(AppScreen.History) }
                        )
                        HomeToolTile(
                            title = "Achievements",
                            subtitle = "${summary.unlockedAchievementsCount}/9 unlocked",
                            icon = Icons.Default.EmojiEvents,
                            tint = TrophyGold,
                            containerColor = cardBg,
                            modifier = Modifier.weight(1f).testTag("home_tile_achievements"),
                            onClick = { viewModel.navigateTo(AppScreen.Achievements) }
                        )
                    }
                }
            }

            // 6. 8 Training Categories (61 Total Drills)
            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("8 Training Categories (61 Drills)", style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Text(
                            text = "View All",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PitchMintHighlight,
                            modifier = Modifier.clickable { viewModel.openTrainingSubTab(TrainingSubTab.CATEGORIES) }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(TrainingCategory.entries) { cat ->
                            val count = viewModel.categoryExercises.count { it.categoryKey == cat.name }
                            val icon = categoryIcon(cat)
                            SportyCard(
                                modifier = Modifier.width(146.dp),
                                containerColor = cardBg,
                                onClick = { viewModel.openTrainingSubTab(TrainingSubTab.CATEGORIES, categoryFilter = cat) }
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(icon, cat.displayName, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                        }
                                        Text(cat.letter, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(cat.displayName, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                                    Text("$count drills", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }

            // 7. Recommended Drill Today
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SportyCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_daily_recommendation_card"),
                        containerColor = cardBg,
                        onClick = { viewModel.openExercise(recommended) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("RECOMMENDED DRILL", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                                    Text(
                                        text = recommended.categoryLabel,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                            Text(recommended.name, style = MaterialTheme.typography.titleLarge)
                            Text(recommended.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${recommended.durationMinutes} min • ${recommended.recommendedReps}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { viewModel.launchExercisesInTimer(listOf(recommended), recommended.name) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.PlayArrow, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Train Now", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun categoryIcon(cat: TrainingCategory): ImageVector = when (cat) {
    TrainingCategory.BALL_MASTERY -> Icons.Default.SportsSoccer
    TrainingCategory.DRIBBLING -> Icons.AutoMirrored.Filled.DirectionsRun
    TrainingCategory.PASSING -> Icons.Default.SwapHoriz
    TrainingCategory.SHOOTING -> Icons.Default.SportsSoccer
    TrainingCategory.SPEED_AGILITY -> Icons.Default.Bolt
    TrainingCategory.PHYSICAL_CONDITIONING -> Icons.Default.FitnessCenter
    TrainingCategory.DEFENDING -> Icons.Default.Shield
    TrainingCategory.GOALKEEPER -> Icons.Default.SportsHandball
}

@Composable
private fun DashboardKpiItem(
    value: String,
    label: String,
    icon: ImageVector,
    tint: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = tint, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun HomeToolTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    tint: Color,
    containerColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    SportyCard(modifier = modifier, containerColor = containerColor, onClick = onClick) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(tint.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, title, tint = tint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
        }
    }
}
