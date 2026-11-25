package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.DatabaseInitializer
import com.example.data.local.DateUtils
import com.example.data.local.FootballDatabase
import com.example.data.local.SettingsDataStore
import com.example.data.model.AchievementEntity
import com.example.data.model.AppSettings
import com.example.data.model.CompletedSessionEntity
import com.example.data.model.DailyPlanItemEntity
import com.example.data.model.FootballExercise
import com.example.data.model.FootballProgram
import com.example.data.model.LessonSection
import com.example.data.model.PersonalRecordEntity
import com.example.data.model.PlannedWorkoutEntity
import com.example.data.model.PlayerPosition
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.ProgramModuleCompletionEntity
import com.example.data.model.SavedTrainingCheckpointEntity
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory
import com.example.data.model.TrainingGoalEntity
import com.example.data.repository.DailyPlanGenerator
import com.example.data.repository.FootballRepository
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
import java.util.Calendar
import java.util.Locale

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
    val totalCompletedDrills: Int = 0,
    val totalMinutes: Int = 0,
    val totalHoursFormatted: String = "0.0",
    val streakDays: Int = 0,
    val weeklySessionsCount: Int = 0,
    val weeklyGoalSessions: Int = 5,
    val weeklyProgressPercent: Int = 0,
    val todayMinutes: Int = 0,
    val dailyMinutesGoal: Int = 30,
    val dailyProgressPercent: Int = 0,
    val activeProgramTitle: String = "Beginner Foundations",
    val activeProgramDone: Int = 0,
    val activeProgramTotal: Int = 6,
    val activeProgramPercent: Int = 0,
    val weeklyPoints: List<DailyChartPoint> = emptyList(),
    val monthlyPoints: List<DailyChartPoint> = emptyList(),
    val categoryMastery: List<CategoryMasteryStat> = emptyList(),
    val unlockedAchievementsCount: Int = 0,
    val totalAchievementsCount: Int = 9
)

enum class TrainingSubTab(val label: String) {
    CATEGORIES("8 Categories (61)"),
    PROGRAMS("Programs"),
    FITNESS("Warmup & Recovery"),
    RULES("Tactics & Rules")
}

enum class TimerPhase {
    WORK,
    REST,
    FINISHED
}

data class TrainingTimerUiState(
    val queue: List<FootballExercise> = emptyList(),
    val currentIndex: Int = 0,
    val phase: TimerPhase = TimerPhase.WORK,
    val totalPhaseSeconds: Int = 60,
    val remainingSeconds: Int = 60,
    val isRunning: Boolean = false,
    val hasStarted: Boolean = false,
    val customTitle: String = "Football Training Timer",
    val completedInSessionCount: Int = 0,
    val lastNotificationText: String? = null
) {
    val currentExercise: FootballExercise? get() = queue.getOrNull(currentIndex)
    val hasNextExercise: Boolean get() = currentIndex + 1 < queue.size
    val hasPreviousExercise: Boolean get() = currentIndex > 0
}

