package com.example.data.local

import com.example.data.model.ActivityLogEntity
import com.example.data.model.ActivityType
import com.example.data.model.MealCategory
import com.example.data.model.MealLogEntity
import com.example.data.model.ReminderCategory
import com.example.data.model.ReminderEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WaterLogEntity
import com.example.data.model.WeightLogEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {
    private val keyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayFormat = SimpleDateFormat("EEE, MMM d", Locale.US)
    private val shortDayFormat = SimpleDateFormat("EEE", Locale.US)

    fun todayKey(): String = keyFormat.format(Calendar.getInstance().time)

    fun daysAgoKey(daysAgo: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return keyFormat.format(cal.time)
    }

    fun formatDisplayDate(dateKey: String): String {
        return try {
            val date = keyFormat.parse(dateKey)
            if (dateKey == todayKey()) "Today • ${displayFormat.format(date!!)}"
            else if (dateKey == daysAgoKey(1)) "Yesterday • ${displayFormat.format(date!!)}"
            else displayFormat.format(date!!)
        } catch (_: Exception) {
            dateKey
        }
    }

    fun shortDayLabel(dateKey: String): String {
        return try {
            val date = keyFormat.parse(dateKey)
            shortDayFormat.format(date!!)
        } catch (_: Exception) {
            dateKey.takeLast(2)
        }
    }

    fun lastNDaysKeys(days: Int): List<String> {
        return (days - 1 downTo 0).map { daysAgoKey(it) }
    }
}

object DatabaseInitializer {
    suspend fun populateIfEmpty(database: NutriFitDatabase) {
        withContext(Dispatchers.IO) {
            val profileDao = database.userProfileDao()
            if (profileDao.getProfileOnce() != null) return@withContext

            val today = DateUtils.todayKey()
            val d1 = DateUtils.daysAgoKey(1)
            val d2 = DateUtils.daysAgoKey(2)
            val d3 = DateUtils.daysAgoKey(3)
            val d4 = DateUtils.daysAgoKey(4)
            val d5 = DateUtils.daysAgoKey(5)

            profileDao.upsertProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Alex Morgan",
                    email = "alex@nutrifit.offline",
                    heightCm = 170f,
                    startWeightKg = 72.4f,
                    targetWeightKg = 66.0f,
                    dailyCalorieGoal = 2000,
                    dailyWaterGoalMl = 2250,
                    dailyActivityMinutesGoal = 30,
                    dailyDistanceKmGoal = 3.0f,
                    waterReminderEnabled = true,
                    waterReminderIntervalHours = 2,
                    mealReminderEnabled = true,
                    activityReminderEnabled = true,
                    isLoggedIn = false
                )
            )

            // Seed Reminders
            val reminderDao = database.reminderDao()
            if (reminderDao.getAllReminders().first().isEmpty()) {
                reminderDao.insertReminder(
                    ReminderEntity(
                        title = "Morning Hydration Boost",
                        description = "Drink 250 ml of water right after waking up",
                        timeLabel = "07:30 AM",
                        category = ReminderCategory.WATER.name,
                        isEnabled = true,
                        lastCompletedDateKey = today
                    )
                )
                reminderDao.insertReminder(
                    ReminderEntity(
                        title = "Balanced Lunch & Greens",
                        description = "Include leafy greens and lean protein in lunch",
                        timeLabel = "12:30 PM",
                        category = ReminderCategory.NUTRITION.name,
                        isEnabled = true
                    )
                )
                reminderDao.insertReminder(
                    ReminderEntity(
                        title = "Afternoon Water Check-in",
                        description = "Stay hydrated — reach 1,500 ml before 4 PM",
                        timeLabel = "03:00 PM",
                        category = ReminderCategory.WATER.name,
                        isEnabled = true
                    )
                )
                reminderDao.insertReminder(
                    ReminderEntity(
                        title = "Evening Walk or Jog",
                        description = "Aim for 30 minutes brisk walk or light jog",
                        timeLabel = "05:30 PM",
                        category = ReminderCategory.ACTIVITY.name,
                        isEnabled = true
                    )
                )
                reminderDao.insertReminder(
                    ReminderEntity(
                        title = "Morning Weight Check",
                        description = "Record weight weekly or daily before breakfast",
                        timeLabel = "07:00 AM",
                        category = ReminderCategory.WEIGHT.name,
                        isEnabled = true,
                        lastCompletedDateKey = today
                    )
                )
            }

