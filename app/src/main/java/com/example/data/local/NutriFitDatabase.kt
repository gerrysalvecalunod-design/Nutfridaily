package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ActivityLogEntity
import com.example.data.model.MealLogEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WaterLogEntity
import com.example.data.model.WeightLogEntity

@Database(
    entities = [
        UserProfileEntity::class,
        MealLogEntity::class,
        WaterLogEntity::class,
        WeightLogEntity::class,
        ActivityLogEntity::class,
        ReminderEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NutriFitDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun mealLogDao(): MealLogDao
    abstract fun waterLogDao(): WaterLogDao
    abstract fun weightLogDao(): WeightLogDao
    abstract fun activityLogDao(): ActivityLogDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: NutriFitDatabase? = null

        fun getDatabase(context: Context): NutriFitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NutriFitDatabase::class.java,
                    "nutrifit_daily_offline_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
