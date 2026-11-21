package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.DatabaseInitializer
import com.example.data.local.DateUtils
import com.example.data.local.FootballDatabase
import com.example.data.local.SettingsDataStore
import com.example.data.local.TacticsSerializer
import com.example.data.model.AppSettings
import com.example.data.model.CompletedLessonEntity
import com.example.data.model.DifficultyLevel
import com.example.data.model.PersonalRecordEntity
import com.example.data.model.PlannedWorkoutEntity
import com.example.data.model.PlayerPosition
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.PreferredFoot
import com.example.data.model.ProgramDayCompletionEntity
import com.example.data.model.SavedTacticEntity
import com.example.data.model.SkillCategory
import com.example.data.model.TacticArrow
import com.example.data.model.TacticMarker
import com.example.data.model.TrainingLesson
import com.example.data.model.TrainingProgram
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DailyChartPoint(
    val dateKey: String,
    val label: String,
    val minutes: Int,
    val sessions: Int
)

data class SkillRating(
    val category: SkillCategory,
    val scorePercent: Int,
    val completedCount: Int
)

data class DashboardSummary(
    val completedSessionsTotal: Int = 0,
    val totalMinutes: Int = 0,
    val streakDays: Int = 0,
    val activeProgramProgressPercent: Int = 0,
    val todayCompletedCount: Int = 0,
    val weeklyPoints: List<DailyChartPoint> = emptyList(),
    val monthlyPoints: List<DailyChartPoint> = emptyList(),
    val skillRatings: List<SkillRating> = emptyList()
)

enum class TimerToolTab(val label: String) {
    STOPWATCH("Stopwatch"),
    COUNTDOWN("Countdown"),
    INTERVAL("Interval HIIT"),
    REST("Rest Timer")
}

data class TimersUiState(
    val selectedTab: TimerToolTab = TimerToolTab.STOPWATCH,
    // Stopwatch
    val stopwatchMillis: Long = 0L,
    val stopwatchRunning: Boolean = false,
    val laps: List<String> = emptyList(),
    // Countdown
    val countdownInitialSec: Int = 300,
    val countdownRemainingSec: Int = 300,
    val countdownRunning: Boolean = false,
    // Interval HIIT
    val workSeconds: Int = 30,
    val restSeconds: Int = 15,
    val totalRounds: Int = 8,
    val currentRound: Int = 1,
    val isWorkPhase: Boolean = true,
    val intervalRemainingSec: Int = 30,
    val intervalRunning: Boolean = false,
    // Rest Timer
    val restPresetSec: Int = 60,
    val restRemainingSec: Int = 60,
    val restRunning: Boolean = false
)

enum class TrainingSubTab(val label: String) {
    MODULES("Skill Drills"),
    FITNESS("Fitness"),
    POSITIONS("Positions"),
    TIMERS("Timers"),
    PLANNER("Planner"),
    TACTICS("Tactics")
}

