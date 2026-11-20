package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.DatabaseInitializer
import com.example.data.local.DateUtils
import com.example.data.local.NutriFitDatabase
import com.example.data.model.ActivityLogEntity
import com.example.data.model.ActivityType
import com.example.data.model.DayProgressSummary
import com.example.data.model.HealthyFoodSuggestion
import com.example.data.model.MealCategory
import com.example.data.model.MealLogEntity
import com.example.data.model.ReminderCategory
import com.example.data.model.ReminderEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WaterLogEntity
import com.example.data.model.WeightLogEntity
import com.example.data.repository.NutriFitRepository
import com.example.ui.screens.AppScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class TodayOverviewState(
    val caloriesConsumed: Int = 0,
    val calorieGoal: Int = 2000,
    val mealsCount: Int = 0,
    val healthyMealsCount: Int = 0,
    val waterMl: Int = 0,
    val waterGoalMl: Int = 2250,
    val latestWeightKg: Float = 70.9f,
    val startWeightKg: Float = 72.4f,
    val targetWeightKg: Float = 66.0f,
    val activityMinutes: Int = 0,
    val activityGoalMinutes: Int = 30,
    val distanceKm: Float = 0f,
    val activitySteps: Int = 0,
    val caloriesBurned: Int = 0,
    val habitsCompleted: Int = 0
)

