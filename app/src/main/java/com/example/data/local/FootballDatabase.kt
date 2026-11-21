package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CompletedLessonEntity
import com.example.data.model.DifficultyLevel
import com.example.data.model.PersonalRecordEntity
import com.example.data.model.PlannedWorkoutEntity
import com.example.data.model.PlayerPosition
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.PreferredFoot
import com.example.data.model.ProgramDayCompletionEntity
import com.example.data.model.SavedTacticEntity
import com.example.data.model.SkillCategory
import com.example.data.model.TacticArrow
import com.example.data.model.TacticMarker
import com.example.data.model.TrainingGoalEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Database(
    entities = [
        PlayerProfileEntity::class,
        CompletedLessonEntity::class,
        ProgramDayCompletionEntity::class,
        PersonalRecordEntity::class,
        SavedTacticEntity::class,
        PlannedWorkoutEntity::class,
        TrainingGoalEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class FootballDatabase : RoomDatabase() {
    abstract fun playerProfileDao(): PlayerProfileDao
    abstract fun completedLessonDao(): CompletedLessonDao
    abstract fun programCompletionDao(): ProgramCompletionDao
    abstract fun personalRecordDao(): PersonalRecordDao
    abstract fun savedTacticDao(): SavedTacticDao
    abstract fun plannedWorkoutDao(): PlannedWorkoutDao
    abstract fun trainingGoalDao(): TrainingGoalDao

    companion object {
        @Volatile
        private var INSTANCE: FootballDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `training_goals` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `title` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `targetValue` INTEGER NOT NULL,
                        `currentValue` INTEGER NOT NULL,
                        `unit` TEXT NOT NULL,
                        `isCompleted` INTEGER NOT NULL,
                        `createdDateKey` TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): FootballDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FootballDatabase::class.java,
                    "footballpro_academy_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

object DateUtils {
    private val keyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayFormat = SimpleDateFormat("EEE, MMM d", Locale.US)
    private val shortDayFormat = SimpleDateFormat("EEE", Locale.US)

    fun todayKey(): String = keyFormat.format(Calendar.getInstance().time)

    fun daysAgoKey(daysAgo: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return keyFormat.format(cal.time)
    }

    fun formatDisplayDate(dateKey: String): String {
        return try {
            val date = keyFormat.parse(dateKey) ?: return dateKey
            when (dateKey) {
                todayKey() -> "Today • ${displayFormat.format(date)}"
                daysAgoKey(1) -> "Yesterday • ${displayFormat.format(date)}"
                else -> displayFormat.format(date)
            }
        } catch (_: Exception) {
            dateKey
        }
    }

    fun shortDayLabel(dateKey: String): String {
        return try {
            val date = keyFormat.parse(dateKey) ?: return dateKey.takeLast(2)
            shortDayFormat.format(date)
        } catch (_: Exception) {
            dateKey.takeLast(2)
        }
    }

    fun lastNDaysKeys(days: Int): List<String> = (days - 1 downTo 0).map { daysAgoKey(it) }
}

object TacticsSerializer {
    fun serializeMarkers(markers: List<TacticMarker>): String {
        return markers.joinToString(";") { m ->
            "${m.id}|${m.label.replace("|", "")}|${m.xFraction}|${m.yFraction}|${m.team}"
        }
    }

    fun deserializeMarkers(raw: String): List<TacticMarker> {
        if (raw.isBlank()) return emptyList()
        return raw.split(";").mapNotNull { token ->
            val p = token.split("|")
            if (p.size >= 5) {
                TacticMarker(
                    id = p[0],
                    label = p[1],
                    xFraction = p[2].toFloatOrNull() ?: 0.5f,
                    yFraction = p[3].toFloatOrNull() ?: 0.5f,
                    team = p[4]
                )
            } else null
        }
    }

    fun serializeArrows(arrows: List<TacticArrow>): String {
        return arrows.joinToString(";") { a ->
            "${a.id}|${a.startX}|${a.startY}|${a.endX}|${a.endY}"
        }
    }

    fun deserializeArrows(raw: String): List<TacticArrow> {
        if (raw.isBlank()) return emptyList()
        return raw.split(";").mapNotNull { token ->
            val p = token.split("|")
            if (p.size >= 5) {
                TacticArrow(
                    id = p[0],
                    startX = p[1].toFloatOrNull() ?: 0.5f,
                    startY = p[2].toFloatOrNull() ?: 0.5f,
                    endX = p[3].toFloatOrNull() ?: 0.5f,
                    endY = p[4].toFloatOrNull() ?: 0.4f
                )
            } else null
        }
    }

    fun defaultFormationMarkers(formation: String): List<TacticMarker> {
        val base = when (formation) {
            "4-4-2" -> listOf(
                TacticMarker("gk", "GK", 0.50f, 0.90f, "HOME"),
                TacticMarker("lb", "LB", 0.16f, 0.72f, "HOME"),
                TacticMarker("lcb", "CB", 0.38f, 0.75f, "HOME"),
                TacticMarker("rcb", "CB", 0.62f, 0.75f, "HOME"),
                TacticMarker("rb", "RB", 0.84f, 0.72f, "HOME"),
                TacticMarker("lm", "LM", 0.16f, 0.48f, "HOME"),
                TacticMarker("lcm", "CM", 0.39f, 0.52f, "HOME"),
                TacticMarker("rcm", "CM", 0.61f, 0.52f, "HOME"),
                TacticMarker("rm", "RM", 0.84f, 0.48f, "HOME"),
                TacticMarker("lst", "ST", 0.38f, 0.25f, "HOME"),
                TacticMarker("rst", "ST", 0.62f, 0.25f, "HOME")
            )
            "3-5-2" -> listOf(
                TacticMarker("gk", "GK", 0.50f, 0.90f, "HOME"),
                TacticMarker("lcb", "CB", 0.27f, 0.75f, "HOME"),
                TacticMarker("ccb", "CB", 0.50f, 0.77f, "HOME"),
                TacticMarker("rcb", "CB", 0.73f, 0.75f, "HOME"),
                TacticMarker("lwb", "LWB", 0.14f, 0.50f, "HOME"),
                TacticMarker("cdm", "CDM", 0.50f, 0.59f, "HOME"),
                TacticMarker("lcm", "CM", 0.33f, 0.48f, "HOME"),
                TacticMarker("rcm", "CM", 0.67f, 0.48f, "HOME"),
                TacticMarker("rwb", "RWB", 0.86f, 0.50f, "HOME"),
                TacticMarker("lst", "ST", 0.39f, 0.24f, "HOME"),
                TacticMarker("rst", "ST", 0.61f, 0.24f, "HOME")
            )
            "4-2-3-1" -> listOf(
                TacticMarker("gk", "GK", 0.50f, 0.90f, "HOME"),
                TacticMarker("lb", "LB", 0.16f, 0.73f, "HOME"),
                TacticMarker("lcb", "CB", 0.38f, 0.76f, "HOME"),
                TacticMarker("rcb", "CB", 0.62f, 0.76f, "HOME"),
                TacticMarker("rb", "RB", 0.84f, 0.73f, "HOME"),
                TacticMarker("ldm", "CDM", 0.38f, 0.58f, "HOME"),
                TacticMarker("rdm", "CDM", 0.62f, 0.58f, "HOME"),
                TacticMarker("lam", "LW", 0.20f, 0.38f, "HOME"),
                TacticMarker("cam", "CAM", 0.50f, 0.40f, "HOME"),
                TacticMarker("ram", "RW", 0.80f, 0.38f, "HOME"),
                TacticMarker("st", "ST", 0.50f, 0.20f, "HOME")
            )
            else -> listOf( // 4-3-3
                TacticMarker("gk", "GK", 0.50f, 0.90f, "HOME"),
                TacticMarker("lb", "LB", 0.16f, 0.72f, "HOME"),
                TacticMarker("lcb", "CB", 0.38f, 0.75f, "HOME"),
                TacticMarker("rcb", "CB", 0.62f, 0.75f, "HOME"),
                TacticMarker("rb", "RB", 0.84f, 0.72f, "HOME"),
                TacticMarker("cdm", "CDM", 0.50f, 0.58f, "HOME"),
                TacticMarker("lcm", "CM", 0.30f, 0.47f, "HOME"),
                TacticMarker("rcm", "CM", 0.70f, 0.47f, "HOME"),
                TacticMarker("lw", "LW", 0.20f, 0.24f, "HOME"),
                TacticMarker("st", "ST", 0.50f, 0.20f, "HOME"),
                TacticMarker("rw", "RW", 0.80f, 0.24f, "HOME")
            )
        }
        return base + TacticMarker("ball", "BALL", 0.50f, 0.50f, "BALL")
    }
}

object DatabaseInitializer {
    suspend fun populateIfEmpty(database: FootballDatabase) {
        withContext(Dispatchers.IO) {
            val profileDao = database.playerProfileDao()
            if (profileDao.getProfileOnce() != null) return@withContext

            val today = DateUtils.todayKey()
            val d1 = DateUtils.daysAgoKey(1)
            val d2 = DateUtils.daysAgoKey(2)
            val d4 = DateUtils.daysAgoKey(4)

            profileDao.upsertProfile(
                PlayerProfileEntity(
                    id = 1,
                    playerName = "Jordan Silva",
                    age = 19,
                    heightCm = 176,
                    weightKg = 70.0f,
                    playingPosition = PlayerPosition.CENTRAL_MID.name,
                    preferredFoot = PreferredFoot.RIGHT.name,
                    experienceLevel = DifficultyLevel.INTERMEDIATE.name,
                    personalGoals = "Sharpen first touch under pressure, build 90-min stamina, and master weak-foot finishing.",
                    onboardingCompleted = false
                )
            )

            // Seed starter completed sessions for streak & charts
            val completedDao = database.completedLessonDao()
            listOf(
                CompletedLessonEntity(lessonId = "bc_first_touch", dateKey = d4, categoryKey = SkillCategory.BALL_CONTROL.name, categoryLabel = "Ball Control", title = "First Touch Cushion", durationMinutes = 15, timestamp = System.currentTimeMillis() - 4 * 86_400_000L),
                CompletedLessonEntity(lessonId = "dr_cone_slalom", dateKey = d2, categoryKey = SkillCategory.DRIBBLING.name, categoryLabel = "Dribbling", title = "Cone Dribbling Slalom", durationMinutes = 15, timestamp = System.currentTimeMillis() - 2 * 86_400_000L),
                CompletedLessonEntity(lessonId = "pa_short_pass", dateKey = d1, categoryKey = SkillCategory.PASSING.name, categoryLabel = "Passing", title = "Crisp Short Passing", durationMinutes = 15, timestamp = System.currentTimeMillis() - 86_400_000L),
                CompletedLessonEntity(lessonId = "fit_speed_beg", dateKey = d1, categoryKey = "FITNESS_SPEED", categoryLabel = "Speed", title = "10m–20m Acceleration Starts", durationMinutes = 20, timestamp = System.currentTimeMillis() - 82_000_000L),
                CompletedLessonEntity(lessonId = "sh_power_drive", dateKey = today, categoryKey = SkillCategory.SHOOTING.name, categoryLabel = "Shooting", title = "Laces Power Shooting", durationMinutes = 20, timestamp = System.currentTimeMillis() - 3_600_000L)
            ).forEach { completedDao.insertCompleted(it) }

            // Seed starter Day 1 completed in 7-day challenge
            database.programCompletionDao().upsertDayCompletion(
                ProgramDayCompletionEntity(
                    programId = "program_7",
                    dayNumber = 1,
                    isCompleted = true,
                    completedDateKey = d1
                )
            )

            // Seed Personal Records
            val recordDao = database.personalRecordDao()
            listOf(
                PersonalRecordEntity(title = "Max Juggle Streak", valueText = "64", unit = "touches", updatedDateKey = today),
                PersonalRecordEntity(title = "30m Sprint PB", valueText = "4.28", unit = "sec", updatedDateKey = d2),
                PersonalRecordEntity(title = "Wall Passes (1 min)", valueText = "48", unit = "passes", updatedDateKey = d1),
                PersonalRecordEntity(title = "Cooper 12-Min Run", valueText = "2,850", unit = "meters", updatedDateKey = d4)
            ).forEach { recordDao.insert(it) }

            // Seed Default Saved Tactic (4-3-3 High Press)
            val starterMarkers = TacticsSerializer.defaultFormationMarkers("4-3-3") + listOf(
                TacticMarker("opp1", "9", 0.50f, 0.34f, "AWAY"),
                TacticMarker("opp2", "4", 0.36f, 0.16f, "AWAY"),
                TacticMarker("opp3", "5", 0.64f, 0.16f, "AWAY")
            )
            val starterArrows = listOf(
                TacticArrow("a1", 0.20f, 0.24f, 0.38f, 0.14f),
                TacticArrow("a2", 0.70f, 0.47f, 0.78f, 0.28f)
            )
            database.savedTacticDao().insert(
                SavedTacticEntity(
                    name = "4-3-3 Wing Overload",
                    formation = "4-3-3",
                    markersSerialized = TacticsSerializer.serializeMarkers(starterMarkers),
                    arrowsSerialized = TacticsSerializer.serializeArrows(starterArrows),
                    notes = "Wingers tuck inside while #8 pushes into half-space."
                )
            )

            // Seed Weekly Workout Planner entries
            val plannerDao = database.plannedWorkoutDao()
            listOf(
                PlannedWorkoutEntity(dayOfWeek = "Mon", dateKey = today, title = "Ball Control & First Touch", categoryLabel = "Ball Control", durationMinutes = 25, isCompleted = true),
                PlannedWorkoutEntity(dayOfWeek = "Tue", dateKey = today, title = "Speed & Cone Agility", categoryLabel = "Fitness", durationMinutes = 30, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Wed", dateKey = today, title = "Wall & One-Touch Passing", categoryLabel = "Passing", durationMinutes = 25, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Thu", dateKey = today, title = "Power & Placement Shooting", categoryLabel = "Shooting", durationMinutes = 30, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Fri", dateKey = today, title = "1v1 Defending & Recovery", categoryLabel = "Defending", durationMinutes = 25, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Sat", dateKey = today, title = "Match Stamina HIIT", categoryLabel = "Stamina", durationMinutes = 35, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Sun", dateKey = today, title = "Mobility Cooldown & Stretch", categoryLabel = "Cooldown", durationMinutes = 15, isCompleted = false)
            ).forEach { plannerDao.insert(it) }

            // Seed Starter Training Goals
            val goalDao = database.trainingGoalDao()
            listOf(
                TrainingGoalEntity(title = "Complete 5 Ball Control Drills", category = "Skill", targetValue = 5, currentValue = 1, unit = "drills", isCompleted = false, createdDateKey = today),
                TrainingGoalEntity(title = "7-Day Speed & Stamina Streak", category = "Fitness", targetValue = 7, currentValue = 3, unit = "days", isCompleted = false, createdDateKey = today),
                TrainingGoalEntity(title = "100 Weak-Foot Finishing Shots", category = "Match Prep", targetValue = 100, currentValue = 40, unit = "shots", isCompleted = false, createdDateKey = today)
            ).forEach { goalDao.insert(it) }
        }
    }
}
