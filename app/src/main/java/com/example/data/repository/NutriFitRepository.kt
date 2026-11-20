package com.example.data.repository

import com.example.data.local.NutriFitDatabase
import com.example.data.model.ActivityLogEntity
import com.example.data.model.HealthyFoodSuggestion
import com.example.data.model.MealCategory
import com.example.data.model.MealLogEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WaterLogEntity
import com.example.data.model.WeightLogEntity
import kotlinx.coroutines.flow.Flow

class NutriFitRepository(private val database: NutriFitDatabase) {
    private val profileDao = database.userProfileDao()
    private val mealDao = database.mealLogDao()
    private val waterDao = database.waterLogDao()
    private val weightDao = database.weightLogDao()
    private val activityDao = database.activityLogDao()
    private val reminderDao = database.reminderDao()

    val userProfile: Flow<UserProfileEntity?> = profileDao.observeProfile()
    val allMeals: Flow<List<MealLogEntity>> = mealDao.getAllMeals()
    val allWaterLogs: Flow<List<WaterLogEntity>> = waterDao.getAllWaterLogs()
    val allWeightLogs: Flow<List<WeightLogEntity>> = weightDao.getAllWeightLogs()
    val allActivities: Flow<List<ActivityLogEntity>> = activityDao.getAllActivities()
    val allReminders: Flow<List<ReminderEntity>> = reminderDao.getAllReminders()

    fun getMealsForDate(dateKey: String): Flow<List<MealLogEntity>> = mealDao.getMealsForDate(dateKey)
    fun getWaterLogsForDate(dateKey: String): Flow<List<WaterLogEntity>> = waterDao.getWaterLogsForDate(dateKey)
    fun getActivitiesForDate(dateKey: String): Flow<List<ActivityLogEntity>> = activityDao.getActivitiesForDate(dateKey)

    suspend fun upsertProfile(profile: UserProfileEntity) = profileDao.upsertProfile(profile)
    suspend fun getProfileOnce(): UserProfileEntity? = profileDao.getProfileOnce()

    suspend fun insertMeal(meal: MealLogEntity) = mealDao.insertMeal(meal)
    suspend fun deleteMeal(meal: MealLogEntity) = mealDao.deleteMeal(meal)

    suspend fun insertWaterLog(log: WaterLogEntity) = waterDao.insertWaterLog(log)
    suspend fun deleteWaterLog(log: WaterLogEntity) = waterDao.deleteWaterLog(log)

    suspend fun insertWeightLog(log: WeightLogEntity) = weightDao.insertWeightLog(log)
    suspend fun deleteWeightLog(log: WeightLogEntity) = weightDao.deleteWeightLog(log)

    suspend fun insertActivity(log: ActivityLogEntity) = activityDao.insertActivity(log)
    suspend fun deleteActivity(log: ActivityLogEntity) = activityDao.deleteActivity(log)

    suspend fun insertReminder(reminder: ReminderEntity) = reminderDao.insertReminder(reminder)
    suspend fun updateReminder(reminder: ReminderEntity) = reminderDao.updateReminder(reminder)
    suspend fun deleteReminder(reminder: ReminderEntity) = reminderDao.deleteReminder(reminder)

    val healthyFoodSuggestions: List<HealthyFoodSuggestion> = listOf(
        HealthyFoodSuggestion(
            id = 1,
            name = "Berry Chia Overnight Oats",
            mealCategory = MealCategory.BREAKFAST,
            calories = 320,
            proteinGrams = 13,
            carbsGrams = 48,
            fatGrams = 9,
            benefitTag = "High Fiber • Sustained Energy",
            description = "Rolled oats soaked with chia seeds, blueberries, and unsweetened almond milk."
        ),
        HealthyFoodSuggestion(
            id = 2,
            name = "Avocado & Poached Egg Toast",
            mealCategory = MealCategory.BREAKFAST,
            calories = 360,
            proteinGrams = 16,
            carbsGrams = 28,
            fatGrams = 20,
            benefitTag = "Healthy Fats • Protein Rich",
            description = "Whole-grain sourdough topped with mashed avocado, chili flakes, and 2 eggs."
        ),
        HealthyFoodSuggestion(
            id = 3,
            name = "Spinach Banana Green Smoothie",
            mealCategory = MealCategory.BREAKFAST,
            calories = 240,
            proteinGrams = 11,
            carbsGrams = 38,
            fatGrams = 5,
            benefitTag = "Antioxidants • Hydrating",
            description = "Fresh baby spinach blended with banana, Greek yogurt, and flaxseeds."
        ),
        HealthyFoodSuggestion(
            id = 4,
            name = "Grilled Chicken & Quinoa Bowl",
            mealCategory = MealCategory.LUNCH,
            calories = 480,
            proteinGrams = 38,
            carbsGrams = 44,
            fatGrams = 15,
            benefitTag = "Lean Muscle Fuel",
            description = "Herb-marinated chicken breast over fluffy quinoa, cherry tomatoes, and cucumber."
        ),
        HealthyFoodSuggestion(
            id = 5,
            name = "Tuna & Chickpea Mediterranean Salad",
            mealCategory = MealCategory.LUNCH,
            calories = 410,
            proteinGrams = 32,
            carbsGrams = 35,
            fatGrams = 14,
            benefitTag = "Omega-3 • Heart Healthy",
            description = "Water-packed tuna tossed with chickpeas, arugula, olives, and lemon vinaigrette."
        ),
        HealthyFoodSuggestion(
            id = 6,
            name = "Baked Salmon & Roasted Broccoli",
            mealCategory = MealCategory.DINNER,
            calories = 490,
            proteinGrams = 36,
            carbsGrams = 24,
            fatGrams = 25,
            benefitTag = "Omega-3 • Low Carb",
            description = "Lemon-garlic salmon fillet paired with crispy oven-roasted broccoli and carrots."
        ),
        HealthyFoodSuggestion(
            id = 7,
            name = "Veggie Tofu & Edamame Stir-Fry",
            mealCategory = MealCategory.DINNER,
            calories = 390,
            proteinGrams = 24,
            carbsGrams = 42,
            fatGrams = 12,
            benefitTag = "Plant Protein • Light Dinner",
            description = "Firm tofu sauteed with bell peppers, snap peas, ginger, and brown rice."
        ),
        HealthyFoodSuggestion(
            id = 8,
            name = "Greek Yogurt & Roasted Almonds",
            mealCategory = MealCategory.SNACKS,
            calories = 190,
            proteinGrams = 15,
            carbsGrams = 12,
            fatGrams = 9,
            benefitTag = "Probiotic • Low Sugar",
            description = "Plain non-fat Greek yogurt drizzled with a touch of honey and sliced almonds."
        ),
        HealthyFoodSuggestion(
            id = 9,
            name = "Crisp Apple & Peanut Butter",
            mealCategory = MealCategory.SNACKS,
            calories = 210,
            proteinGrams = 6,
            carbsGrams = 26,
            fatGrams = 10,
            benefitTag = "Pre-Walk Fuel",
            description = "Fresh red apple slices served with 1 tbsp natural unsweetened peanut butter."
        ),
        HealthyFoodSuggestion(
            id = 10,
            name = "Steamed Edamame with Sea Salt",
            mealCategory = MealCategory.SNACKS,
            calories = 150,
            proteinGrams = 14,
            carbsGrams = 11,
            fatGrams = 5,
            benefitTag = "Plant Fiber • Clean Snack",
            description = "Warm lightly salted edamame pods packed with plant-based protein."
        )
    )
}