class FootballViewModel(application: Application) : AndroidViewModel(application) {
    private val database = FootballDatabase.getDatabase(application)
    private val settingsStore = SettingsDataStore(application)
    private val repository = FootballRepository(database, settingsStore)
    private val profileWriteMutex = Mutex()

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Splash)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _trainingSubTab = MutableStateFlow(TrainingSubTab.CATEGORIES)
    val trainingSubTab: StateFlow<TrainingSubTab> = _trainingSubTab.asStateFlow()

    private val _selectedExercise = MutableStateFlow<FootballExercise?>(null)
    val selectedExercise: StateFlow<FootballExercise?> = _selectedExercise.asStateFlow()

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

    val dailyPlanItems: StateFlow<List<DailyPlanItemEntity>> = repository.dailyPlanItems
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

    val programs: List<FootballProgram> = repository.programs
    val programExercises: List<FootballExercise> = repository.programExercises
    val categoryExercises: List<FootballExercise> = repository.categoryExercises
    val fitnessExercises: List<FootballExercise> = repository.fitnessExercises
    val rulesLessons: List<FootballExercise> = repository.rulesLessons
    val allExercises: List<FootballExercise> = repository.allExercises

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

            ensureTodayDailyPlanGenerated()

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
                        lastNotificationText = "Saved pitch progress restored. Tap Resume to continue!"
                    )
                }
            }
        }

        // Automatically evaluate & save all 9 achievements in SQLite whenever sessions change
        viewModelScope.launch {
            completedSessions.collect { sessions ->
                evaluateAndPersistAchievements(sessions)
            }
        }
    }

    companion object {
        fun computeSummary(
            sessions: List<CompletedSessionEntity>,
            progDone: List<ProgramModuleCompletionEntity>,
            currentSettings: AppSettings,
            categoryExercises: List<FootballExercise>,
            programs: List<FootballProgram>,
            achievements: List<AchievementEntity> = emptyList()
        ): DashboardSummary {
            val today = DateUtils.todayKey()
            val totalMinutes = sessions.sumOf { it.durationMinutes }
            val totalDrills = sessions.sumOf { it.completedDrillsCount.coerceAtLeast(1) }
            val hoursFormatted = String.format(Locale.US, "%.1f", totalMinutes / 60f)
            val todayMinutes = sessions.filter { it.dateKey == today }.sumOf { it.durationMinutes }

            val activeDates = sessions.map { it.dateKey }.toSet()
            var streak = 0
            var offset = if (activeDates.contains(today)) 0 else 1
            while (activeDates.contains(DateUtils.daysAgoKey(offset))) {
                streak++
                offset++
            }

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

            val activeProg = programs.find { it.id == currentSettings.activeProgramId } ?: programs.first()
            val doneInProg = progDone.count { it.programId == activeProg.id && it.isCompleted }
            val totalInProg = activeProg.moduleIds.size.coerceAtLeast(1)
            val progPct = ((doneInProg.toFloat() / totalInProg) * 100f).toInt().coerceIn(0, 100)

            val mastery = TrainingCategory.entries.map { cat ->
                val totalInCat = categoryExercises.count { it.categoryKey == cat.name }.coerceAtLeast(1)
                val catLogs = sessions.filter { it.categoryKey == cat.name }
                val uniqueDone = catLogs.map { it.exerciseId }.distinct().size
                val pct = if (catLogs.isEmpty()) 0 else ((uniqueDone.toFloat() / totalInCat) * 100f).toInt().coerceIn(0, 100)
                CategoryMasteryStat(category = cat, completedCount = catLogs.size, percent = pct)
            }

            val unlockedCount = achievements.count { it.unlocked }
            val totalAchCount = achievements.size.coerceAtLeast(9)

            return DashboardSummary(
                totalSessions = sessions.size,
                totalCompletedDrills = totalDrills,
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

    private suspend fun evaluateAndPersistAchievements(sessions: List<CompletedSessionEntity>) {
        val current = repository.getAchievementsOnce().ifEmpty { DatabaseInitializer.defaultAchievements() }
        val today = DateUtils.todayKey()
        val totalSessions = sessions.size
        val totalDrills = sessions.sumOf { it.completedDrillsCount.coerceAtLeast(1) }
        val totalMinutes = sessions.sumOf { it.durationMinutes }

        val ballMasterCount = sessions.count {
            it.categoryKey == TrainingCategory.BALL_MASTERY.name ||
                it.categoryKey == TrainingCategory.DRIBBLING.name ||
                it.categoryKey == TrainingCategory.PASSING.name
        }
        val speedMasterCount = sessions.count {
            it.categoryKey == TrainingCategory.SPEED_AGILITY.name ||
                it.categoryKey == TrainingCategory.PHYSICAL_CONDITIONING.name
        }
        val shootingMasterCount = sessions.count {
            it.categoryKey == TrainingCategory.SHOOTING.name
        }

        val activeDates = sessions.map { it.dateKey }.toSet()
        var streak = 0
        var offset = if (activeDates.contains(today)) 0 else 1
        while (activeDates.contains(DateUtils.daysAgoKey(offset))) {
            streak++
            offset++
        }

        val updated = current.map { ach ->
            val progress = when (ach.id) {
                "ach_first_training" -> totalSessions.coerceAtMost(1)
                "ach_sessions_5" -> totalSessions.coerceAtMost(5)
                "ach_sessions_10" -> totalSessions.coerceAtMost(10)
                "ach_drills_50" -> totalDrills.coerceAtMost(50)
                "ach_hours_5" -> totalMinutes.coerceAtMost(300)
                "ach_streak_7" -> streak.coerceAtMost(7)
                "ach_ball_master" -> ballMasterCount.coerceAtMost(5)
                "ach_speed_master" -> speedMasterCount.coerceAtMost(5)
                "ach_shooting_master" -> shootingMasterCount.coerceAtMost(5)
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

    // Personalized Daily Plan Generation & Continuation
    private suspend fun ensureTodayDailyPlanGenerated(forceRegenerate: Boolean = false) {
        val today = DateUtils.todayKey()
        val existing = repository.getDailyPlanOnce()
        val profile = repository.getProfileOnce() ?: PlayerProfileEntity()
        val needsNew = forceRegenerate || existing.isEmpty() || existing.first().planDateKey != today
        if (needsNew) {
            val generated = DailyPlanGenerator.generatePlanItems(
                profile = profile,
                allCategoryExercises = categoryExercises,
                fitnessExercises = fitnessExercises,
                dateKey = today
            )
            repository.replaceDailyPlan(generated)
        }
    }

    fun regenerateDailyPlan() {
        viewModelScope.launch { ensureTodayDailyPlanGenerated(forceRegenerate = true) }
    }

    fun toggleDailyPlanItemCompleted(item: DailyPlanItemEntity) {
        viewModelScope.launch {
            val nextDone = !item.isCompleted
            repository.updateDailyPlanItem(item.copy(isCompleted = nextDone))
            val syncId = "daily_${item.planDateKey}_${item.id}"
            if (nextDone && !item.isRestSlot) {
                val ex = repository.findExerciseById(item.exerciseId)
                repository.deleteCompletedByExerciseId(syncId)
                repository.insertCompletedSession(
                    CompletedSessionEntity(
                        exerciseId = syncId,
                        dateKey = DateUtils.todayKey(),
                        categoryKey = ex?.categoryKey ?: TrainingCategory.BALL_MASTERY.name,
                        categoryLabel = ex?.categoryLabel ?: item.categoryLabel,
                        title = item.title,
                        durationMinutes = (item.durationSeconds / 60).coerceAtLeast(3),
                        completedDrillsCount = 1,
                        drillsSummary = "${item.sectionLabel} • ${ex?.recommendedReps ?: "${item.durationSeconds}s"}",
                        resultSummary = "Completed in Daily Plan"
                    )
                )
                playCue(isFinish = true)
            } else if (!nextDone) {
                repository.deleteCompletedByExerciseId(syncId)
            }
        }
    }

    fun startDailyPlanInTimer(remainingOnly: Boolean = true) {
        val items = dailyPlanItems.value
        val drillExercises = items
            .filter { !it.isRestSlot && (!remainingOnly || !it.isCompleted) }
            .mapNotNull { repository.findExerciseById(it.exerciseId) }
            .ifEmpty {
                items.filter { !it.isRestSlot }.mapNotNull { repository.findExerciseById(it.exerciseId) }
            }
            .ifEmpty { listOf(recommendedExerciseForToday()) }
        launchExercisesInTimer(drillExercises, "Today's Personalized Plan")
    }

    fun hasUnfinishedSession(): Boolean {
        val cp = savedCheckpoint.value
        if (cp != null && cp.isActive) return true
        val drills = dailyPlanItems.value.filter { !it.isRestSlot }
        val doneCount = drills.count { it.isCompleted }
        return doneCount in 1 until drills.size
    }

    fun continueOrStartTodaysTraining() {
        val cp = savedCheckpoint.value
        if (cp != null && cp.isActive) {
            _selectedExercise.value = null
            _currentScreen.value = AppScreen.Timer
        } else {
            startDailyPlanInTimer(remainingOnly = true)
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

    fun openExercise(exercise: FootballExercise) {
        _selectedExercise.value = exercise
        _currentScreen.value = AppScreen.Training
    }

    fun closeExercise() {
        _selectedExercise.value = null
    }

    fun recommendedExerciseForToday(): FootballExercise {
        val level = SkillLevel.entries.find { it.name == playerProfile.value?.skillLevel } ?: SkillLevel.BEGINNER
        val pool = categoryExercises.filter { it.difficulty == level }
        val daySeed = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        return if (pool.isNotEmpty()) pool[daySeed % pool.size] else categoryExercises.first()
    }

    // Automatic Profile Saving (No Save Button Required)
    fun autoUpdatePlayerName(name: String) {
        viewModelScope.launch {
            profileWriteMutex.withLock {
                val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
                repository.upsertProfile(existing.copy(playerName = name, updatedAt = System.currentTimeMillis()))
                _lastAutoSavedAt.value = System.currentTimeMillis()
            }
        }
    }

    fun autoUpdatePlayerAge(age: Int) {
        val clamped = age.coerceIn(5, 85)
        viewModelScope.launch {
            profileWriteMutex.withLock {
                val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
                repository.upsertProfile(existing.copy(age = clamped, updatedAt = System.currentTimeMillis()))
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
                repository.upsertProfile(existing.copy(skillLevel = skillLevel.name, updatedAt = System.currentTimeMillis()))
            }
            repository.setActiveProgramId(progId)
            _selectedProgramId.value = progId
            if (dailyPlanItems.value.none { it.isCompleted }) {
                ensureTodayDailyPlanGenerated(forceRegenerate = true)
            }
            _lastAutoSavedAt.value = System.currentTimeMillis()
        }
    }

    fun autoUpdatePosition(position: PlayerPosition) {
        viewModelScope.launch {
            profileWriteMutex.withLock {
                val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
                repository.upsertProfile(existing.copy(playerPosition = position.name, updatedAt = System.currentTimeMillis()))
            }
            if (dailyPlanItems.value.none { it.isCompleted }) {
                ensureTodayDailyPlanGenerated(forceRegenerate = true)
            }
            _lastAutoSavedAt.value = System.currentTimeMillis()
        }
    }

    fun autoUpdatePreferredDuration(minutes: Int) {
        val clamped = minutes.coerceIn(15, 60)
        viewModelScope.launch {
            profileWriteMutex.withLock {
                val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
                repository.upsertProfile(existing.copy(preferredDurationMinutes = clamped, updatedAt = System.currentTimeMillis()))
            }
            repository.setPreferredDuration(clamped)
            repository.setDailyMinutesGoal(clamped)
            if (dailyPlanItems.value.none { it.isCompleted }) {
                ensureTodayDailyPlanGenerated(forceRegenerate = true)
            }
            _lastAutoSavedAt.value = System.currentTimeMillis()
        }
    }

    fun autoUpdateTrainingGoals(goals: String) {
        viewModelScope.launch {
            profileWriteMutex.withLock {
                val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
                repository.upsertProfile(existing.copy(trainingGoals = goals, updatedAt = System.currentTimeMillis()))
            }
            _lastAutoSavedAt.value = System.currentTimeMillis()
        }
    }

    fun completeOnboarding(
        name: String,
        age: Int,
        skillLevel: SkillLevel,
        position: PlayerPosition,
        preferredDurationMinutes: Int,
        goalText: String
    ) {
        viewModelScope.launch {
            val progId = when (skillLevel) {
                SkillLevel.BEGINNER -> "prog_beginner"
                SkillLevel.INTERMEDIATE -> "prog_intermediate"
                SkillLevel.ADVANCED -> "prog_advanced"
            }
            val safeDuration = preferredDurationMinutes.coerceIn(15, 60)
            profileWriteMutex.withLock {
                val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
                val resolvedName = name.trim().ifBlank { existing.playerName.trim() }.ifBlank { "Player" }
                repository.upsertProfile(
                    existing.copy(
                        playerName = resolvedName,
                        age = age.coerceIn(5, 85),
                        skillLevel = skillLevel.name,
                        playerPosition = position.name,
                        preferredDurationMinutes = safeDuration,
                        trainingGoals = goalText.trim().ifBlank { existing.trainingGoals },
                        onboardingCompleted = true,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
            repository.setPreferredDuration(safeDuration)
            repository.setDailyMinutesGoal(safeDuration)
            repository.setActiveProgramId(progId)
            _selectedProgramId.value = progId
            ensureTodayDailyPlanGenerated(forceRegenerate = true)
            _lastAutoSavedAt.value = System.currentTimeMillis()
            _currentScreen.value = AppScreen.Home
        }
    }

    // Exercise & Session Logging
    fun logExerciseCompleted(
        exercise: FootballExercise,
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
                    completedDrillsCount = 1,
                    drillsSummary = exercise.recommendedReps,
                    resultSummary = resultSummary
                )
            )
            // Also check off matching item in today's daily plan if present
            val planMatch = dailyPlanItems.value.find { !it.isCompleted && it.exerciseId == exercise.id }
            if (planMatch != null) {
                repository.updateDailyPlanItem(planMatch.copy(isCompleted = true))
            }
            playCue(isFinish = true)
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
                        completedDrillsCount = 1,
                        drillsSummary = ex?.recommendedReps ?: "Program Module",
                        resultSummary = "Module Completed (${prog?.level?.displayName ?: "Program"})"
                    )
                )
                playCue(isFinish = true)
            } else {
                repository.deleteCompletedByExerciseId(syncId)
            }
        }
    }

    fun resetProgram(programId: String) {
        viewModelScope.launch { repository.resetProgram(programId) }
    }

    // Interactive Training Timer Queue, Checkpointing, Previous/Next, Sound & Vibration
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

    fun launchExercisesInTimer(exercises: List<FootballExercise>, title: String? = null) {
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
            lastNotificationText = "Loaded ${exercises.size} drill(s). Tap Start when ready!"
        )
        _timerState.value = newState
        persistTimerCheckpoint(newState, active = true)
        _selectedExercise.value = null
        _currentScreen.value = AppScreen.Timer
    }

    fun quickStartRecommendedWorkout() {
        val rec = recommendedExerciseForToday()
        val warmup = fitnessExercises.first()
        val cooldown = fitnessExercises.last()
        launchExercisesInTimer(listOf(warmup, rec, cooldown), "Daily Pitch Session")
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
            lastNotificationText = if (active.phase == TimerPhase.WORK) "Training active — stay sharp!" else "Rest recovery active..."
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

    fun previousExerciseInTimer() {
        timerJob?.cancel()
        val s = _timerState.value
        val prevIdx = (s.currentIndex - 1).coerceAtLeast(0)
        val prevEx = s.queue.getOrNull(prevIdx) ?: return
        val updated = s.copy(
            currentIndex = prevIdx,
            phase = TimerPhase.WORK,
            totalPhaseSeconds = prevEx.timerWorkSeconds,
            remainingSeconds = prevEx.timerWorkSeconds,
            isRunning = false,
            hasStarted = true,
            lastNotificationText = "Moved back to: ${prevEx.name}"
        )
        _timerState.value = updated
        persistTimerCheckpoint(updated, active = true)
        playCue(isFinish = false)
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
                                lastNotificationText = "Drill done! Rest ${restSec}s before next drill."
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
                                lastNotificationText = "Workout finished & saved to History!"
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
        if (settings.value.vibrationEnabled) {
            SoundAndReminderHelper.triggerVibration(
                context = getApplication(),
                isFinishOrRestEnd = isFinish
            )
        }
    }

    // Weekly Planner CRUD
    fun addPlannedWorkout(
        dayOfWeek: String,
        title: String,
        categoryLabel: String,
        selectedExercises: List<FootballExercise>,
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
                        completedDrillsCount = workout.exerciseIdsSerialized.split(",").count { it.isNotBlank() }.coerceAtLeast(1),
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
                    category = category.trim().ifBlank { "Ball Mastery" },
                    targetValue = targetValue.coerceIn(1, 9999),
                    currentValue = 0,
                    unit = unit.trim().ifBlank { "reps" },
                    isCompleted = false,
                    createdDateKey = DateUtils.todayKey()
                )
            )
        }
    }

    fun incrementTrainingGoal(goal: TrainingGoalEntity, delta: Int) {
        viewModelScope.launch {
            val next = (goal.currentValue + delta).coerceIn(0, goal.targetValue)
            repository.updateGoal(
                goal.copy(currentValue = next, isCompleted = next >= goal.targetValue)
            )
        }
    }

    fun deleteTrainingGoal(goal: TrainingGoalEntity) {
        viewModelScope.launch { repository.deleteGoal(goal) }
    }

    // Settings & Confirmed Reset
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

    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setVibrationEnabled(enabled)
            if (enabled) {
                SoundAndReminderHelper.triggerVibration(getApplication(), isFinishOrRestEnd = false)
            }
        }
    }

    fun setRemindersEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setRemindersEnabled(enabled) }
    }

    fun setReminderTimeText(timeText: String) {
        viewModelScope.launch { repository.setReminderTimeText(timeText) }
    }

    fun setWeeklySessionsGoal(sessions: Int) {
        viewModelScope.launch {
            repository.setWeeklySessionsGoal(sessions)
            _lastAutoSavedAt.value = System.currentTimeMillis()
        }
    }

    fun resetAllTrainingProgress() {
        timerJob?.cancel()
        viewModelScope.launch {
            repository.resetAllProgress()
            _selectedProgramId.value = "prog_beginner"
            _selectedExercise.value = null
            _selectedCategoryFilter.value = null
            _lastAutoSavedAt.value = null
            val firstEx = categoryExercises.first()
            _timerState.value = TrainingTimerUiState(
                queue = listOf(firstEx),
                totalPhaseSeconds = firstEx.timerWorkSeconds,
                remainingSeconds = firstEx.timerWorkSeconds,
                customTitle = firstEx.name
            )
            _profileResetVersion.value += 1
            _currentScreen.value = AppScreen.Welcome
        }
    }
}
