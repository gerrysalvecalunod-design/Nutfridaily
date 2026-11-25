package com.example.data.repository

import com.example.data.model.DailyPlanItemEntity
import com.example.data.model.FootballExercise
import com.example.data.model.PlayerPosition
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingCategory

object DailyPlanGenerator {

    fun generatePlanItems(
        profile: PlayerProfileEntity,
        allCategoryExercises: List<FootballExercise>,
        fitnessExercises: List<FootballExercise>,
        dateKey: String
    ): List<DailyPlanItemEntity> {
        val level = SkillLevel.entries.find { it.name == profile.skillLevel } ?: SkillLevel.BEGINNER
        val position = PlayerPosition.entries.find { it.name == profile.playerPosition } ?: PlayerPosition.STRIKER
        val duration = profile.preferredDurationMinutes.coerceIn(15, 60)
        val goalLower = profile.trainingGoals.lowercase()

        val mainDrillCount = when {
            duration <= 15 -> 2
            duration <= 20 -> 3
            duration <= 30 -> 4
            duration <= 45 -> 5
            else -> 6
        }

        val preferredCategories = priorityCategoriesFor(position, goalLower)

        val scored = allCategoryExercises.sortedWith(
            compareByDescending<FootballExercise> { ex ->
                var score = 0
                val catIdx = preferredCategories.indexOfFirst { it.name == ex.categoryKey }
                if (catIdx >= 0) score += (10 - catIdx) * 10
                if (ex.difficulty == level) score += 25
                else if (level == SkillLevel.BEGINNER && ex.difficulty == SkillLevel.INTERMEDIATE) score += 8
                else if (level == SkillLevel.ADVANCED && ex.difficulty == SkillLevel.INTERMEDIATE) score += 10
                if (goalLower.contains("shoot") && ex.categoryKey == TrainingCategory.SHOOTING.name) score += 20
                if (goalLower.contains("pass") && ex.categoryKey == TrainingCategory.PASSING.name) score += 20
                if ((goalLower.contains("dribbl") || goalLower.contains("skill")) &&
                    (ex.categoryKey == TrainingCategory.DRIBBLING.name || ex.categoryKey == TrainingCategory.BALL_MASTERY.name)
                ) score += 20
                if ((goalLower.contains("speed") || goalLower.contains("fast") || goalLower.contains("agil")) &&
                    ex.categoryKey == TrainingCategory.SPEED_AGILITY.name
                ) score += 20
                if ((goalLower.contains("stamina") || goalLower.contains("fit") || goalLower.contains("strength")) &&
                    ex.categoryKey == TrainingCategory.PHYSICAL_CONDITIONING.name
                ) score += 20
                if (goalLower.contains("defend") && ex.categoryKey == TrainingCategory.DEFENDING.name) score += 20
                if ((goalLower.contains("keep") || goalLower.contains("save")) &&
                    ex.categoryKey == TrainingCategory.GOALKEEPER.name
                ) score += 20
                score
            }
        )

        val selectedMain = mutableListOf<FootballExercise>()
        val usedCats = mutableSetOf<String>()
        for (ex in scored) {
            if (selectedMain.size >= mainDrillCount) break
            if (ex.categoryKey !in usedCats || selectedMain.size >= preferredCategories.size) {
                selectedMain.add(ex)
                usedCats.add(ex.categoryKey)
            }
        }
        if (selectedMain.size < mainDrillCount) {
            for (ex in scored) {
                if (selectedMain.size >= mainDrillCount) break
                if (selectedMain.none { it.id == ex.id }) selectedMain.add(ex)
            }
        }

        val warmup = fitnessExercises.find { it.id == "fit_warmup" } ?: fitnessExercises.first()
        val cooldown = fitnessExercises.find { it.id == "fit_cooldown" } ?: fitnessExercises.last()

        val items = mutableListOf<DailyPlanItemEntity>()
        var order = 0

        items += DailyPlanItemEntity(
            planDateKey = dateKey,
            sortOrder = order++,
            sectionLabel = "Warm-up",
            exerciseId = warmup.id,
            title = warmup.name,
            categoryLabel = "Warm-up • ${position.displayName}",
            durationSeconds = warmup.timerWorkSeconds,
            isRestSlot = false,
            isCompleted = false,
            focusNote = "Prepare ankles, hips, and calves for ${level.displayName} tempo."
        )

        selectedMain.forEachIndexed { idx, ex ->
            items += DailyPlanItemEntity(
                planDateKey = dateKey,
                sortOrder = order++,
                sectionLabel = "Main Drill ${idx + 1}",
                exerciseId = ex.id,
                title = ex.name,
                categoryLabel = ex.categoryLabel,
                durationSeconds = ex.timerWorkSeconds,
                isRestSlot = false,
                isCompleted = false,
                focusNote = "${ex.recommendedReps} • Tailored for ${position.displayName}"
            )
            if (idx < selectedMain.lastIndex) {
                items += DailyPlanItemEntity(
                    planDateKey = dateKey,
                    sortOrder = order++,
                    sectionLabel = "Rest Interval",
                    exerciseId = "rest_${ex.id}",
                    title = "Active Recovery & Hydration (${ex.restTimeSeconds}s)",
                    categoryLabel = "Rest Interval",
                    durationSeconds = ex.restTimeSeconds,
                    isRestSlot = true,
                    isCompleted = false,
                    focusNote = "Shake out legs, steady breathing, and set cones for next drill."
                )
            }
        }

        items += DailyPlanItemEntity(
            planDateKey = dateKey,
            sortOrder = order,
            sectionLabel = "Cool-down",
            exerciseId = cooldown.id,
            title = cooldown.name,
            categoryLabel = "Cool-down",
            durationSeconds = cooldown.timerWorkSeconds,
            isRestSlot = false,
            isCompleted = false,
            focusNote = "Stretch hamstrings, quads, and calves gently."
        )

        return items
    }

