package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BadmintonDatabase
import com.example.data.local.DatabaseInitializer
import com.example.data.local.DateUtils
import com.example.data.local.SettingsDataStore
import com.example.data.model.AchievementEntity
import com.example.data.model.AppSettings
import com.example.data.model.BadmintonExercise
import com.example.data.model.BadmintonProgram
import com.example.data.model.CompletedSessionEntity
import com.example.data.model.LessonSection
import com.example.data.model.PersonalRecordEntity
import com.example.data.model.PlannedWorkoutEntity
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.ProgramModuleCompletionEntity
import com.example.data.model.SavedTrainingCheckpointEntity
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory
import com.example.data.model.TrainingGoalEntity
import com.example.data.repository.BadmintonRepository
import com.example.data.util.SoundAndReminderHelper
import com.example.ui.screens.AppScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class DailyChartPoint(
    val dateKey: String,
    val label: String,
    val minutes: Int,
    val sessions: Int
)

data class CategoryMasteryStat(
    val category: TrainingCategory,
    val completedCount: Int,
    val percent: Int
)

data class DashboardSummary(
    val totalSessions: Int = 0,
    val totalMinutes: Int = 0,
    val totalHoursFormatted: String = "0.0",
    val streakDays: Int = 0,
    val weeklySessionsCount: Int = 0,
    val weeklyGoalSessions: Int = 5,
    val weeklyProgressPercent: Int = 0,
    val todayMinutes: Int = 0,
    val dailyMinutesGoal: Int = 30,
    val dailyProgressPercent: Int = 0,
    val activeProgramTitle: String = "Beginner Training",
    val activeProgramDone: Int = 0,
    val activeProgramTotal: Int = 6,
    val activeProgramPercent: Int = 0,
    val weeklyPoints: List<DailyChartPoint> = emptyList(),
    val monthlyPoints: List<DailyChartPoint> = emptyList(),
    val categoryMastery: List<CategoryMasteryStat> = emptyList(),
    val unlockedAchievementsCount: Int = 0,
    val totalAchievementsCount: Int = 8
)

enum class TrainingSubTab(val label: String) {
    PROGRAMS("Programs"),
    CATEGORIES("Categories"),
    FITNESS("Fitness"),
    RULES("Rules & Learn"),
    TIMER("Timer")
}

enum class TimerPhase {
    WORK,
    REST,
    FINISHED
}

data class TrainingTimerUiState(
    val queue: List<BadmintonExercise> = emptyList(),
    val currentIndex: Int = 0,
    val phase: TimerPhase = TimerPhase.WORK,
    val totalPhaseSeconds: Int = 60,
    val remainingSeconds: Int = 60,
    val isRunning: Boolean = false,
    val hasStarted: Boolean = false,
    val customTitle: String = "Quick Court Timer",
    val completedInSessionCount: Int = 0,
    val lastNotificationText: String? = null
) {
    val currentExercise: BadmintonExercise? get() = queue.getOrNull(currentIndex)
    val hasNextExercise: Boolean get() = currentIndex + 1 < queue.size
}