            // Seed Meals (today + past days)
            val mealDao = database.mealLogDao()
            listOf(
                MealLogEntity(dateKey = today, mealType = MealCategory.BREAKFAST.name, foodName = "Oatmeal with Banana & Chia Seeds", calories = 340, proteinGrams = 12, carbsGrams = 54, fatGrams = 8, isHealthyChoice = true, notes = "Warm oats with cinnamon"),
                MealLogEntity(dateKey = today, mealType = MealCategory.LUNCH.name, foodName = "Grilled Chicken & Quinoa Salad", calories = 490, proteinGrams = 38, carbsGrams = 42, fatGrams = 16, isHealthyChoice = true, notes = "Olive oil & lemon dressing"),
                MealLogEntity(dateKey = today, mealType = MealCategory.SNACKS.name, foodName = "Apple Slices & Almonds", calories = 180, proteinGrams = 5, carbsGrams = 22, fatGrams = 9, isHealthyChoice = true),
                MealLogEntity(dateKey = d1, mealType = MealCategory.BREAKFAST.name, foodName = "Greek Yogurt Berry Parfait", calories = 290, proteinGrams = 18, carbsGrams = 34, fatGrams = 6, isHealthyChoice = true),
                MealLogEntity(dateKey = d1, mealType = MealCategory.LUNCH.name, foodName = "Steamed Salmon with Brown Rice", calories = 560, proteinGrams = 36, carbsGrams = 48, fatGrams = 20, isHealthyChoice = true),
                MealLogEntity(dateKey = d1, mealType = MealCategory.DINNER.name, foodName = "Vegetable Tofu Stir-Fry", calories = 410, proteinGrams = 22, carbsGrams = 44, fatGrams = 14, isHealthyChoice = true),
                MealLogEntity(dateKey = d2, mealType = MealCategory.BREAKFAST.name, foodName = "Whole Grain Avocado Toast & Egg", calories = 380, proteinGrams = 16, carbsGrams = 32, fatGrams = 18, isHealthyChoice = true),
                MealLogEntity(dateKey = d2, mealType = MealCategory.LUNCH.name, foodName = "Turkey & Spinach Whole Wheat Wrap", calories = 440, proteinGrams = 28, carbsGrams = 46, fatGrams = 12, isHealthyChoice = true),
                MealLogEntity(dateKey = d2, mealType = MealCategory.DINNER.name, foodName = "Lentil Soup & Roasted Broccoli", calories = 390, proteinGrams = 20, carbsGrams = 52, fatGrams = 8, isHealthyChoice = true)
            ).forEach { mealDao.insertMeal(it) }

            // Seed Water Logs
            val waterDao = database.waterLogDao()
            listOf(
                WaterLogEntity(dateKey = today, amountMl = 250, label = "Morning Glass (250 ml)"),
                WaterLogEntity(dateKey = today, amountMl = 500, label = "Bottle Refill (500 ml)"),
                WaterLogEntity(dateKey = today, amountMl = 250, label = "Lunch Glass (250 ml)"),
                WaterLogEntity(dateKey = d1, amountMl = 2250, label = "Daily Hydration Goal"),
                WaterLogEntity(dateKey = d2, amountMl = 2000, label = "Daily Hydration"),
                WaterLogEntity(dateKey = d3, amountMl = 2500, label = "Daily Hydration Goal"),
                WaterLogEntity(dateKey = d4, amountMl = 1750, label = "Daily Hydration"),
                WaterLogEntity(dateKey = d5, amountMl = 2250, label = "Daily Hydration Goal")
            ).forEach { waterDao.insertWaterLog(it) }

            // Seed Weight History
            val weightDao = database.weightLogDao()
            listOf(
                WeightLogEntity(dateKey = d5, weightKg = 72.4f, note = "Starting baseline"),
                WeightLogEntity(dateKey = d4, weightKg = 72.1f, note = "Morning weigh-in"),
                WeightLogEntity(dateKey = d3, weightKg = 71.8f, note = "Consistent hydration"),
                WeightLogEntity(dateKey = d2, weightKg = 71.5f, note = "Post-jog morning"),
                WeightLogEntity(dateKey = d1, weightKg = 71.2f, note = "Clean meals all day"),
                WeightLogEntity(dateKey = today, weightKg = 70.9f, note = "Feeling energized!")
            ).forEach { weightDao.insertWeightLog(it) }

            // Seed Walking & Jogging Activities
            val activityDao = database.activityLogDao()
            listOf(
                ActivityLogEntity(dateKey = today, activityType = ActivityType.WALKING.name, durationMinutes = 22, distanceKm = 1.9f, steps = 2500, caloriesBurned = 95, note = "Morning park brisk walk"),
                ActivityLogEntity(dateKey = d1, activityType = ActivityType.JOGGING.name, durationMinutes = 32, distanceKm = 4.2f, steps = 4900, caloriesBurned = 290, note = "Sunset trail jog"),
                ActivityLogEntity(dateKey = d2, activityType = ActivityType.WALKING.name, durationMinutes = 35, distanceKm = 2.8f, steps = 3800, caloriesBurned = 140, note = "Evening neighborhood walk"),
                ActivityLogEntity(dateKey = d3, activityType = ActivityType.JOGGING.name, durationMinutes = 25, distanceKm = 3.3f, steps = 3900, caloriesBurned = 225, note = "Steady pace cardio"),
                ActivityLogEntity(dateKey = d4, activityType = ActivityType.WALKING.name, durationMinutes = 40, distanceKm = 3.2f, steps = 4300, caloriesBurned = 165, note = "After-dinner stroll")
            ).forEach { activityDao.insertActivity(it) }
        }
    }
}
