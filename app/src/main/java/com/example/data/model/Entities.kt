package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SkillLevel(val displayName: String, val subtitle: String) {
    BEGINNER("Beginner", "Ball mastery, clean passing, first touch & pitch basics"),
    INTERMEDIATE("Intermediate", "1v1 dribbling, driven shots, crossing & pressing"),
    ADVANCED("Advanced", "Explosive finishing, high-tempo rondos & tactical phases")
}

enum class PlayerPosition(val displayName: String) {
    STRIKER("Striker"),
    LEFT_WINGER("Left Winger"),
    RIGHT_WINGER("Right Winger"),
    CENTRAL_MIDFIELDER("Central Midfielder"),
    DEFENSIVE_MIDFIELDER("Defensive Midfielder"),
    ATTACKING_MIDFIELDER("Attacking Midfielder"),
    DEFENDER("Defender"),
    FULLBACK("Fullback"),
    CENTER_BACK("Center Back"),
    GOALKEEPER("Goalkeeper")
}

enum class TrainingCategory(val letter: String, val displayName: String, val subtitle: String) {
    BALL_MASTERY("A", "Ball Mastery", "Toe taps, foundations, sole rolls & tight ball feel"),
    DRIBBLING("B", "Dribbling", "Cone slalom, 1v1 beats, zigzag & skill moves"),
    PASSING("C", "Passing", "Short, wall, 1-touch, 2-touch & long accuracy"),
    SHOOTING("D", "Shooting", "Power, finesse, instep, volleys & penalties"),
    SPEED_AGILITY("E", "Speed & Agility", "10m/20m sprints, shuttles, ladder & reactions"),
    PHYSICAL_CONDITIONING("F", "Physical Conditioning", "Endurance, core, leg strength, squats & planks"),
    DEFENDING("G", "Defending", "Footwork, 1v1 jockey, interception & recovery"),
    GOALKEEPER("H", "Goalkeeper Training", "Reactions, catching, diving, distribution & handling")
}

enum class FitnessArea(val displayName: String) {
    WARMUP("Warm-up"),
    STRETCHING("Mobility"),
    SPRINTS("Sprint Bursts"),
    LADDER_AGILITY("Cone & Ladder"),
    SQUATS("Leg Power"),
    LUNGES("Split Lunges"),
    CORE_PLANKS("Core Stability"),
    COOLDOWN("Cool-down")
}

enum class LessonSection {
    PROGRAM,
    CATEGORY,
    FITNESS,
    RULES
}

enum class PitchDiagramType {
    SLALOM_DRIBBLING,
    FINISHING_ZONES,
    PASSING_TRIANGLE,
    DEFENSE_SHADOW,
    PENALTY_SET_PIECE,
    AGILITY_LADDER,
    FORMATION_433,
    PITCH_DIMENSIONS,
    GOALKEEPER_ANGLES
}

data class FootballExercise(
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
    val diagramType: PitchDiagramType = PitchDiagramType.SLALOM_DRIBBLING,
    val motionPhases: List<String> = listOf(
        "Scan Pitch: Check space before touch",
        "First Touch: Cushion ball into stride",
        "Execute Action: Sharp pass, dribble, or strike",
        "Recover Shape: Reset athletic stance"
    )
)

data class FootballProgram(
    val id: String,
    val level: SkillLevel,
    val title: String,
    val badge: String,
    val description: String,
    val focusSummary: String,
    val moduleIds: List<String>
)

data class AppSettings(
    val isDarkTheme: Boolean = true,
    val soundEnabled: Boolean = true,
    val soundToneIndex: Int = 0, // 0 = Crisp Beep, 1 = Pitch Whistle, 2 = Soft Chime
    val vibrationEnabled: Boolean = true,
    val remindersEnabled: Boolean = false,
    val reminderTimeText: String = "07:00",
    val preferredDurationMinutes: Int = 30,
    val dailyMinutesGoal: Int = 30,
    val weeklySessionsGoal: Int = 5,
    val activeProgramId: String = "prog_beginner"
)

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val playerName: String = "",
    val age: Int = 18,
    val skillLevel: String = SkillLevel.BEGINNER.name,
    val playerPosition: String = PlayerPosition.STRIKER.name,
    val trainingGoals: String = "Sharpen ball mastery, first touch, and clinical finishing.",
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
    val completedDrillsCount: Int = 1,
    val drillsSummary: String = "",
    val resultSummary: String = "Completed",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_plan_items")
data class DailyPlanItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planDateKey: String,
    val sortOrder: Int,
    val sectionLabel: String, // "Warm-up", "Main Drill", "Rest Interval", "Cool-down"
    val exerciseId: String,
    val title: String,
    val categoryLabel: String,
    val durationSeconds: Int,
    val isRestSlot: Boolean = false,
    val isCompleted: Boolean = false,
    val focusNote: String = ""
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
    val category: String = "Ball Mastery",
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
