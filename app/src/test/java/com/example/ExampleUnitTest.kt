package com.example

import com.example.data.local.DatabaseInitializer
import com.example.data.local.DateUtils
import com.example.data.model.AppSettings
import com.example.data.model.CompletedSessionEntity
import com.example.data.model.FitnessArea
import com.example.data.model.ProgramModuleCompletionEntity
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory
import com.example.data.repository.CategoriesAndFitnessCurriculum
import com.example.data.repository.ProgramsAndRulesCurriculum
import com.example.viewmodel.BadmintonViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun programsCurriculum_hasBeginnerIntermediateAndAdvancedWithSixModulesEach() {
        val programs = ProgramsAndRulesCurriculum.programs
        assertEquals(3, programs.size)
        assertTrue(programs.any { it.level == SkillLevel.BEGINNER && it.moduleIds.size == 6 })
        assertTrue(programs.any { it.level == SkillLevel.INTERMEDIATE && it.moduleIds.size == 6 })
        assertTrue(programs.any { it.level == SkillLevel.ADVANCED && it.moduleIds.size == 6 })

        val moduleIds = ProgramsAndRulesCurriculum.programExercises.map { it.id }.toSet()
        programs.forEach { program ->
            program.moduleIds.forEach { modId ->
                assertTrue("Missing program module $modId", moduleIds.contains(modId))
            }
        }
    }

    @Test
    fun rulesCurriculum_coversAllSevenBadmintonLessons() {
        val rules = ProgramsAndRulesCurriculum.rulesLessons
        assertEquals(7, rules.size)
        assertTrue(rules.all { it.instructions.isNotEmpty() && it.trainingTips.isNotEmpty() })
    }

    @Test
    fun categoriesCurriculum_coversAllNineTrainingCategories() {
        val exercises = CategoriesAndFitnessCurriculum.categoryExercises
        TrainingCategory.entries.forEach { cat ->
            val catExercises = exercises.filter { it.categoryKey == cat.name }
            assertTrue("Category ${cat.name} must have exercises", catExercises.isNotEmpty())
            catExercises.forEach { ex ->
                assertTrue(ex.instructions.isNotEmpty())
                assertTrue(ex.trainingTips.isNotEmpty())
                assertTrue(ex.timerWorkSeconds > 0)
            }
        }
    }

    @Test
    fun fitnessCurriculum_coversAllEightAreasWithSafetyInstructions() {
        val fitness = CategoriesAndFitnessCurriculum.fitnessExercises
        assertEquals(8, fitness.size)
        FitnessArea.entries.forEach { area ->
            val item = fitness.find { it.categoryKey == area.name }
            assertTrue("Missing fitness area ${area.name}", item != null)
            assertTrue("Safety instructions required for ${area.name}", item!!.safetyInstructions.isNotEmpty())
        }
    }

    @Test
    fun dashboardSummary_computesEmptyAndCompletedMetricsCorrectly() {
        val empty = BadmintonViewModel.computeSummary(
            sessions = emptyList(),
            progDone = emptyList(),
            currentSettings = AppSettings(),
            categoryExercises = CategoriesAndFitnessCurriculum.categoryExercises,
            programs = ProgramsAndRulesCurriculum.programs,
            achievements = DatabaseInitializer.defaultAchievements()
        )
        assertEquals(0, empty.totalSessions)
        assertEquals(0, empty.totalMinutes)
        assertEquals("0.0", empty.totalHoursFormatted)
        assertEquals(0, empty.streakDays)
        assertEquals(7, empty.weeklyPoints.size)
        assertEquals(28, empty.monthlyPoints.size)
        assertEquals(0, empty.unlockedAchievementsCount)
        assertEquals(8, empty.totalAchievementsCount)

        val today = DateUtils.todayKey()
        val footworkEx = CategoriesAndFitnessCurriculum.categoryExercises.first {
            it.categoryKey == TrainingCategory.FOOTWORK.name
        }
        val completed = listOf(
            CompletedSessionEntity(
                exerciseId = footworkEx.id,
                dateKey = today,
                categoryKey = TrainingCategory.FOOTWORK.name,
                categoryLabel = TrainingCategory.FOOTWORK.displayName,
                title = footworkEx.name,
                durationMinutes = 30,
                drillsSummary = footworkEx.recommendedReps,
                resultSummary = "Timer Completed • 100%"
            )
        )
        val progDone = listOf(
            ProgramModuleCompletionEntity("prog_beginner", "beg_rules", true, today),
            ProgramModuleCompletionEntity("prog_beginner", "beg_grip", true, today),
            ProgramModuleCompletionEntity("prog_beginner", "beg_ready", true, today)
        )
        val populated = BadmintonViewModel.computeSummary(
            sessions = completed,
            progDone = progDone,
            currentSettings = AppSettings(dailyMinutesGoal = 30, weeklySessionsGoal = 5, activeProgramId = "prog_beginner"),
            categoryExercises = CategoriesAndFitnessCurriculum.categoryExercises,
            programs = ProgramsAndRulesCurriculum.programs,
            achievements = DatabaseInitializer.defaultAchievements().mapIndexed { idx, a ->
                if (idx == 0) a.copy(unlocked = true, progressCurrent = 1) else a
            }
        )
        assertEquals(1, populated.totalSessions)
        assertEquals(30, populated.totalMinutes)
        assertEquals(100, populated.dailyProgressPercent)
        assertEquals(20, populated.weeklyProgressPercent)
        assertEquals(50, populated.activeProgramPercent)
        assertEquals(1, populated.streakDays)
        assertEquals(1, populated.unlockedAchievementsCount)
    }
}
