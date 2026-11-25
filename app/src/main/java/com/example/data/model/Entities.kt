package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SkillLevel(val displayName: String, val subtitle: String) {
    BEGINNER("Beginner", "Rules, grips, ready stance & fundamentals"),
    INTERMEDIATE("Intermediate", "Smashes, drops, clears, net play & defense"),
    ADVANCED("Advanced", "Jump smashes, deception, combos & tactics")
}

enum class TrainingCategory(val displayName: String, val subtitle: String) {
    FOOTWORK("Footwork Training", "6-corner steps, split-step & court recovery"),
    SMASH("Smash Training", "Power pronation, jump smash & steep angles"),
    SERVING("Serving Training", "Short backhand, flick & high deep serves"),
    DEFENSIVE("Defensive Training", "Low blocks, high lifts & flat drive defense"),
    SPEED_AGILITY("Speed & Agility", "Explosive lunges, chasse & sprint bursts"),
    ENDURANCE("Endurance Training", "Multi-shuttle stamina & rally conditioning"),
    ACCURACY("Accuracy Training", "Target cones, line drops & tight net shots"),
    REACTION_TIME("Reaction Time", "Fast drive exchanges & reflex shadow cues"),
    MATCH_STRATEGY("Match Strategy", "Singles pressure & doubles rotation tactics")
}

enum class FitnessArea(val displayName: String) {
    WARMUP("Warm-up"),
    STRETCHING("Stretching"),
    JUMP_ROPE("Jump Rope"),
    SQUATS("Squats"),
    LUNGES("Lunges"),
    PLANKS("Planks"),
    AGILITY_DRILLS("Agility Drills"),
    COOLDOWN("Cool-down")
}

enum class LessonSection {
    PROGRAM,
    CATEGORY,
    FITNESS,
    RULES
}

enum class CourtDiagramType {
    SIX_CORNER_FOOTWORK,
    SMASH_TRAJECTORY,
    SERVE_ZONES,
    NET_HAIRPIN,
    DEFENSE_BLOCK,
    CLEAR_AND_DROP,
    DOUBLES_ROTATION,
    COURT_DIMENSIONS,
    CONDITIONING_AGILITY
}

data class BadmintonExercise(
    val id: String,
    val name: String,
    val section: LessonSection,
    val categoryKey: String,
    val categoryLabel: String,
    val description: String,
    val instructions: List<String>,
    val durationMinutes: Int,
    val timerWorkSeconds: Int,
    val difficulty: SkillLevel,
    val recommendedReps: String,
    val restTimeSeconds: Int,
    val trainingTips: List<String>,
    val safetyInstructions: List<String> = emptyList(),
    val diagramType: CourtDiagramType = CourtDiagramType.SIX_CORNER_FOOTWORK,
    val motionPhases: List<String> = listOf(
        "Ready Stance: Knees bent, racket up",
        "Split-Step: Push off as opponent strikes",
        "Execute Stroke: Clean contact in front",
        "Recover Base: Return to center court"
    )
)

data class BadmintonProgram(
    val id: String,
    val level: SkillLevel,
    val title: String,
    val badge: String,
    val description: String,
    val focusSummary: String,
    val moduleIds: List<String>
)

data class AppSettings(
    val isDarkTheme: Boolean = false,
    val soundEnabled: Boolean = true,
    val soundToneIndex: Int = 0, // 0 = Crisp Beep, 1 = Whistle Cue, 2 = Soft Chime
    val preferredDurationMinutes: Int = 30,
    val dailyMinutesGoal: Int = 30,
    val weeklySessionsGoal: Int = 5,
    val activeProgramId: String = "prog_beginner"
)

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val playerName: String = "",
    val skillLevel: String = SkillLevel.BEGINNER.name,
    val trainingGoals: String = "Build crisp 6-corner footwork, consistent low serves, and explosive smash power.",
    val preferredDurationMinutes: Int = 30,
    val onboardingCompleted: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "completed_sessions")
data class CompletedSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseId: String,
    val dateKey: String,
    val categoryKey: String,
    val categoryLabel: String,
    val title: String,
    val durationMinutes: Int,
    val drillsSummary: String = "",
    val resultSummary: String = "Completed",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "program_module_completions", primaryKeys = ["programId", "moduleId"])
data class ProgramModuleCompletionEntity(
    val programId: String,
    val moduleId: String,
    val isCompleted: Boolean,
    val completedDateKey: String = ""
)

@Entity(tableName = "planned_workouts")
data class PlannedWorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayOfWeek: String, // Mon..Sun
    val title: String,
    val categoryLabel: String,
    val exerciseIdsSerialized: String = "",
    val durationMinutes: Int = 30,
    val isCompleted: Boolean = false
)

@Entity(tableName = "personal_records")
data class PersonalRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val valueText: String,
    val unit: String,
    val updatedDateKey: String
)

@Entity(tableName = "training_goals")
data class TrainingGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String = "Footwork",
    val targetValue: Int = 5,
    val currentValue: Int = 0,
    val unit: String = "sessions",
    val isCompleted: Boolean = false,
    val createdDateKey: String = ""
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val unlocked: Boolean = false,
    val unlockedDateKey: String = "",
    val progressCurrent: Int = 0,
    val progressTarget: Int = 1
)

@Entity(tableName = "saved_training_checkpoint")
data class SavedTrainingCheckpointEntity(
    @PrimaryKey val id: Int = 1,
    val queueIdsSerialized: String = "",
    val customTitle: String = "",
    val currentIndex: Int = 0,
    val phase: String = "WORK",
    val totalPhaseSeconds: Int = 60,
    val remainingSeconds: Int = 60,
    val completedInSessionCount: Int = 0,
    val isActive: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
