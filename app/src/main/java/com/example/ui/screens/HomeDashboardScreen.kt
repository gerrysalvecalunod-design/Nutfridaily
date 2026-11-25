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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.example.ui.theme.StadiumForestDark
import com.example.ui.theme.StadiumPitchGreen
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
    val recommended = viewModel.recommendedExerciseForToday()
    val nextProgramPair = viewModel.nextProgramModuleToContinue()
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val skillLevel = SkillLevel.entries.find { it.name == profile?.skillLevel } ?: SkillLevel.BEGINNER
    val position = PlayerPosition.entries.find { it.name == profile?.playerPosition } ?: PlayerPosition.ALL_ROUNDER
    val displayPlayerName = profile?.playerName?.trim()?.ifBlank { "Player" } ?: "Player"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Stadium Floodlight Hero Header & Quick-Start Training
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(258.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_football_hero_banner),
                    contentDescription = "Football Stadium Pitch",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0.0f to StadiumDeepBlack.copy(alpha = 0.52f),
                                0.68f to StadiumForestDark.copy(alpha = 0.86f),
                                1.0f to MaterialTheme.colorScheme.background
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
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
                                color = StadiumForestDark,
                                shadowElevation = 6.dp,
                                border = BorderStroke(1.5.dp, PitchMintHighlight),
                                modifier = Modifier.size(54.dp)
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
                            Column {
                                Text(
                                    text = "${DateUtils.formatDisplayDate(DateUtils.todayKey())} • ${position.displayName}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PitchMintHighlight
                                )
                                Text(
                                    text = "Welcome back, $displayPlayerName!",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Color.White,
                                    maxLines = 2
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.55f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.WifiOff, "Offline Mode", tint = PitchMintHighlight, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "OFFLINE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.quickStartRecommendedWorkout() },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) MintEmeraldPrimary else Color.White,
                                contentColor = StadiumForestDark
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                                .testTag("home_quick_start_button")
                        ) {
                            Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Quick-Start Session",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = StadiumForestDark.copy(alpha = 0.9f),
                            border = BorderStroke(1.dp, PitchMintHighlight.copy(alpha = 0.45f)),
                            modifier = Modifier
                                .height(54.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { viewModel.openTrainingSubTab(TrainingSubTab.TIMER) }
                                .testTag("home_timer_shortcut")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Timer, "Timer", tint = PitchMintHighlight, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text("Timer", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // 2. Continue Training Card (Restores Saved Timer Checkpoint or Active Program Progress)
        if ((checkpoint != null && checkpoint!!.isActive) || nextProgramPair != null) {
            item {
                val hasActiveCheckpoint = checkpoint != null && checkpoint!!.isActive
                val subtitleText = if (hasActiveCheckpoint) {
                    val mins = timerState.remainingSeconds / 60
                    val secs = timerState.remainingSeconds % 60
                    val formatted = String.format(Locale.US, "%02d:%02d", mins, secs)
                    "${timerState.customTitle} • Drill ${timerState.currentIndex + 1}/${timerState.queue.size.coerceAtLeast(1)} ($formatted left)"
                } else {
                    val (prog, ex) = nextProgramPair!!
                    "${prog.title} (${summary.activeProgramDone}/${summary.activeProgramTotal}) • Next: ${ex.name}"
                }

                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SportyCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_continue_training_card"),
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (isDark) 0.55f else 0.72f),
                        borderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                        onClick = { viewModel.continueTrainingFromCheckpoint() }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restore,
                                    contentDescription = "Continue Training",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (hasActiveCheckpoint) "Continue Saved Session" else "Continue Program Training",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = subtitleText,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                                    maxLines = 2
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = if (hasActiveCheckpoint) "Resume" else "Continue",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. KPI Metrics: Weekly Progress, Sessions, Minutes, Streak, Badges
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SportyCard(
                    modifier = Modifier.fillMaxWidth(),
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
                            DashboardKpiItem(
                                value = "${summary.totalSessions}",
                                label = "Sessions",
                                icon = Icons.Default.CheckCircle,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            DashboardKpiItem(
                                value = "${summary.totalMinutes}m",
                                label = "Minutes",
                                icon = Icons.Default.Schedule,
                                tint = TurfAccentTeal
                            )
                            DashboardKpiItem(
                                value = "${summary.streakDays}d",
                                label = "Streak",
                                icon = Icons.Default.LocalFireDepartment,
                                tint = AmberTimer
                            )
                            DashboardKpiItem(
                                value = "${summary.unlockedAchievementsCount}/${summary.totalAchievementsCount}",
                                label = "Badges",
                                icon = Icons.Default.EmojiEvents,
                                tint = TrophyGold
                            )
                        }
                    }
                }
            }
        }

        // 4. Daily Pitch Recommendation Card
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Daily Recommendation", style = MaterialTheme.typography.titleLarge)
                    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                        Text(
                            text = "${skillLevel.displayName.uppercase()} TIER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                SportyCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_daily_recommendation_card"),
                    onClick = {
                        viewModel.navigateTo(AppScreen.Training)
                        viewModel.openExercise(recommended)
                    }
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(108.dp)
                                .background(
                                    Brush.horizontalGradient(listOf(StadiumForestDark, StadiumPitchGreen, TurfAccentTeal))
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = recommended.categoryLabel.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PitchMintHighlight
                                )
                                Text(
                                    text = recommended.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White
                                )
                            }
                            Surface(
                                shape = CircleShape,
                                color = StadiumForestDark,
                                border = BorderStroke(1.dp, PitchMintHighlight.copy(alpha = 0.7f)),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                                    .size(42.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_football_app_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = recommended.description,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
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
                                        .clickable {
                                            viewModel.launchExercisesInTimer(listOf(recommended), recommended.name)
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.PlayArrow, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Start Drill", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Structured Training Programs
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Training Programs", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "View All",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { viewModel.openTrainingSubTab(TrainingSubTab.PROGRAMS) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(viewModel.programs) { prog ->
                        SportyCard(
                            modifier = Modifier
                                .width(192.dp)
                                .testTag("home_program_${prog.id}"),
                            onClick = { viewModel.openTrainingSubTab(TrainingSubTab.PROGRAMS, programId = prog.id) }
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                                    Text(
                                        text = prog.badge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                                Text(prog.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1)
                                Text("6 structured modules", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // 6. 9 Football Skill Categories
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("9 Skill Modules", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "Explore",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { viewModel.openTrainingSubTab(TrainingSubTab.CATEGORIES) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(TrainingCategory.entries) { cat ->
                        val icon = when (cat) {
                            TrainingCategory.DRIBBLING -> Icons.AutoMirrored.Filled.DirectionsRun
                            TrainingCategory.PASSING -> Icons.Default.SwapHoriz
                            TrainingCategory.SHOOTING -> Icons.Default.SportsSoccer
                            TrainingCategory.DEFENDING -> Icons.Default.Shield
                            TrainingCategory.SPEED_AGILITY -> Icons.Default.Bolt
                            TrainingCategory.ENDURANCE -> Icons.Default.FitnessCenter
                            TrainingCategory.SET_PIECES -> Icons.Default.Flag
                            TrainingCategory.REACTION_VISION -> Icons.Default.Visibility
                            TrainingCategory.MATCH_TACTICS -> Icons.Default.Psychology
                        }
                        SportyCard(
                            modifier = Modifier.width(142.dp),
                            onClick = { viewModel.openTrainingSubTab(TrainingSubTab.CATEGORIES, categoryFilter = cat) }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(icon, cat.displayName, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(19.dp))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(cat.displayName, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                                Text("3 drills", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // 7. Academy & Conditioning Shortcuts
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Academy & Conditioning", style = MaterialTheme.typography.titleLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    HomeToolTile(
                        title = "Fitness & Speed",
                        subtitle = "8 conditioning drills",
                        icon = Icons.Default.FitnessCenter,
                        tint = AmberTimer,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openTrainingSubTab(TrainingSubTab.FITNESS) }
                    )
                    HomeToolTile(
                        title = "Tactics & Rules",
                        subtitle = "7 pro lessons",
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        tint = MintEmeraldPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openTrainingSubTab(TrainingSubTab.RULES) }
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    HomeToolTile(
                        title = "Workout Planner",
                        subtitle = "Weekly Mon–Sun plan",
                        icon = Icons.Default.CalendarMonth,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(AppScreen.Planner) }
                    )
                    HomeToolTile(
                        title = "Progress Stats",
                        subtitle = "${summary.totalHoursFormatted} hrs logged",
                        icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        tint = TurfAccentTeal,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(AppScreen.Progress) }
                    )
                }
            }
        }
    }
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    SportyCard(modifier = modifier, onClick = onClick) {
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
