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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BadmintonExercise
import com.example.data.model.FitnessArea
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory
import com.example.ui.components.OfflineAnimatedCourtPlayer
import com.example.ui.components.SportyCard
import com.example.ui.theme.AmberTimer
import com.example.ui.theme.CobaltAccent
import com.example.ui.theme.CourtCyan
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.VictoryGold
import com.example.viewmodel.BadmintonViewModel
import com.example.viewmodel.TrainingSubTab

@Composable
fun TrainingScreen(
    viewModel: BadmintonViewModel,
    modifier: Modifier = Modifier
) {
    val subTab by viewModel.trainingSubTab.collectAsStateWithLifecycle()
    val selectedExercise by viewModel.selectedExercise.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
    val selectedProgramId by viewModel.selectedProgramId.collectAsStateWithLifecycle()
    val completedSessions by viewModel.completedSessions.collectAsStateWithLifecycle()
    val programCompletions by viewModel.programCompletions.collectAsStateWithLifecycle()

    val completedIds = remember(completedSessions) {
        completedSessions.map { it.exerciseId }.toSet()
    }

    if (selectedExercise != null) {
        BackHandler { viewModel.closeExercise() }
        ExerciseDetailScreen(
            exercise = selectedExercise!!,
            isCompletedOnce = completedIds.contains(selectedExercise!!.id),
            onBack = { viewModel.closeExercise() },
            onMarkCompleted = { viewModel.logExerciseCompleted(selectedExercise!!) },
            onStartInTimer = { viewModel.launchExercisesInTimer(listOf(selectedExercise!!), selectedExercise!!.name) }
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
            Text("Badminton Training", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "Programs, 9 skill modules, conditioning, rules & timer",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

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
                TrainingSubTab.PROGRAMS -> {
                    val activeProg = viewModel.programs.find { it.id == selectedProgramId } ?: viewModel.programs.first()
                    val doneModules = programCompletions
                        .filter { it.programId == activeProg.id && it.isCompleted }
                        .map { it.moduleId }
                        .toSet()
                    val modules = activeProg.moduleIds.mapNotNull { id ->
                        viewModel.programExercises.find { it.id == id }
                    }
                    val pct = ((doneModules.size.toFloat() / modules.size.coerceAtLeast(1)) * 100).toInt().coerceIn(0, 100)

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                viewModel.programs.forEach { prog ->
                                    val sel = prog.id == activeProg.id
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { viewModel.selectProgramId(prog.id) }
                                            .testTag("program_tab_${prog.level.name.lowercase()}")
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = prog.level.displayName,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (sel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            SportyCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                                            Text(
                                                text = activeProg.badge,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                        Text(
                                            text = "${doneModules.size}/${modules.size} completed ($pct%)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(activeProg.title, style = MaterialTheme.typography.titleLarge)
                                    Text(activeProg.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    LinearProgressIndicator(
                                        progress = { pct / 100f },
                                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                        Button(
                                            onClick = { viewModel.launchExercisesInTimer(modules, activeProg.title) },
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Start Program", fontWeight = FontWeight.Bold)
                                        }
                                        if (doneModules.isNotEmpty()) {
                                            OutlinedButton(
                                                onClick = { viewModel.resetProgram(activeProg.id) },
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Text("Reset")
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        items(modules, key = { it.id }) { ex ->
                            val isDone = doneModules.contains(ex.id)
                            SportyCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("program_module_${ex.id}"),
                                onClick = { viewModel.openExercise(ex) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { viewModel.toggleProgramModuleCompleted(activeProg.id, ex.id, isDone) }
                                    ) {
                                        Icon(
                                            imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                            contentDescription = "Toggle module completion",
                                            tint = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(ex.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text(ex.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${ex.durationMinutes} min • ${ex.recommendedReps} • Rest ${ex.restTimeSeconds}s",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                TrainingSubTab.CATEGORIES -> {
                    var levelFilter by remember { mutableStateOf<SkillLevel?>(null) }
                    val filtered = viewModel.categoryExercises.filter { ex ->
                        (selectedCategory == null || ex.categoryKey == selectedCategory!!.name) &&
                            (levelFilter == null || ex.difficulty == levelFilter)
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
                                        FilterChipPill("All 9 Categories", selectedCategory == null) {
                                            viewModel.selectCategoryFilter(null)
                                        }
                                    }
                                    items(TrainingCategory.entries) { cat ->
                                        FilterChipPill(cat.displayName, selectedCategory == cat) {
                                            viewModel.selectCategoryFilter(cat)
                                        }
                                    }
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    FilterChipPill("All Levels", levelFilter == null) { levelFilter = null }
                                    SkillLevel.entries.forEach { lvl ->
                                        FilterChipPill(lvl.displayName, levelFilter == lvl) { levelFilter = lvl }
                                    }
                                }
                            }
                        }

                        items(filtered, key = { it.id }) { ex ->
                            ExerciseCardItem(
                                exercise = ex,
                                isCompleted = completedIds.contains(ex.id),
                                onClick = { viewModel.openExercise(ex) }
                            )
                        }
                    }
                }

                TrainingSubTab.FITNESS -> {
                    var areaFilter by remember { mutableStateOf<FitnessArea?>(null) }
                    val filtered = if (areaFilter == null) viewModel.fitnessExercises
                    else viewModel.fitnessExercises.filter { it.categoryKey == areaFilter!!.name }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                item {
                                    FilterChipPill("All Fitness (8)", areaFilter == null) { areaFilter = null }
                                }
                                items(FitnessArea.entries) { area ->
                                    FilterChipPill(area.displayName, areaFilter == area) { areaFilter = area }
                                }
                            }
                        }

                        items(filtered, key = { it.id }) { ex ->
                            ExerciseCardItem(
                                exercise = ex,
                                isCompleted = completedIds.contains(ex.id),
                                onClick = { viewModel.openExercise(ex) }
                            )
                        }
                    }
                }

                TrainingSubTab.RULES -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            SportyCard(
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("Badminton Rules & Court Knowledge", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                                    Text(
                                        "Tap any lesson below for official court measurements, scoring, service rules, faults, and racket guidance — 100% offline.",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        items(viewModel.rulesLessons, key = { it.id }) { lesson ->
                            ExerciseCardItem(
                                exercise = lesson,
                                isCompleted = completedIds.contains(lesson.id),
                                onClick = { viewModel.openExercise(lesson) }
                            )
                        }
                    }
                }

                TrainingSubTab.TIMER -> {
                    InteractiveTimerPanel(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun FilterChipPill(
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
private fun ExerciseCardItem(
    exercise: BadmintonExercise,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    val diffColor = when (exercise.difficulty) {
        SkillLevel.BEGINNER -> EmeraldSuccess
        SkillLevel.INTERMEDIATE -> CobaltAccent
        SkillLevel.ADVANCED -> AmberTimer
    }

    SportyCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("exercise_card_${exercise.id}"),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                        Text(
                            text = exercise.categoryLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = diffColor.copy(alpha = 0.16f)) {
                        Text(
                            text = exercise.difficulty.displayName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = diffColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(exercise.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(exercise.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${exercise.durationMinutes} min • ${exercise.recommendedReps} • Rest ${exercise.restTimeSeconds}s",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (isCompleted) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.CheckCircle, "Completed", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
private fun ExerciseDetailScreen(
    exercise: BadmintonExercise,
    isCompletedOnce: Boolean,
    onBack: () -> Unit,
    onMarkCompleted: () -> Unit,
    onStartInTimer: () -> Unit
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
                IconButton(onClick = onBack, modifier = Modifier.testTag("exercise_detail_back")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = MaterialTheme.colorScheme.primary)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(exercise.categoryLabel.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(exercise.name, style = MaterialTheme.typography.headlineMedium)
                }
            }
        }

        // Offline Video / Animation Support Card
        item {
            SportyCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Interactive Offline Animation & Court Guide", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    OfflineAnimatedCourtPlayer(
                        diagramType = exercise.diagramType,
                        motionPhases = exercise.motionPhases
                    )
                }
            }
        }

        // Specs Card
        item {
            SportyCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(exercise.description, style = MaterialTheme.typography.bodyLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SpecLabel(Icons.Default.Schedule, "${exercise.durationMinutes} min")
                        SpecLabel(Icons.Default.Repeat, exercise.difficulty.displayName)
                        SpecLabel(Icons.Default.Timer, "Rest ${exercise.restTimeSeconds}s")
                    }
                    SpecLabel(Icons.Default.Repeat, "Recommended Reps: ${exercise.recommendedReps}")
                }
            }
        }

        // Instructions
        item {
            SportyCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Step-by-Step Instructions", style = MaterialTheme.typography.titleLarge)
                    exercise.instructions.forEachIndexed { idx, step ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("${idx + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(step, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Safety Instructions (for Fitness & Conditioning)
        if (exercise.safetyInstructions.isNotEmpty()) {
            item {
                SportyCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = AmberTimer.copy(alpha = 0.12f),
                    borderColor = AmberTimer.copy(alpha = 0.45f)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.HealthAndSafety, null, tint = AmberTimer, modifier = Modifier.size(19.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Safety Instructions", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AmberTimer)
                        }
                        exercise.safetyInstructions.forEach { safe ->
                            Text("• $safe", fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Training Tips
        item {
            SportyCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.28f)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lightbulb, null, tint = VictoryGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Coach Training Tips", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    exercise.trainingTips.forEach { tip ->
                        Text("• $tip", fontSize = 13.sp)
                    }
                }
            }
        }

        // Actions
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onStartInTimer,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("start_exercise_timer_button")
                ) {
                    Icon(Icons.Default.Timer, null, tint = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start in Interactive Timer", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                }

                OutlinedButton(
                    onClick = {
                        onMarkCompleted()
                        justLogged = true
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("mark_exercise_completed_button")
                ) {
                    Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (justLogged) "Saved to Progress!" else if (isCompletedOnce) "Log Session Again" else "Mark Exercise Completed",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun SpecLabel(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(5.dp))
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
