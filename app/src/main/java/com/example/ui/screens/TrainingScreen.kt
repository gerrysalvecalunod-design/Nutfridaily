package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DifficultyLevel
import com.example.data.model.FitnessCategory
import com.example.data.model.PlayerPosition
import com.example.data.model.SkillCategory
import com.example.data.model.TrainingLesson
import com.example.ui.components.DrillDiagramCanvas
import com.example.ui.components.StadiumCard
import com.example.ui.theme.AmberTimer
import com.example.ui.theme.GoldTrophy
import com.example.ui.theme.NeonPitchGreen
import com.example.ui.theme.TacticalCyan
import com.example.viewmodel.FootballViewModel
import com.example.viewmodel.TrainingSubTab

@Composable
fun TrainingScreen(
    viewModel: FootballViewModel,
    modifier: Modifier = Modifier
) {
    val subTab by viewModel.trainingSubTab.collectAsStateWithLifecycle()
    val selectedLesson by viewModel.selectedLesson.collectAsStateWithLifecycle()
    val selectedSkillFilter by viewModel.selectedSkillFilter.collectAsStateWithLifecycle()
    val completedLessons by viewModel.completedLessons.collectAsStateWithLifecycle()

    val completedIds = remember(completedLessons) {
        completedLessons.map { it.lessonId }.toSet()
    }

    if (selectedLesson != null) {
        BackHandler { viewModel.closeLesson() }
        LessonDetailView(
            lesson = selectedLesson!!,
            isCompletedOnce = completedIds.contains(selectedLesson!!.id),
            onBack = { viewModel.closeLesson() },
            onMarkCompleted = { viewModel.markLessonCompleted(selectedLesson!!) },
            onOpenTimer = {
                viewModel.closeLesson()
                viewModel.selectTrainingSubTab(TrainingSubTab.TIMERS)
            }
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
            Text(
                text = "Training Hub",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Drills, fitness, position coaching & tactical tools",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Sub-Tab Strip
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(TrainingSubTab.entries) { tab ->
                val selected = tab == subTab
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .height(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { viewModel.selectTrainingSubTab(tab) }
                        .testTag("training_subtab_${tab.name.lowercase()}")
                ) {
                    Box(modifier = Modifier.padding(horizontal = 14.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = tab.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (subTab) {
                TrainingSubTab.MODULES -> {
                    SkillModulesList(
                        lessons = viewModel.skillLessons,
                        selectedCategory = selectedSkillFilter,
                        completedIds = completedIds,
                        onSelectCategory = { viewModel.selectSkillFilter(it) },
                        onOpenLesson = { viewModel.openLesson(it) }
                    )
                }

                TrainingSubTab.FITNESS -> {
                    FitnessModulesList(
                        lessons = viewModel.fitnessLessons,
                        completedIds = completedIds,
                        onOpenLesson = { viewModel.openLesson(it) }
                    )
                }

                TrainingSubTab.POSITIONS -> {
                    PositionsModulesList(
                        lessons = viewModel.positionLessons,
                        completedIds = completedIds,
                        onOpenLesson = { viewModel.openLesson(it) }
                    )
                }

                TrainingSubTab.TIMERS -> TimersToolPanel(viewModel = viewModel)
                TrainingSubTab.PLANNER -> PlannerAndCalendarPanel(viewModel = viewModel)
                TrainingSubTab.TACTICS -> TacticsBoardPanel(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun SkillModulesList(
    lessons: List<TrainingLesson>,
    selectedCategory: SkillCategory?,
    completedIds: Set<String>,
    onSelectCategory: (SkillCategory?) -> Unit,
    onOpenLesson: (TrainingLesson) -> Unit
) {
    val filtered = if (selectedCategory == null) lessons else lessons.filter { it.categoryKey == selectedCategory.name }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterPill(
                        label = "All (30)",
                        selected = selectedCategory == null,
                        onClick = { onSelectCategory(null) }
                    )
                }
                items(SkillCategory.entries) { cat ->
                    FilterPill(
                        label = cat.displayName,
                        selected = selectedCategory == cat,
                        onClick = { onSelectCategory(cat) }
                    )
                }
            }
        }

        items(filtered, key = { it.id }) { lesson ->
            LessonListCard(
                lesson = lesson,
                isCompleted = completedIds.contains(lesson.id),
                onClick = { onOpenLesson(lesson) }
            )
        }
    }
}

@Composable
private fun FitnessModulesList(
    lessons: List<TrainingLesson>,
    completedIds: Set<String>,
    onOpenLesson: (TrainingLesson) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<FitnessCategory?>(null) }
    var selectedLevel by remember { mutableStateOf<DifficultyLevel?>(null) }

    val filtered = lessons.filter { l ->
        (selectedCategory == null || l.categoryKey == selectedCategory!!.name) &&
            (selectedLevel == null || l.difficulty == selectedLevel)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterPill("All Fitness", selectedCategory == null) { selectedCategory = null }
                    }
                    items(FitnessCategory.entries) { cat ->
                        FilterPill(cat.displayName, selectedCategory == cat) { selectedCategory = cat }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterPill("All Levels", selectedLevel == null) { selectedLevel = null }
                    DifficultyLevel.entries.forEach { lvl ->
                        FilterPill(lvl.displayName, selectedLevel == lvl) { selectedLevel = lvl }
                    }
                }
            }
        }

        items(filtered, key = { it.id }) { lesson ->
            LessonListCard(
                lesson = lesson,
                isCompleted = completedIds.contains(lesson.id),
                onClick = { onOpenLesson(lesson) }
            )
        }
    }
}

@Composable
private fun PositionsModulesList(
    lessons: List<TrainingLesson>,
    completedIds: Set<String>,
    onOpenLesson: (TrainingLesson) -> Unit
) {
    var selectedPosition by remember { mutableStateOf<PlayerPosition?>(null) }
    val filtered = if (selectedPosition == null) lessons else lessons.filter { it.categoryKey == selectedPosition!!.name }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterPill("All Positions", selectedPosition == null) { selectedPosition = null }
                }
                items(PlayerPosition.entries) { pos ->
                    FilterPill("${pos.shortCode} • ${pos.displayName}", selectedPosition == pos) {
                        selectedPosition = pos
                    }
                }
            }
        }

        if (selectedPosition != null) {
            item {
                StadiumCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(selectedPosition!!.displayName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                        Text(selectedPosition!!.roleSummary, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        items(filtered, key = { it.id }) { lesson ->
            LessonListCard(
                lesson = lesson,
                isCompleted = completedIds.contains(lesson.id),
                onClick = { onOpenLesson(lesson) }
            )
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LessonListCard(
    lesson: TrainingLesson,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    val diffColor = when (lesson.difficulty) {
        DifficultyLevel.BEGINNER -> NeonPitchGreen
        DifficultyLevel.INTERMEDIATE -> TacticalCyan
        DifficultyLevel.ADVANCED -> AmberTimer
    }

    StadiumCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lesson_card_${lesson.id}"),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                        Text(
                            text = lesson.categoryLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = diffColor.copy(alpha = 0.16f)) {
                        Text(
                            text = lesson.difficulty.displayName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = diffColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(lesson.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(
                    text = lesson.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${lesson.durationMinutes} min • ${lesson.recommendedReps}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            if (isCompleted) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun LessonDetailView(
    lesson: TrainingLesson,
    isCompletedOnce: Boolean,
    onBack: () -> Unit,
    onMarkCompleted: () -> Unit,
    onOpenTimer: () -> Unit
) {
    var justLogged by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("lesson_detail_back")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = MaterialTheme.colorScheme.primary)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(lesson.categoryLabel.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(lesson.title, style = MaterialTheme.typography.headlineMedium)
                }
            }
        }

        // Illustrated Drill Diagram (Offline Canvas Visual)
        item {
            StadiumCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    DrillDiagramCanvas(diagramType = lesson.diagramType)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Illustrated Drill Blueprint (Offline)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Key Specs Row
        item {
            StadiumCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(lesson.description, style = MaterialTheme.typography.bodyLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        SpecChip(Icons.Default.Schedule, "${lesson.durationMinutes} min")
                        SpecChip(Icons.Default.Repeat, lesson.difficulty.displayName)
                    }
                    SpecChip(Icons.Default.Repeat, "Reps: ${lesson.recommendedReps}")
                    SpecChip(Icons.Default.Build, "Equipment: ${lesson.equipment}")
                }
            }
        }

        // Step-by-step Instructions
        item {
            StadiumCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Step-by-Step Instructions", style = MaterialTheme.typography.titleLarge)
                    lesson.instructions.forEachIndexed { idx, step ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(step, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Coaching Tips
        item {
            StadiumCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lightbulb, null, tint = GoldTrophy, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Coaching Tips", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    lesson.coachingTips.forEach { tip ->
                        Text("• $tip", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        // Complete & Timer Buttons
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        onMarkCompleted()
                        justLogged = true
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("mark_lesson_completed_button")
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (justLogged) "Session Logged to History!" else if (isCompletedOnce) "Log Session Again" else "Mark as Completed",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                OutlinedButton(
                    onClick = onOpenTimer,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Timer, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Launch Workout Timer", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SpecChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