    private fun priorityCategoriesFor(position: PlayerPosition, goalLower: String): List<TrainingCategory> {
        val base = when (position) {
            PlayerPosition.STRIKER -> listOf(
                TrainingCategory.SHOOTING,
                TrainingCategory.SPEED_AGILITY,
                TrainingCategory.BALL_MASTERY,
                TrainingCategory.DRIBBLING
            )
            PlayerPosition.LEFT_WINGER, PlayerPosition.RIGHT_WINGER -> listOf(
                TrainingCategory.DRIBBLING,
                TrainingCategory.SPEED_AGILITY,
                TrainingCategory.SHOOTING,
                TrainingCategory.BALL_MASTERY
            )
            PlayerPosition.CENTRAL_MIDFIELDER -> listOf(
                TrainingCategory.PASSING,
                TrainingCategory.BALL_MASTERY,
                TrainingCategory.PHYSICAL_CONDITIONING,
                TrainingCategory.DRIBBLING
            )
            PlayerPosition.DEFENSIVE_MIDFIELDER -> listOf(
                TrainingCategory.DEFENDING,
                TrainingCategory.PASSING,
                TrainingCategory.PHYSICAL_CONDITIONING,
                TrainingCategory.BALL_MASTERY
            )
            PlayerPosition.ATTACKING_MIDFIELDER -> listOf(
                TrainingCategory.BALL_MASTERY,
                TrainingCategory.PASSING,
                TrainingCategory.DRIBBLING,
                TrainingCategory.SHOOTING
            )
            PlayerPosition.DEFENDER, PlayerPosition.CENTER_BACK -> listOf(
                TrainingCategory.DEFENDING,
                TrainingCategory.PASSING,
                TrainingCategory.PHYSICAL_CONDITIONING,
                TrainingCategory.SPEED_AGILITY
            )
            PlayerPosition.FULLBACK -> listOf(
                TrainingCategory.SPEED_AGILITY,
                TrainingCategory.DEFENDING,
                TrainingCategory.PASSING,
                TrainingCategory.PHYSICAL_CONDITIONING
            )
            PlayerPosition.GOALKEEPER -> listOf(
                TrainingCategory.GOALKEEPER,
                TrainingCategory.SPEED_AGILITY,
                TrainingCategory.PHYSICAL_CONDITIONING,
                TrainingCategory.PASSING
            )
        }
        return if (goalLower.contains("shoot") && position != PlayerPosition.GOALKEEPER) {
            (listOf(TrainingCategory.SHOOTING) + base).distinct()
        } else {
            base
        }
    }
}
