package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AchievementEntity
import com.example.data.model.CompletedSessionEntity
import com.example.data.model.PersonalRecordEntity
import com.example.data.model.PlannedWorkoutEntity
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
abstract class BadmintonDatabase : RoomDatabase() {
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
        private var INSTANCE: BadmintonDatabase? = null

        fun getDatabase(context: Context): BadmintonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BadmintonDatabase::class.java,
                    "badminton_pro_offline_db"
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
            id = "ach_first_rally",
            title = "First Rally",
            description = "Complete your first offline training session.",
            progressTarget = 1
        ),
        AchievementEntity(
            id = "ach_hour_court",
            title = "Hour on Court",
            description = "Log 60 total minutes of badminton training.",
            progressTarget = 60
        ),
        AchievementEntity(
            id = "ach_footwork_3",
            title = "Footwork Specialist",
            description = "Complete 3 Footwork or Speed drills.",
            progressTarget = 3
        ),
        AchievementEntity(
            id = "ach_smash_2",
            title = "Smash Powerhouse",
            description = "Complete 2 Smash or Attack drills.",
            progressTarget = 2
        ),
        AchievementEntity(
            id = "ach_streak_3",
            title = "3-Day Streak",
            description = "Train 3 consecutive days without missing.",
            progressTarget = 3
        ),
        AchievementEntity(
            id = "ach_streak_7",
            title = "Weekly Warrior",
            description = "Build a 7-day consecutive training streak.",
            progressTarget = 7
        ),
        AchievementEntity(
            id = "ach_program_grad",
            title = "Program Graduate",
            description = "Complete all 6 modules in any skill program.",
            progressTarget = 6
        ),
        AchievementEntity(
            id = "ach_sessions_10",
            title = "Consistency Pro",
            description = "Complete 10 total training sessions.",
            progressTarget = 10
        )
    )

    suspend fun populateIfEmpty(database: BadmintonDatabase) {
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
                    trainingGoals = "Master 6-corner footwork, clean backhand serves, and explosive overhead smashes.",
                    preferredDurationMinutes = 30,
                    onboardingCompleted = false
                )
            )

            val recordDao = database.personalRecordDao()
            listOf(
                PersonalRecordEntity(title = "6-Corner 20 Shuttle Run", valueText = "0.0", unit = "sec", updatedDateKey = today),
                PersonalRecordEntity(title = "Consecutive Wall Volleys", valueText = "0", unit = "hits", updatedDateKey = today),
                PersonalRecordEntity(title = "Short Serve Target Accuracy", valueText = "0/20", unit = "hits", updatedDateKey = today)
            ).forEach { recordDao.insert(it) }

            val plannerDao = database.plannedWorkoutDao()
            listOf(
                PlannedWorkoutEntity(dayOfWeek = "Mon", title = "6-Corner Footwork & Split-Step", categoryLabel = "Footwork Training", exerciseIdsSerialized = "fit_warmup,cat_foot_6corner,cat_foot_split", durationMinutes = 30, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Tue", title = "Smash Power & Overhead Mechanics", categoryLabel = "Smash Training", exerciseIdsSerialized = "fit_warmup,cat_smash_full,cat_smash_stick", durationMinutes = 30, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Wed", title = "Precision Serving & Net Hairpins", categoryLabel = "Accuracy Training", exerciseIdsSerialized = "cat_serve_low,cat_acc_net,cat_acc_drop", durationMinutes = 25, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Thu", title = "Speed, Agility & Jump Conditioning", categoryLabel = "Speed & Agility", exerciseIdsSerialized = "fit_jumprope,fit_agility,fit_lunges", durationMinutes = 30, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Fri", title = "Fast Defense & Reaction Drives", categoryLabel = "Defensive Training", exerciseIdsSerialized = "cat_def_block,cat_react_wall,cat_def_lift", durationMinutes = 25, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Sat", title = "Multi-Shuttle Endurance & Match Tactics", categoryLabel = "Endurance Training", exerciseIdsSerialized = "cat_end_multi,cat_strat_singles,fit_cooldown", durationMinutes = 40, isCompleted = false),
                PlannedWorkoutEntity(dayOfWeek = "Sun", title = "Active Recovery, Planks & Stretching", categoryLabel = "Fitness & Conditioning", exerciseIdsSerialized = "fit_stretch,fit_planks,fit_cooldown", durationMinutes = 20, isCompleted = false)
            ).forEach { plannerDao.insert(it) }

            val goalDao = database.trainingGoalDao()
            listOf(
                TrainingGoalEntity(title = "Complete Beginner Program", category = "Program", targetValue = 6, currentValue = 0, unit = "modules", isCompleted = false, createdDateKey = today),
                TrainingGoalEntity(title = "500 Shadow Footwork Steps", category = "Footwork", targetValue = 500, currentValue = 0, unit = "steps", isCompleted = false, createdDateKey = today),
                TrainingGoalEntity(title = "Weekly 5-Day Court Streak", category = "Consistency", targetValue = 5, currentValue = 0, unit = "days", isCompleted = false, createdDateKey = today)
            ).forEach { goalDao.insert(it) }
        }
    }
}
