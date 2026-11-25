package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AchievementEntity
import com.example.data.model.CompletedSessionEntity
import com.example.data.model.PersonalRecordEntity
import com.example.data.model.PlannedWorkoutEntity
import com.example.data.model.PlayerPosition
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.ProgramModuleCompletionEntity
import com.example.data.model.SavedTrainingCheckpointEntity
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingGoalEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        PlayerProfileEntity::class,
        CompletedSessionEntity::class,
        ProgramModuleCompletionEntity::class,
        PlannedWorkoutEntity::class,
        PersonalRecordEntity::class,
        TrainingGoalEntity::class,
        AchievementEntity::class,
        SavedTrainingCheckpointEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class FootballDatabase : RoomDatabase() {
    abstract fun playerProfileDao(): PlayerProfileDao
    abstract fun completedSessionDao(): CompletedSessionDao
    abstract fun programModuleCompletionDao(): ProgramModuleCompletionDao
    abstract fun plannedWorkoutDao(): PlannedWorkoutDao
    abstract fun personalRecordDao(): PersonalRecordDao
    abstract fun trainingGoalDao(): TrainingGoalDao
    abstract fun achievementDao(): AchievementDao
    abstract fun savedCheckpointDao(): SavedCheckpointDao

    companion object {
        @Volatile
        private var INSTANCE: FootballDatabase? = null

        fun getDatabase(context: Context): FootballDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FootballDatabase::class.java,
                    "football_pro_offline_db"
                )
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
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.US)

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

    fun formatTimestampTime(timestamp: Long): String {
        return try {
            timeFormat.format(Date(timestamp))
        } catch (_: Exception) {
            ""
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

object DatabaseInitializer {
    fun defaultAchievements(): List<AchievementEntity> = listOf(
        AchievementEntity(
            id = "ach_first_kickoff",
            title = "First Kickoff",
            description = "Complete your first offline football training session.",
            progressTarget = 1
        ),
        AchievementEntity(
            id = "ach_hour_pitch",
            title = "Hour on the Pitch",
            description = "Log 60 total minutes of football training.",
            progressTarget = 60
        ),
        AchievementEntity(
            id = "ach_dribble_3",
            title = "Ball Mastery Pro",
            description = "Complete 3 Dribbling or Speed & Agility drills.",
            progressTarget = 3
        ),
        AchievementEntity(
            id = "ach_finisher_2",
            title = "Clinical Finisher",
            description = "Complete 2 Shooting or Set Piece drills.",
            progressTarget = 2
        ),
        AchievementEntity(
            id = "ach_streak_3",
            title = "Hat-Trick Streak",
            description = "Train 3 consecutive days without missing.",
            progressTarget = 3
        ),
        AchievementEntity(
            id = "ach_streak_7",
            title = "Matchweek Captain",
            description = "Build a 7-day consecutive training streak.",
            progressTarget = 7
        ),
        AchievementEntity(
            id = "ach_academy_grad",
            title = "Academy Graduate",
            description = "Complete all 6 modules in any skill program.",
            progressTarget = 6
        ),
        AchievementEntity(
            id = "ach_sessions_10",
            title = "Squad Regular",
            description = "Complete 10 total football sessions offline.",
            progressTarget = 10
        )
    )

    suspend fun populateIfEmpty(database: FootballDatabase) {
        withContext(Dispatchers.IO) {
            val profileDao = database.playerProfileDao()
            val achDao = database.achievementDao()

            if (achDao.getAllOnce().isEmpty()) {
                achDao.upsertAll(defaultAchievements())
            }

            if (profileDao.getProfileOnce() != null) return@withContext

            val today = DateUtils.todayKey()

            profileDao.upsertProfile(
                PlayerProfileEntity(
                    id = 1,
                    playerName = "",
                    skillLevel = SkillLevel.BEGINNER.name,
                    playerPosition = PlayerPosition.ALL_ROUNDER.name,
                    trainingGoals = "Sharpen first touch, tight cone dribbling, and clinical box finishing.",
                    preferredDurationMinutes = 30,
                    onboardingCompleted = false
                )
            )

            val recordDao = database.personalRecordDao()
            listOf(
                PersonalRecordEntity(title = "Keepy-Uppy Juggling Streak", valueText = "0", unit = "touches", updatedDateKey = today),
                PersonalRecordEntity(title = "Cone Slalom 10-Gate Time", valueText = "0.0", unit = "sec", updatedDateKey = today),
                PersonalRecordEntity(title = "40m Sprint Best", valueText = "0.0", unit = "sec", updatedDateKey = today)
            ).forEach { recordDao.insert(it) }

            val plannerDao = database.plannedWorkoutDao()
            listOf(
                PlannedWorkoutEntity(dayOfWeek = "Mon", title = "Ball Mastery & Cone Slalom", categoryLabel = "Dribbling & Mastery", exerciseIdsSerialized = "fit_warmup,cat_drib_slalom,cat_drib_sole", durationMinutes = 30, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Tue", title = "One-Touch Passing & First Touch", categoryLabel = "Passing & First Touch", exerciseIdsSerialized = "fit_warmup,cat_pass_wall,cat_pass_cushion", durationMinutes = 30, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Wed", title = "Box Finishing & Finesse Shots", categoryLabel = "Shooting & Finishing", exerciseIdsSerialized = "cat_shoot_laces,cat_shoot_finesse,cat_shoot_volley", durationMinutes = 35, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Thu", title = "Speed, Cone Cuts & Sprint Power", categoryLabel = "Speed & Agility", exerciseIdsSerialized = "fit_sprints,fit_ladder,fit_squats", durationMinutes = 30, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Fri", title = "1v1 Defending & Reaction Scanning", categoryLabel = "Defending & 1v1", exerciseIdsSerialized = "cat_def_jockey,cat_react_scan,cat_def_recovery", durationMinutes = 25, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Sat", title = "Match Stamina & 4-3-3 Tactics", categoryLabel = "Stamina & Endurance", exerciseIdsSerialized = "cat_end_shuttle,cat_tact_433,fit_cooldown", durationMinutes = 40, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Sun", title = "Active Mobility, Core & Set Pieces", categoryLabel = "Mobility & Core", exerciseIdsSerialized = "fit_stretch,fit_planks,cat_set_freekick", durationMinutes = 25, isCompleted = false)
            ).forEach { plannerDao.insert(it) }

            val goalDao = database.trainingGoalDao()
            listOf(
                TrainingGoalEntity(title = "Complete Beginner Program", category = "Program", targetValue = 6, currentValue = 0, unit = "modules", isCompleted = false, createdDateKey = today),
                TrainingGoalEntity(title = "500 Weak-Foot Wall Passes", category = "Passing", targetValue = 500, currentValue = 0, unit = "passes", isCompleted = false, createdDateKey = today),
                TrainingGoalEntity(title = "Weekly 5-Day Pitch Streak", category = "Consistency", targetValue = 5, currentValue = 0, unit = "days", isCompleted = false, createdDateKey = today)
            ).forEach { goalDao.insert(it) }
        }
    }
}
