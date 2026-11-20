package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MealCategory(val displayName: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACKS("Snacks")
}

enum class ActivityType(val displayName: String) {
    WALKING("Walking"),
    JOGGING("Jogging")
}

enum class ReminderCategory(val displayName: String) {
    WATER("Hydration"),
    NUTRITION("Meals"),
    ACTIVITY("Walk & Jog"),
    WEIGHT("Weight")
}

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Alex Morgan",
    val email: String = "alex@local.offline",
    val heightCm: Float = 170f,
    val startWeightKg: Float = 72.5f,
    val targetWeightKg: Float = 66.0f,
    val dailyCalorieGoal: Int = 2000,
    val dailyWaterGoalMl: Int = 2250,
    val dailyActivityMinutesGoal: Int = 30,
    val dailyDistanceKmGoal: Float = 3.0f,
    val waterReminderEnabled: Boolean = true,
    val waterReminderIntervalHours: Int = 2,
    val mealReminderEnabled: Boolean = true,
    val activityReminderEnabled: Boolean = true,
    val isLoggedIn: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "meal_logs")
data class MealLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateKey: String,
    val mealType: String, // MealCategory.name
    val foodName: String,
    val calories: Int,
    val proteinGrams: Int = 0,
    val carbsGrams: Int = 0,
    val fatGrams: Int = 0,
    val isHealthyChoice: Boolean = true,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "water_logs")
data class WaterLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateKey: String,
    val amountMl: Int,
    val label: String = "Glass (250 ml)",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "weight_logs")
data class WeightLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateKey: String,
    val weightKg: Float,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateKey: String,
    val activityType: String, // ActivityType.name
    val durationMinutes: Int,
    val distanceKm: Float,
    val steps: Int,
    val caloriesBurned: Int,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val timeLabel: String,
    val category: String, // ReminderCategory.name
    val isEnabled: Boolean = true,
    val lastCompletedDateKey: String = ""
)

data class HealthyFoodSuggestion(
    val id: Int,
    val name: String,
    val mealCategory: MealCategory,
    val calories: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatGrams: Int,
    val benefitTag: String,
    val description: String
)

data class DayProgressSummary(
    val dateKey: String,
    val shortDayLabel: String,
    val caloriesConsumed: Int,
    val waterMl: Int,
    val activityMinutes: Int,
    val distanceKm: Float,
    val habitsMetCount: Int
)
