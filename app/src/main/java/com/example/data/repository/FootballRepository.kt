package com.example.data.repository

import com.example.data.local.FootballDatabase
import com.example.data.local.SettingsDataStore
import com.example.data.model.AppSettings
import com.example.data.model.CompletedLessonEntity
import com.example.data.model.PersonalRecordEntity
import com.example.data.model.PlannedWorkoutEntity
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.ProgramDayCompletionEntity
import com.example.data.model.SavedTacticEntity
import com.example.data.model.TrainingGoalEntity
import com.example.data.model.TrainingLesson
import com.example.data.model.TrainingProgram
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FootballRepository(
    private val database: FootballDatabase,
    private val settingsStore: SettingsDataStore
) {
    private val profileDao = database.playerProfileDao()
    private val completedDao = database.completedLessonDao()
    private val programDao = database.programCompletionDao()
    private val recordDao = database.personalRecordDao()
    private val tacticDao = database.savedTacticDao()
    private val plannerDao = database.plannedWorkoutDao()
    private val goalDao = database.trainingGoalDao()

    val playerProfile: Flow<PlayerProfileEntity?> = profileDao.observeProfile()
    val completedLessons: Flow<List<CompletedLessonEntity>> = completedDao.observeAllCompleted()
    val programCompletions: Flow<List<ProgramDayCompletionEntity>> = programDao.observeAll()
    val personalRecords: Flow<List<PersonalRecordEntity>> = recordDao.observeAll()
    val savedTactics: Flow<List<SavedTacticEntity>> = tacticDao.observeAll()
    val plannedWorkouts: Flow<List<PlannedWorkoutEntity>> = plannerDao.observeAll()
    val trainingGoals: Flow<List<TrainingGoalEntity>> = goalDao.observeAll()
    val settingsFlow: Flow<AppSettings> = settingsStore.settingsFlow

    val allSkillLessons: List<TrainingLesson> = SkillAndFitnessCurriculum.skillLessons
    val allFitnessLessons: List<TrainingLesson> = SkillAndFitnessCurriculum.fitnessLessons
    val allPositionLessons: List<TrainingLesson> = PositionsAndProgramsCurriculum.positionLessons
    val allLessons: List<TrainingLesson> = allSkillLessons + allFitnessLessons + allPositionLessons
    val allPrograms: List<TrainingProgram> = PositionsAndProgramsCurriculum.programs

    suspend fun getProfileOnce(): PlayerProfileEntity? = profileDao.getProfileOnce()

    suspend fun upsertProfile(profile: PlayerProfileEntity) = profileDao.upsertProfile(profile)

    suspend fun insertCompletedLesson(item: CompletedLessonEntity) = completedDao.insertCompleted(item)

    suspend fun deleteCompletedLesson(id: Long) = completedDao.deleteById(id)

    suspend fun deleteCompletedLessonByLessonId(lessonId: String) = completedDao.deleteByLessonId(lessonId)

    suspend fun upsertProgramDayCompletion(entry: ProgramDayCompletionEntity) = programDao.upsertDayCompletion(entry)

    suspend fun resetProgram(programId: String) = programDao.resetProgram(programId)

    suspend fun insertRecord(record: PersonalRecordEntity) = recordDao.insert(record)

    suspend fun updateRecord(record: PersonalRecordEntity) = recordDao.update(record)

    suspend fun deleteRecord(record: PersonalRecordEntity) = recordDao.delete(record)

    suspend fun insertTactic(tactic: SavedTacticEntity) = tacticDao.insert(tactic)

    suspend fun deleteTactic(tactic: SavedTacticEntity) = tacticDao.delete(tactic)

    suspend fun insertPlannedWorkout(workout: PlannedWorkoutEntity) = plannerDao.insert(workout)

    suspend fun updatePlannedWorkout(workout: PlannedWorkoutEntity) = plannerDao.update(workout)

    suspend fun deletePlannedWorkout(workout: PlannedWorkoutEntity) = plannerDao.delete(workout)

    suspend fun insertGoal(goal: TrainingGoalEntity) = goalDao.insert(goal)
    suspend fun updateGoal(goal: TrainingGoalEntity) = goalDao.update(goal)
    suspend fun deleteGoal(goal: TrainingGoalEntity) = goalDao.delete(goal)

    suspend fun setDarkTheme(enabled: Boolean) = settingsStore.setDarkTheme(enabled)
    suspend fun setSoundEnabled(enabled: Boolean) = settingsStore.setSoundEnabled(enabled)
    suspend fun setReminderEnabled(enabled: Boolean) = settingsStore.setReminderEnabled(enabled)
    suspend fun setReminderTime(timeLabel: String) = settingsStore.setReminderTime(timeLabel)
    suspend fun setActiveProgramId(programId: String) = settingsStore.setActiveProgramId(programId)
    suspend fun setAppLanguage(languageCode: String) = settingsStore.setAppLanguage(languageCode)
    suspend fun setDailyMinutesGoal(minutes: Int) = settingsStore.setDailyMinutesGoal(minutes)
    suspend fun setWeeklySessionsGoal(sessions: Int) = settingsStore.setWeeklySessionsGoal(sessions)

    suspend fun clearAllLocalData() {
        withContext(Dispatchers.IO) {
            database.clearAllTables()
            settingsStore.resetAllSettings()
            profileDao.upsertProfile(
                PlayerProfileEntity(
                    id = 1,
                    playerName = "Player One",
                    onboardingCompleted = false
                )
            )
        }
    }
}
