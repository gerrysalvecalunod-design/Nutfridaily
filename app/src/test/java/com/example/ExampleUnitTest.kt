package com.example

import com.example.data.local.DateUtils
import com.example.data.model.ActivityType
import com.example.data.model.MealCategory
import com.example.data.model.ReminderCategory
import com.example.data.model.UserProfileEntity
import com.example.viewmodel.TodayOverviewState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun mealCategories_containAllFourMeals() {
        val names = MealCategory.entries.map { it.name }
        assertEquals(listOf("BREAKFAST", "LUNCH", "DINNER", "SNACKS"), names)
    }

    @Test
    fun activityTypes_includeWalkingAndJogging() {
        val types = ActivityType.entries.map { it.name }
        assertTrue(types.contains("WALKING"))
        assertTrue(types.contains("JOGGING"))
        assertEquals(2, types.size)
    }

    @Test
    fun reminderCategories_coverAllHealthDomains() {
        val cats = ReminderCategory.entries.map { it.name }
        assertTrue(cats.contains("WATER"))
        assertTrue(cats.contains("NUTRITION"))
        assertTrue(cats.contains("ACTIVITY"))
        assertTrue(cats.contains("WEIGHT"))
    }

    @Test
    fun dateUtils_lastSevenDaysEndsWithToday() {
        val keys = DateUtils.lastNDaysKeys(7)
        assertEquals(7, keys.size)
        assertEquals(DateUtils.todayKey(), keys.last())
    }

    @Test
    fun userProfile_hasHealthyDefaults() {
        val profile = UserProfileEntity()
        val overview = TodayOverviewState()
        assertEquals(2000, profile.dailyCalorieGoal)
        assertEquals(2250, profile.dailyWaterGoalMl)
        assertEquals(30, profile.dailyActivityMinutesGoal)
        assertEquals(overview.calorieGoal, profile.dailyCalorieGoal)
    }
}
