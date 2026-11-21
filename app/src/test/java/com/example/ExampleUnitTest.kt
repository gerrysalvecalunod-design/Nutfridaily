package com.example

import com.example.data.local.DateUtils
import com.example.data.local.TacticsSerializer
import com.example.data.model.AppSettings
import com.example.data.model.CompletedLessonEntity
import com.example.data.model.DifficultyLevel
import com.example.data.model.FitnessCategory
import com.example.data.model.PlayerPosition
import com.example.data.model.ProgramDayCompletionEntity
import com.example.data.model.SkillCategory
import com.example.data.model.TacticArrow
import com.example.data.model.TrainingGoalEntity
import com.example.data.repository.PositionsAndProgramsCurriculum
import com.example.data.repository.SkillAndFitnessCurriculum
import com.example.ui.theme.stringsFor
import com.example.viewmodel.FootballViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun skillCurriculum_containsAllSixModulesWithFiveLessonsEach() {
        val lessons = SkillAndFitnessCurriculum.skillLessons
        assertEquals(30, lessons.size)
        SkillCategory.entries.forEach { cat ->
            assertEquals(5, lessons.count { it.categoryKey == cat.name })
        }
    }

    @Test
    fun fitnessCurriculum_coversAllEightCategoriesAndThreeTiers() {
        val fitness = SkillAndFitnessCurriculum.fitnessLessons
        assertEquals(24, fitness.size)
        FitnessCategory.entries.forEach { cat ->
            val catLessons = fitness.filter { it.categoryKey == cat.name }
            assertEquals(3, catLessons.size)
            assertTrue(catLessons.any { it.difficulty == DifficultyLevel.BEGINNER })
            assertTrue(catLessons.any { it.difficulty == DifficultyLevel.INTERMEDIATE })
            assertTrue(catLessons.any { it.difficulty == DifficultyLevel.ADVANCED })
        }
    }

    @Test
    fun positionCurriculum_coversAllEightPositions() {
        val posLessons = PositionsAndProgramsCurriculum.positionLessons
        PlayerPosition.entries.forEach { pos ->
            assertTrue(posLessons.any { it.categoryKey == pos.name })
        }
    }

    @Test
    fun trainingPrograms_include7Day30DayAnd90DaySchedules() {
        val programs = PositionsAndProgramsCurriculum.programs
        assertEquals(3, programs.size)
        assertEquals(7, programs[0].days.size)
        assertEquals(30, programs[1].days.size)
        assertEquals(90, programs[2].days.size)
        assertTrue(programs.all { p -> p.days.any { it.isRestDay } })
    }

    @Test
    fun tacticsSerializer_roundTripsMarkersAndArrows() {
        val markers = TacticsSerializer.defaultFormationMarkers("4-3-3")
        val decodedMarkers = TacticsSerializer.deserializeMarkers(TacticsSerializer.serializeMarkers(markers))
        assertEquals(markers.size, decodedMarkers.size)

        val arrows = listOf(TacticArrow("a1", 0.2f, 0.3f, 0.5f, 0.1f))
        val decodedArrows = TacticsSerializer.deserializeArrows(TacticsSerializer.serializeArrows(arrows))
        assertEquals(1, decodedArrows.size)
        assertEquals(0.2f, decodedArrows[0].startX, 0.001f)
    }

    @Test
    fun progressCalculation_startsAtZeroAndReachesHundredPercentOnAllCategoryDrills() {
        val skillLessons = SkillAndFitnessCurriculum.skillLessons
        val programs = PositionsAndProgramsCurriculum.programs
        val settings = AppSettings(dailyMinutesGoal = 30, weeklySessionsGoal = 5, activeProgramId = "program_7")

        val emptySummary = FootballViewModel.computeSummary(
            completed = emptyList(),
            progCompletions = emptyList(),
            currentSettings = settings,
            goals = emptyList(),
            skillLessons = skillLessons,
            programs = programs
        )
        assertTrue(emptySummary.skillRatings.all { it.scorePercent == 0 })
        assertEquals(0, emptySummary.dailyGoalPercent)

        val today = DateUtils.todayKey()
        val ballControlDrills = skillLessons.filter { it.categoryKey == SkillCategory.BALL_CONTROL.name }
        val completedAllBallControl = ballControlDrills.map { lesson ->
            CompletedLessonEntity(
                lessonId = lesson.id,
                dateKey = today,
                categoryKey = lesson.categoryKey,
                categoryLabel = lesson.categoryLabel,
                title = lesson.title,
                durationMinutes = 10
            )
        }
        val fullSummary = FootballViewModel.computeSummary(
            completed = completedAllBallControl,
            progCompletions = listOf(ProgramDayCompletionEntity("program_7", 1, true, today)),
            currentSettings = settings,
            goals = listOf(TrainingGoalEntity(title = "Test", isCompleted = true)),
            skillLessons = skillLessons,
            programs = programs
        )
        val bcRating = fullSummary.skillRatings.first { it.category == SkillCategory.BALL_CONTROL }
        val defRating = fullSummary.skillRatings.first { it.category == SkillCategory.DEFENDING }
        assertEquals(100, bcRating.scorePercent)
        assertEquals(0, defRating.scorePercent)
        assertEquals(100, fullSummary.dailyGoalPercent)
        assertEquals(100, fullSummary.weeklyGoalPercent)
        assertEquals(1, fullSummary.completedGoalsCount)
    }

    @Test
    fun languageStrings_supportEnglishAndTagalog() {
        val en = stringsFor("en")
        val tl = stringsFor("tl")
        assertEquals("History", en.navHistory)
        assertEquals("Kasaysayan", tl.navHistory)
        assertEquals("Settings", en.navSettings)
        assertEquals("Mga Setting", tl.navSettings)
        assertEquals("Training Goals", en.goalsScreenTitle)
        assertEquals("Mga Layunin sa Pagsasanay", tl.goalsScreenTitle)
    }
}
