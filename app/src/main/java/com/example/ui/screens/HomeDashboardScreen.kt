package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.graphics.luminance
import com.example.R
import com.example.data.local.DateUtils
import com.example.data.model.PlayerPosition
import com.example.data.model.SkillCategory
import com.example.ui.components.CircularProgressRing
import com.example.ui.components.StadiumCard
import com.example.ui.components.WeeklyActivityBarChart
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.AmberTimer
import com.example.ui.theme.CrimsonOpponent
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SpotlightGold
import com.example.viewmodel.FootballViewModel
import com.example.viewmodel.TrainingSubTab

@Composable
fun HomeDashboardScreen(
    viewModel: FootballViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.playerProfile.collectAsStateWithLifecycle()
    val summary by viewModel.dashboardSummary.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val programCompletions by viewModel.programCompletions.collectAsStateWithLifecycle()

    val position = PlayerPosition.entries.find { it.name == profile?.playingPosition } ?: PlayerPosition.CENTRAL_MID
    val activeProgram = viewModel.programs.find { it.id == settings.activeProgramId } ?: viewModel.programs.first()
    val completedDayNumbers = programCompletions
        .filter { it.programId == activeProgram.id && it.isCompleted }
        .map { it.dayNumber }
        .toSet()
    val nextDayPlan = activeProgram.days.firstOrNull { !completedDayNumbers.contains(it.dayNumber) } ?: activeProgram.days.last()

    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val dashCardBg = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.82f else 0.88f)
    val dashCardBorder = MaterialTheme.colorScheme.primary.copy(alpha = if (isDark) 0.34f else 0.42f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Full-screen uploaded Ronaldo #7 artwork as dashboard background
        Image(
            painter = painterResource(id = R.drawable.bg_dashboard_hero),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )

        // Adaptive gradient scrim to keep artwork luminous at top and cards ultra-readable below
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isDark) {
                        Brush.verticalGradient(
                            0.0f to Color(0xFF0A0705).copy(alpha = 0.42f),
                            0.35f to Color(0xFF120B07).copy(alpha = 0.56f),
                            0.72f to Color(0xFF0C0705).copy(alpha = 0.78f),
                            1.0f to Color(0xFF0A0705).copy(alpha = 0.90f)
                        )
                    } else {
                        Brush.verticalGradient(
                            0.0f to Color(0xFFFDF8F2).copy(alpha = 0.52f),
                            0.45f to Color(0xFFFDF8F2).copy(alpha = 0.72f),
                            1.0f to Color(0xFFFDF8F2).copy(alpha = 0.90f)
                        )
                    }
                )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Open Hero Header showcasing the background artwork + Player Profile & Stats Pill
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = DateUtils.formatDisplayDate(DateUtils.todayKey()),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) SpotlightGold else MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Welcome, ${profile?.playerName?.split(" ")?.firstOrNull() ?: "Player"}!",
                                style = MaterialTheme.typography.headlineLarge,
                                color = if (isDark) Color.White else MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isDark) Color(0xFF1B100A).copy(alpha = 0.78f) else dashCardBg,
                            modifier = Modifier
                                .clickable { viewModel.navigateTo(AppScreen.Profile) }
                                .testTag("home_profile_chip")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    "Profile",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = position.shortCode,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Breathing room so the golden spotlight & raised #7 celebration remain visible
                    Spacer(modifier = Modifier.height(64.dp))

                    // Progress, Total Workouts & Streak Row
                    StadiumCard(
                        containerColor = dashCardBg,
                        borderColor = dashCardBorder,
                        onClick = { viewModel.navigateTo(AppScreen.Progress) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressRing(
                                progress = summary.activeProgramProgressPercent / 100f,
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 7.dp,
                                modifier = Modifier.size(60.dp)
                            ) {
                                Text(
                                    text = "${summary.activeProgramProgressPercent}%",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                HeroStatItem(
                                    label = "Workouts",
                                    value = "${summary.completedSessionsTotal}",
                                    accent = MaterialTheme.colorScheme.primary
                                )
                                HeroStatItem(
                                    label = "Streak",
                                    value = "${summary.streakDays}d",
                                    accent = CrimsonOpponent,
                                    icon = Icons.Default.LocalFireDepartment
                                )
                                HeroStatItem(
                                    label = "Minutes",
                                    value = "${summary.totalMinutes}m",
                                    accent = AmberGlow
                                )
                            }
                        }
                    }
                }
            }

        // 2. Today's Training Plan Card
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Training Plan",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = activeProgram.badge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.Programs) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                StadiumCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = dashCardBg,
                    borderColor = dashCardBorder
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "${activeProgram.title} • Day ${nextDayPlan.dayNumber}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "${nextDayPlan.durationMinutes} min",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = nextDayPlan.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Focus: ${nextDayPlan.focusArea}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        nextDayPlan.exercises.forEach { ex ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(ex, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { viewModel.navigateTo(AppScreen.Programs) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("home_open_program_button")
                            ) {
                                Icon(Icons.Default.PlayArrow, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Open Plan", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { viewModel.openTrainingWithSubTab(TrainingSubTab.TIMERS) }
                                    .testTag("home_quick_timer_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Timer, "Timer", tint = AmberTimer, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text("Timers", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Quick Access Training Categories (6 Skill Modules + Fitness + Positions)
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Training Categories",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "View all",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { viewModel.openTrainingWithSubTab(TrainingSubTab.MODULES) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(SkillCategory.entries) { cat ->
                        val icon = when (cat) {
                            SkillCategory.BALL_CONTROL -> Icons.Default.SportsSoccer
                            SkillCategory.DRIBBLING -> Icons.Default.Bolt
                            SkillCategory.PASSING -> Icons.AutoMirrored.Filled.DirectionsRun
                            SkillCategory.SHOOTING -> Icons.Default.SportsSoccer
                            SkillCategory.DEFENDING -> Icons.Default.Security
                            SkillCategory.GOALKEEPER -> Icons.Default.SportsHandball
                        }
                        QuickCategoryTile(
                            title = cat.displayName,
                            subtitle = "5 drills",
                            icon = icon,
                            accent = MaterialTheme.colorScheme.primary,
                            containerColor = dashCardBg,
                            borderColor = dashCardBorder,
                            onClick = { viewModel.openTrainingWithSubTab(TrainingSubTab.MODULES, cat) }
                        )
                    }
                }
            }
        }

        // 4. Quick Training Tools & Position Coaching Row
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Pro Tools & Coaching",
                    style = MaterialTheme.typography.titleLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ToolShortcutCard(
                        title = "Fitness",
                        sub = "8 areas • 3 levels",
                        icon = Icons.Default.FitnessCenter,
                        accent = CrimsonOpponent,
                        containerColor = dashCardBg,
                        borderColor = dashCardBorder,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openTrainingWithSubTab(TrainingSubTab.FITNESS) }
                    )
                    ToolShortcutCard(
                        title = "Positions",
                        sub = "${position.shortCode} & 7 more",
                        icon = Icons.Default.Person,
                        accent = MaterialTheme.colorScheme.primary,
                        containerColor = dashCardBg,
                        borderColor = dashCardBorder,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openTrainingWithSubTab(TrainingSubTab.POSITIONS) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ToolShortcutCard(
                        title = "Tactics Board",
                        sub = "Formations & arrows",
                        icon = Icons.Default.Palette,
                        accent = AmberGlow,
                        containerColor = dashCardBg,
                        borderColor = dashCardBorder,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openTrainingWithSubTab(TrainingSubTab.TACTICS) }
                    )
                    ToolShortcutCard(
                        title = "Planner & Cal",
                        sub = "Weekly schedule",
                        icon = Icons.Default.CalendarMonth,
                        accent = FlameOrange,
                        containerColor = dashCardBg,
                        borderColor = dashCardBorder,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openTrainingWithSubTab(TrainingSubTab.PLANNER) }
                    )
                }
            }
        }

        // 5. Weekly Activity Summary Chart
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                StadiumCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = dashCardBg,
                    borderColor = dashCardBorder,
                    onClick = { viewModel.navigateTo(AppScreen.Progress) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Weekly Activity Summary",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = "${summary.todayCompletedCount} sessions completed today",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Open Progress",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        WeeklyActivityBarChart(
                            points = summary.weeklyPoints,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        }
    }
}

@Composable
private fun HeroStatItem(
    label: String,
    value: String,
    accent: Color,
    icon: ImageVector? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(icon, null, tint = accent, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(2.dp))
            }
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = accent
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuickCategoryTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    containerColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    StadiumCard(
        modifier = Modifier
            .width(126.dp)
            .testTag("quick_cat_${title.lowercase().replace(" ", "_")}"),
        containerColor = containerColor,
        borderColor = borderColor,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = accent, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
            Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ToolShortcutCard(
    title: String,
    sub: String,
    icon: ImageVector,
    accent: Color,
    containerColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    StadiumCard(
        modifier = modifier,
        containerColor = containerColor,
        borderColor = borderColor,
        onClick = onClick
    ) {
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
                    .background(accent.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = accent, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(sub, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
        }
    }
}
