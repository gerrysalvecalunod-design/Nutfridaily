package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ActivityLogEntity
import com.example.data.model.MealLogEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WaterLogEntity
import com.example.data.model.WeightLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun observeProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: UserProfileEntity)
}

@Dao
interface MealLogDao {
    @Query("SELECT * FROM meal_logs ORDER BY timestamp DESC")
    fun getAllMeals(): Flow<List<MealLogEntity>>

    @Query("SELECT * FROM meal_logs WHERE dateKey = :dateKey ORDER BY timestamp DESC")
    fun getMealsForDate(dateKey: String): Flow<List<MealLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealLogEntity): Long

    @Delete
    suspend fun deleteMeal(meal: MealLogEntity)
}

@Dao
interface WaterLogDao {
    @Query("SELECT * FROM water_logs ORDER BY timestamp DESC")
    fun getAllWaterLogs(): Flow<List<WaterLogEntity>>

    @Query("SELECT * FROM water_logs WHERE dateKey = :dateKey ORDER BY timestamp DESC")
    fun getWaterLogsForDate(dateKey: String): Flow<List<WaterLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaterLog(log: WaterLogEntity): Long

    @Delete
    suspend fun deleteWaterLog(log: WaterLogEntity)
}

@Dao
interface WeightLogDao {
    @Query("SELECT * FROM weight_logs ORDER BY dateKey DESC, timestamp DESC")
    fun getAllWeightLogs(): Flow<List<WeightLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightLog(log: WeightLogEntity): Long

    @Delete
    suspend fun deleteWeightLog(log: WeightLogEntity)
}

@Dao
interface ActivityLogDao {
    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC")
    fun getAllActivities(): Flow<List<ActivityLogEntity>>

    @Query("SELECT * FROM activity_logs WHERE dateKey = :dateKey ORDER BY timestamp DESC")
    fun getActivitiesForDate(dateKey: String): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(log: ActivityLogEntity): Long

    @Delete
    suspend fun deleteActivity(log: ActivityLogEntity)
}

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders ORDER BY id ASC")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)
}