class BadmintonViewModel(application: Application) : AndroidViewModel(application) {
    private val database = BadmintonDatabase.getDatabase(application)
    private val settingsStore = SettingsDataStore(application)
    private val repository = BadmintonRepository(database, settingsStore)
    private val profileWriteMutex = Mutex()

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Splash)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _trainingSubTab = MutableStateFlow(TrainingSubTab.PROGRAMS)
    val trainingSubTab: StateFlow<TrainingSubTab> = _trainingSubTab.asStateFlow()

    private val _selectedExercise = MutableStateFlow<BadmintonExercise?>(null)
    val selectedExercise: StateFlow<BadmintonExercise?> = _selectedExercise.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow<TrainingCategory?>(null)
    val selectedCategoryFilter: StateFlow<TrainingCategory?> = _selectedCategoryFilter.asStateFlow()

    private val _selectedProgramId = MutableStateFlow("prog_beginner")
    val selectedProgramId: StateFlow<String> = _selectedProgramId.asStateFlow()

    private val _lastAutoSavedAt = MutableStateFlow<Long?>(null)
    val lastAutoSavedAt: StateFlow<Long?> = _lastAutoSavedAt.asStateFlow()

    private val _profileResetVersion = MutableStateFlow(0)
    val profileResetVersion: StateFlow<Int> = _profileResetVersion.asStateFlow()

    val playerProfile: StateFlow<PlayerProfileEntity?> = repository.playerProfile
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val settings: StateFlow<AppSettings> = repository.settingsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppSettings())

    val completedSessions: StateFlow<List<CompletedSessionEntity>> = repository.completedSessions
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val programCompletions: StateFlow<List<ProgramModuleCompletionEntity>> = repository.programCompletions
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val plannedWorkouts: StateFlow<List<PlannedWorkoutEntity>> = repository.plannedWorkouts
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val personalRecords: StateFlow<List<PersonalRecordEntity>> = repository.personalRecords
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val trainingGoals: StateFlow<List<TrainingGoalEntity>> = repository.trainingGoals
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val achievements: StateFlow<List<AchievementEntity>> = repository.achievements
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val savedCheckpoint: StateFlow<SavedTrainingCheckpointEntity?> = repository.savedCheckpoint
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val programs: List<BadmintonProgram> = repository.programs
    val programExercises: List<BadmintonExercise> = repository.programExercises
    val categoryExercises: List<BadmintonExercise> = repository.categoryExercises
    val fitnessExercises: List<BadmintonExercise> = repository.fitnessExercises
    val rulesLessons: List<BadmintonExercise> = repository.rulesLessons
    val allExercises: List<BadmintonExercise> = repository.allExercises

    val dashboardSummary: StateFlow<DashboardSummary> = combine(
        completedSessions,
        programCompletions,
        settings,
        achievements
    ) { sessions, progDone, currentSettings, achList ->
        computeSummary(
            sessions = sessions,
            progDone = progDone,
            currentSettings = currentSettings,
            categoryExercises = categoryExercises,
            programs = programs,
            achievements = achList
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, DashboardSummary())

    private val _timerState = MutableStateFlow(
        TrainingTimerUiState(
            queue = listOf(categoryExercises.first()),
            totalPhaseSeconds = categoryExercises.first().timerWorkSeconds,
            remainingSeconds = categoryExercises.first().timerWorkSeconds,
            customTitle = categoryExercises.first().name
        )
    )
    val timerState: StateFlow<TrainingTimerUiState> = _timerState.asStateFlow()
    private var timerJob: Job? = null
    private val initJob: Job

    init {
        initJob = viewModelScope.launch {
            DatabaseInitializer.populateIfEmpty(database)
            val storedSettings = repository.settingsFlow.first()
            _selectedProgramId.value = storedSettings.activeProgramId

            // Restore any saved in-progress training checkpoint across app/device restart
            val checkpoint = repository.getCheckpointOnce()
            if (checkpoint != null && checkpoint.isActive && checkpoint.queueIdsSerialized.isNotBlank()) {
                val restoredQueue = checkpoint.queueIdsSerialized
                    .split(",")
                    .mapNotNull { id -> repository.findExerciseById(id.trim()) }
                if (restoredQueue.isNotEmpty()) {
                    val safeIdx = checkpoint.currentIndex.coerceIn(0, restoredQueue.lastIndex)
                    val restoredPhase = runCatching { TimerPhase.valueOf(checkpoint.phase) }
                        .getOrDefault(TimerPhase.WORK)
                    _timerState.value = TrainingTimerUiState(
                        queue = restoredQueue,
                        currentIndex = safeIdx,
                        phase = restoredPhase,
                        totalPhaseSeconds = checkpoint.totalPhaseSeconds.coerceAtLeast(1),
                        remainingSeconds = checkpoint.remainingSeconds.coerceAtLeast(1),
                        isRunning = false,
                        hasStarted = true,
                        customTitle = checkpoint.customTitle.ifBlank { restoredQueue[safeIdx].name },
                        completedInSessionCount = checkpoint.completedInSessionCount,
                        lastNotificationText = "Saved progress restored. Tap Resume to continue!"
                    )
                }
            }
        }

        // Keep achievements automatically evaluated and saved in SQLite whenever sessions or modules change
        viewModelScope.launch {
            combine(completedSessions, programCompletions) { sessions, progDone ->
                sessions to progDone
            }.collect { (sessions, progDone) ->
                evaluateAndPersistAchievements(sessions, progDone)
            }
        }
    }

    companion object {
        fun computeSummary(
            sessions: List<CompletedSessionEntity>,
            progDone: List<ProgramModuleCompletionEntity>,
            currentSettings: AppSettings,
            categoryExercises: List<BadmintonExercise>,
            programs: List<BadmintonProgram>,
            achievements: List<AchievementEntity> = emptyList()
        ): DashboardSummary {
            val today = DateUtils.todayKey()
            val totalMinutes = sessions.sumOf { it.durationMinutes }
            val hoursFormatted = String.format(java.util.Locale.US, "%.1f", totalMinutes / 60f)

            val todayMinutes = sessions.filter { it.dateKey == today }.sumOf { it.durationMinutes }

            // Streak calculation
            val activeDates = sessions.map { it.dateKey }.toSet()
            var streak = 0
            var offset = if (activeDates.contains(today)) 0 else 1
            while (activeDates.contains(DateUtils.daysAgoKey(offset))) {
                streak++
                offset++
            }

            // Weekly chart (last 7 days)
            val weekKeys = DateUtils.lastNDaysKeys(7)
            val weekSet = weekKeys.toSet()
            val weeklyPts = weekKeys.map { k ->
                val dayItems = sessions.filter { it.dateKey == k }
                DailyChartPoint(
                    dateKey = k,
                    label = DateUtils.shortDayLabel(k),
                    minutes = dayItems.sumOf { it.durationMinutes },
                    sessions = dayItems.size
                )
            }
            val weeklyCount = sessions.count { it.dateKey in weekSet }
            val weeklyGoal = currentSettings.weeklySessionsGoal.coerceAtLeast(1)
            val weeklyPct = ((weeklyCount.toFloat() / weeklyGoal) * 100f).toInt().coerceIn(0, 100)

            val dailyGoal = currentSettings.dailyMinutesGoal.coerceAtLeast(1)
            val dailyPct = ((todayMinutes.toFloat() / dailyGoal) * 100f).toInt().coerceIn(0, 100)

            // Monthly 28-day heatmap
            val monthKeys = DateUtils.lastNDaysKeys(28)
            val monthlyPts = monthKeys.mapIndexed { idx, k ->
                val dayItems = sessions.filter { it.dateKey == k }
                DailyChartPoint(
                    dateKey = k,
                    label = "${idx + 1}",
                    minutes = dayItems.sumOf { it.durationMinutes },
                    sessions = dayItems.size
                )
            }

            // Active Program progress
            val activeProg = programs.find { it.id == currentSettings.activeProgramId } ?: programs.first()
            val doneInProg = progDone.count { it.programId == activeProg.id && it.isCompleted }
            val totalInProg = activeProg.moduleIds.size.coerceAtLeast(1)
            val progPct = ((doneInProg.toFloat() / totalInProg) * 100f).toInt().coerceIn(0, 100)

            // Category mastery
            val mastery = TrainingCategory.entries.map { cat ->
                val totalInCat = categoryExercises.count { it.categoryKey == cat.name }.coerceAtLeast(1)
                val catLogs = sessions.filter { it.categoryKey == cat.name }
                val uniqueDone = catLogs.map { it.exerciseId }.distinct().size
                val pct = if (catLogs.isEmpty()) 0 else ((uniqueDone.toFloat() / totalInCat) * 100f).toInt().coerceIn(0, 100)
                CategoryMasteryStat(category = cat, completedCount = catLogs.size, percent = pct)
            }

            val unlockedCount = achievements.count { it.unlocked }
            val totalAchCount = achievements.size.coerceAtLeast(8)

            return DashboardSummary(
                totalSessions = sessions.size,
                totalMinutes = totalMinutes,
                totalHoursFormatted = hoursFormatted,
                streakDays = streak,
                weeklySessionsCount = weeklyCount,
                weeklyGoalSessions = weeklyGoal,
                weeklyProgressPercent = weeklyPct,
                todayMinutes = todayMinutes,
                dailyMinutesGoal = dailyGoal,
                dailyProgressPercent = dailyPct,
                activeProgramTitle = activeProg.title,
                activeProgramDone = doneInProg,
                activeProgramTotal = totalInProg,
                activeProgramPercent = progPct,
                weeklyPoints = weeklyPts,
                monthlyPoints = monthlyPts,
                categoryMastery = mastery,
                unlockedAchievementsCount = unlockedCount,
                totalAchievementsCount = totalAchCount
            )
        }
    }

    private suspend fun evaluateAndPersistAchievements(
        sessions: List<CompletedSessionEntity>,
        progDone: List<ProgramModuleCompletionEntity>
    ) {
        val current = repository.getAchievementsOnce().ifEmpty { DatabaseInitializer.defaultAchievements() }
        val today = DateUtils.todayKey()
        val totalSessions = sessions.size
        val totalMinutes = sessions.sumOf { it.durationMinutes }
        val footworkDone = sessions.count {
            it.categoryKey == TrainingCategory.FOOTWORK.name ||
                it.categoryKey == TrainingCategory.SPEED_AGILITY.name
        }
        val smashDone = sessions.count { it.categoryKey == TrainingCategory.SMASH.name }

        val activeDates = sessions.map { it.dateKey }.toSet()
        var streak = 0
        var offset = if (activeDates.contains(today)) 0 else 1
        while (activeDates.contains(DateUtils.daysAgoKey(offset))) {
            streak++
            offset++
        }

        val maxProgCompleted = programs.maxOfOrNull { p ->
            progDone.count { it.programId == p.id && it.isCompleted }
        } ?: 0

        val updated = current.map { ach ->
            val progress = when (ach.id) {
                "ach_first_rally" -> totalSessions.coerceAtMost(1)
                "ach_hour_court" -> totalMinutes.coerceAtMost(60)
                "ach_footwork_3" -> footworkDone.coerceAtMost(3)
                "ach_smash_2" -> smashDone.coerceAtMost(2)
                "ach_streak_3" -> streak.coerceAtMost(3)
                "ach_streak_7" -> streak.coerceAtMost(7)
                "ach_program_grad" -> maxProgCompleted.coerceAtMost(6)
                "ach_sessions_10" -> totalSessions.coerceAtMost(10)
                else -> ach.progressCurrent
            }
            val nowUnlocked = ach.unlocked || progress >= ach.progressTarget
            ach.copy(
                progressCurrent = progress,
                unlocked = nowUnlocked,
                unlockedDateKey = if (nowUnlocked && ach.unlockedDateKey.isBlank()) today else ach.unlockedDateKey
            )
        }
        if (updated != current) {
            repository.upsertAchievements(updated)
        }
    }

    // Navigation & Startup
    fun onSplashComplete() {
        viewModelScope.launch {
            initJob.join()
            val storedProfile = repository.getProfileOnce()
            val onboarded = storedProfile?.onboardingCompleted == true
            _currentScreen.value = if (onboarded) AppScreen.Home else AppScreen.Welcome
        }
    }

    fun navigateTo(screen: AppScreen) {
        _selectedExercise.value = null
        _currentScreen.value = screen
    }

    fun openTrainingSubTab(
        subTab: TrainingSubTab,
        categoryFilter: TrainingCategory? = null,
        programId: String? = null
    ) {
        _selectedExercise.value = null
        _trainingSubTab.value = subTab
        if (categoryFilter != null || subTab == TrainingSubTab.CATEGORIES) {
            _selectedCategoryFilter.value = categoryFilter
        }
        if (programId != null) {
            _selectedProgramId.value = programId
        }
        _currentScreen.value = AppScreen.Training
    }

    fun selectTrainingSubTab(subTab: TrainingSubTab) {
        _trainingSubTab.value = subTab
    }

    fun selectCategoryFilter(cat: TrainingCategory?) {
        _selectedCategoryFilter.value = cat
    }

    fun selectProgramId(programId: String) {
        _selectedProgramId.value = programId
        viewModelScope.launch { repository.setActiveProgramId(programId) }
    }

    fun openExercise(exercise: BadmintonExercise) {
        _selectedExercise.value = exercise
    }

    fun closeExercise() {
        _selectedExercise.value = null
    }

    // Daily Recommendation based on saved player skill level
    fun recommendedExerciseForToday(): BadmintonExercise {
        val level = SkillLevel.entries.find { it.name == playerProfile.value?.skillLevel } ?: SkillLevel.BEGINNER
        val pool = allExercises.filter { it.difficulty == level && it.section != LessonSection.RULES }
        val daySeed = CalendarDayHelper.dayOfYear()
        return if (pool.isNotEmpty()) pool[daySeed % pool.size] else categoryExercises.first()
    }

    // Next incomplete module in active program for Continue Training
    fun nextProgramModuleToContinue(): Pair<BadmintonProgram, BadmintonExercise>? {
        val activeProg = programs.find { it.id == settings.value.activeProgramId } ?: programs.first()
        val doneIds = programCompletions.value
            .filter { it.programId == activeProg.id && it.isCompleted }
            .map { it.moduleId }
            .toSet()
        val nextModuleId = activeProg.moduleIds.firstOrNull { it !in doneIds } ?: return null
        val exercise = repository.findExerciseById(nextModuleId) ?: return null
        return activeProg to exercise
    }

    fun continueTrainingFromCheckpoint() {
        val cp = savedCheckpoint.value
        if (cp != null && cp.isActive) {
            _trainingSubTab.value = TrainingSubTab.TIMER
            _selectedExercise.value = null
            _currentScreen.value = AppScreen.Training
        } else {
            val nextPair = nextProgramModuleToContinue()
            if (nextPair != null) {
                val (prog, ex) = nextPair
                _selectedProgramId.value = prog.id
                _trainingSubTab.value = TrainingSubTab.PROGRAMS
                _selectedExercise.value = ex
                _currentScreen.value = AppScreen.Training
            } else {
                quickStartRecommendedWorkout()
            }
        }
    }

    // Welcome & Automatic Profile Saving (no Save button required!)
    fun autoUpdatePlayerName(name: String) {
        viewModelScope.launch {
            profileWriteMutex.withLock {
                val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
                repository.upsertProfile(
                    existing.copy(
                        playerName = name,
                        updatedAt = System.currentTimeMillis()
                    )
                )
                _lastAutoSavedAt.value = System.currentTimeMillis()
            }
        }
    }

    fun autoUpdateSkillLevel(skillLevel: SkillLevel) {
        viewModelScope.launch {
            val progId = when (skillLevel) {
                SkillLevel.BEGINNER -> "prog_beginner"
                SkillLevel.INTERMEDIATE -> "prog_intermediate"
                SkillLevel.ADVANCED -> "prog_advanced"
            }
            profileWriteMutex.withLock {
                val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
                repository.upsertProfile(
                    existing.copy(
                        skillLevel = skillLevel.name,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
            repository.setActiveProgramId(progId)
            _selectedProgramId.value = progId
            _lastAutoSavedAt.value = System.currentTimeMillis()
        }
    }

    fun autoUpdatePreferredDuration(minutes: Int) {
        val clamped = minutes.coerceIn(10, 120)
        viewModelScope.launch {
            profileWriteMutex.withLock {
                val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
                repository.upsertProfile(
                    existing.copy(
                        preferredDurationMinutes = clamped,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
            repository.setPreferredDuration(clamped)
            repository.setDailyMinutesGoal(clamped)
            _lastAutoSavedAt.value = System.currentTimeMillis()
        }
    }

    fun autoUpdateTrainingGoals(goals: String) {
        viewModelScope.launch {
            profileWriteMutex.withLock {
                val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
                repository.upsertProfile(
                    existing.copy(
                        trainingGoals = goals,
                        updatedAt = System.currentTimeMillis()
                    )
                )
                _lastAutoSavedAt.value = System.currentTimeMillis()
            }
        }
    }

    fun completeOnboarding(
        name: String,
        skillLevel: SkillLevel,
        preferredDurationMinutes: Int,
        goalText: String
    ) {
        viewModelScope.launch {
            val progId = when (skillLevel) {
                SkillLevel.BEGINNER -> "prog_beginner"
                SkillLevel.INTERMEDIATE -> "prog_intermediate"
                SkillLevel.ADVANCED -> "prog_advanced"
            }
            profileWriteMutex.withLock {
                val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
                val resolvedName = name.trim().ifBlank { existing.playerName.trim() }.ifBlank { "Player" }
                repository.upsertProfile(
                    existing.copy(
                        playerName = resolvedName,
                        skillLevel = skillLevel.name,
                        preferredDurationMinutes = preferredDurationMinutes.coerceIn(10, 120),
                        trainingGoals = goalText.trim().ifBlank { existing.trainingGoals },
                        onboardingCompleted = true,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
            repository.setPreferredDuration(preferredDurationMinutes)
            repository.setDailyMinutesGoal(preferredDurationMinutes)
            repository.setActiveProgramId(progId)
            _selectedProgramId.value = progId
            _lastAutoSavedAt.value = System.currentTimeMillis()
            _currentScreen.value = AppScreen.Home
        }
    }

    // Exercise & Program Logging (automatically records date, duration, drills, and results)
    fun logExerciseCompleted(
        exercise: BadmintonExercise,
        resultSummary: String = "Completed • ${exercise.recommendedReps}"
    ) {
        viewModelScope.launch {
            repository.insertCompletedSession(
                CompletedSessionEntity(
                    exerciseId = exercise.id,
                    dateKey = DateUtils.todayKey(),
                    categoryKey = exercise.categoryKey,
                    categoryLabel = exercise.categoryLabel,
                    title = exercise.name,
                    durationMinutes = exercise.durationMinutes,
                    drillsSummary = exercise.recommendedReps,
                    resultSummary = resultSummary
                )
            )
            if (settings.value.soundEnabled) {
                SoundAndReminderHelper.playCueBeep(isFinishOrRestEnd = true, toneIndex = settings.value.soundToneIndex)
            }
        }
    }

    fun deleteCompletedSession(id: Long) {
        viewModelScope.launch { repository.deleteCompletedSession(id) }
    }

    fun setActiveProgram(programId: String) {
        _selectedProgramId.value = programId
        viewModelScope.launch { repository.setActiveProgramId(programId) }
    }

    fun toggleProgramModuleCompleted(programId: String, moduleId: String, currentlyCompleted: Boolean) {
        viewModelScope.launch {
            val next = !currentlyCompleted
            repository.upsertProgramModuleCompletion(
                ProgramModuleCompletionEntity(
                    programId = programId,
                    moduleId = moduleId,
                    isCompleted = next,
                    completedDateKey = if (next) DateUtils.todayKey() else ""
                )
            )
            val syncId = "prog_${programId}_$moduleId"
            if (next) {
                val ex = repository.findExerciseById(moduleId)
                val prog = programs.find { it.id == programId }
                repository.deleteCompletedByExerciseId(syncId)
                repository.insertCompletedSession(
                    CompletedSessionEntity(
                        exerciseId = syncId,
                        dateKey = DateUtils.todayKey(),
                        categoryKey = ex?.categoryKey ?: "PROGRAM",
                        categoryLabel = prog?.title ?: "Training Program",
                        title = ex?.name ?: "Program Module",
                        durationMinutes = ex?.durationMinutes ?: 15,
                        drillsSummary = ex?.recommendedReps ?: "Program Module",
                        resultSummary = "Module Completed (${prog?.level?.displayName ?: "Program"})"
                    )
                )
                if (settings.value.soundEnabled) {
                    SoundAndReminderHelper.playCueBeep(true, settings.value.soundToneIndex)
                }
            } else {
                repository.deleteCompletedByExerciseId(syncId)
            }
        }
    }

    fun resetProgram(programId: String) {
        viewModelScope.launch { repository.resetProgram(programId) }
    }

    // Interactive Training Timer Queue, Checkpointing & Controls
    private fun persistTimerCheckpoint(state: TrainingTimerUiState, active: Boolean) {
        viewModelScope.launch {
            if (!active || state.queue.isEmpty() || state.phase == TimerPhase.FINISHED) {
                repository.clearCheckpoint()
            } else {
                repository.upsertCheckpoint(
                    SavedTrainingCheckpointEntity(
                        id = 1,
                        queueIdsSerialized = state.queue.joinToString(",") { it.id },
                        customTitle = state.customTitle,
                        currentIndex = state.currentIndex,
                        phase = state.phase.name,
                        totalPhaseSeconds = state.totalPhaseSeconds,
                        remainingSeconds = state.remainingSeconds,
                        completedInSessionCount = state.completedInSessionCount,
                        isActive = true,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun launchExercisesInTimer(exercises: List<BadmintonExercise>, title: String? = null) {
        if (exercises.isEmpty()) return
        pauseTimer()
        val first = exercises.first()
        val newState = TrainingTimerUiState(
            queue = exercises,
            currentIndex = 0,
            phase = TimerPhase.WORK,
            totalPhaseSeconds = first.timerWorkSeconds,
            remainingSeconds = first.timerWorkSeconds,
            isRunning = false,
            hasStarted = false,
            customTitle = title ?: first.name,
            completedInSessionCount = 0,
            lastNotificationText = "Loaded ${exercises.size} exercise(s). Tap Start when ready!"
        )
        _timerState.value = newState
        persistTimerCheckpoint(newState, active = true)
        _selectedExercise.value = null
        _trainingSubTab.value = TrainingSubTab.TIMER
        _currentScreen.value = AppScreen.Training
    }

    fun quickStartRecommendedWorkout() {
        val rec = recommendedExerciseForToday()
        val warmup = fitnessExercises.first() // Warm-up
        val cooldown = fitnessExercises.last() // Cool-down
        launchExercisesInTimer(listOf(warmup, rec, cooldown), "Daily Recommended Session")
    }

    fun startOrResumeTimer() {
        val s = _timerState.value
        if (s.isRunning) return
        if (s.phase == TimerPhase.FINISHED) {
            resetTimer()
        }
        val active = _timerState.value
        val updated = active.copy(
            isRunning = true,
            hasStarted = true,
            lastNotificationText = if (active.phase == TimerPhase.WORK) "Training active — keep form sharp!" else "Rest recovery active..."
        )
        _timerState.value = updated
        persistTimerCheckpoint(updated, active = true)
        playCue(isFinish = false)
        startTimerTicker()
    }

    fun pauseTimer() {
        timerJob?.cancel()
        val updated = _timerState.value.copy(isRunning = false)
        _timerState.value = updated
        if (updated.hasStarted && updated.phase != TimerPhase.FINISHED) {
            persistTimerCheckpoint(updated, active = true)
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        val curEx = _timerState.value.currentExercise
        val workSec = curEx?.timerWorkSeconds ?: _timerState.value.totalPhaseSeconds.coerceAtLeast(30)
        val updated = _timerState.value.copy(
            phase = TimerPhase.WORK,
            totalPhaseSeconds = workSec,
            remainingSeconds = workSec,
            isRunning = false,
            hasStarted = false,
            lastNotificationText = "Timer reset."
        )
        _timerState.value = updated
        persistTimerCheckpoint(updated, active = _timerState.value.queue.isNotEmpty())
    }

    fun startRestTimerNow(restSeconds: Int = 30) {
        timerJob?.cancel()
        val updated = _timerState.value.copy(
            phase = TimerPhase.REST,
            totalPhaseSeconds = restSeconds,
            remainingSeconds = restSeconds,
            isRunning = true,
            hasStarted = true,
            lastNotificationText = "Rest timer started (${restSeconds}s)"
        )
        _timerState.value = updated
        persistTimerCheckpoint(updated, active = true)
        playCue(isFinish = false)
        startTimerTicker()
    }

    fun setCustomCountdownSeconds(seconds: Int) {
        timerJob?.cancel()
        val clamped = seconds.coerceIn(15, 1800)
        val updated = _timerState.value.copy(
            phase = TimerPhase.WORK,
            totalPhaseSeconds = clamped,
            remainingSeconds = clamped,
            isRunning = false,
            hasStarted = false,
            lastNotificationText = "Countdown set to ${clamped}s"
        )
        _timerState.value = updated
        persistTimerCheckpoint(updated, active = true)
    }

    fun nextExerciseInTimer() {
        timerJob?.cancel()
        val s = _timerState.value
        val curEx = s.currentExercise
        if (curEx != null) {
            val elapsedSec = (s.totalPhaseSeconds - s.remainingSeconds).coerceAtLeast(1)
            val resultMsg = if (s.phase == TimerPhase.WORK && s.hasStarted) {
                "Timer Completed (${elapsedSec}s) • ${curEx.recommendedReps}"
            } else {
                "Drill Completed • ${curEx.recommendedReps}"
            }
            logExerciseCompleted(curEx, resultSummary = resultMsg)
        }
        val nextIdx = s.currentIndex + 1
        if (nextIdx < s.queue.size) {
            val nextEx = s.queue[nextIdx]
            val updated = s.copy(
                currentIndex = nextIdx,
                phase = TimerPhase.WORK,
                totalPhaseSeconds = nextEx.timerWorkSeconds,
                remainingSeconds = nextEx.timerWorkSeconds,
                isRunning = false,
                hasStarted = true,
                completedInSessionCount = s.completedInSessionCount + 1,
                lastNotificationText = "Ready for: ${nextEx.name}"
            )
            _timerState.value = updated
            persistTimerCheckpoint(updated, active = true)
            playCue(isFinish = true)
        } else {
            val finished = s.copy(
                phase = TimerPhase.FINISHED,
                remainingSeconds = 0,
                isRunning = false,
                completedInSessionCount = s.completedInSessionCount + (if (curEx != null) 1 else 0),
                lastNotificationText = "Session complete & saved to Training History!"
            )
            _timerState.value = finished
            persistTimerCheckpoint(finished, active = false)
            playCue(isFinish = true)
        }
    }

    private fun startTimerTicker() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var tickCounter = 0
            while (_timerState.value.isRunning) {
                delay(1000L)
                val cur = _timerState.value
                if (!cur.isRunning) break
                tickCounter++
                if (cur.remainingSeconds > 1) {
                    val nextState = cur.copy(remainingSeconds = cur.remainingSeconds - 1)
                    _timerState.value = nextState
                    if (tickCounter % 5 == 0) {
                        persistTimerCheckpoint(nextState, active = true)
                    }
                } else {
                    if (cur.phase == TimerPhase.WORK) {
                        val doneEx = cur.currentExercise
                        if (doneEx != null) {
                            logExerciseCompleted(
                                doneEx,
                                resultSummary = "Full Timer (${cur.totalPhaseSeconds}s) • 100%"
                            )
                        }
                        val restSec = doneEx?.restTimeSeconds ?: 30
                        if (cur.hasNextExercise && restSec > 0) {
                            val restState = cur.copy(
                                phase = TimerPhase.REST,
                                totalPhaseSeconds = restSec,
                                remainingSeconds = restSec,
                                isRunning = true,
                                completedInSessionCount = cur.completedInSessionCount + 1,
                                lastNotificationText = "Work done! Rest ${restSec}s before next drill."
                            )
                            _timerState.value = restState
                            persistTimerCheckpoint(restState, active = true)
                            playCue(isFinish = true)
                        } else if (cur.hasNextExercise) {
                            nextExerciseInTimer()
                            break
                        } else {
                            val finished = cur.copy(
                                phase = TimerPhase.FINISHED,
                                remainingSeconds = 0,
                                isRunning = false,
                                completedInSessionCount = cur.completedInSessionCount + 1,
                                lastNotificationText = "Workout finished & saved to Progress!"
                            )
                            _timerState.value = finished
                            persistTimerCheckpoint(finished, active = false)
                            playCue(isFinish = true)
                            break
                        }
                    } else if (cur.phase == TimerPhase.REST) {
                        if (cur.hasNextExercise) {
                            val nextIdx = cur.currentIndex + 1
                            val nextEx = cur.queue[nextIdx]
                            val nextState = cur.copy(
                                currentIndex = nextIdx,
                                phase = TimerPhase.WORK,
                                totalPhaseSeconds = nextEx.timerWorkSeconds,
                                remainingSeconds = nextEx.timerWorkSeconds,
                                isRunning = true,
                                lastNotificationText = "Rest over! Next: ${nextEx.name}"
                            )
                            _timerState.value = nextState
                            persistTimerCheckpoint(nextState, active = true)
                            playCue(isFinish = true)
                        } else {
                            val finished = cur.copy(
                                phase = TimerPhase.FINISHED,
                                remainingSeconds = 0,
                                isRunning = false,
                                lastNotificationText = "Rest complete!"
                            )
                            _timerState.value = finished
                            persistTimerCheckpoint(finished, active = false)
                            playCue(isFinish = true)
                            break
                        }
                    }
                }
            }
        }
    }

    private fun playCue(isFinish: Boolean) {
        if (settings.value.soundEnabled) {
            SoundAndReminderHelper.playCueBeep(
                isFinishOrRestEnd = isFinish,
                toneIndex = settings.value.soundToneIndex
            )
        }
    }

    // Daily Workout Planner
    fun addPlannedWorkout(
        dayOfWeek: String,
        title: String,
        categoryLabel: String,
        selectedExercises: List<BadmintonExercise>,
        durationMinutes: Int
    ) {
        if (title.isBlank() && selectedExercises.isEmpty()) return
        val finalTitle = title.trim().ifBlank {
            selectedExercises.joinToString(" + ") { it.name }.take(48)
        }
        val ids = selectedExercises.joinToString(",") { it.id }
        viewModelScope.launch {
            repository.insertPlannedWorkout(
                PlannedWorkoutEntity(
                    dayOfWeek = dayOfWeek,
                    title = finalTitle,
                    categoryLabel = categoryLabel.ifBlank { selectedExercises.firstOrNull()?.categoryLabel ?: "Custom Workout" },
                    exerciseIdsSerialized = ids,
                    durationMinutes = durationMinutes.coerceIn(5, 180),
                    isCompleted = false
                )
            )
        }
    }

    fun togglePlannedWorkoutCompleted(workout: PlannedWorkoutEntity) {
        viewModelScope.launch {
            val nextDone = !workout.isCompleted
            repository.updatePlannedWorkout(workout.copy(isCompleted = nextDone))
            val syncId = "plan_${workout.id}"
            if (nextDone) {
                val drillsText = workout.exerciseIdsSerialized
                    .split(",")
                    .mapNotNull { id -> repository.findExerciseById(id.trim())?.name }
                    .joinToString(", ")
                    .ifBlank { workout.title }
                repository.deleteCompletedByExerciseId(syncId)
                repository.insertCompletedSession(
                    CompletedSessionEntity(
                        exerciseId = syncId,
                        dateKey = DateUtils.todayKey(),
                        categoryKey = "PLANNER",
                        categoryLabel = workout.categoryLabel,
                        title = workout.title,
                        durationMinutes = workout.durationMinutes,
                        drillsSummary = drillsText,
                        resultSummary = "${workout.dayOfWeek} Planned Workout Completed"
                    )
                )
                playCue(isFinish = true)
            } else {
                repository.deleteCompletedByExerciseId(syncId)
            }
        }
    }

    fun deletePlannedWorkout(workout: PlannedWorkoutEntity) {
        viewModelScope.launch { repository.deletePlannedWorkout(workout) }
    }

    fun startPlannedWorkoutInTimer(workout: PlannedWorkoutEntity) {
        val exercises = workout.exerciseIdsSerialized
            .split(",")
            .mapNotNull { id -> repository.findExerciseById(id.trim()) }
            .ifEmpty { listOf(recommendedExerciseForToday()) }
        launchExercisesInTimer(exercises, "${workout.dayOfWeek}: ${workout.title}")
    }

    // Personal Records & Goals
    fun addPersonalRecord(title: String, value: String, unit: String) {
        if (title.isBlank() || value.isBlank()) return
        viewModelScope.launch {
            repository.insertRecord(
                PersonalRecordEntity(
                    title = title.trim(),
                    valueText = value.trim(),
                    unit = unit.trim(),
                    updatedDateKey = DateUtils.todayKey()
                )
            )
        }
    }

    fun updatePersonalRecord(record: PersonalRecordEntity, newValue: String) {
        if (newValue.isBlank()) return
        viewModelScope.launch {
            repository.updateRecord(
                record.copy(valueText = newValue.trim(), updatedDateKey = DateUtils.todayKey())
            )
        }
    }

    fun deletePersonalRecord(record: PersonalRecordEntity) {
        viewModelScope.launch { repository.deleteRecord(record) }
    }

    fun addTrainingGoal(title: String, category: String, targetValue: Int, unit: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insertGoal(
                TrainingGoalEntity(
                    title = title.trim(),
                    category = category.trim().ifBlank { "Footwork" },
                    targetValue = targetValue.coerceIn(1, 9999),
                    currentValue = 0,
                    unit = unit.trim().ifBlank { "sessions" },
                    isCompleted = false,
                    createdDateKey = DateUtils.todayKey()
                )
            )
        }
    }

    fun incrementGoal(goal: TrainingGoalEntity, delta: Int) {
        viewModelScope.launch {
            val nextVal = (goal.currentValue + delta).coerceIn(0, goal.targetValue)
            val done = nextVal >= goal.targetValue
            repository.updateGoal(goal.copy(currentValue = nextVal, isCompleted = done))
            if (done && !goal.isCompleted) playCue(isFinish = true)
        }
    }

    fun deleteGoal(goal: TrainingGoalEntity) {
        viewModelScope.launch { repository.deleteGoal(goal) }
    }

    // Settings
    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { repository.setDarkTheme(enabled) }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setSoundEnabled(enabled) }
    }

    fun setSoundToneIndex(index: Int) {
        viewModelScope.launch {
            repository.setSoundToneIndex(index)
            if (settings.value.soundEnabled) {
                SoundAndReminderHelper.playCueBeep(isFinishOrRestEnd = true, toneIndex = index)
            }
        }
    }

    fun setDailyMinutesGoal(minutes: Int) {
        viewModelScope.launch { repository.setDailyMinutesGoal(minutes) }
    }

    fun setWeeklySessionsGoal(sessions: Int) {
        viewModelScope.launch { repository.setWeeklySessionsGoal(sessions) }
    }

    fun resetAllTrainingProgress() {
        pauseTimer()
        viewModelScope.launch {
            repository.resetAllProgress()
            _selectedExercise.value = null
            _selectedProgramId.value = "prog_beginner"
            val defaultEx = categoryExercises.first()
            _timerState.value = TrainingTimerUiState(
                queue = listOf(defaultEx),
                totalPhaseSeconds = defaultEx.timerWorkSeconds,
                remainingSeconds = defaultEx.timerWorkSeconds,
                customTitle = defaultEx.name
            )
            _lastAutoSavedAt.value = null
            _profileResetVersion.value = _profileResetVersion.value + 1
            _currentScreen.value = AppScreen.Welcome
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

private object CalendarDayHelper {
    fun dayOfYear(): Int = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
}