class FootballViewModel(application: Application) : AndroidViewModel(application) {
    private val database = FootballDatabase.getDatabase(application)
    private val settingsStore = SettingsDataStore(application)
    private val repository = FootballRepository(database, settingsStore)

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Splash)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _trainingSubTab = MutableStateFlow(TrainingSubTab.MODULES)
    val trainingSubTab: StateFlow<TrainingSubTab> = _trainingSubTab.asStateFlow()

    private val _selectedLesson = MutableStateFlow<TrainingLesson?>(null)
    val selectedLesson: StateFlow<TrainingLesson?> = _selectedLesson.asStateFlow()

    private val _selectedSkillFilter = MutableStateFlow<SkillCategory?>(null)
    val selectedSkillFilter: StateFlow<SkillCategory?> = _selectedSkillFilter.asStateFlow()

    val playerProfile: StateFlow<PlayerProfileEntity?> = repository.playerProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val settings: StateFlow<AppSettings> = repository.settingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    val completedLessons: StateFlow<List<CompletedLessonEntity>> = repository.completedLessons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val programCompletions: StateFlow<List<ProgramDayCompletionEntity>> = repository.programCompletions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val personalRecords: StateFlow<List<PersonalRecordEntity>> = repository.personalRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedTactics: StateFlow<List<SavedTacticEntity>> = repository.savedTactics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val plannedWorkouts: StateFlow<List<PlannedWorkoutEntity>> = repository.plannedWorkouts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val skillLessons: List<TrainingLesson> = repository.allSkillLessons
    val fitnessLessons: List<TrainingLesson> = repository.allFitnessLessons
    val positionLessons: List<TrainingLesson> = repository.allPositionLessons
    val allLessons: List<TrainingLesson> = repository.allLessons
    val programs: List<TrainingProgram> = repository.allPrograms

    val dashboardSummary: StateFlow<DashboardSummary> = combine(
        completedLessons,
        programCompletions,
        settings
    ) { completed, progCompletions, currentSettings ->
        val today = DateUtils.todayKey()
        val totalMinutes = completed.sumOf { it.durationMinutes }
        val todayCount = completed.count { it.dateKey == today }

        // Compute training streak
        val activeDates = completed.map { it.dateKey }.toSet()
        var streak = 0
        var dayOffset = if (activeDates.contains(today)) 0 else 1
        while (activeDates.contains(DateUtils.daysAgoKey(dayOffset))) {
            streak++
            dayOffset++
        }

        // Active program completion %
        val activeProgram = programs.find { it.id == currentSettings.activeProgramId } ?: programs.first()
        val doneDays = progCompletions.count { it.programId == activeProgram.id && it.isCompleted }
        val programPct = ((doneDays.toFloat() / activeProgram.totalDays.coerceAtLeast(1)) * 100).toInt().coerceIn(0, 100)

        // Weekly 7-day chart points
        val weekKeys = DateUtils.lastNDaysKeys(7)
        val weeklyPts = weekKeys.map { k ->
            val items = completed.filter { it.dateKey == k }
            DailyChartPoint(
                dateKey = k,
                label = DateUtils.shortDayLabel(k),
                minutes = items.sumOf { it.durationMinutes },
                sessions = items.size
            )
        }

        // Monthly 28-day activity points
        val monthKeys = DateUtils.lastNDaysKeys(28)
        val monthlyPts = monthKeys.mapIndexed { idx, k ->
            val items = completed.filter { it.dateKey == k }
            DailyChartPoint(
                dateKey = k,
                label = "${idx + 1}",
                minutes = items.sumOf { it.durationMinutes },
                sessions = items.size
            )
        }

        // Skill improvement ratings per category
        val ratings = SkillCategory.entries.map { cat ->
            val catDone = completed.count { it.categoryKey == cat.name }
            val uniqueDone = completed.filter { it.categoryKey == cat.name }.map { it.lessonId }.distinct().size
            val pct = (25 + uniqueDone * 12 + (catDone - uniqueDone).coerceAtLeast(0) * 3).coerceIn(20, 100)
            SkillRating(category = cat, scorePercent = pct, completedCount = catDone)
        }

        DashboardSummary(
            completedSessionsTotal = completed.size,
            totalMinutes = totalMinutes,
            streakDays = streak,
            activeProgramProgressPercent = programPct,
            todayCompletedCount = todayCount,
            weeklyPoints = weeklyPts,
            monthlyPoints = monthlyPts,
            skillRatings = ratings
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardSummary())

    // Timers state
    private val _timers = MutableStateFlow(TimersUiState())
    val timers: StateFlow<TimersUiState> = _timers.asStateFlow()
    private var timerJob: Job? = null

    // Tactics Board state
    private val _tacticFormation = MutableStateFlow("4-3-3")
    val tacticFormation: StateFlow<String> = _tacticFormation.asStateFlow()

    private val _tacticMarkers = MutableStateFlow(TacticsSerializer.defaultFormationMarkers("4-3-3"))
    val tacticMarkers: StateFlow<List<TacticMarker>> = _tacticMarkers.asStateFlow()

    private val _tacticArrows = MutableStateFlow<List<TacticArrow>>(emptyList())
    val tacticArrows: StateFlow<List<TacticArrow>> = _tacticArrows.asStateFlow()

    init {
        viewModelScope.launch {
            DatabaseInitializer.populateIfEmpty(database)
        }
    }

    // Navigation
    fun onSplashComplete() {
        val onboarded = playerProfile.value?.onboardingCompleted == true
        _currentScreen.value = if (onboarded) AppScreen.Home else AppScreen.Welcome
    }

    fun navigateTo(screen: AppScreen) {
        _selectedLesson.value = null
        _currentScreen.value = screen
    }

    fun openTrainingWithSubTab(subTab: TrainingSubTab, skillFilter: SkillCategory? = null) {
        _selectedLesson.value = null
        _trainingSubTab.value = subTab
        _selectedSkillFilter.value = skillFilter
        _currentScreen.value = AppScreen.Training
    }

    fun selectTrainingSubTab(subTab: TrainingSubTab) {
        _trainingSubTab.value = subTab
    }

    fun selectSkillFilter(category: SkillCategory?) {
        _selectedSkillFilter.value = category
    }

    fun openLesson(lesson: TrainingLesson) {
        _selectedLesson.value = lesson
    }

    fun closeLesson() {
        _selectedLesson.value = null
    }

    // Welcome & Profile
    fun completeOnboarding(
        name: String,
        age: Int,
        position: PlayerPosition,
        preferredFoot: PreferredFoot,
        experienceLevel: DifficultyLevel
    ) {
        viewModelScope.launch {
            val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
            repository.upsertProfile(
                existing.copy(
                    playerName = name.trim().ifBlank { existing.playerName },
                    age = age.coerceIn(7, 65),
                    playingPosition = position.name,
                    preferredFoot = preferredFoot.name,
                    experienceLevel = experienceLevel.name,
                    onboardingCompleted = true,
                    updatedAt = System.currentTimeMillis()
                )
            )
            _currentScreen.value = AppScreen.Home
        }
    }

    fun savePlayerProfile(
        name: String,
        age: Int,
        heightCm: Int,
        weightKg: Float,
        position: PlayerPosition,
        foot: PreferredFoot,
        level: DifficultyLevel,
        goals: String
    ) {
        viewModelScope.launch {
            val existing = repository.getProfileOnce() ?: PlayerProfileEntity()
            repository.upsertProfile(
                existing.copy(
                    playerName = name.trim().ifBlank { existing.playerName },
                    age = age.coerceIn(7, 70),
                    heightCm = heightCm.coerceIn(110, 220),
                    weightKg = weightKg.coerceIn(30f, 160f),
                    playingPosition = position.name,
                    preferredFoot = foot.name,
                    experienceLevel = level.name,
                    personalGoals = goals.trim(),
                    onboardingCompleted = true,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    // Lesson Completion
    fun markLessonCompleted(lesson: TrainingLesson) {
        viewModelScope.launch {
            repository.insertCompletedLesson(
                CompletedLessonEntity(
                    lessonId = lesson.id,
                    dateKey = DateUtils.todayKey(),
                    categoryKey = lesson.categoryKey,
                    categoryLabel = lesson.categoryLabel,
                    title = lesson.title,
                    durationMinutes = lesson.durationMinutes
                )
            )
            if (settings.value.soundEnabled) {
                SoundAndReminderHelper.playCueBeep(isFinishOrWhistle = true)
            }
        }
    }

    fun logCustomTimerWorkout(title: String, categoryLabel: String, minutes: Int) {
        viewModelScope.launch {
            repository.insertCompletedLesson(
                CompletedLessonEntity(
                    lessonId = "custom_${System.currentTimeMillis()}",
                    dateKey = DateUtils.todayKey(),
                    categoryKey = "TIMER_SESSION",
                    categoryLabel = categoryLabel,
                    title = title,
                    durationMinutes = minutes.coerceAtLeast(1)
                )
            )
        }
    }

    // Programs
    fun selectActiveProgram(programId: String) {
        viewModelScope.launch {
            repository.setActiveProgramId(programId)
        }
    }

    fun toggleProgramDayCompleted(programId: String, dayNumber: Int, currentlyCompleted: Boolean) {
        viewModelScope.launch {
            repository.upsertProgramDayCompletion(
                ProgramDayCompletionEntity(
                    programId = programId,
                    dayNumber = dayNumber,
                    isCompleted = !currentlyCompleted,
                    completedDateKey = if (!currentlyCompleted) DateUtils.todayKey() else ""
                )
            )
            if (!currentlyCompleted && settings.value.soundEnabled) {
                SoundAndReminderHelper.playCueBeep(isFinishOrWhistle = true)
            }
        }
    }

    fun resetProgramProgress(programId: String) {
        viewModelScope.launch {
            repository.resetProgram(programId)
        }
    }

    // Personal Records
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

    // Weekly Planner
    fun addPlannedWorkout(dayOfWeek: String, title: String, category: String, durationMinutes: Int) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insertPlannedWorkout(
                PlannedWorkoutEntity(
                    dayOfWeek = dayOfWeek,
                    dateKey = DateUtils.todayKey(),
                    title = title.trim(),
                    categoryLabel = category.trim().ifBlank { "Training" },
                    durationMinutes = durationMinutes.coerceIn(5, 180),
                    isCompleted = false
                )
            )
        }
    }

    fun togglePlannedWorkout(workout: PlannedWorkoutEntity) {
        viewModelScope.launch {
            repository.updatePlannedWorkout(workout.copy(isCompleted = !workout.isCompleted))
        }
    }

    fun deletePlannedWorkout(workout: PlannedWorkoutEntity) {
        viewModelScope.launch { repository.deletePlannedWorkout(workout) }
    }

    // Tactics Board
    fun applyFormation(formation: String) {
        _tacticFormation.value = formation
        _tacticMarkers.value = TacticsSerializer.defaultFormationMarkers(formation)
        _tacticArrows.value = emptyList()
    }

    fun moveTacticMarker(markerId: String, newX: Float, newY: Float) {
        val clampedX = newX.coerceIn(0.05f, 0.95f)
        val clampedY = newY.coerceIn(0.05f, 0.95f)
        _tacticMarkers.value = _tacticMarkers.value.map {
            if (it.id == markerId) it.copy(xFraction = clampedX, yFraction = clampedY) else it
        }
    }

    fun addTacticMarker(label: String, team: String) {
        val id = "m_${System.currentTimeMillis()}_${_tacticMarkers.value.size}"
        val y = when (team) {
            "AWAY" -> 0.25f
            "BALL" -> 0.50f
            else -> 0.65f
        }
        _tacticMarkers.value = _tacticMarkers.value + TacticMarker(
            id = id,
            label = label.ifBlank { if (team == "BALL") "BALL" else "P" },
            xFraction = 0.50f,
            yFraction = y,
            team = team
        )
    }

    fun addTacticArrow(startX: Float, startY: Float, endX: Float, endY: Float) {
        val dx = endX - startX
        val dy = endY - startY
        if ((dx * dx + dy * dy) < 0.001f) return
        val id = "arr_${System.currentTimeMillis()}"
        _tacticArrows.value = _tacticArrows.value + TacticArrow(
            id = id,
            startX = startX.coerceIn(0.04f, 0.96f),
            startY = startY.coerceIn(0.04f, 0.96f),
            endX = endX.coerceIn(0.04f, 0.96f),
            endY = endY.coerceIn(0.04f, 0.96f)
        )
    }

    fun undoLastArrow() {
        if (_tacticArrows.value.isNotEmpty()) {
            _tacticArrows.value = _tacticArrows.value.dropLast(1)
        }
    }

    fun clearTacticArrows() {
        _tacticArrows.value = emptyList()
    }

    fun saveCurrentTactic(name: String, notes: String) {
        viewModelScope.launch {
            repository.insertTactic(
                SavedTacticEntity(
                    name = name.trim().ifBlank { "${_tacticFormation.value} Setup" },
                    formation = _tacticFormation.value,
                    markersSerialized = TacticsSerializer.serializeMarkers(_tacticMarkers.value),
                    arrowsSerialized = TacticsSerializer.serializeArrows(_tacticArrows.value),
                    notes = notes.trim()
                )
            )
        }
    }

    fun loadSavedTactic(tactic: SavedTacticEntity) {
        _tacticFormation.value = tactic.formation
        _tacticMarkers.value = TacticsSerializer.deserializeMarkers(tactic.markersSerialized)
        _tacticArrows.value = TacticsSerializer.deserializeArrows(tactic.arrowsSerialized)
    }

    fun deleteSavedTactic(tactic: SavedTacticEntity) {
        viewModelScope.launch { repository.deleteTactic(tactic) }
    }

    // Training Timers Logic
    fun selectTimerTab(tab: TimerToolTab) {
        pauseAllTimers()
        _timers.value = _timers.value.copy(selectedTab = tab)
    }

    private fun pauseAllTimers() {
        timerJob?.cancel()
        _timers.value = _timers.value.copy(
            stopwatchRunning = false,
            countdownRunning = false,
            intervalRunning = false,
            restRunning = false
        )
    }

    fun toggleStopwatch() {
        val s = _timers.value
        if (s.stopwatchRunning) {
            pauseAllTimers()
        } else {
            pauseAllTimers()
            _timers.value = _timers.value.copy(stopwatchRunning = true)
            if (settings.value.soundEnabled) SoundAndReminderHelper.playCueBeep(false)
            timerJob = viewModelScope.launch {
                while (_timers.value.stopwatchRunning) {
                    delay(100L)
                    _timers.value = _timers.value.copy(stopwatchMillis = _timers.value.stopwatchMillis + 100L)
                }
            }
        }
    }

    fun addStopwatchLap() {
        val ms = _timers.value.stopwatchMillis
        val totalSec = ms / 1000
        val mins = totalSec / 60
        val secs = totalSec % 60
        val tenths = (ms % 1000) / 100
        val lapStr = String.format(java.util.Locale.US, "Lap %d — %02d:%02d.%d", _timers.value.laps.size + 1, mins, secs, tenths)
        _timers.value = _timers.value.copy(laps = listOf(lapStr) + _timers.value.laps)
    }

    fun resetStopwatch() {
        pauseAllTimers()
        _timers.value = _timers.value.copy(stopwatchMillis = 0L, laps = emptyList())
    }

    fun setCountdownPreset(seconds: Int) {
        pauseAllTimers()
        _timers.value = _timers.value.copy(countdownInitialSec = seconds, countdownRemainingSec = seconds)
    }

    fun toggleCountdown() {
        val s = _timers.value
        if (s.countdownRunning) {
            pauseAllTimers()
        } else {
            val startSec = if (s.countdownRemainingSec <= 0) s.countdownInitialSec else s.countdownRemainingSec
            pauseAllTimers()
            _timers.value = _timers.value.copy(countdownRemainingSec = startSec, countdownRunning = true)
            if (settings.value.soundEnabled) SoundAndReminderHelper.playCueBeep(false)
            timerJob = viewModelScope.launch {
                while (_timers.value.countdownRunning && _timers.value.countdownRemainingSec > 0) {
                    delay(1000L)
                    val next = (_timers.value.countdownRemainingSec - 1).coerceAtLeast(0)
                    _timers.value = _timers.value.copy(countdownRemainingSec = next)
                    if (next == 0) {
                        _timers.value = _timers.value.copy(countdownRunning = false)
                        if (settings.value.soundEnabled) SoundAndReminderHelper.playCueBeep(true)
                    }
                }
            }
        }
    }

    fun resetCountdown() {
        pauseAllTimers()
        _timers.value = _timers.value.copy(countdownRemainingSec = _timers.value.countdownInitialSec)
    }

    fun configureInterval(workSec: Int, restSec: Int, rounds: Int) {
        pauseAllTimers()
        _timers.value = _timers.value.copy(
            workSeconds = workSec.coerceIn(10, 300),
            restSeconds = restSec.coerceIn(5, 180),
            totalRounds = rounds.coerceIn(1, 30),
            currentRound = 1,
            isWorkPhase = true,
            intervalRemainingSec = workSec.coerceIn(10, 300)
        )
    }

    fun toggleIntervalTimer() {
        val s = _timers.value
        if (s.intervalRunning) {
            pauseAllTimers()
        } else {
            pauseAllTimers()
            _timers.value = _timers.value.copy(intervalRunning = true)
            if (settings.value.soundEnabled) SoundAndReminderHelper.playCueBeep(false)
            timerJob = viewModelScope.launch {
                while (_timers.value.intervalRunning) {
                    delay(1000L)
                    val cur = _timers.value
                    if (cur.intervalRemainingSec > 1) {
                        _timers.value = cur.copy(intervalRemainingSec = cur.intervalRemainingSec - 1)
                    } else {
                        if (cur.isWorkPhase) {
                            // Transition Work -> Rest (or finish if last round)
                            if (cur.currentRound >= cur.totalRounds) {
                                _timers.value = cur.copy(intervalRunning = false, intervalRemainingSec = 0)
                                if (settings.value.soundEnabled) SoundAndReminderHelper.playCueBeep(true)
                            } else {
                                _timers.value = cur.copy(isWorkPhase = false, intervalRemainingSec = cur.restSeconds)
                                if (settings.value.soundEnabled) SoundAndReminderHelper.playCueBeep(false)
                            }
                        } else {
                            // Transition Rest -> Next Round Work
                            _timers.value = cur.copy(
                                isWorkPhase = true,
                                currentRound = cur.currentRound + 1,
                                intervalRemainingSec = cur.workSeconds
                            )
                            if (settings.value.soundEnabled) SoundAndReminderHelper.playCueBeep(true)
                        }
                    }
                }
            }
        }
    }

    fun resetIntervalTimer() {
        pauseAllTimers()
        _timers.value = _timers.value.copy(
            currentRound = 1,
            isWorkPhase = true,
            intervalRemainingSec = _timers.value.workSeconds
        )
    }

    fun setRestPreset(seconds: Int) {
        pauseAllTimers()
        _timers.value = _timers.value.copy(restPresetSec = seconds, restRemainingSec = seconds)
    }

    fun toggleRestTimer() {
        val s = _timers.value
        if (s.restRunning) {
            pauseAllTimers()
        } else {
            val startSec = if (s.restRemainingSec <= 0) s.restPresetSec else s.restRemainingSec
            pauseAllTimers()
            _timers.value = _timers.value.copy(restRemainingSec = startSec, restRunning = true)
            timerJob = viewModelScope.launch {
                while (_timers.value.restRunning && _timers.value.restRemainingSec > 0) {
                    delay(1000L)
                    val next = (_timers.value.restRemainingSec - 1).coerceAtLeast(0)
                    _timers.value = _timers.value.copy(restRemainingSec = next)
                    if (next == 0) {
                        _timers.value = _timers.value.copy(restRunning = false)
                        if (settings.value.soundEnabled) SoundAndReminderHelper.playCueBeep(true)
                    }
                }
            }
        }
    }

    // Settings & Data Wipe
    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { repository.setDarkTheme(enabled) }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setSoundEnabled(enabled) }
    }

    fun setReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setReminderEnabled(enabled) }
    }

    fun setReminderTime(timeLabel: String) {
        viewModelScope.launch { repository.setReminderTime(timeLabel) }
    }

    fun clearAllLocalData() {
        pauseAllTimers()
        viewModelScope.launch {
            repository.clearAllLocalData()
            _tacticFormation.value = "4-3-3"
            _tacticMarkers.value = TacticsSerializer.defaultFormationMarkers("4-3-3")
            _tacticArrows.value = emptyList()
            _selectedLesson.value = null
            _currentScreen.value = AppScreen.Welcome
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