class NutriFitViewModel(application: Application) : AndroidViewModel(application) {
    private val database = NutriFitDatabase.getDatabase(application)
    private val repository = NutriFitRepository(database)

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Splash)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _splashFinished = MutableStateFlow(false)
    val splashFinished: StateFlow<Boolean> = _splashFinished.asStateFlow()

    private val _selectedDateKey = MutableStateFlow(DateUtils.todayKey())
    val selectedDateKey: StateFlow<String> = _selectedDateKey.asStateFlow()

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allMeals: StateFlow<List<MealLogEntity>> = repository.allMeals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWaterLogs: StateFlow<List<WaterLogEntity>> = repository.allWaterLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weightLogs: StateFlow<List<WeightLogEntity>> = repository.allWeightLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allActivities: StateFlow<List<ActivityLogEntity>> = repository.allActivities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reminders: StateFlow<List<ReminderEntity>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val mealsForSelectedDate: StateFlow<List<MealLogEntity>> = _selectedDateKey
        .flatMapLatest { dateKey -> repository.getMealsForDate(dateKey) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val waterLogsForSelectedDate: StateFlow<List<WaterLogEntity>> = _selectedDateKey
        .flatMapLatest { dateKey -> repository.getWaterLogsForDate(dateKey) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val activitiesForSelectedDate: StateFlow<List<ActivityLogEntity>> = _selectedDateKey
        .flatMapLatest { dateKey -> repository.getActivitiesForDate(dateKey) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val foodSuggestions: List<HealthyFoodSuggestion> = repository.healthyFoodSuggestions

    // Live Walking/Jogging Stopwatch State
    private val _activeWorkoutType = MutableStateFlow(ActivityType.WALKING)
    val activeWorkoutType: StateFlow<ActivityType> = _activeWorkoutType.asStateFlow()

    private val _stopwatchSeconds = MutableStateFlow(0L)
    val stopwatchSeconds: StateFlow<Long> = _stopwatchSeconds.asStateFlow()

    private val _isStopwatchRunning = MutableStateFlow(false)
    val isStopwatchRunning: StateFlow<Boolean> = _isStopwatchRunning.asStateFlow()

    private var timerJob: Job? = null

    private val todayLogsPair = combine(allMeals, allWaterLogs) { meals, water -> meals to water }
    private val todayFitnessPair = combine(weightLogs, allActivities) { weights, activities -> weights to activities }

    val todayOverview: StateFlow<TodayOverviewState> = combine(
        userProfile,
        todayLogsPair,
        todayFitnessPair
    ) { profile, (meals, waterLogs), (weights, activities) ->
        val todayKey = DateUtils.todayKey()
        val todayMeals = meals.filter { it.dateKey == todayKey }
        val todayWater = waterLogs.filter { it.dateKey == todayKey }
        val todayActivities = activities.filter { it.dateKey == todayKey }

        val calConsumed = todayMeals.sumOf { it.calories }
        val calGoal = profile?.dailyCalorieGoal ?: 2000
        val waterMl = todayWater.sumOf { it.amountMl }
        val waterGoal = profile?.dailyWaterGoalMl ?: 2250
        val actMinutes = todayActivities.sumOf { it.durationMinutes }
        val actGoal = profile?.dailyActivityMinutesGoal ?: 30
        val distKm = todayActivities.sumOf { it.distanceKm.toDouble() }.toFloat()
        val steps = todayActivities.sumOf { it.steps }
        val calBurned = todayActivities.sumOf { it.caloriesBurned }
        val latestWeight = weights.firstOrNull()?.weightKg ?: (profile?.startWeightKg ?: 70.0f)
        val startWeight = profile?.startWeightKg ?: (weights.lastOrNull()?.weightKg ?: latestWeight)
        val targetWeight = profile?.targetWeightKg ?: 66.0f

        var habitsCount = 0
        if (todayMeals.isNotEmpty()) habitsCount++
        if (waterMl >= waterGoal) habitsCount++
        if (actMinutes >= actGoal) habitsCount++
        if (weights.any { it.dateKey == todayKey }) habitsCount++

        TodayOverviewState(
            caloriesConsumed = calConsumed,
            calorieGoal = calGoal,
            mealsCount = todayMeals.size,
            healthyMealsCount = todayMeals.count { it.isHealthyChoice },
            waterMl = waterMl,
            waterGoalMl = waterGoal,
            latestWeightKg = latestWeight,
            startWeightKg = startWeight,
            targetWeightKg = targetWeight,
            activityMinutes = actMinutes,
            activityGoalMinutes = actGoal,
            distanceKm = distKm,
            activitySteps = steps,
            caloriesBurned = calBurned,
            habitsCompleted = habitsCount
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodayOverviewState())

    val weeklyProgress: StateFlow<List<DayProgressSummary>> = combine(
        userProfile,
        todayLogsPair,
        allActivities
    ) { profile, (meals, waterLogs), activities ->
        val keys = DateUtils.lastNDaysKeys(7)
        val waterGoal = profile?.dailyWaterGoalMl ?: 2250
        val actGoal = profile?.dailyActivityMinutesGoal ?: 30

        keys.map { key ->
            val dMeals = meals.filter { it.dateKey == key }
            val dWater = waterLogs.filter { it.dateKey == key }
            val dActs = activities.filter { it.dateKey == key }

            val cal = dMeals.sumOf { it.calories }
            val water = dWater.sumOf { it.amountMl }
            val mins = dActs.sumOf { it.durationMinutes }
            val dist = dActs.sumOf { it.distanceKm.toDouble() }.toFloat()

            var habitsMet = 0
            if (dMeals.isNotEmpty()) habitsMet++
            if (water >= waterGoal) habitsMet++
            if (mins >= actGoal) habitsMet++

            DayProgressSummary(
                dateKey = key,
                shortDayLabel = DateUtils.shortDayLabel(key),
                caloriesConsumed = cal,
                waterMl = water,
                activityMinutes = mins,
                distanceKm = dist,
                habitsMetCount = habitsMet
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            DatabaseInitializer.populateIfEmpty(database)
        }
    }

    // Navigation & Splash
    fun onSplashComplete() {
        _splashFinished.value = true
        val loggedIn = userProfile.value?.isLoggedIn == true
        _currentScreen.value = if (loggedIn) AppScreen.Home else AppScreen.WelcomeLogin
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun selectDate(dateKey: String) {
        _selectedDateKey.value = dateKey
    }

    fun stepDate(deltaDays: Int) {
        val keys = DateUtils.lastNDaysKeys(14)
        val idx = keys.indexOf(_selectedDateKey.value)
        if (idx >= 0) {
            val next = (idx + deltaDays).coerceIn(0, keys.lastIndex)
            _selectedDateKey.value = keys[next]
        } else {
            _selectedDateKey.value = DateUtils.todayKey()
        }
    }

    // Welcome / Login / Profile Actions
    fun enterAppWithProfile(
        name: String,
        currentWeightKg: Float,
        targetWeightKg: Float,
        dailyWaterGoalMl: Int,
        dailyCalorieGoal: Int,
        dailyActivityMinutesGoal: Int
    ) {
        viewModelScope.launch {
            val existing = repository.getProfileOnce() ?: UserProfileEntity()
            val cleanName = name.trim().ifBlank { existing.name }
            val updated = existing.copy(
                name = cleanName,
                targetWeightKg = targetWeightKg,
                dailyWaterGoalMl = dailyWaterGoalMl,
                dailyCalorieGoal = dailyCalorieGoal,
                dailyActivityMinutesGoal = dailyActivityMinutesGoal,
                isLoggedIn = true
            )
            repository.upsertProfile(updated)

            // If user entered a custom starting weight different from latest, log today's weigh-in
            val latest = weightLogs.value.firstOrNull()
            if (currentWeightKg > 0f && (latest == null || kotlin.math.abs(latest.weightKg - currentWeightKg) > 0.05f)) {
                repository.insertWeightLog(
                    WeightLogEntity(
                        dateKey = DateUtils.todayKey(),
                        weightKg = currentWeightKg,
                        note = "Check-in weight"
                    )
                )
            }
            _currentScreen.value = AppScreen.Home
        }
    }

    fun quickEnterAsDemo() {
        viewModelScope.launch {
            val existing = repository.getProfileOnce() ?: UserProfileEntity()
            repository.upsertProfile(existing.copy(isLoggedIn = true))
            _currentScreen.value = AppScreen.Home
        }
    }

    fun signOutLocalProfile() {
        viewModelScope.launch {
            val existing = repository.getProfileOnce() ?: return@launch
            repository.upsertProfile(existing.copy(isLoggedIn = false))
            _currentScreen.value = AppScreen.WelcomeLogin
        }
    }

    fun updateGoals(
        name: String,
        calorieGoal: Int,
        waterGoalMl: Int,
        activityMinutesGoal: Int,
        targetWeightKg: Float
    ) {
        viewModelScope.launch {
            val existing = repository.getProfileOnce() ?: UserProfileEntity()
            repository.upsertProfile(
                existing.copy(
                    name = name.trim().ifBlank { existing.name },
                    dailyCalorieGoal = calorieGoal.coerceIn(1000, 5000),
                    dailyWaterGoalMl = waterGoalMl.coerceIn(1000, 6000),
                    dailyActivityMinutesGoal = activityMinutesGoal.coerceIn(10, 240),
                    targetWeightKg = targetWeightKg.coerceIn(30f, 250f)
                )
            )
        }
    }

    // Daily Nutrition Actions
    fun addMeal(
        mealCategory: MealCategory,
        foodName: String,
        calories: Int,
        protein: Int,
        carbs: Int,
        fat: Int,
        isHealthyChoice: Boolean,
        notes: String
    ) {
        viewModelScope.launch {
            repository.insertMeal(
                MealLogEntity(
                    dateKey = _selectedDateKey.value,
                    mealType = mealCategory.name,
                    foodName = foodName.trim(),
                    calories = calories.coerceAtLeast(0),
                    proteinGrams = protein.coerceAtLeast(0),
                    carbsGrams = carbs.coerceAtLeast(0),
                    fatGrams = fat.coerceAtLeast(0),
                    isHealthyChoice = isHealthyChoice,
                    notes = notes.trim()
                )
            )
        }
    }

    fun logSuggestedFood(suggestion: HealthyFoodSuggestion) {
        viewModelScope.launch {
            repository.insertMeal(
                MealLogEntity(
                    dateKey = DateUtils.todayKey(),
                    mealType = suggestion.mealCategory.name,
                    foodName = suggestion.name,
                    calories = suggestion.calories,
                    proteinGrams = suggestion.proteinGrams,
                    carbsGrams = suggestion.carbsGrams,
                    fatGrams = suggestion.fatGrams,
                    isHealthyChoice = true,
                    notes = suggestion.benefitTag
                )
            )
        }
    }

    fun deleteMeal(meal: MealLogEntity) {
        viewModelScope.launch { repository.deleteMeal(meal) }
    }

    // Water Tracker Actions
    fun addWater(amountMl: Int, label: String) {
        viewModelScope.launch {
            repository.insertWaterLog(
                WaterLogEntity(
                    dateKey = _selectedDateKey.value,
                    amountMl = amountMl.coerceAtLeast(50),
                    label = label
                )
            )
        }
    }

    fun deleteWaterLog(log: WaterLogEntity) {
        viewModelScope.launch { repository.deleteWaterLog(log) }
    }

    fun toggleWaterReminder(enabled: Boolean) {
        viewModelScope.launch {
            val existing = repository.getProfileOnce() ?: return@launch
            repository.upsertProfile(existing.copy(waterReminderEnabled = enabled))
        }
    }

    fun setWaterReminderInterval(hours: Int) {
        viewModelScope.launch {
            val existing = repository.getProfileOnce() ?: return@launch
            repository.upsertProfile(existing.copy(waterReminderIntervalHours = hours))
        }
    }

    // Weight Tracker Actions
    fun addWeight(weightKg: Float, note: String) {
        viewModelScope.launch {
            repository.insertWeightLog(
                WeightLogEntity(
                    dateKey = DateUtils.todayKey(),
                    weightKg = weightKg.coerceIn(25f, 300f),
                    note = note.trim()
                )
            )
        }
    }

    fun deleteWeightLog(log: WeightLogEntity) {
        viewModelScope.launch { repository.deleteWeightLog(log) }
    }

    fun updateTargetWeight(targetKg: Float) {
        viewModelScope.launch {
            val existing = repository.getProfileOnce() ?: return@launch
            repository.upsertProfile(existing.copy(targetWeightKg = targetKg.coerceIn(30f, 250f)))
        }
    }

    // Walking & Jogging Actions
    fun setWorkoutType(type: ActivityType) {
        _activeWorkoutType.value = type
    }

    fun startWorkout() {
        if (_isStopwatchRunning.value) return
        _isStopwatchRunning.value = true
        timerJob = viewModelScope.launch {
            while (_isStopwatchRunning.value) {
                delay(1000L)
                _stopwatchSeconds.value += 1L
            }
        }
    }

    fun pauseWorkout() {
        _isStopwatchRunning.value = false
        timerJob?.cancel()
    }

    fun finishAndSaveWorkout(note: String = "") {
        pauseWorkout()
        val secs = _stopwatchSeconds.value
        val mins = (secs / 60L).toInt().coerceAtLeast(1)
        val type = _activeWorkoutType.value
        val speedKmPerMin = if (type == ActivityType.JOGGING) 0.135f else 0.082f
        val dist = ((mins * speedKmPerMin) * 10f).roundToInt() / 10f
        val stepsPerMin = if (type == ActivityType.JOGGING) 155 else 108
        val steps = mins * stepsPerMin
        val kcalPerMin = if (type == ActivityType.JOGGING) 9 else 4
        val burned = mins * kcalPerMin

        addActivity(
            type = type,
            durationMinutes = mins,
            distanceKm = dist.coerceAtLeast(0.1f),
            steps = steps,
            caloriesBurned = burned,
            note = note.ifBlank { "Timed ${type.displayName} session" }
        )
        _stopwatchSeconds.value = 0L
    }

    fun resetWorkoutTimer() {
        pauseWorkout()
        _stopwatchSeconds.value = 0L
    }

    fun addActivity(
        type: ActivityType,
        durationMinutes: Int,
        distanceKm: Float,
        steps: Int? = null,
        caloriesBurned: Int? = null,
        note: String = ""
    ) {
        viewModelScope.launch {
            val mins = durationMinutes.coerceAtLeast(1)
            val estSteps = steps ?: (mins * if (type == ActivityType.JOGGING) 155 else 108)
            val estCal = caloriesBurned ?: (mins * if (type == ActivityType.JOGGING) 9 else 4)
            repository.insertActivity(
                ActivityLogEntity(
                    dateKey = _selectedDateKey.value,
                    activityType = type.name,
                    durationMinutes = mins,
                    distanceKm = distanceKm.coerceAtLeast(0.1f),
                    steps = estSteps,
                    caloriesBurned = estCal,
                    note = note.trim()
                )
            )
        }
    }

    fun deleteActivity(log: ActivityLogEntity) {
        viewModelScope.launch { repository.deleteActivity(log) }
    }

    // Reminders Actions
    fun toggleReminderCompletedToday(reminder: ReminderEntity) {
        val today = DateUtils.todayKey()
        val nextDate = if (reminder.lastCompletedDateKey == today) "" else today
        viewModelScope.launch {
            repository.updateReminder(reminder.copy(lastCompletedDateKey = nextDate))
        }
    }

    fun toggleReminderEnabled(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.updateReminder(reminder.copy(isEnabled = !reminder.isEnabled))
        }
    }

    fun addCustomReminder(title: String, description: String, timeLabel: String, category: ReminderCategory) {
        viewModelScope.launch {
            repository.insertReminder(
                ReminderEntity(
                    title = title.trim(),
                    description = description.trim(),
                    timeLabel = timeLabel.trim().ifBlank { "08:00 AM" },
                    category = category.name,
                    isEnabled = true
                )
            )
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch { repository.deleteReminder(reminder) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
