package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SkillCategory(val displayName: String, val subtitle: String) {
    BALL_CONTROL("Ball Control", "First touch, mastery & juggling"),
    DRIBBLING("Dribbling", "Cones, speed & 1v1 feints"),
    PASSING("Passing", "Short, long & one-touch"),
    SHOOTING("Shooting", "Power, placement & volleys"),
    DEFENDING("Defending", "Tackling, marking & positioning"),
    GOALKEEPER("Goalkeeper", "Diving, catching & distribution")
}

enum class FitnessCategory(val displayName: String) {
    SPEED("Speed"),
    AGILITY("Agility"),
    STAMINA("Stamina"),
    ENDURANCE("Endurance"),
    STRENGTH("Strength"),
    JUMP("Jump Training"),
    WARMUP("Warm-up"),
    COOLDOWN("Cooldown & Stretch")
}

enum class PlayerPosition(val shortCode: String, val displayName: String, val roleSummary: String) {
    GOALKEEPER("GK", "Goalkeeper", "Shot-stopping, commanding the box & distribution"),
    CENTER_BACK("CB", "Center Back", "Aerial dominance, line organization & clean tackles"),
    FULL_BACK("FB", "Full Back", "1v1 defending, overlapping stamina & whipped crosses"),
    DEFENSIVE_MID("CDM", "Defensive Midfielder", "Interceptions, shielding the backline & tempo passes"),
    CENTRAL_MID("CM", "Central Midfielder", "360° scanning, box-to-box stamina & link-up play"),
    ATTACKING_MID("CAM", "Attacking Midfielder", "Half-space turns, through balls & edge-of-box shots"),
    WINGER("WG", "Winger", "Explosive 1v1 dribbling, cut-insides & final-third delivery"),
    STRIKER("ST", "Striker", "Box movement, clinical 1-touch finishing & hold-up play")
}

enum class DifficultyLevel(val displayName: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced")
}

enum class PreferredFoot(val displayName: String) {
    RIGHT("Right Foot"),
    LEFT("Left Foot"),
    BOTH("Both Feet")
}

enum class DrillDiagramType {
    CONES_SLALOM,
    TRIANGLE_PASSING,
    SHOOTING_ARCS,
    DEFENSIVE_CHANNEL,
    GOALKEEPER_ARCS,
    SPRINT_LANES,
    JUGGLE_CIRCLE
}

enum class LessonGroup {
    SKILL_MODULE,
    FITNESS,
    POSITION
}

data class TrainingLesson(
    val id: String,
    val title: String,
    val group: LessonGroup,
    val categoryKey: String, // SkillCategory.name or FitnessCategory.name or PlayerPosition.name
    val categoryLabel: String,
    val description: String,
    val instructions: List<String>,
    val difficulty: DifficultyLevel,
    val recommendedReps: String,
    val durationMinutes: Int,
    val equipment: String,
    val coachingTips: List<String>,
    val diagramType: DrillDiagramType
)

data class ProgramDayPlan(
    val dayNumber: Int,
    val title: String,
    val isRestDay: Boolean,
    val durationMinutes: Int,
    val focusArea: String,
    val exercises: List<String>
)

data class TrainingProgram(
    val id: String,
    val title: String,
    val totalDays: Int,
    val badge: String,
    val description: String,
    val weeklyStructureSummary: String,
    val days: List<ProgramDayPlan>
)

data class TacticMarker(
    val id: String,
    val label: String,
    val xFraction: Float,
    val yFraction: Float,
    val team: String // "HOME", "AWAY", "BALL"
)

data class TacticArrow(
    val id: String,
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float
)

data class AppSettings(
    val isDarkTheme: Boolean = true,
    val soundEnabled: Boolean = true,
    val reminderEnabled: Boolean = true,
    val reminderTimeLabel: String = "05:30 PM",
    val activeProgramId: String = "program_7"
)

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val playerName: String = "Striker One",
    val age: Int = 19,
    val heightCm: Int = 176,
    val weightKg: Float = 70.0f,
    val playingPosition: String = PlayerPosition.CENTRAL_MID.name,
    val preferredFoot: String = PreferredFoot.RIGHT.name,
    val experienceLevel: String = DifficultyLevel.INTERMEDIATE.name,
    val personalGoals: String = "Improve weak-foot finishing, first touch under pressure, and 90-minute match stamina.",
    val onboardingCompleted: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "completed_lessons")
data class CompletedLessonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lessonId: String,
    val dateKey: String,
    val categoryKey: String,
    val categoryLabel: String,
    val title: String,
    val durationMinutes: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "program_day_completions", primaryKeys = ["programId", "dayNumber"])
data class ProgramDayCompletionEntity(
    val programId: String,
    val dayNumber: Int,
    val isCompleted: Boolean,
    val completedDateKey: String = ""
)

@Entity(tableName = "personal_records")
data class PersonalRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val valueText: String,
    val unit: String,
    val updatedDateKey: String
)

@Entity(tableName = "saved_tactics")
data class SavedTacticEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val formation: String,
    val markersSerialized: String,
    val arrowsSerialized: String,
    val notes: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "planned_workouts")
data class PlannedWorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayOfWeek: String, // Mon..Sun
    val dateKey: String,
    val title: String,
    val categoryLabel: String,
    val durationMinutes: Int,
    val isCompleted: Boolean = false
)
