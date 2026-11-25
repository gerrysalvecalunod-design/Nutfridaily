package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AchievementEntity
import com.example.data.model.CompletedSessionEntity
import com.example.data.model.DailyPlanItemEntity
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
        DailyPlanItemEntity::class,
        ProgramModuleCompletionEntity::class,
        PlannedWorkoutEntity::class,
        PersonalRecordEntity::class,
        TrainingGoalEntity::class,
        AchievementEntity::class,
        SavedTrainingCheckpointEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class FootballDatabase : RoomDatabase() {
    abstract fun playerProfileDao(): PlayerProfileDao
    abstract fun completedSessionDao(): CompletedSessionDao
    abstract fun dailyPlanDao(): DailyPlanDao
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
            id = "ach_first_training",
            title = "First Training",
            description = "Complete your very first football training session offline.",
            progressTarget = 1
        ),
        AchievementEntity(
            id = "ach_sessions_5",
            title = "5 Training Sessions",
            description = "Complete 5 total football training sessions.",
            progressTarget = 5
        ),
        AchievementEntity(
            id = "ach_sessions_10",
            title = "10 Training Sessions",
            description = "Complete 10 total football training sessions.",
            progressTarget = 10
        ),
        AchievementEntity(
            id = "ach_drills_50",
            title = "50 Drills Completed",
            description = "Complete 50 individual football drills or plan steps.",
            progressTarget = 50
        ),
        AchievementEntity(
            id = "ach_hours_5",
            title = "5-Hour Training Milestone",
            description = "Accumulate 300 minutes (5 hours) of total pitch training.",
            progressTarget = 300
        ),
        AchievementEntity(
            id = "ach_streak_7",
            title = "7-Day Training Streak",
            description = "Train 7 consecutive days without missing a session.",
            progressTarget = 7
        ),
        AchievementEntity(
            id = "ach_ball_master",
            title = "Ball Master",
            description = "Complete 5 Ball Mastery, Dribbling, or Passing drills.",
            progressTarget = 5
        ),
        AchievementEntity(
            id = "ach_speed_master",
            title = "Speed Master",
            description = "Complete 5 Speed & Agility or Conditioning drills.",
            progressTarget = 5
        ),
        AchievementEntity(
            id = "ach_shooting_master",
            title = "Shooting Master",
            description = "Complete 5 Shooting & Finishing drills.",
            progressTarget = 5
        )
    )

    suspend fun populateIfEmpty(database: FootballDatabase) {
        withContext(Dispatchers.IO) {
            val profileDao = database.playerProfileDao()
            val achDao = database.achievementDao()

            val existingAch = achDao.getAllOnce()
            val targetIds = defaultAchievements().map { it.id }.toSet()
            if (existingAch.isEmpty() || existingAch.map { it.id }.toSet() != targetIds) {
                achDao.upsertAll(defaultAchievements())
            }

            if (profileDao.getProfileOnce() != null) return@withContext

            val today = DateUtils.todayKey()

            profileDao.upsertProfile(
                PlayerProfileEntity(
                    id = 1,
                    playerName = "",
                    age = 18,
                    skillLevel = SkillLevel.BEGINNER.name,
                    playerPosition = PlayerPosition.STRIKER.name,
                    trainingGoals = "Sharpen ball mastery, first touch, and clinical finishing.",
                    preferredDurationMinutes = 30,
                    onboardingCompleted = false
                )
            )

            val recordDao = database.personalRecordDao()
            listOf(
                PersonalRecordEntity(title = "Keepy-Uppy Juggling Streak", valueText = "0", unit = "touches", updatedDateKey = today),
                PersonalRecordEntity(title = "10m Sprint Best", valueText = "0.0", unit = "sec", updatedDateKey = today),
                PersonalRecordEntity(title = "20m Sprint Best", valueText = "0.0", unit = "sec", updatedDateKey = today)
            ).forEach { recordDao.insert(it) }

            val plannerDao = database.plannedWorkoutDao()
            listOf(
                PlannedWorkoutEntity(dayOfWeek = "Mon", title = "Ball Mastery & Cone Dribbling", categoryLabel = "Ball Mastery", exerciseIdsSerialized = "fit_warmup,bm_toe_taps,drib_cone", durationMinutes = 30, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Tue", title = "One-Touch & Wall Passing", categoryLabel = "Passing", exerciseIdsSerialized = "fit_warmup,pass_wall,pass_one_touch", durationMinutes = 30, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Wed", title = "Power & Finesse Shooting", categoryLabel = "Shooting", exerciseIdsSerialized = "shoot_power,shoot_finesse,shoot_accuracy", durationMinutes = 35, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Thu", title = "10m Sprints & Agility Drills", categoryLabel = "Speed & Agility", exerciseIdsSerialized = "spd_10m,spd_agility,cond_squats", durationMinutes = 30, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Fri", title = "1v1 Defending & Interceptions", categoryLabel = "Defending", exerciseIdsSerialized = "def_footwork,def_1v1,def_interception", durationMinutes = 25, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Sat", title = "Endurance, Stamina & Core", categoryLabel = "Physical Conditioning", exerciseIdsSerialized = "cond_endurance,cond_stamina,cond_plank", durationMinutes = 40, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Sun", title = "Goalkeeper Handling & Mobility", categoryLabel = "Goalkeeper Training", exerciseIdsSerialized = "gk_reaction,gk_catching,fit_cooldown", durationMinutes = 25, isCompleted = false)
            ).forEach { plannerDao.insert(it) }

            val goalDao = database.trainingGoalDao()
            listOf(
                TrainingGoalEntity(title = "Complete Beginner Program", category = "Program", targetValue = 6, currentValue = 0, unit = "modules", isCompleted = false, createdDateKey = today),
                TrainingGoalEntity(title = "500 Weak-Foot Wall Passes", category = "Passing", targetValue = 500, currentValue = 0, unit = "passes", isCompleted = false, createdDateKey = today),
                TrainingGoalEntity(title = "7-Day Training Streak", category = "Consistency", targetValue = 7, currentValue = 0, unit = "days", isCompleted = false, createdDateKey = today)
            ).forEach { goalDao.insert(it) }
        }
    }
}
