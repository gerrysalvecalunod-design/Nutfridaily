package com.example

import com.example.data.local.DatabaseInitializer
import com.example.data.local.DateUtils
import com.example.data.model.AppSettings
import com.example.data.model.CompletedSessionEntity
import com.example.data.model.FitnessArea
import com.example.data.model.PlayerPosition
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.ProgramModuleCompletionEntity
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory
import com.example.data.repository.ConditioningAndDefenseCurriculum
import com.example.data.repository.DailyPlanGenerator
import com.example.data.repository.ProgramsAndTacticsCurriculum
import com.example.data.repository.SkillAndFitnessCurriculum
import com.example.viewmodel.FootballViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    private val allCategoryExercises =
        SkillAndFitnessCurriculum.attackAndBallExercises +
            ConditioningAndDefenseCurriculum.speedDefenseAndGkExercises

    @Test
    fun programsCurriculum_hasBeginnerIntermediateAndAdvancedWithSixModulesEach() {
        val programs = ProgramsAndTacticsCurriculum.programs
        assertEquals(3, programs.size)
        assertTrue(programs.any { it.level == SkillLevel.BEGINNER && it.moduleIds.size == 6 })
        assertTrue(programs.any { it.level == SkillLevel.INTERMEDIATE && it.moduleIds.size == 6 })
        assertTrue(programs.any { it.level == SkillLevel.ADVANCED && it.moduleIds.size == 6 })

        val moduleIds = ProgramsAndTacticsCurriculum.programExercises.map { it.id }.toSet()
        programs.forEach { program ->
            program.moduleIds.forEach { modId ->
                assertTrue("Missing program module $modId", moduleIds.contains(modId))
            }
        }
    }

    @Test
    fun rulesCurriculum_coversAllSevenFootballLessons() {
        val rules = ProgramsAndTacticsCurriculum.rulesLessons
        assertEquals(7, rules.size)
        assertTrue(rules.all { it.instructions.isNotEmpty() && it.trainingTips.isNotEmpty() })
    }

    @Test
    fun categoriesCurriculum_coversAllEightCategoriesAndSixtyOneDrills() {
        assertEquals(8, TrainingCategory.entries.size)
        assertEquals(61, allCategoryExercises.size)
        assertEquals(10, PlayerPosition.entries.size)

        val expectedCounts = mapOf(
            TrainingCategory.BALL_MASTERY to 8,
            TrainingCategory.DRIBBLING to 7,
            TrainingCategory.PASSING to 7,
            TrainingCategory.SHOOTING to 8,
            TrainingCategory.SPEED_AGILITY to 8,
            TrainingCategory.PHYSICAL_CONDITIONING to 9,
            TrainingCategory.DEFENDING to 7,
            TrainingCategory.GOALKEEPER to 7
        )

        TrainingCategory.entries.forEach { cat ->
            val catExercises = allCategoryExercises.filter { it.categoryKey == cat.name }
            assertEquals("Unexpected drill count for ${cat.name}", expectedCounts[cat], catExercises.size)
            catExercises.forEach { ex ->
                assertTrue(ex.instructions.isNotEmpty())
                assertTrue(ex.trainingTips.isNotEmpty())
                assertTrue(ex.timerWorkSeconds > 0)
            }
        }
    }

    @Test
    fun fitnessCurriculum_coversAllEightAreasWithSafetyInstructions() {
        val fitness = ConditioningAndDefenseCurriculum.fitnessExercises
        assertEquals(8, fitness.size)
        FitnessArea.entries.forEach { area ->
            val item = fitness.find { it.categoryKey == area.name }
            assertTrue("Missing fitness area ${area.name}", item != null)
            assertTrue("Safety instructions required for ${area.name}", item!!.safetyInstructions.isNotEmpty())
        }
    }

    @Test
    fun dailyPlanGenerator_buildsPersonalizedWarmupMainDrillsRestAndCooldown() {
        val strikerProfile = PlayerProfileEntity(
            playerName = "Cristiano",
            age = 22,
            skillLevel = SkillLevel.INTERMEDIATE.name,
            playerPosition = PlayerPosition.STRIKER.name,
            trainingGoals = "Sharpen shooting and agility",
            preferredDurationMinutes = 30
        )
        val plan = DailyPlanGenerator.generatePlanItems(
            profile = strikerProfile,
            allCategoryExercises = allCategoryExercises,
            fitnessExercises = ConditioningAndDefenseCurriculum.fitnessExercises,
            dateKey = DateUtils.todayKey()
        )
        assertEquals("Warm-up", plan.first().sectionLabel)
        assertEquals("Cool-down", plan.last().sectionLabel)
        val mainDrills = plan.filter { it.sectionLabel.startsWith("Main Drill") }
        val restSlots = plan.filter { it.isRestSlot }
        assertEquals(4, mainDrills.size)
        assertEquals(3, restSlots.size)

        val gkProfile = strikerProfile.copy(
            playerPosition = PlayerPosition.GOALKEEPER.name,
            preferredDurationMinutes = 15
        )
        val gkPlan = DailyPlanGenerator.generatePlanItems(
            profile = gkProfile,
            allCategoryExercises = allCategoryExercises,
            fitnessExercises = ConditioningAndDefenseCurriculum.fitnessExercises,
            dateKey = DateUtils.todayKey()
        )
        assertEquals(2, gkPlan.count { it.sectionLabel.startsWith("Main Drill") })
        assertTrue(gkPlan.any { it.categoryLabel == TrainingCategory.GOALKEEPER.displayName })
    }

    @Test
    fun defaultAchievements_containsAllNineMilestones() {
        val achievements = DatabaseInitializer.defaultAchievements()
        assertEquals(9, achievements.size)
    }

    @Test
    fun dashboardSummary_computesEmptyAndCompletedMetricsCorrectly() {
        val empty = FootballViewModel.computeSummary(
            sessions = emptyList(),
            progDone = emptyList(),
            currentSettings = AppSettings(),
            categoryExercises = allCategoryExercises,
            programs = ProgramsAndTacticsCurriculum.programs,
            achievements = DatabaseInitializer.defaultAchievements()
        )
        assertEquals(0, empty.totalSessions)
        assertEquals(0, empty.totalCompletedDrills)
        assertEquals(0, empty.totalMinutes)
        assertEquals("0.0", empty.totalHoursFormatted)
        assertEquals(0, empty.streakDays)
        assertEquals(7, empty.weeklyPoints.size)
        assertEquals(28, empty.monthlyPoints.size)
        assertEquals(0, empty.unlockedAchievementsCount)
        assertEquals(9, empty.totalAchievementsCount)

        val today = DateUtils.todayKey()
        val dribbleEx = allCategoryExercises.first {
            it.categoryKey == TrainingCategory.DRIBBLING.name
        }
        val completed = listOf(
            CompletedSessionEntity(
                exerciseId = dribbleEx.id,
                dateKey = today,
                categoryKey = TrainingCategory.DRIBBLING.name,
                categoryLabel = TrainingCategory.DRIBBLING.displayName,
                title = dribbleEx.name,
                durationMinutes = 30,
                completedDrillsCount = 3,
                drillsSummary = dribbleEx.recommendedReps,
                resultSummary = "Timer Completed • 100%"
            )
        )
        val progDone = listOf(
            ProgramModuleCompletionEntity("prog_beginner", "beg_rules", true, today),
            ProgramModuleCompletionEntity("prog_beginner", "beg_touch", true, today),
            ProgramModuleCompletionEntity("prog_beginner", "beg_dribble", true, today)
        )
        val populated = FootballViewModel.computeSummary(
            sessions = completed,
            progDone = progDone,
            currentSettings = AppSettings(dailyMinutesGoal = 30, weeklySessionsGoal = 5, activeProgramId = "prog_beginner"),
            categoryExercises = allCategoryExercises,
            programs = ProgramsAndTacticsCurriculum.programs,
            achievements = DatabaseInitializer.defaultAchievements().mapIndexed { idx, a ->
                if (idx == 0) a.copy(unlocked = true, progressCurrent = 1) else a
            }
        )
        assertEquals(1, populated.totalSessions)
        assertEquals(3, populated.totalCompletedDrills)
        assertEquals(30, populated.totalMinutes)
        assertEquals(100, populated.dailyProgressPercent)
        assertEquals(20, populated.weeklyProgressPercent)
        assertEquals(50, populated.activeProgramPercent)
        assertEquals(1, populated.streakDays)
        assertEquals(1, populated.unlockedAchievementsCount)
    }
}
