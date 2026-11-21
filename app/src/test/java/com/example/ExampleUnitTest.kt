package com.example

import com.example.data.local.TacticsSerializer
import com.example.data.model.DifficultyLevel
import com.example.data.model.FitnessCategory
import com.example.data.model.PlayerPosition
import com.example.data.model.SkillCategory
import com.example.data.model.TacticArrow
import com.example.data.repository.PositionsAndProgramsCurriculum
import com.example.data.repository.SkillAndFitnessCurriculum
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
}
