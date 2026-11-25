package com.example.data.repository

import com.example.data.local.BadmintonDatabase
import com.example.data.local.DatabaseInitializer
import com.example.data.local.SettingsDataStore
import com.example.data.model.AchievementEntity
import com.example.data.model.AppSettings
import com.example.data.model.BadmintonExercise
import com.example.data.model.BadmintonProgram
import com.example.data.model.CompletedSessionEntity
import com.example.data.model.PersonalRecordEntity
import com.example.data.model.PlannedWorkoutEntity
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.ProgramModuleCompletionEntity
import com.example.data.model.SavedTrainingCheckpointEntity
import com.example.data.model.SkillLevel
import com.example.data.model.TrainingGoalEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BadmintonRepository(
    private val database: BadmintonDatabase,
    private val settingsStore: SettingsDataStore
) {
    private val profileDao = database.playerProfileDao()
    private val completedDao = database.completedSessionDao()
    private val programDao = database.programModuleCompletionDao()
    private val plannerDao = database.plannedWorkoutDao()
    private val recordDao = database.personalRecordDao()
    private val goalDao = database.trainingGoalDao()
    private val achievementDao = database.achievementDao()
    private val checkpointDao = database.savedCheckpointDao()

    val playerProfile: Flow<PlayerProfileEntity?> = profileDao.observeProfile()
    val completedSessions: Flow<List<CompletedSessionEntity>> = completedDao.observeAll()
    val programCompletions: Flow<List<ProgramModuleCompletionEntity>> = programDao.observeAll()
    val plannedWorkouts: Flow<List<PlannedWorkoutEntity>> = plannerDao.observeAll()
    val personalRecords: Flow<List<PersonalRecordEntity>> = recordDao.observeAll()
    val trainingGoals: Flow<List<TrainingGoalEntity>> = goalDao.observeAll()
    val achievements: Flow<List<AchievementEntity>> = achievementDao.observeAll()
    val savedCheckpoint: Flow<SavedTrainingCheckpointEntity?> = checkpointDao.observeCheckpoint()
    val settingsFlow: Flow<AppSettings> = settingsStore.settingsFlow

    val programs: List<BadmintonProgram> = ProgramsAndRulesCurriculum.programs
    val programExercises: List<BadmintonExercise> = ProgramsAndRulesCurriculum.programExercises
    val rulesLessons: List<BadmintonExercise> = ProgramsAndRulesCurriculum.rulesLessons
    val categoryExercises: List<BadmintonExercise> = CategoriesAndFitnessCurriculum.categoryExercises
    val fitnessExercises: List<BadmintonExercise> = CategoriesAndFitnessCurriculum.fitnessExercises

    val allExercises: List<BadmintonExercise> =
        programExercises + categoryExercises + fitnessExercises + rulesLessons

    fun findExerciseById(id: String): BadmintonExercise? = allExercises.find { it.id == id }

    suspend fun getProfileOnce(): PlayerProfileEntity? = profileDao.getProfileOnce()

    suspend fun upsertProfile(profile: PlayerProfileEntity) = profileDao.upsertProfile(profile)

    suspend fun insertCompletedSession(item: CompletedSessionEntity) = completedDao.insert(item)

    suspend fun deleteCompletedSession(id: Long) = completedDao.deleteById(id)

    suspend fun deleteCompletedByExerciseId(exerciseId: String) = completedDao.deleteByExerciseId(exerciseId)

    suspend fun upsertProgramModuleCompletion(entry: ProgramModuleCompletionEntity) = programDao.upsert(entry)

    suspend fun resetProgram(programId: String) = programDao.resetProgram(programId)

    suspend fun insertPlannedWorkout(workout: PlannedWorkoutEntity) = plannerDao.insert(workout)

    suspend fun updatePlannedWorkout(workout: PlannedWorkoutEntity) = plannerDao.update(workout)

    suspend fun deletePlannedWorkout(workout: PlannedWorkoutEntity) = plannerDao.delete(workout)

    suspend fun insertRecord(record: PersonalRecordEntity) = recordDao.insert(record)

    suspend fun updateRecord(record: PersonalRecordEntity) = recordDao.update(record)

    suspend fun deleteRecord(record: PersonalRecordEntity) = recordDao.delete(record)

    suspend fun insertGoal(goal: TrainingGoalEntity) = goalDao.insert(goal)

    suspend fun updateGoal(goal: TrainingGoalEntity) = goalDao.update(goal)

    suspend fun deleteGoal(goal: TrainingGoalEntity) = goalDao.delete(goal)

    suspend fun getAchievementsOnce(): List<AchievementEntity> = achievementDao.getAllOnce()

    suspend fun upsertAchievements(items: List<AchievementEntity>) = achievementDao.upsertAll(items)

    suspend fun getCheckpointOnce(): SavedTrainingCheckpointEntity? = checkpointDao.getCheckpointOnce()

    suspend fun upsertCheckpoint(checkpoint: SavedTrainingCheckpointEntity) = checkpointDao.upsertCheckpoint(checkpoint)

    suspend fun clearCheckpoint() = checkpointDao.clearCheckpoint()

    suspend fun setDarkTheme(enabled: Boolean) = settingsStore.setDarkTheme(enabled)

    suspend fun setSoundEnabled(enabled: Boolean) = settingsStore.setSoundEnabled(enabled)

    suspend fun setSoundToneIndex(index: Int) = settingsStore.setSoundToneIndex(index)

    suspend fun setPreferredDuration(minutes: Int) = settingsStore.setPreferredDuration(minutes)

    suspend fun setDailyMinutesGoal(minutes: Int) = settingsStore.setDailyMinutesGoal(minutes)

    suspend fun setWeeklySessionsGoal(sessions: Int) = settingsStore.setWeeklySessionsGoal(sessions)

    suspend fun setActiveProgramId(programId: String) = settingsStore.setActiveProgramId(programId)

    suspend fun resetAllProgress() {
        withContext(Dispatchers.IO) {
            database.clearAllTables()
            settingsStore.resetAllSettings()
            DatabaseInitializer.populateIfEmpty(database)
        }
    }
}
