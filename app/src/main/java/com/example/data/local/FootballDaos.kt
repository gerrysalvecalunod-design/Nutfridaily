package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CompletedLessonEntity
import com.example.data.model.PersonalRecordEntity
import com.example.data.model.PlannedWorkoutEntity
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.ProgramDayCompletionEntity
import com.example.data.model.SavedTacticEntity
import com.example.data.model.TrainingGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerProfileDao {
    @Query("SELECT * FROM player_profile WHERE id = 1 LIMIT 1")
    fun observeProfile(): Flow<PlayerProfileEntity?>

    @Query("SELECT * FROM player_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfileOnce(): PlayerProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: PlayerProfileEntity)
}

@Dao
interface CompletedLessonDao {
    @Query("SELECT * FROM completed_lessons ORDER BY timestamp DESC")
    fun observeAllCompleted(): Flow<List<CompletedLessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompleted(item: CompletedLessonEntity)

    @Query("DELETE FROM completed_lessons WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM completed_lessons WHERE lessonId = :lessonId")
    suspend fun deleteByLessonId(lessonId: String)
}

@Dao
interface ProgramCompletionDao {
    @Query("SELECT * FROM program_day_completions")
    fun observeAll(): Flow<List<ProgramDayCompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDayCompletion(entry: ProgramDayCompletionEntity)

    @Query("DELETE FROM program_day_completions WHERE programId = :programId")
    suspend fun resetProgram(programId: String)
}

@Dao
interface PersonalRecordDao {
    @Query("SELECT * FROM personal_records ORDER BY id ASC")
    fun observeAll(): Flow<List<PersonalRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: PersonalRecordEntity)

    @Update
    suspend fun update(record: PersonalRecordEntity)

    @Delete
    suspend fun delete(record: PersonalRecordEntity)
}

@Dao
interface SavedTacticDao {
    @Query("SELECT * FROM saved_tactics ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<SavedTacticEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tactic: SavedTacticEntity)

    @Delete
    suspend fun delete(tactic: SavedTacticEntity)
}

@Dao
interface PlannedWorkoutDao {
    @Query("SELECT * FROM planned_workouts ORDER BY id ASC")
    fun observeAll(): Flow<List<PlannedWorkoutEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: PlannedWorkoutEntity)

    @Update
    suspend fun update(workout: PlannedWorkoutEntity)

    @Delete
    suspend fun delete(workout: PlannedWorkoutEntity)
}

@Dao
interface TrainingGoalDao {
    @Query("SELECT * FROM training_goals ORDER BY isCompleted ASC, id DESC")
    fun observeAll(): Flow<List<TrainingGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: TrainingGoalEntity)

    @Update
    suspend fun update(goal: TrainingGoalEntity)

    @Delete
    suspend fun delete(goal: TrainingGoalEntity)
}
