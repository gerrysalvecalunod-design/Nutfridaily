package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AchievementEntity
import com.example.data.model.CompletedSessionEntity
import com.example.data.model.PersonalRecordEntity
import com.example.data.model.PlannedWorkoutEntity
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.ProgramModuleCompletionEntity
import com.example.data.model.SavedTrainingCheckpointEntity
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
interface CompletedSessionDao {
    @Query("SELECT * FROM completed_sessions ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<CompletedSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CompletedSessionEntity)

    @Query("DELETE FROM completed_sessions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM completed_sessions WHERE exerciseId = :exerciseId")
    suspend fun deleteByExerciseId(exerciseId: String)
}

@Dao
interface ProgramModuleCompletionDao {
    @Query("SELECT * FROM program_module_completions")
    fun observeAll(): Flow<List<ProgramModuleCompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: ProgramModuleCompletionEntity)

    @Query("DELETE FROM program_module_completions WHERE programId = :programId")
    suspend fun resetProgram(programId: String)
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

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements ORDER BY unlocked DESC, id ASC")
    fun observeAll(): Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievements")
    suspend fun getAllOnce(): List<AchievementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<AchievementEntity>)
}

@Dao
interface SavedCheckpointDao {
    @Query("SELECT * FROM saved_training_checkpoint WHERE id = 1 LIMIT 1")
    fun observeCheckpoint(): Flow<SavedTrainingCheckpointEntity?>

    @Query("SELECT * FROM saved_training_checkpoint WHERE id = 1 LIMIT 1")
    suspend fun getCheckpointOnce(): SavedTrainingCheckpointEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCheckpoint(checkpoint: SavedTrainingCheckpointEntity)

    @Query("DELETE FROM saved_training_checkpoint WHERE id = 1")
    suspend fun clearCheckpoint()
}
